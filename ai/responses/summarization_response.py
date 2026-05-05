from pydantic import BaseModel


class SummarizationResponse(BaseModel):
    results: list[str]
