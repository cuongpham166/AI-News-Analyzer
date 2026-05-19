CREATE TABLE IF NOT EXISTS entity (
    id SERIAL PRIMARY KEY,
    value TEXT UNIQUE NOT NULL,
    entity_type_id integer REFERENCES entity_type (id)
);