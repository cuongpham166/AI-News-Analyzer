
from typing import List, Dict
import numpy as np
import torch
import onnxruntime as ort
from datasets import load_dataset
from sklearn.metrics import accuracy_score, f1_score, classification_report
from transformers import AutoTokenizer, AutoModelForSequenceClassification

from ai.analyzer.sentiment_classifier import sentiment_classifier
from ai.tokenizer.sentiment.sentiment_tokenizer import SentimentTokenizer
from ai.config.labels.sentiment_labels import SentimentLabel

pytorch_model_dir = "ai/models/sentiment/pytorch"
local_dir = "ai/models/sentiment"

int8_onnx_model_dir = "ai/models/sentiment/int8_onnx"
int8_onnx_model_path = "ai/models/sentiment/int8_onnx/model_int8.onnx"

class SentimentClassifierEvaluator:
    def __init__(self):
        self.model = AutoModelForSequenceClassification.from_pretrained(pytorch_model_dir, local_files_only=True)
        self.device = torch.device("cpu")
        self.model.to(self.device)
        self.model.eval()
        self.sentiment_tokenizer = SentimentTokenizer(pytorch_model_dir)
        self.sentiment_labels = SentimentLabel()

        self.session = ort.InferenceSession(int8_onnx_model_path, providers=['CPUExecutionProvider'])
        self.sentiment_tokenizer_int8 = SentimentTokenizer(int8_onnx_model_dir)

    def evaluate_pytorch(self, articles: List[str]):
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
            results.append({
                "prediction_id": prediction_class,
                "label": label,
                "score": score
            })

        return results

    def evaluate_onnx(self, articles: List[str]):
        tokenized_inputs = self.sentiment_tokenizer_int8.encode(articles)
        ort_inputs = {
            "input_ids": tokenized_inputs["input_ids"].numpy(),
            "attention_mask": tokenized_inputs["attention_mask"].numpy()
        }
        logits = self.session.run(None, ort_inputs)[0]
        probabilities = np.exp(logits) / np.exp(logits).sum(axis=-1, keepdims=True)
        prediction_ids = logits.argmax(axis=-1)
        results = []

        for i in range(len(articles)):
            prediction_class = prediction_ids[i].item()
            label = self.sentiment_labels.id2label(prediction_class)
            score = probabilities[i][prediction_class].item()
            results.append({
                "prediction_id": prediction_class,
                "label": label,
                "score": score
            })
        return results

    def evaluate_dataset(self, model, csv_path, batch_size=4, limit=3000):
        dataset = load_dataset("csv",data_files=csv_path,encoding="cp1252")["train"]
        dataset = dataset.shuffle(seed=42).select(range(min(limit, len(dataset))))

        label_mapping = {
            0: 0,
            2: 1
        }

        all_predictions = []
        all_labels = []

        for i in range(0, len(dataset), batch_size):
            batch = dataset[i:i + batch_size]

            texts = batch["text"]
            labels = [label_mapping[x] for x in batch["label"]]


            if model == "onnx":
                results = self.evaluate_onnx(texts)
            else:
                results = self.evaluate_pytorch(texts)

            preds = [r["prediction_id"] for r in results]

            all_predictions.extend(preds)
            all_labels.extend(labels)

        accuracy = accuracy_score(all_labels, all_predictions)

        f1 = f1_score(
            all_labels,
            all_predictions,
            average="binary"
        )

        print(f"Accuracy: {accuracy:.4f}")
        print(f"F1 Score: {f1:.4f}")
        print(
            classification_report(
                all_labels,
                all_predictions,
                target_names=["negative", "positive"]
            )
        )
        return {
            "accuracy": accuracy,
            "f1": f1
        }

if __name__ == "__main__":
    validation_dataset = "ai/dataset/sentiment_classifier/sentiment_valid_dataset.csv"
    sentiment_classifier_evaluator = SentimentClassifierEvaluator()
    sentiment_classifier_evaluator.evaluate_dataset("pytorch", validation_dataset)
    print("-----------------------------------------------------------------------------")
    sentiment_classifier_evaluator.evaluate_dataset("onnx", validation_dataset)