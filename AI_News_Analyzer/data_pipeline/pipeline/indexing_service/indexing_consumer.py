import json
import asyncio
from datetime import datetime, timezone
from data_pipeline.pipeline.indexing_service.indexing_processor import IndexingProcessor


from data_pipeline.nats.client import create_js
from data_pipeline.nats.streams import ensure_stream, ENRICHED_SUBJECT, AI_SUBJECT, SAVED_SUBJECT
from data_pipeline.config.indexing_config import get_elasticsearch_config

class IndexingConsumer:
    def __init__(self, js=None, elastic_config=None):
        self.js = js
        self.elastic_processor = IndexingProcessor(elastic_config)

    async def process_ai_message(self, msg):
        ai_article = json.loads(msg.data.decode())
        print("Elastic Bridge:process_ai_message: ", ai_article)
        try:
            index_time = datetime.now(timezone.utc).isoformat()
            ai_article["@timestamp"] = index_time
            self.elastic_processor.index_news_document(ai_article)
            await msg.ack()
        except Exception as e:
            print(f"Error processing ai article: {e}")
            await msg.nak(delay=5)

    async def run(self):
        sub = await self.js.subscribe(
            AI_SUBJECT,
            durable="indexing-consumer",
            deliver_policy="all",
            ack_wait=30,
            max_deliver=5,
            manual_ack=True,
        )
        print(f"Subscribed to {AI_SUBJECT}. Waiting for messages...")
        async for msg in sub.messages:
            await self.process_ai_message(msg)


async def main():
    js = await create_js()
    await ensure_stream(js)
    elastic_config = get_elasticsearch_config()
    elastic_consumer = IndexingConsumer(js,elastic_config)
    await elastic_consumer.run()


if __name__ == "__main__":
    asyncio.run(main())
