from transformers import AutoTokenizer, AutoModelForSequenceClassification
from typing import List, Dict, Any

pytorch_model_dir = "ai/models/sentiment/pytorch"

class SentimentTokenizer:
    def __init__(self, model_dir:str=pytorch_model_dir):
        self.sentiment_tokenizer = AutoTokenizer.from_pretrained(model_dir,local_files_only=True)

    def encode(self, texts:List[str]):
        return self.sentiment_tokenizer(
            texts,
            padding=True,
            truncation=True,
            max_length=128,
            return_tensors="pt" #return_tensors="np" # Return NumPy arrays (for ONNX compatibility) 
        )

    def save(self, local_dir:str="ai/models/sentiment/pytorch"):
        return self.sentiment_tokenizer.save_pretrained(local_dir)
