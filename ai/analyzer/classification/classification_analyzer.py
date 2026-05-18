from typing import List, Dict

import numpy as np
import torch

from transformers import AutoTokenizer, AutoModelForSequenceClassification

from ai.tokenizer.classification.classification_tokenizer import ClassificationTokenizer
from ai.responses.classification_response import ClassificationResponse, ClassificationResult

pytorch_model_dir = "ai/models/classification/pytorch"
local_dir = "ai/models/classification/pytorch"


class ClassificationAnalyzer:
    def __init__(self):
        self.model = AutoModelForSequenceClassification.from_pretrained(pytorch_model_dir, local_files_only=True)
        self.device = torch.device("cpu")
        self.model.to(self.device)
        self.model.eval()
        self.classification_tokenizer = ClassificationTokenizer(pytorch_model_dir)

    def save(self):
        self.model.save_pretrained(local_dir)
        self.classification_tokenizer.save(local_dir)

    def analyze_input(self, articles: List[str]) -> ClassificationResponse:
        labels = ["politics", "economy", "entertainment", "environment",
                  "sports", "technology", "health", "culture"]
        
        """
        labels = [
            "politics",
            "economy",
            "technology",
            "sports",
            "health",
            "entertainment",
            "science",
            "world"
        ]
        """

        all_texts = []
        all_hypotheses = []
        mapping = []

        for i, article in enumerate(articles):
            for label in labels:
                all_texts.append(article)
                all_hypotheses.append(f"This text is about {label}.")
                mapping.append(i)

        tokenized_inputs = self.classification_tokenizer.encode(all_texts, all_hypotheses)
        tokenized_inputs = {k: v.to(self.device) for k, v in tokenized_inputs.items()}

        with torch.no_grad():
            logits = self.model(**tokenized_inputs).logits

        label_logits = logits[:, 0] - logits[:, 1]
        scores = torch.softmax(label_logits.view(len(articles), len(labels)), dim=1)

        results = []
        for article_scores in scores:
            best_idx = torch.argmax(article_scores).item()
            results.append(
                ClassificationResult(
                    topic=labels[best_idx]
                )
            )
        return ClassificationResponse(results=results)
