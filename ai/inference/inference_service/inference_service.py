from typing import List, Dict
from datetime import datetime

from ai.analyzer.sentiment.sentiment_analyzer import SentimentAnalyzer
from ai.analyzer.classification.classification_analyzer import ClassificationAnalyzer
from ai.analyzer.summarization.summarization_analyzer import SummarizationAnalyzer
from ai.analyzer.ner.ner_analyzer import NERAnalyzer

class InferenceService:
    def __init__(self):
        self.sentiment_analyzer = SentimentAnalyzer()
        self.classfication_analyzer = ClassificationAnalyzer()
        self.summarization_analyzer = SummarizationAnalyzer()
        self.ner_analyzer = NERAnalyzer()

    def analyze(self, articles: List[str]):
        inference_results = []
        for article in articles:
            summary = self.summarization_analyzer.analyze_input([article["text"]])
            
            if len(article["text"])<1000:
                sentiment_input = [article["text"]]
            else:
                sentiment_input = summary

            sentiment = self.sentiment_analyzer.analyze_input(sentiment_input)

            classification = self.classfication_analyzer.analyze_input([article["title"]])

            ner_input = article["text"]+" "+summary[0]
            ner = self.ner_analyzer.analyze_input([ner_input])
            prediction_record = {
                "summarized_text":summary[0],
                "sentiment":sentiment,
                "classification":classification,
                "ner":ner,
                "link":article["link"],
                "source":article["source"],
            }
            inference_results.append(prediction_record)
            
        merged_data = [dict(item1, **item2) for item1, item2 in zip(articles, inference_results)]
        processed_inference_data = self.process_inference(merged_data)
        return processed_inference_data

    def process_inference(self, inference_records: List[str]):
        processed_inference = []
        for record in inference_records:
            sentiment = self.process_sentiment(record["sentiment"])
            classification = self.process_classification(record["classification"])
            entities = self.process_ner(record["ner"])
            inferece_record = {
                **sentiment,
                **classification,
                "entities":entities,
                "summary":record["summarized_text"],
                "link":record["link"],
                "publish_date":record["publish_date"],
                "title":record["title"],
                "source":record["source"]
            }

            processed_inference.append(inferece_record)
        return processed_inference


    def process_sentiment(self, sentiment_record:List[str]):
        sentiment_label = sentiment_record[0]["label"]
        sentiment_result = {
            "sentiment_label":sentiment_label.lower(),
            "sentiment":sentiment_record[0][sentiment_label.lower()] if sentiment_label == "POSITIVE" else sentiment_record[0][sentiment_label.lower()]*-1
        }
        return sentiment_result

    def process_classification(self, classification_record:List[str]):
        classification_records = classification_record[0]
        if len(classification_records) > 0:
            max_score = classification_records[0]["score"]
            max_index = 0
            for i in range(len(classification_records)):
                if classification_records[i]["score"] > max_score:
                    max_score = classification_records[i]["score"]
                    max_index = i
            topic_result = {"topic":classification_records[max_index]["label"]}
        else:
            topic_result = {"topic":""}
        return topic_result

    def process_ner(self, ner_record:List[str]):
        ner_records = ner_record[0]
        if len(ner_records) > 0:
            seen = set()
            unique_records = []
            for record in ner_records:
                if record["value"] not in seen:
                    seen.add(record["value"])
                    unique_records.append(record)
            entities = unique_records
        else:
            entities = []
        return entities