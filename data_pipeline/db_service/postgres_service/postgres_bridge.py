import time
import json
import asyncio
from nats.aio.client import Client as NATS
import nats 
from datetime import datetime, timezone
import tldextract

from data_pipeline.db_service.postgres_service.postgres_layer import PostgresLayer

import os
from dotenv import load_dotenv
load_dotenv()
nats_url = os.getenv("NATS_URL")

postgres_host = os.getenv("POSTGRES_HOST")
postgres_port = os.getenv("POSTGRES_PORT")
postgres_dbname = os.getenv("POSTGRES_DBNAME")
postgres_user = os.getenv("POSTGRES_USER")
postgres_password = os.getenv("POSTGRES_PASSWORD")

STREAM_NAME = "ARTICLES"
RAW_SUBJECT = "articles.raw"
ENRICHED_SUBJECT = "articles.enriched"
AI_SUBJECT = "articles.ai"


conn_params = {
    "host": postgres_host,
    "port": postgres_port,
    "dbname": postgres_dbname,
    "user": postgres_user,
    "password": postgres_password
}

class PostgresBridge:
    def __init__(self, js=None,conn_params=conn_params):
        self.js = js
        self.conn_params = conn_params
        self.db_layer = PostgresLayer(self.conn_params)
        self.db_layer.connect()

    def check_connection(self):
        self.db_layer.check_connection()

    async def process_enriched_message(self, msg):
        enriched_ariticle = json.loads(msg.data.decode())
        try:
            self.db_layer.insert_news_data(enriched_ariticle)
            await msg.ack()
        except Exception as e:
            print(f"Error processing enriched article: {e}")
            await msg.term()


    async def process_ai_message(self, msg):
        ai_ariticle = json.loads(msg.data.decode())
        try:
            if ai_ariticle["source"] == "DW":
                print("process_ai_message: ", ai_ariticle)
                
            self.db_layer.update_news_data(ai_ariticle)
            await msg.ack()
        except Exception as e:
            print(f"Error processing ai article: {e}")
            await msg.term()


    async def retrieve_enriched_articles(self):
        sub = await self.js.subscribe(ENRICHED_SUBJECT, durable="enriched-articles-consumer-postgres",deliver_policy="new",manual_ack=True)
        print(f"Subscribed to {ENRICHED_SUBJECT}. Waiting for messages...")
        async for msg in sub.messages:
            await self.process_enriched_message(msg)

    async def retrieve_ai_articles(self):
        sub = await self.js.subscribe(AI_SUBJECT, durable="ai-articles-consumer-postgres",deliver_policy="new",manual_ack=True)
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
        missing_news = self.db_layer.fetch_missing_data()
        for row in missing_news:
            sql_timestamp = row[1]
            epoch_seconds = int(sql_timestamp.timestamp())

            ext = tldextract.extract(row[2])
            domain_name = (ext.domain).upper()

            enriched_article = {
                "title": row[0],
                "publish_date": epoch_seconds,
                "source":domain_name,
                "link": row[2],
                "language": row[3],
                "text": row[4]
            }
            await self.publish_article(enriched_article)
            
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
    db_brige = PostgresBridge(js,conn_params)
    await asyncio.gather(
        db_brige.retrieve_enriched_articles(),
        db_brige.retrieve_ai_articles(),
        db_brige.recover_missing_data()
    )

if __name__ == "__main__":
    asyncio.run(main())