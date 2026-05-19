import nats
import os
from dotenv import load_dotenv

load_dotenv()

async def create_js():
    nats_url = os.getenv("NATS_URL")
    nc = await nats.connect(nats_url)
    return nc.jetstream()
