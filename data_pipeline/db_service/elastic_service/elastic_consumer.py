import json
import asyncio
from nats.aio.client import Client as NATS
import nats
from datetime import datetime, timezone
from data_pipeline.db_service.elastic_service.elastic_processor import ElasticProcessor

import os
from dotenv import load_dotenv

from data_pipeline.nats.client import create_js
from data_pipeline.nats.streams import ensure_stream, ENRICHED_SUBJECT, AI_SUBJECT, SAVED_SUBJECT

load_dotenv()
nats_url = os.getenv("NATS_URL")


class ElasticConsumer:
    def __init__(self, js=None):
        self.js = js
        self.elastic_processor = ElasticProcessor()

    async def process_ai_message(self, msg):
        ai_ariticle = json.loads(msg.data.decode())
        print("Elstic Bridge:process_ai_message: ", ai_ariticle)
        try:
            index_time = datetime.now(timezone.utc).isoformat()
            ai_ariticle["@timestamp"] = index_time
            self.elastic_processor.index_news_document(ai_ariticle)
            await msg.ack()
        except Exception as e:
            print(f"Error processing ai article: {e}")
            await msg.nak()

    async def run(self):
        sub = await self.js.subscribe(
            AI_SUBJECT,
            durable="ai-articles-consumer-elastic",
            deliver_policy="all",
            manual_ack=True
        )
        print(f"Subscribed to {AI_SUBJECT}. Waiting for messages...")
        async for msg in sub.messages:
            await self.process_ai_message(msg)


async def main():
    js = await create_js(nats_url)
    await ensure_stream(js)
    elastic_consumer = ElasticConsumer(js)
    await elastic_consumer.run()


if __name__ == "__main__":
    asyncio.run(main())
