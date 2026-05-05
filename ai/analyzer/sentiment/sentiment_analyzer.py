from typing import List, Dict

import numpy as np
import torch

from transformers import AutoTokenizer, AutoModelForSequenceClassification

from ai.tokenizer.sentiment.sentiment_tokenizer import SentimentTokenizer
from ai.responses.sentiment_response import SentimentResponse, SentimentResult

pytorch_model_dir = "ai/models/sentiment/pytorch"
local_dir = "ai/models/sentiment"


class SentimentAnalyzer:
    def __init__(self):
        self.model = AutoModelForSequenceClassification.from_pretrained(pytorch_model_dir, local_files_only=True)
        self.model = torch.quantization.quantize_dynamic(
            self.model,
            {torch.nn.Linear},
            dtype=torch.qint8
        )
        self.device = torch.device("cpu")
        self.model.to(self.device)
        self.model.eval()
        self.sentiment_tokenizer = SentimentTokenizer(pytorch_model_dir)

    def save(self):
        self.model.save_pretrained(local_dir)
        self.sentiment_tokenizer.save(local_dir)

    def analyze_input(self, articles: List[str]) -> SentimentResponse:
        prediction_result = []
        tokenized_inputs = self.sentiment_tokenizer.encode(articles).to(self.device)

        """
        if "token_type_ids" in tokenized_inputs:
            del tokenized_inputs["token_type_ids"]
        """

        with torch.no_grad():
            output = self.model(**tokenized_inputs)
            logits = output.logits

        probabilities = torch.softmax(logits, dim=-1)
        prediction_ids = logits.argmax(dim=-1)

        results = []

        labels = ["negative", "positive"]

        for i in range(len(articles)):
            prediction_class = prediction_ids[i].item()
            label = labels[prediction_class]
            score = probabilities[i][prediction_class].item()

            results.append(
                SentimentResult(
                    label=label,
                    score=round(score, 4)
                )
            )
        return SentimentResponse(results=results)
