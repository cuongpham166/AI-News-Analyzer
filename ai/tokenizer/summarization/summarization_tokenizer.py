from transformers import AutoTokenizer
from typing import List, Dict, Any

model_dir = "ai/models/summarization/pytorch"

class SummarizationTokenizer:
    def __init__(self, model_dir:str=model_dir):
        self.summarization_tokenizer = AutoTokenizer.from_pretrained(model_dir,local_files_only=True)

    def encode(self, texts:List[str]):
        return self.summarization_tokenizer(
            texts,
            padding=True,
            truncation=True,
            max_length=128,
            return_tensors="pt" #return_tensors="np" # Return NumPy arrays (for ONNX compatibility) 
        )

    def batch_decode(self, summary_ids):
        return self.summarization_tokenizer.batch_decode(summary_ids, skip_special_tokens=True, clean_up_tokenization_spaces=False)[0]

    def save(self, local_dir:str="ai/models/summarization/pytorch"):
        return self.summarization_tokenizer.save_pretrained(local_dir)

