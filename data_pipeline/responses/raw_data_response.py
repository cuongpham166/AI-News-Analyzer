from pydantic import BaseModel


class RawDataResponse(BaseModel):
    title: str
    link: str
    summary: str
    rss_pub_date: str
