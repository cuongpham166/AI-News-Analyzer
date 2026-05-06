import os
import asyncio
import json
from dotenv import load_dotenv

from data_pipeline.nats.client import create_js
from data_pipeline.nats.streams import ensure_stream, RAW_SUBJECT, ENRICHED_SUBJECT
from data_pipeline.processing_service.raw_data_processor import RawDataProcessor
from data_pipeline.responses.processed_data_response import ProcessedDataResponse

load_dotenv()
nats_url = os.getenv("NATS_URL")


class RawDataConsumer:
    def __init__(self, js, processor):
        self.js = js
        self.processor = processor

    async def handle(self, msg):
        try:
            raw_data = json.loads(msg.data.decode())
            processed_result: ProcessedDataResponse = await self.processor.process(raw_data)
            processed_msg = processed_result.model_dump_json().encode()
            if processed_msg:
                await self.js.publish(
                    ENRICHED_SUBJECT,
                    processed_msg
                )
            await msg.ack()
        except Exception as e:
            print(f"Error: {e}")
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

        print(f"Subscribed to {RAW_SUBJECT}")

        async for msg in sub.messages:
            await self.handle(msg)


async def main():
    js = await create_js(nats_url)
    await ensure_stream(js)

    processor = RawDataProcessor()
    consumer = RawDataConsumer(js, processor)

    await consumer.run()
