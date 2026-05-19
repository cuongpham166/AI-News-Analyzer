import json
import asyncio
import tldextract

from data_pipeline.pipeline.article_service.article_processor import ArticleProcessor

from data_pipeline.nats.client import create_js
from data_pipeline.nats.streams import ensure_stream, ENRICHED_SUBJECT, AI_SUBJECT
from data_pipeline.config.article_config import get_postgres_config


class ArticleConsumer:
    def __init__(self, js=None, conn_params=None):
        self.js = js
        self.conn_params = conn_params
        self.db_processor = ArticleProcessor(self.conn_params)
        self.db_processor.connect()
        self.db_processor.create_init_tables()

    def check_connection(self):
        self.db_processor.check_connection()

    async def process_enriched_message(self, msg):
        enriched_article = json.loads(msg.data.decode())
        try:
            self.db_processor.insert_news_data(enriched_article)
            await msg.ack()
        except Exception as e:
            print(f"Error processing enriched article: {e}")
            await msg.nak(delay=5)

    async def process_ai_message(self, msg):
        ai_article = json.loads(msg.data.decode())
        try:
            self.db_processor.update_news_data(ai_article)
            await msg.ack()
        except Exception as e:
            print(f"Error processing ai article: {e}")
            await msg.nak(delay=5)

    async def retrieve_enriched_articles(self):
        sub = await self.js.subscribe(
            ENRICHED_SUBJECT,
            durable="article-consumer-enriched",
            deliver_policy="all",
            ack_wait=30,
            max_deliver=5,
            manual_ack=True,
        )
        print(f"Subscribed to {ENRICHED_SUBJECT}. Waiting for messages...")
        async for msg in sub.messages:
            await self.process_enriched_message(msg)

    async def retrieve_ai_articles(self):
        sub = await self.js.subscribe(
            AI_SUBJECT,
            durable="article-consumer-ai",
            deliver_policy="all",
            ack_wait=30,
            max_deliver=5,
            manual_ack=True,
        )
        print(f"Subscribed to {AI_SUBJECT}. Waiting for messages...")
        async for msg in sub.messages:
            await self.process_ai_message(msg)

    async def publish_article(self, article: dict):
        await self.js.publish(
            ENRICHED_SUBJECT,
            json.dumps(article).encode()
        )

    async def recover_missing_data(self):
        print("recover_missing_data")
        missing_news = self.db_processor.fetch_missing_data()
        for row in missing_news:
            sql_timestamp = row[1]
            epoch_seconds = int(sql_timestamp.timestamp())

            ext = tldextract.extract(row[2])
            domain_name = ext.domain.upper()

            enriched_article = {
                "title": row[0],
                "publish_date": epoch_seconds,
                "source": domain_name,
                "link": row[2],
                "language": row[3],
                "text": row[4]
            }
            await self.publish_article(enriched_article)


async def main():
    js = await create_js()
    await ensure_stream(js)
    conn_params = get_postgres_config()
    postgres_consumer = ArticleConsumer(js, conn_params)
    await asyncio.gather(
        postgres_consumer.retrieve_enriched_articles(),
        postgres_consumer.retrieve_ai_articles(),
        postgres_consumer.recover_missing_data()
    )


if __name__ == "__main__":
    asyncio.run(main())
