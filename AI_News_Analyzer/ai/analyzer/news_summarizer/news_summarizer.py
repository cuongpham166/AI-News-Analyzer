from typing import List, Dict

import numpy as np
import torch

from transformers import AutoTokenizer, BartForConditionalGeneration

from ai.tokenizer.summarization.summarization_tokenizer import SummarizationTokenizer
from ai.responses.summarization_response import SummarizationResponse

pytorch_model_dir = "ai/models/summarization/pytorch"
local_dir = "ai/models/summarization/pytorch"


class NewsSummarizer:
    def __init__(self):
        self.model = BartForConditionalGeneration.from_pretrained(pytorch_model_dir, local_files_only=True)
        self.device = torch.device("cpu")
        self.model.to(self.device)
        self.model.eval()
        self.summarization_tokenizer = SummarizationTokenizer(pytorch_model_dir)

    def save(self):
        self.model.save_pretrained(local_dir)
        self.summarization_tokenizer.save(local_dir)

    def summarize_ids(self, tokenized_inputs):
        summary_ids = self.model.generate(
            tokenized_inputs["input_ids"],
            attention_mask=tokenized_inputs.get("attention_mask"),
            num_beams=4,
            max_length=120,
            min_length=30,
            length_penalty=2.0,
            no_repeat_ngram_size=3,
            repetition_penalty=1.2,
            early_stopping=True
        )
        return summary_ids

    def analyze_input(self, articles: List[str]) -> SummarizationResponse:
        tokenized_inputs = self.summarization_tokenizer.encode(articles)
        tokenized_inputs = {k: v.to(self.device) for k, v in tokenized_inputs.items()}

        summary_ids = self.summarize_ids(tokenized_inputs)
        summaries = self.summarization_tokenizer.batch_decode(summary_ids)

        return SummarizationResponse(results=summaries)
