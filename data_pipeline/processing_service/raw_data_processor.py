from langdetect import detect
from newspaper import Article
import tldextract
from datetime import datetime
from email.utils import parsedate_to_datetime

from data_pipeline.responses.processed_data_response import ProcessedDataResponse
import json
import asyncio


class RawDataProcessor:
    def __init__(self):
        self.enriched_links = set()

    def is_duplicate(self, link):
        return link in self.enriched_links

    async def process_message(self, msg: dict) -> ProcessedDataResponse | None:
        raw_data = json.loads(msg.data.decode())
        link = raw_data.get("link")
        rss_date_str = raw_data.get("rss_pub_date")

        if not link or self.is_duplicate(link):
            return None

        article_obj = Article(link)

        await asyncio.to_thread(article_obj.download)
        await asyncio.to_thread(article_obj.parse)

        # --- timestamp logic ---
        if rss_date_str:
            try:
                dt_object = parsedate_to_datetime(rss_date_str)
                timestamp = int(dt_object.timestamp())
            except Exception:
                timestamp = int(datetime.now().timestamp())
        elif article_obj.publish_date:
            timestamp = int(article_obj.publish_date.timestamp())
        else:
            timestamp = int(datetime.now().timestamp())

        ext = tldextract.extract(link)
        domain_name = ext.domain.upper()

        self.enriched_links.add(link)

        return ProcessedDataResponse(
            title=article_obj.title,
            publish_date=timestamp,
            source=domain_name,
            link=link,
            language=detect(article_obj.text[:500]),
            text=article_obj.text
        )
