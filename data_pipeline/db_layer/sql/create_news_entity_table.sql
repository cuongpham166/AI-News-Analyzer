CREATE TABLE IF NOT EXISTS news_entity (
    id SERIAL PRIMARY KEY,
    news_id integer NOT NULL,
    entity_id integer NOT NULL,
    UNIQUE(news_id, entity_id),
    FOREIGN KEY (news_id) REFERENCES news(id),
    FOREIGN KEY (entity_id) REFERENCES entity(id)
);