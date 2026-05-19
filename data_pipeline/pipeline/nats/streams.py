from nats.js.errors import NotFoundError

STREAM_NAME = "ARTICLES"
RAW_SUBJECT = "articles.raw"
ENRICHED_SUBJECT = "articles.enriched"
SAVED_SUBJECT = "articles.saved"
AI_SUBJECT = "articles.ai"


async def ensure_stream(js):
    try:
        await js.stream_info(STREAM_NAME)
    except NotFoundError:
        await js.add_stream(
            name=STREAM_NAME,
            subjects=[RAW_SUBJECT, ENRICHED_SUBJECT, SAVED_SUBJECT, AI_SUBJECT],
            retention="limits",
            max_msgs=-1,
        )
