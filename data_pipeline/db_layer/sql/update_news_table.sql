UPDATE news
SET summary=%s, sentiment_label=%s, sentiment=%s, topic_id = (
    SELECT id FROM topic t WHERE t.name = %s
) 
WHERE link = %s;