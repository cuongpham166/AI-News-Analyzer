from pydantic import BaseModel


class ClassificationResult(BaseModel):
    topic: str


class ClassificationResponse(BaseModel):
    results: list[ClassificationResult]
