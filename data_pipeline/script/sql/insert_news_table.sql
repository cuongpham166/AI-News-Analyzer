INSERT INTO news (title, publish_date, link, lang, full_text, source_id)
VALUES (
  %s, to_timestamp(%s), %s, %s, %s,
  (SELECT id FROM source WHERE name = %s)
)
ON CONFLICT (link) DO NOTHING
RETURNING id;