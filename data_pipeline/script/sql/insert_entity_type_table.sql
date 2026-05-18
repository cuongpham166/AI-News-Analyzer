INSERT INTO entity_type (name)
VALUES (%s) 
ON CONFLICT (name) DO NOTHING;