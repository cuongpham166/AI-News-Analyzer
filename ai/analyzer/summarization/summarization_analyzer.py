from typing import List, Dict

import numpy as np
import torch

from transformers import AutoTokenizer, BartForConditionalGeneration

from ai.tokenizer.summarization.summarization_tokenizer import SummarizationTokenizer

pytorch_model_dir = "ai/models/summarization/pytorch"
local_dir = "ai/models/summarization/pytorch"

class SummarizationAnalyzer:
    def __init__(self):
        self.model = BartForConditionalGeneration.from_pretrained(pytorch_model_dir,local_files_only=True)
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self.model.to(self.device)
        self.model.eval()
        self.summarization_tokenizer = SummarizationTokenizer(pytorch_model_dir)

    def save(self):
        self.model.save_pretrained(local_dir)
        self.summarization_tokenizer.save(local_dir)

    def summarize_ids(self, tokenized_inputs):
        summary_ids = self.model.generate(tokenized_inputs["input_ids"], num_beams=2, min_length=0, max_length=100)
        return summary_ids

    def analyze_input(self, articles: List[str]):
        prediction_result = []

        for article in articles:
            tokenized_inputs = self.summarization_tokenizer.encode([article]).to(self.device)
            summary_ids = self.summarize_ids(tokenized_inputs)
            summary = self.summarization_tokenizer.batch_decode(summary_ids)
            prediction_result.append(summary)

        return prediction_result