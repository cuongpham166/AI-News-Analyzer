import json
import asyncio
from nats.aio.client import Client as NATS
import nats
import hashlib

from nats.js.errors import NotFoundError

from ai.inference.inference_service.inference_processor import InferenceProcessor
from ai.responses.inference_news_response import InferenceNewsResponse
import os
from dotenv import load_dotenv

from data_pipeline.nats.client import create_js
from data_pipeline.nats.streams import ensure_stream, ENRICHED_SUBJECT, AI_SUBJECT, STREAM_NAME

load_dotenv()
nats_url = os.getenv("NATS_URL")

semaphore = asyncio.Semaphore(4)  # allow 4 concurrent inferences


class InferenceConsumer:
    def __init__(self, js=None):
        self.js = js
        self.inference_processor = InferenceProcessor()

    async def publish_article(self, article: InferenceNewsResponse):
        await self.js.publish(
            AI_SUBJECT,
            article.model_dump_json().encode()
        )

    async def process_message(self, msg):
        processed_article = json.loads(msg.data.decode())
        try:
            inference_result = self.inference_processor.analyze([processed_article]).results
            for res in inference_result:
                inference_news = InferenceNewsResponse(
                    link=res.link,
                    summary=res.summarization,
                    sentiment_label=res.sentiment.label,
                    sentiment=res.sentiment.score,
                    topic=res.classification.topic,
                    entities=res.ner.entities
                )
                await self.publish_article(inference_news)
                await msg.ack()

        except Exception as e:
            print(f"Error processing article: {e}")
            await msg.term()

    async def run(self):
        try:
            await self.js.delete_consumer(STREAM_NAME, "enriched-articles-consumer-1")
        except Exception:
            pass

        sub = await self.js.subscribe(
            ENRICHED_SUBJECT,
            durable="enriched-articles-consumer-1",
            deliver_policy="all",
            manual_ack=True
        )
        print(f"Subscribed to {ENRICHED_SUBJECT}. Waiting for messages...")
        async for msg in sub.messages:
            await self.process_message(msg)


async def main():
    js = await create_js(nats_url)
    await ensure_stream(js)
    inference_consumer = InferenceConsumer(js)
    await inference_consumer.run()


if __name__ == "__main__":
    asyncio.run(main())
