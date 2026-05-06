import nats


async def create_js(nats_url: str):
    nc = await nats.connect(nats_url)
    return nc.jetstream()
