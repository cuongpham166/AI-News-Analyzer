INSERT INTO news(link, summary, sentiment_label, sentiment, topic_id)
VALUES (%s, %s, %s, %s, (SELECT id FROM topic WHERE name = %s))
ON CONFLICT (link) DO UPDATE
SET summary = EXCLUDED.summary,
    sentiment_label = EXCLUDED.sentiment_label,
    sentiment = EXCLUDED.sentiment,
    topic_id = EXCLUDED.topic_id;