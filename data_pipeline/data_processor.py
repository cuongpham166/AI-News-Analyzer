from bs4 import BeautifulSoup
import ftfy
from rapidfuzz import fuzz
from langdetect import detect
from newspaper import Article
import tldextract
from datetime import datetime
import hashlib
import json
import asyncio
from nats.aio.client import Client as NATS
import nats 
from email.utils import parsedate_to_datetime

import os
from dotenv import load_dotenv
load_dotenv()
nats_url = os.getenv("NATS_URL")

STREAM_NAME = "ARTICLES"
RAW_SUBJECT = "articles.raw"
ENRICHED_SUBJECT = "articles.enriched"

class DataProcessor:
    def __init__(self, js=None):
        self.js = js
        self.enriched_links = set()  # deduplication

    async def publish_article(self, article: dict):
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
                "source":domain_name,
                "link": link,
                "language": detect(article_obj.text[:500]),
                "text": article_obj.text
            }
            self.enriched_links.add(link)
            await self.publish_article(enriched_article)
            #print(f"Published enriched article: {article_obj.title}")
            #print(f"Published enriched article: {enriched_article}")
            await msg.ack()
        except Exception as e:
            print(f"Error processing article {link}: {e}")
            await msg.term()

    async def retrieve_raw_articles(self):
        sub = await self.js.subscribe(RAW_SUBJECT, durable="raw-articles-consumer",deliver_policy="new",manual_ack=True)
        print(f"Subscribed to {RAW_SUBJECT}. Waiting for messages...")
        async for msg in sub.messages:
            await self.process_message(msg)

async def ensure_stream(js):
    try:
        await js.stream_info(STREAM_NAME)
    except NotFoundError:
        await js.add_stream(name=STREAM_NAME, subjects=[RAW_SUBJECT, ENRICHED_SUBJECT])
        print(f"Stream '{STREAM_NAME}' created")

async def main():
    nc = await nats.connect(nats_url)
    js = nc.jetstream()
    await ensure_stream(js)
    processor = DataProcessor(js)
    await processor.retrieve_raw_articles()

if __name__ == "__main__":
    asyncio.run(main())