CREATE TABLE IF NOT EXISTS news (
    id SERIAL PRIMARY KEY,
    title TEXT,
    publish_date TIMESTAMP,
    link TEXT UNIQUE NOT NULL,
    lang TEXT,
    full_text TEXT,
    summary TEXT,
    sentiment_label TEXT,
    sentiment NUMERIC(4,2),
    topic_id integer REFERENCES topic(id),
    source_id integer REFERENCES source(id)
);