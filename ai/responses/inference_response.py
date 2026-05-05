from pydantic import BaseModel
from ai.responses.sentiment_response import SentimentResult
from ai.responses.classification_response import ClassificationResult
from ai.responses.ner_response import NerResult


class InferenceResult(BaseModel):
    link: str
    publish_date: int
    title: str
    source: str
    sentiment: SentimentResult
    classification: ClassificationResult
    ner: NerResult
    summarization: str


class InferenceResponse(BaseModel):
    results: list[InferenceResult]
