import feedparser
from bs4 import BeautifulSoup
import ftfy
from rapidfuzz import fuzz
from langdetect import detect
from newspaper import Article
import hashlib
import json
import asyncio
import random

from nats.aio.client import Client as NATS
import nats 

import os
from dotenv import load_dotenv
load_dotenv()
nats_url = os.getenv("NATS_URL")

STREAM_NAME = "ARTICLES"
RAW_SUBJECT = "articles.raw"
ENRICHED_SUBJECT = "articles.enriched"

class RssScraper:
    def __init__(self, rss_url, js=None):
        self.rss_url = rss_url
        self.js = js
        self.seen_links = set()

    async def publish_article(self, article: dict):
        await self.js.publish(
            RAW_SUBJECT, 
            json.dumps(article).encode()
        )

    async def scrap_article(self):
        for news_url in self.rss_url:
            feed = feedparser.parse(news_url)
            for entry in feed.entries:
                link = getattr(entry, "link", "")
                if not link or link in self.seen_links:
                    continue
                self.seen_links.add(link)
                raw_date = getattr(entry, "published", "")
                article_dict = {
                    "title": getattr(entry, "title", ""),
                    "link": link,
                    "summary": getattr(entry, "summary", ""),
                    "rss_pub_date": raw_date
                }
                await self.publish_article(article_dict)
                print(f"Published raw article: {link}")

    async def run_periodically(self):
        while True:
            try:
                await self.scrap_article()
            except Exception as e:
                print(f"RSS scraper error: {e}")
            wait_time = 6 * 60 * 60
            print(f"RSS scraper sleeping {wait_time}s\n")
            await asyncio.sleep(wait_time)


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

    rss_arr = [
        "https://news.un.org/feed/subscribe/en/news/all/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/region/global/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/region/middle-east/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/region/africa/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/region/europe/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/region/americas/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/region/asia-pacific/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/topic/health/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/topic/un-affairs/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/topic/law-and-crime-prevention/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/topic/human-rights/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/topic/humanitarian-aid/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/topic/climate-change/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/topic/culture-and-education/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/topic/economic-development/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/topic/women/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/topic/peace-and-security/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/topic/migrants-and-refugees/feed/rss.xml",
        "https://news.un.org/feed/subscribe/en/news/topic/sdgs/feed/rss.xml",
    ]
    scraper = RssScraper(rss_arr, js)
    await scraper.run_periodically()

if __name__ == "__main__":
    asyncio.run(main())
