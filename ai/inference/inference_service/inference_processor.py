import torch
from typing import List
from ai.analyzer.sentiment.sentiment_analyzer import SentimentAnalyzer
from ai.analyzer.classification.classification_analyzer import ClassificationAnalyzer
from ai.analyzer.summarization.summarization_analyzer import SummarizationAnalyzer
from ai.analyzer.ner.ner_analyzer import NERAnalyzer
from ai.responses.inference_response import InferenceResponse, InferenceResult


class InferenceProcessor:
    def __init__(self):
        self.sentiment_analyzer = SentimentAnalyzer()
        self.classification_analyzer = ClassificationAnalyzer()
        self.summarization_analyzer = SummarizationAnalyzer()
        self.ner_analyzer = NERAnalyzer()

        self.device = torch.device("cpu")
        torch.set_num_threads(4)

    def get_summary_text(self, summary):
        if not summary:
            return ""

        if hasattr(summary, "results") and summary.results:
            return summary.results[0]

        return str(summary)

    def analyze(self, articles: List[dict]) -> InferenceResponse:
        texts = [a["text"] for a in articles]
        titles = [a["title"] for a in articles]

        summaries = self.summarization_analyzer.analyze_input(texts)

        sentiment_inputs = [
            self.get_summary_text(summaries.results[i]) if len(texts[i]) > 1000 else texts[i]
            for i in range(len(texts))
        ]
        sentiment = self.sentiment_analyzer.analyze_input(sentiment_inputs)

        classification = self.classification_analyzer.analyze_input(titles)

        ner_inputs = [
            texts[i] + " " + self.get_summary_text(summaries.results[i])
            for i in range(len(texts))
        ]

        ner = self.ner_analyzer.analyze_input(ner_inputs)

        results = []

        for i in range(len(articles)):
            results.append(
                InferenceResult(
                    link=articles[i]["link"],
                    publish_date=articles[i].get("publish_date"),
                    title=articles[i]["title"],
                    source=articles[i]["source"],
                    sentiment=sentiment.results[i],
                    classification=classification.results[i],
                    ner=ner.results[i],
                    summarization=summaries.results[i]
                )
            )

        return InferenceResponse(results=results)
