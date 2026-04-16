from typing import List, Dict

import torch
import torch.onnx
import onnxruntime as ort
from optimum.onnxruntime import OptimizationConfig, ORTOptimizer

from transformers import AutoTokenizer, BartForConditionalGeneration
from ai.tokenizer.summarization.summarization_tokenizer import SummarizationTokenizer

pytorch_model_dir = "ai/models/summarization/pytorch"
onnx_model_dir = "ai/models/summarization/onnx"

onnx_output_dir = "ai/models/summarization/onnx"
optimized_onnx_output_dir = "ai/models/summarization/optimized_onnx"

class SummarizationModelOptimizer:
    def __init__(self):
        self.model = BartForConditionalGeneration.from_pretrained(pytorch_model_dir,local_files_only=True)
        self.summarization_tokenizer = SummarizationTokenizer(pytorch_model_dir)

    def convert_to_ONNX(self):
        dummy_texts = [
            "Further attacks have been reported across the Middle East as the war enters a second month, with two more Indonesian peacekeepers killed in Southern Lebanon.", 
        ]
        encoded_inputs = self.summarization_tokenizer.encode(dummy_texts)
        print(f"Encoded inputs: {encoded_inputs}")
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
            opset_version=13, #17
            do_constant_folding=True,
            verbose=True
        )
        print(f"ONNX model saved to {onnx_output_dir}")
        self.summarization_tokenizer.save(onnx_output_dir)

    def graph_optimize(self):
        optimizer = ORTOptimizer.from_pretrained(onnx_model_dir) 
        optimization_config = OptimizationConfig(
            optimization_level=2,
            enable_transformers_specific_optimizations=True,
            optimize_for_gpu=False,
        )
        print(f"Optimized ONNX model saved to {optimized_onnx_output_dir}")
        optimizer.optimize(save_dir=optimized_onnx_output_dir, optimization_config=optimization_config)
        self.summarization_tokenizer.save(optimized_onnx_output_dir)        

if __name__ == '__main__':
    summarization_model_converter = SummarizationModelOptimizer()
    summarization_model_converter.convert_to_ONNX()