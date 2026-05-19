from typing import List, Dict

import numpy as np
import torch
import onnxruntime as ort

from transformers import AutoTokenizer, AutoModelForSequenceClassification

from ai.tokenizer.sentiment.sentiment_tokenizer import SentimentTokenizer
from ai.responses.sentiment_response import SentimentResponse, SentimentResult
from ai.config.labels.sentiment_labels import SentimentLabel
pytorch_model_dir = "ai/models/sentiment/pytorch"
local_dir = "ai/models/sentiment"

int8_onnx_model_dir = "ai/models/sentiment/int8_onnx"
int8_onnx_model_path = "ai/models/sentiment/int8_onnx/model_int8.onnx"


class SentimentClassifier:
    def __init__(self):
        self.model = AutoModelForSequenceClassification.from_pretrained(pytorch_model_dir, local_files_only=True)
        self.device = torch.device("cpu")
        self.model.to(self.device)
        self.model.eval()
        self.sentiment_tokenizer = SentimentTokenizer(pytorch_model_dir)
        self.sentiment_labels = SentimentLabel()

        self.session = ort.InferenceSession(int8_onnx_model_path, providers=['CPUExecutionProvider'])
        self.sentiment_tokenizer_int8 = SentimentTokenizer(int8_onnx_model_dir)

    def save(self):
        self.model.save_pretrained(local_dir)
        self.sentiment_tokenizer.save(local_dir)

    def analyze_input(self, articles: List[str]) -> SentimentResponse:
        tokenized_inputs = self.sentiment_tokenizer.encode(articles).to(self.device)

        with torch.no_grad():
            output = self.model(**tokenized_inputs)
            logits = output.logits

        probabilities = torch.softmax(logits, dim=-1)
        prediction_ids = logits.argmax(dim=-1)

        results = []

        for i in range(len(articles)):
            prediction_class = prediction_ids[i].item()
            label = self.sentiment_labels.id2label(prediction_class)
            score = probabilities[i][prediction_class].item()

            results.append(
                SentimentResult(
                    label=label,
                    score=round(score, 4)
                )
            )
        return SentimentResponse(results=results)

    def analyze_input_onnx(self, articles: List[str]) -> SentimentResponse:
        tokenized_inputs = self.sentiment_tokenizer_int8.encode(articles)
        ort_inputs = {
            "input_ids": tokenized_inputs["input_ids"].numpy(),
            "attention_mask": tokenized_inputs["attention_mask"].numpy()
        }

        logits = self.session.run(None, ort_inputs)[0]
        exp_logits = np.exp(logits - np.max(logits, axis=-1, keepdims=True))
        probabilities = exp_logits / exp_logits.sum(axis=-1, keepdims=True)
        prediction_ids = logits.argmax(axis=-1)
        results = []

        for i in range(len(articles)):
            prediction_class = prediction_ids[i].item()
            label = self.sentiment_labels.id2label(prediction_class)
            score = probabilities[i][prediction_class].item()

            results.append(
                SentimentResult(
                    label=label,
                    score=round(score, 4)
                )
            )
        return SentimentResponse(results=results)