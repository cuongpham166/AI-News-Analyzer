from typing import List

import feedparser
import asyncio
import aiohttp

from data_pipeline.responses.raw_data_response import RawDataResponse

class IngestionProcessor:
    def __init__(self, rss_urls):
        self.rss_urls = rss_urls
        self.seen_links = set()

    async def fetch_feed(self, session, url):
        try:
            async with session.get(url, timeout=10) as resp:
                resp.raise_for_status()
                content = await resp.read()
                return feedparser.parse(content)
        except Exception as e:
            print(f"Error fetching {url}: {e}")
            return None

    async def scrape(self) -> list[RawDataResponse]:
        self.seen_links.clear()
        results: list[RawDataResponse] = []

        async with aiohttp.ClientSession() as session:
            tasks = [self.fetch_feed(session, url) for url in self.rss_urls]
            feeds = await asyncio.gather(*tasks, return_exceptions=True)

        for feed in feeds:
            if isinstance(feed, Exception):
                print(f"Feed error: {feed}")
                continue
            if not feed:
                continue
                
            for entry in feed.entries:
                link = getattr(entry, "link", "")
                if not link or link in self.seen_links:
                    continue
                self.seen_links.add(link)
                results.append(
                    RawDataResponse(
                        title=getattr(entry, "title", ""),
                        link=link,
                        summary=getattr(entry, "summary", ""),
                        rss_pub_date=getattr(entry, "published", "")
                    )
                )
        return results
