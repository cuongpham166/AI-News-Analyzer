from transformers import AutoTokenizer, AutoModelForSequenceClassification
from typing import List, Dict, Any

model_dir = "ai/models/classification/pytorch"

class ClassificationTokenizer:
    def __init__(self, model_dir:str=model_dir):
        self.classification_tokenizer = AutoTokenizer.from_pretrained(model_dir,local_files_only=True)

    def encode(self, texts:List[str], hypothesis:List[str]):
        return self.classification_tokenizer(
            texts,
            hypothesis,
            padding=True,
            truncation=True,
            max_length=128,
            return_tensors="pt" #return_tensors="np" # Return NumPy arrays (for ONNX compatibility) 
        )

    def save(self, local_dir:str="ai/models/classification/pytorch"):
        return self.classification_tokenizer.save_pretrained(local_dir)