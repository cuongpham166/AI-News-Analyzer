from typing import List, Dict

import torch
import torch.onnx
import onnx
import onnxruntime as ort
from optimum.onnxruntime import ORTOptimizer
from optimum.onnxruntime.configuration import OptimizationConfig

from onnxruntime.quantization import quantize_dynamic, QuantType
from onnxsim import simplify
from transformers import AutoTokenizer, AutoModelForSequenceClassification
from ai.tokenizer.classification.classifier_tokenizer import ClassifierTokenizer

pytorch_model_dir = "ai/models/news_classifier/pytorch"
onnx_model_dir = "ai/models/news_classifier/onnx"

onnx_output_dir = "ai/models/news_classifier/onnx"
optimized_onnx_output_dir = "ai/models/news_classifier/optimized_onnx"
int8_onnx_output_dir = "ai/models/news_classifier/int8_onnx"
simplified_onnx_output_dir = "ai/models/news_classifier/simplified_onnx"

class ClassificationModelOptimizer:
    def __init__(self):
        self.model = AutoModelForSequenceClassification.from_pretrained(pytorch_model_dir,local_files_only=True)
        self.classifier_tokenizer = ClassifierTokenizer(pytorch_model_dir)

    def convert_to_ONNX(self):
        dummy_texts = [
            "Emmanuel Macron is the President of France",
            "A shock to oil supplies is rattling financial markets",
            "MIDDLE EAST LIVE 30 March: UN peacekeepers killed amid Israel-Hezbollah clashes"
        ]
        encoded_inputs = self.classifier_tokenizer.encode(dummy_texts)
        torch.onnx.export(
            self.model,
            (encoded_inputs["input_ids"], encoded_inputs["attention_mask"]),
            f"{onnx_output_dir}/model.onnx",
            input_names=["input_ids", "attention_mask"],
            output_names=["logits"],
            dynamic_axes={
                "input_ids": {0: "batch", 1: "sequence"},
                "attention_mask": {0: "batch", 1: "sequence"},
                "logits": {0: "batch"}
            },
            opset_version=17, #17
            do_constant_folding=True #True
        )
        print(f"ONNX model saved to {onnx_output_dir}")
        self.classifier_tokenizer.save(onnx_output_dir)

    def graph_optimize(self):
        optimizer = ORTOptimizer.from_pretrained(onnx_model_dir)
        optimization_config = OptimizationConfig(
            optimization_level=2,
            enable_transformers_specific_optimizations=True,
            optimize_for_gpu=False,
        )
        print(f"Optimized ONNX model saved to {optimized_onnx_output_dir}")
        optimizer.optimize(save_dir=optimized_onnx_output_dir, optimization_config=optimization_config)
        self.classifier_tokenizer.save(optimized_onnx_output_dir)

    def simplify_onnx(self):
        model = onnx.load(optimized_onnx_output_dir + "/model_optimized.onnx")
        model_simp, check = simplify(model)
        onnx.save(model_simp, simplified_onnx_output_dir + "/model_simplified.onnx")

    def graph_quantize(self):
        quantize_dynamic(
            model_input=simplified_onnx_output_dir + "/model_simplified.onnx",
            model_output=int8_onnx_output_dir + "/model_int8.onnx",
            weight_type=QuantType.QInt8,
            extra_options={"DisableShapeInference": True}
        )
if __name__ == '__main__':
    classification_model_converter = ClassificationModelOptimizer()
    classification_model_converter.graph_quantize()