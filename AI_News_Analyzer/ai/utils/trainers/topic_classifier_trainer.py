from multiprocessing import freeze_support
import pandas as pd
import numpy as np
from datasets import Dataset
from sklearn.model_selection import train_test_split
from transformers import (
    AutoTokenizer,
    AutoModelForSequenceClassification,
    TrainingArguments,
    Trainer,
    DataCollatorWithPadding,
    EarlyStoppingCallback
)
import evaluate
from ai.config.training_parameters.topic_classifier_training_params import topic_classifier_training_params_new_cpu


class TopicClassifierTrainer:
    def __init__(
            self,
            dataset_path,
            model_name="distilbert-base-uncased",
            max_length=256,
            test_size=0.2,
            random_state=42,
            output_dir="ai/models/news_classifier",
            training_args_dict=None
    ):
        self.dataset_path = dataset_path
        self.model_name = model_name
        self.max_length = max_length
        self.test_size = test_size
        self.random_state = random_state
        self.output_dir = output_dir
        self.training_args_dict = training_args_dict or {}

        self.df = None
        self.labels = None
        self.label2id = None
        self.id2label = None

        self.train_dataset = None
        self.test_dataset = None

        self.tokenizer = None
        self.model = None
        self.trainer = None

        self.accuracy = evaluate.load("accuracy")
        self.f1 = evaluate.load("f1")

    def load_data(self):
        self.df = pd.read_csv(self.dataset_path)

        self.labels = sorted(self.df["label"].unique())
        self.label2id = {label: idx for idx, label in enumerate(self.labels)}
        self.id2label = {idx: label for label, idx in self.label2id.items()}

        self.df["label"] = self.df["label"].map(self.label2id)

    def split_data(self):
        train_df, test_df = train_test_split(
            self.df,
            test_size=self.test_size,
            random_state=self.random_state,
            stratify=self.df["label"]
        )

        self.train_dataset = Dataset.from_pandas(train_df)
        self.test_dataset = Dataset.from_pandas(test_df)

    def load_tokenizer(self):
        self.tokenizer = AutoTokenizer.from_pretrained(self.model_name)

    def tokenize_function(self, batch):
        return self.tokenizer(
            batch["text"],
            truncation=True,
            padding=False,
            max_length=self.max_length
        )

    def preprocess_datasets(self):
        self.train_dataset = self.train_dataset.map(
            self.tokenize_function,
            batched=True
        )

        self.test_dataset = self.test_dataset.map(
            self.tokenize_function,
            batched=True
        )

        self.train_dataset = self.train_dataset.remove_columns(
            ["text", "__index_level_0__"]
        )

        self.test_dataset = self.test_dataset.remove_columns(
            ["text", "__index_level_0__"]
        )

    def load_model(self):
        self.model = AutoModelForSequenceClassification.from_pretrained(
            self.model_name,
            num_labels=len(self.labels),
            id2label=self.id2label,
            label2id=self.label2id
        )

    def compute_metrics(self, eval_pred):
        logits, labels = eval_pred
        predictions = np.argmax(logits, axis=1)


        accuracy = self.accuracy.compute(
            predictions=predictions,
            references=labels
        )

        f1_score = self.f1.compute(
            predictions=predictions,
            references=labels,
            average="weighted"
        )

        return {
            "accuracy": accuracy["accuracy"],
            "f1": f1_score["f1"]
        }

    def setup_trainer(self):
        training_args = TrainingArguments(
            **self.training_args_dict
        )

        self.trainer = Trainer(
            model=self.model,
            args=training_args,
            train_dataset=self.train_dataset,
            eval_dataset=self.test_dataset,
            tokenizer=self.tokenizer,
            data_collator=DataCollatorWithPadding(self.tokenizer),
            compute_metrics=self.compute_metrics,
            callbacks=[
                EarlyStoppingCallback(early_stopping_patience=2)
            ]
        )

    def train(self):
        self.trainer.train()

    def save(self):
        self.trainer.save_model(self.output_dir)
        self.tokenizer.save_pretrained(self.output_dir)

    def run(self):
        self.load_data()
        self.split_data()

        self.load_tokenizer()
        self.preprocess_datasets()

        self.load_model()
        self.setup_trainer()

        self.train()
        self.save()


if __name__ == "__main__":
    freeze_support()

    trainer = TopicClassifierTrainer(
        dataset_path="ai/dataset/classification/balanced_news_dataset.csv",
        training_args_dict=topic_classifier_training_params_new_cpu
    )

    trainer.run()
