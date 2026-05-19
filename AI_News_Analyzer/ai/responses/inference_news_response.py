from pydantic import BaseModel

from ai.responses.ner_response import NerEntity


class InferenceNewsResponse(BaseModel):
    link: str
    summary: str
    sentiment_label: str
    sentiment: float
    topic: str
    entities: list[NerEntity]
