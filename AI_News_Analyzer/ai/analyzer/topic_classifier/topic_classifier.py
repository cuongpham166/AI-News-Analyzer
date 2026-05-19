from typing import List
import torch
import onnxruntime as ort
from transformers import AutoTokenizer, AutoModelForSequenceClassification
from ai.tokenizer.classification.classifier_tokenizer import ClassifierTokenizer
from ai.responses.classification_response import ClassificationResponse, ClassificationResult
from ai.config.labels.topic_labels import TopicLabel

pytorch_model_dir = "ai/models/topic_classifier/pytorch"
local_dir = "ai/models/topic_classifier/pytorch"

int8_onnx_model_dir = "ai/models/topic_classifier/int8_onnx"
int8_onnx_model_path = "ai/models/topic_classifier/int8_onnx/model_int8.onnx"

class TopicClassifier:
    def __init__(self):
        self.model = AutoModelForSequenceClassification.from_pretrained(pytorch_model_dir, local_files_only=True)
        self.device = torch.device("cpu")
        self.model.to(self.device)
        self.model.eval()
        self.classifier_tokenizer = ClassifierTokenizer(pytorch_model_dir)
        self.topic_labels = TopicLabel()

        self.session = ort.InferenceSession(int8_onnx_model_path, providers=['CPUExecutionProvider'])
        self.classifier_tokenizer_int8 = ClassifierTokenizer(int8_onnx_model_dir)

    def classify(self, articles: List[str]) -> ClassificationResponse:
        tokenized_inputs = self.classifier_tokenizer.encode(articles).to(self.device)

        with torch.no_grad():
            output = self.model(**tokenized_inputs)
            logits = output.logits

        prediction_ids = logits.argmax(dim=-1)

        results = []
        for i in range(len(articles)):
            topic_id = prediction_ids[i].item()
            topic_label = self.topic_labels.id2label(topic_id)
            results.append(
                ClassificationResult(
                    topic=topic_label
                )
            )
        return ClassificationResponse(results=results)

    def classify_onnx(self, articles: List[str]) -> ClassificationResponse:
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
            results.append(
                ClassificationResult(
                    topic=topic_label
                )
            )
        return ClassificationResponse(results=results)