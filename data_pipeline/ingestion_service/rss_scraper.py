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
import aiohttp
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
    def __init__(self, rss_urls, js=None, poll_interval=300):
        self.rss_urls = rss_urls
        self.js = js
        self.seen_links = set()
        self.poll_interval = poll_interval


    async def publish_article(self, article: dict):
        try:
            await asyncio.wait_for(
                self.js.publish(RAW_SUBJECT,json.dumps(article).encode()),
                timeout=10
            )
        except asyncio.TimeoutError:
            print(f"Publish timeout: {article.get('link')}")

    async def fetch_feed(self, session, url):
        try:
            async with session.get(url, timeout=10) as resp:
                resp.raise_for_status()
                content = await resp.read()
                return feedparser.parse(content)
        except Exception as e:
            print(f"Error fetching {url}: {e}")
            return None
        
    async def scrape_once(self):
        async with aiohttp.ClientSession() as session:
            tasks = [self.fetch_feed(session, url) for url in self.rss_urls]
            feeds = await asyncio.gather(*tasks, return_exceptions=True)

        new_articles = 0
        for feed in feeds:
            if not feed or isinstance(feed, Exception):
                continue
            for entry in feed.entries:
                link = getattr(entry, "link", "")
                if not link or link in self.seen_links:
                    continue
                self.seen_links.add(link)
                article_dict = {
                    "title": getattr(entry, "title", ""),
                    "link": link,
                    "summary": getattr(entry, "summary", ""),
                    "rss_pub_date": getattr(entry, "published", "")
                }
                await self.publish_article(article_dict)
                print(f"Published raw article: {link}")
                new_articles += 1
        return new_articles

    async def run_continuously(self):
        print("Starting continuous RSS scraping...")
        while True:
            try:
                new_articles = await self.scrape_once()
                if new_articles:
                    print(f"Scraped {new_articles} new articles.")
            except Exception as e:
                print(f"RSS scraper error: {e}")

            await asyncio.sleep(self.poll_interval)


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
        "https://rss.dw.com/rdf/rss-en-all"
    ]
    scraper = RssScraper(rss_arr, js, poll_interval=300)  # 5 min polling
    await scraper.run_continuously()

if __name__ == "__main__":
    asyncio.run(main())
