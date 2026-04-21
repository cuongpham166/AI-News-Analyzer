import json
import asyncio
from nats.aio.client import Client as NATS
import nats 
from datetime import datetime, timezone
from data_pipeline.db_service.elastic_service.elastic_layer import ElasticLayer

import os
from dotenv import load_dotenv
load_dotenv()
nats_url = os.getenv("NATS_URL")

STREAM_NAME = "ARTICLES"
RAW_SUBJECT = "articles.raw"
ENRICHED_SUBJECT = "articles.enriched"
SAVED_SUBJECT = "articles.saved"
AI_SUBJECT = "articles.ai"

class ElasticBridge:
    def __init__(self, js=None):
        self.js = js
        self.elastic_layer = ElasticLayer()

    async def process_ai_message(self, msg):
        ai_ariticle = json.loads(msg.data.decode())
        print("Elstic Bridge:process_ai_message: ",ai_ariticle)
        try:
            index_time = datetime.now(timezone.utc).isoformat()
            ai_ariticle["@timestamp"] = index_time
            self.elastic_layer.index_news_document(ai_ariticle)
            await msg.ack()
        except Exception as e:
            print(f"Error processing ai article: {e}")
            await msg.term()

    async def retrieve_ai_articles(self):
        sub = await self.js.subscribe(AI_SUBJECT, durable="ai-articles-consumer-2",deliver_policy="new",manual_ack=True)
        print(f"Subscribed to {AI_SUBJECT}. Waiting for messages...")
        async for msg in sub.messages:
            await self.process_ai_message(msg)


async def ensure_stream(js):
    try:
        await js.stream_info(STREAM_NAME)
    except Exception as e:
        await js.add_stream(name=STREAM_NAME, subjects=[RAW_SUBJECT, ENRICHED_SUBJECT,SAVED_SUBJECT,AI_SUBJECT])
        print(f"Stream '{STREAM_NAME}' created")


async def main():
    nc = await nats.connect(nats_url)
    js = nc.jetstream()
    await ensure_stream(js)
    elastic_bridge = ElasticBridge(js)
    await elastic_bridge.retrieve_ai_articles()

if __name__ == "__main__":
    asyncio.run(main())