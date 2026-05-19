import asyncio
from data_pipeline.nats.client import create_js
from data_pipeline.nats.streams import ensure_stream, RAW_SUBJECT
from data_pipeline.responses.raw_data_response import RawDataResponse
from data_pipeline.pipeline.ingestion_service.ingestion_processor import IngestionProcessor
from data_pipeline.config.ingestion_config import get_rss_urls

class IngestionProducer:
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
    rss_urls = get_rss_urls()
    js = await create_js()
    await ensure_stream(js)
    scraper = IngestionProcessor(rss_urls)
    raw_data_producer = IngestionProducer(js, scraper, poll_interval=300)
    await raw_data_producer.run()


if __name__ == "__main__":
    asyncio.run(main())
