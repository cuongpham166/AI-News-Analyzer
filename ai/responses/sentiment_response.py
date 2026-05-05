from pydantic import BaseModel


class SentimentResult(BaseModel):
    label: str
    score: float


class SentimentResponse(BaseModel):
    results: list[SentimentResult]
