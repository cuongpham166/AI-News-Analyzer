from typing import List, Dict

import numpy as np
import torch

from transformers import AutoTokenizer, AutoModelForSequenceClassification

from ai.tokenizer.sentiment.sentiment_tokenizer import SentimentTokenizer

pytorch_model_dir = "ai/models/sentiment/pytorch"
local_dir = "ai/models/sentiment"

class SentimentAnalyzer:
    def __init__(self):
        self.model = AutoModelForSequenceClassification.from_pretrained(pytorch_model_dir,local_files_only=True)
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self.model.to(self.device)
        self.model.eval()
        self.sentiment_tokenizer = SentimentTokenizer(pytorch_model_dir)

    def save(self):
        self.model.save_pretrained(local_dir)
        self.sentiment_tokenizer.save(local_dir)

    def analyze_input(self, articles: List[str]):
        prediction_result = []
        tokenized_inputs = self.sentiment_tokenizer.encode(articles).to(self.device)

        if "token_type_ids" in tokenized_inputs:
            del tokenized_inputs["token_type_ids"]
            
        with torch.no_grad():
            output = self.model(**tokenized_inputs)
            logits = output.logits

        probabilities = torch.softmax(logits, dim=-1)
        prediction_ids = logits.argmax(dim=-1)

        for i, text in enumerate(articles):
            prediction_class = prediction_ids[i].item()
            label = self.model.config.id2label[prediction_class]
            score = probabilities[i][prediction_class].item()
            prediction_record = {
                "label": label,
                "negative": round(probabilities[i][0].item(),2),
                "positive": round(probabilities[i][1].item(),2),
            }

            prediction_result.append(prediction_record)
        return prediction_result