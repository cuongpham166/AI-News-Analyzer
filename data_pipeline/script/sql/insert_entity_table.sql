INSERT INTO entity (value, entity_type_id)
SELECT %s, et.id
FROM entity_type et
WHERE et.name = %s
ON CONFLICT (value) DO NOTHING;