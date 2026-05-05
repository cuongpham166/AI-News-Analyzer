from pydantic import BaseModel


class NerEntity(BaseModel):
    value: str
    type: str


class NerResult(BaseModel):
    entities: list[NerEntity]


class NerResponse(BaseModel):
    results: list[NerResult]
