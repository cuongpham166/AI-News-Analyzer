import torch
from typing import List
from ai.analyzer.sentiment_classifier.sentiment_classifier import SentimentClassifier
from ai.analyzer.topic_classifier.topic_classifier import TopicClassifier
from ai.analyzer.news_summarizer.news_summarizer import NewsSummarizer
from ai.analyzer.entity_classifier.entity_classifier import EntityClassifier
from ai.responses.inference_response import InferenceResponse, InferenceResult


class InferenceProcessor:
    def __init__(self):
        self.entity_classifier = EntityClassifier()
        self.topic_classifier = TopicClassifier()
        self.sentiment_classifier = SentimentClassifier()
        self.news_summarizer = NewsSummarizer()

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

        summaries = self.news_summarizer.analyze_input(texts)

        sentiment_inputs = [
            self.get_summary_text(summaries.results[i]) if len(texts[i]) > 1000 else texts[i]
            for i in range(len(texts))
        ]
        sentiment = self.sentiment_classifier.analyze_input_onnx(sentiment_inputs)

        classification = self.topic_classifier.classify_onnx(titles)

        ner_inputs = [
            texts[i] + " " + self.get_summary_text(summaries.results[i])
            for i in range(len(texts))
        ]

        ner = self.entity_classifier.analyze_input(ner_inputs)

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
