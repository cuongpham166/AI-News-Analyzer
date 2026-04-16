from typing import List, Dict

import numpy as np
import torch

from transformers import AutoTokenizer, AutoModelForSequenceClassification

from ai.tokenizer.classification.classification_tokenizer import ClassificationTokenizer

pytorch_model_dir = "ai/models/classification/pytorch"
local_dir = "ai/models/classification/pytorch"

class ClassificationAnalyzer:
    def __init__(self):
        self.model = AutoModelForSequenceClassification.from_pretrained(pytorch_model_dir,local_files_only=True)
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self.model.to(self.device)
        self.model.eval()
        self.classification_tokenizer = ClassificationTokenizer(pytorch_model_dir)

    def save(self):
        self.model.save_pretrained(local_dir)
        self.classification_tokenizer.save(local_dir)

    def analyze_input(self, articles: List[str]):
        prediction_result = []
        
        labels = ["politics", "economy", "entertainment", "environment", "sports", "technology","health","culture"]

        for article in articles:
            label_scores = {}

            articles_expanded = [article] * len(labels)
            hypotheses = [f"This text is about {label}." for label in labels]

            tokenized_inputs = self.classification_tokenizer.encode(articles_expanded,hypotheses).to(self.device)

            with torch.no_grad():
                output = self.model(**tokenized_inputs)
                logits = output.logits

            label_logits = logits[:, 0] - logits[:, 1]
            scores = torch.softmax(label_logits, dim=0)
            
            label_scores = []
            for label, score in zip(labels, scores):
                label_score = {"label":label,"score":round(float(score) * 100, 2)}
                label_scores.append(label_score)

            prediction_result.append(label_scores)
        return prediction_result