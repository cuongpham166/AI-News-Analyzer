from pydantic import BaseModel


class ProcessedDataResponse(BaseModel):
    title: str
    publish_date: int
    source: str
    link: str
    language: str
    text: str
