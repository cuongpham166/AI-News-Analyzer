import json
import os
import asyncio
from dotenv import load_dotenv

from data_pipeline.nats.client import create_js
from data_pipeline.nats.streams import ensure_stream, RAW_SUBJECT
from data_pipeline.responses.raw_data_response import RawDataResponse
from data_pipeline.ingestion_service.raw_data_scraper import RawDataScraper

load_dotenv()
nats_url = os.getenv("NATS_URL")


class RawDataProducer:
    def __init__(self, js=None, scraper=None, poll_interval=300):
        self.js = js
        self.poll_interval = poll_interval
        self.scraper = scraper

    async def publish_article(self, article: RawDataResponse):
        try:
            ack = await asyncio.wait_for(
                self.js.publish(
                    RAW_SUBJECT,
                    article.model_dump_json().encode()
                ),
                timeout=10
            )
            print(f"Published seq: {ack.seq}")
        except asyncio.TimeoutError:
            print(f"Publish timeout: {article.link}")

    async def run(self):
        while True:
            try:
                new_articles: list[RawDataResponse] = await self.scraper.scrape()
                print(f"Scraped {len(new_articles)} new articles")
                if new_articles:
                    await asyncio.gather(
                        *(self.publish_article(article) for article in new_articles)
                    )
            except Exception as e:
                print(f"RSS scraper error: {e}")
            await asyncio.sleep(self.poll_interval)


async def main():
    rss_urls = ["https://news.un.org/feed/subscribe/en/news/all/rss.xml"]
    js = await create_js(nats_url)
    await ensure_stream(js)
    scraper = RawDataScraper(rss_urls)
    raw_data_producer = RawDataProducer(js, scraper, poll_interval=300)
    await raw_data_producer.run()


if __name__ == "__main__":
    asyncio.run(main())
