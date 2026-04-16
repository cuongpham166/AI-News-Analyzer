from typing import List, Dict

import torch
import torch.onnx
import onnxruntime as ort
from optimum.onnxruntime import OptimizationConfig, ORTOptimizer

from transformers import AutoTokenizer, AutoModelForSequenceClassification
from ai.tokenizer.sentiment.sentiment_tokenizer import SentimentTokenizer

pytorch_model_dir = "ai/models/sentiment/pytorch"
onnx_model_dir = "ai/models/sentiment/onnx"

onnx_output_dir = "ai/models/sentiment/onnx"
optimized_onnx_output_dir = "ai/models/sentiment/optimized_onnx"

class SentimentModelOptimizer:
    def __init__(self):
        self.model = AutoModelForSequenceClassification.from_pretrained(pytorch_model_dir,local_files_only=True)
        self.sentiment_tokenizer = SentimentTokenizer(pytorch_model_dir)

    def convert_to_ONNX(self):
        dummy_texts = [
            "Cooking microwave pizzas, yummy",
            "I hate you"
        ]
        encoded_inputs = self.sentiment_tokenizer.encode(dummy_texts)
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
        self.sentiment_tokenizer.save(onnx_output_dir)

    def graph_optimize(self):
        optimizer = ORTOptimizer.from_pretrained(onnx_model_dir) 
        optimization_config = OptimizationConfig(
            optimization_level=2,
            enable_transformers_specific_optimizations=True,
            optimize_for_gpu=False,
        )
        print(f"Optimized ONNX model saved to {optimized_onnx_output_dir}")
        optimizer.optimize(save_dir=optimized_onnx_output_dir, optimization_config=optimization_config)
        self.sentiment_tokenizer.save(optimized_onnx_output_dir)        

if __name__ == '__main__':
    sentiment_model_converter = SentimentModelOptimizer()
    sentiment_model_converter.graph_optimize()
