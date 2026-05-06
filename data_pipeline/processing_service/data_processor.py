from langdetect import detect
from newspaper import Article
import tldextract
from datetime import datetime

import json
import asyncio
from nats.aio.client import Client as NATS
import nats
from email.utils import parsedate_to_datetime

import os
from dotenv import load_dotenv

load_dotenv()
nats_url = os.getenv("NATS_URL")

from data_pipeline.nats.client import create_js
from data_pipeline.nats.streams import ensure_stream, ENRICHED_SUBJECT, RAW_SUBJECT


class DataProcessor:
    def __init__(self, js=None):
        self.js = js
        self.enriched_links = set()  # deduplication

    async def publish_article(self, article: dict):
        print("publish_article", article)
        await self.js.publish(
            ENRICHED_SUBJECT,
            json.dumps(article).encode()
        )

    def is_duplicate(self, link):
        return link in self.enriched_links

    async def process_message(self, msg):
        raw_data = json.loads(msg.data.decode())
        link = raw_data.get("link")
        rss_date_str = raw_data.get("rss_pub_date")

        if not link or self.is_duplicate(link):
            await msg.ack()
            return

        article_obj = Article(link)
        try:
            await asyncio.to_thread(article_obj.download)
            await asyncio.to_thread(article_obj.parse)

            if rss_date_str:
                try:
                    dt_object = parsedate_to_datetime(rss_date_str)
                    timestamp = dt_object.timestamp()
                except Exception:
                    timestamp = datetime.now().timestamp()
            elif article_obj.publish_date:
                timestamp = article_obj.publish_date.timestamp()
            else:
                timestamp = datetime.now().timestamp()

            # Ensure timestamp is an integer (epoch seconds)
            timestamp = int(timestamp)

            ext = tldextract.extract(link)
            domain_name = (ext.domain).upper()

            enriched_article = {
                "title": article_obj.title,
                "publish_date": timestamp,
                "source": domain_name,
                "link": link,
                "language": detect(article_obj.text[:500]),
                "text": article_obj.text
            }
            self.enriched_links.add(link)
            await self.publish_article(enriched_article)
            await msg.ack()
        except Exception as e:
            print(f"Error processing article {link}: {e}")
            await msg.nak(delay=5)

    async def run(self):
        sub = await self.js.subscribe(
            RAW_SUBJECT,
            durable="raw-articles-consumer",
            deliver_policy="all",
            ack_wait=30,
            max_deliver=5,
            manual_ack=True,
        )
        print(f"Subscribed to {RAW_SUBJECT}. Waiting for messages...")
        async for msg in sub.messages:
            await self.process_message(msg)


async def main():
    js = await create_js(nats_url)
    await ensure_stream(js)
    processor = DataProcessor(js)
    await processor.run()


if __name__ == "__main__":
    asyncio.run(main())
