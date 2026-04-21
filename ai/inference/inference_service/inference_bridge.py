import json
import asyncio
from nats.aio.client import Client as NATS
import nats 
import hashlib

from ai.inference.inference_service.inference_service import InferenceService

import os
from dotenv import load_dotenv
load_dotenv()
nats_url = os.getenv("NATS_URL")

STREAM_NAME = "ARTICLES"
RAW_SUBJECT = "articles.raw"
ENRICHED_SUBJECT = "articles.enriched"
AI_SUBJECT = "articles.ai"

semaphore = asyncio.Semaphore(4)  # allow 4 concurrent inferences

class InferenceBridge:
    def __init__(self, js=None):
        self.js = js
        self.inference_service = InferenceService()

    async def publish_article(self, article: dict):
        await self.js.publish(
            AI_SUBJECT, 
            json.dumps(article).encode()
        )

    async def process_message(self, msg):
        saved_article = json.loads(msg.data.decode())
        try:
            inference_result =self.inference_service.analyze([saved_article])
            await self.publish_article(inference_result[0])
            await msg.ack()

        except Exception as e:
            print(f"Error processing article: {e}")
            await msg.term()


    async def retrieve_enriched_articles(self):
        sub = await self.js.subscribe(ENRICHED_SUBJECT, durable="enriched-articles-consumer-1",deliver_policy="new",manual_ack=True)
        print(f"Subscribed to {ENRICHED_SUBJECT}. Waiting for messages...")
        async for msg in sub.messages:
            #print("Inference_Bridge:retrieve_enriched_articles: ",json.loads(msg.data.decode()))
            await self.process_message(msg)


async def ensure_stream(js):
    try:
        await js.stream_info(STREAM_NAME)
    except NotFoundError:
        await js.add_stream(name=STREAM_NAME, subjects=[RAW_SUBJECT, ENRICHED_SUBJECT,AI_SUBJECT])
        print(f"Stream '{STREAM_NAME}' created")

async def main():
    nc = await nats.connect(nats_url)
    js = nc.jetstream()
    await ensure_stream(js)
    inference_bridge = InferenceBridge(js)
    await inference_bridge.retrieve_enriched_articles()

if __name__ == "__main__":
    asyncio.run(main())