INSERT INTO news_entity (news_id,entity_id)
SELECT n.id, e.id
FROM news n, entity e
WHERE n.link = %s AND e.value = %s 
ON CONFLICT (news_id,entity_id) DO NOTHING;