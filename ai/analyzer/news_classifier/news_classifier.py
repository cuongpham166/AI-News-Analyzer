from typing import List, Dict
import numpy as np
import torch
from transformers import AutoTokenizer, AutoModelForSequenceClassification
from ai.tokenizer.classification.classifier_tokenizer import ClassifierTokenizer
from ai.responses.classification_response import ClassificationResponse, ClassificationResult

pytorch_model_dir = "ai/models/news_classifier_cpu"
local_dir = "ai/models/news_classifier_cpu"

class NewsClassifier:
    def __init__(self):
        self.model = AutoModelForSequenceClassification.from_pretrained(pytorch_model_dir, local_files_only=True)
        self.device = torch.device("cpu")
        self.model.to(self.device)
        self.model.eval()
        self.classifier_tokenizer = ClassifierTokenizer(pytorch_model_dir)
        self.id2label = self.model.config.id2label
        self.label2id = self.model.config.label2id

    def classify(self, articles: List[str]) -> ClassificationResponse:
        tokenized_inputs = self.classifier_tokenizer.encode(articles).to(self.device)

        with torch.no_grad():
            output = self.model(**tokenized_inputs)
            logits = output.logits

        prediction_ids = logits.argmax(dim=-1)

        results = []
        for i in range(len(articles)):
            topic_id = prediction_ids[i].item()
            topic_label = self.id2label[topic_id]
            results.append(
                ClassificationResult(
                    topic=topic_label
                )
            )
        return ClassificationResponse(results=results)
