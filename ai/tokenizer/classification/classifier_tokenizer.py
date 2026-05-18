from transformers import AutoTokenizer
from typing import List

pytorch_model_dir = "ai/models/news_classifier_cpu"


class ClassifierTokenizer:
    def __init__(self, model_dir: str = pytorch_model_dir):
        self.classfier_tokenizer = AutoTokenizer.from_pretrained(model_dir, local_files_only=True)

    def encode(self, texts: List[str]):
        return self.classfier_tokenizer(
            texts,
            padding=True,
            truncation=True,
            max_length=128,
            return_token_type_ids=False,
            return_tensors="pt"
        )

    def save(self, local_dir: str = "ai/models/news_classifier_cpu"):
        return self.classfier_tokenizer.save_pretrained(local_dir)
