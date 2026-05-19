from typing import List
import torch
import onnxruntime as ort
from datasets import load_dataset
from transformers import AutoTokenizer, AutoModelForSequenceClassification

from ai.analyzer.topic_classifier.topic_classifier import TopicClassifier
from ai.tokenizer.classification.classifier_tokenizer import ClassifierTokenizer
from sklearn.metrics import accuracy_score, f1_score, classification_report
from ai.config.labels.topic_labels import TopicLabel

pytorch_model_dir = "ai/models/topic_classifier/pytorch"
local_dir = "ai/models/topic_classifier/pytorch"

int8_onnx_model_dir = "ai/models/topic_classifier/int8_onnx"
int8_onnx_model_path = "ai/models/topic_classifier/int8_onnx/model_int8.onnx"

class TopicClassifierEvaluator:
    def __init__(self):
        self.model = AutoModelForSequenceClassification.from_pretrained(pytorch_model_dir, local_files_only=True)
        self.device = torch.device("cpu")
        self.model.to(self.device)
        self.model.eval()
        self.classifier_tokenizer = ClassifierTokenizer(pytorch_model_dir)
        self.topic_labels = TopicLabel()

        self.session = ort.InferenceSession(int8_onnx_model_path, providers=['CPUExecutionProvider'])
        self.classifier_tokenizer_int8 = ClassifierTokenizer(int8_onnx_model_dir)

    def evaluate_pytorch(self, articles: List[str]):
        tokenized_inputs = self.classifier_tokenizer.encode(articles).to(self.device)
        with torch.no_grad():
            output = self.model(**tokenized_inputs)
            logits = output.logits

        prediction_ids = logits.argmax(dim=-1)

        results = []
        for i in range(len(articles)):
            topic_id = prediction_ids[i].item()
            topic_label = self.topic_labels.id2label(topic_id)
            results.append(topic_label)

        return results


    def evaluate_onnx(self, articles: List[str]):
        tokenized_inputs = self.classifier_tokenizer_int8.encode(articles)
        ort_inputs = {
            "input_ids": tokenized_inputs["input_ids"].numpy(),
            "attention_mask": tokenized_inputs["attention_mask"].numpy()
        }

        logits = self.session.run(None, ort_inputs)[0]

        results = []

        for i in range(len(articles)):
            prediction_ids = logits[i].argmax(axis=-1)
            topic_id = prediction_ids.item()
            topic_label = self.topic_labels.id2label(topic_id)
            results.append(topic_label)

        return results

    def evaluate_dataset(self, model, csv_path, batch_size=4, limit=6000):
        dataset = load_dataset("csv",data_files=csv_path)["train"]
        dataset = dataset.shuffle(seed=42).select(range(min(limit, len(dataset))))

        label_normalization = {
            "sport": "sports"
        }
        all_predictions = []
        all_labels = []

        for i in range(0, len(dataset), batch_size):
            batch = dataset[i:i + batch_size]
            texts = batch["text"]
            labels = batch["label"]


            if model == "onnx":
                results = self.evaluate_onnx(texts)
            else:
                results = self.evaluate_pytorch(texts)

            all_predictions.extend(results)
            all_labels.extend(labels)

        accuracy = accuracy_score(all_labels, all_predictions)

        f1 = f1_score(
            all_labels,
            all_predictions,
            average="macro"
        )

        print(f"Accuracy: {accuracy:.4f}")
        print(f"F1 Score: {f1:.4f}")
        print(
            classification_report(
                all_labels,
                all_predictions,
                target_names=["economy","entertainment","health","politics","science","sports","technology","world"]
            )
        )
        return {
            "accuracy": accuracy,
            "f1": f1
        }

if __name__ == "__main__":
    validation_dataset = "ai/dataset/topic_classifier/topic_valid_dataset.csv"
    topic_classifier_evaluator = TopicClassifierEvaluator()
    topic_classifier_evaluator.evaluate_dataset("pytorch", validation_dataset)
    print("-----------------------------------------------------------------------------")
    topic_classifier_evaluator.evaluate_dataset("onnx", validation_dataset)
