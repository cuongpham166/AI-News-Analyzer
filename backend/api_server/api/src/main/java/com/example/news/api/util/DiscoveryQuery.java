package com.example.news.api.util;

import org.springframework.stereotype.Component;

@Component
public class DiscoveryQuery {
    public DiscoveryQuery(){};

    public String createSourceConstraintQuery(){
        return "CREATE CONSTRAINT source_name_unique IF NOT EXISTS "
                + "FOR (s:Source) "
                + "REQUIRE s.name IS UNIQUE;";
    }

    public String createTopicConstraintQuery(){
        return "CREATE CONSTRAINT topic_name_unique IF NOT EXISTS "
                + "FOR (t:Topic) "
                + "REQUIRE t.name IS UNIQUE;";
    }

    public String createEntityConstraintQuery(){
        return "CREATE CONSTRAINT entity_name_unique IF NOT EXISTS "
                + "FOR (e:Entity) "
                + "REQUIRE e.name IS UNIQUE;";
    }


    public String createNewsConstraintQuery(){
        return "CREATE CONSTRAINT news_link_unique IF NOT EXISTS "
                + "FOR (n:News) "
                + "REQUIRE n.link IS UNIQUE;";
    }

    public String integrateDataIntoNeo4jQuery(){
        return "MERGE (s:Source {name: $source_name}) "
                + "MERGE (t:Topic {name: $topic_name}) "
                + "MERGE (n:News {link: $link}) "
                + "ON CREATE SET "
                + " n.title = $title, "
                + " n.publish_date = $publish_date, "
                + " n.sentiment = $sentiment "
                + "MERGE (s)-[:PUBLISHED]->(n) "
                + "MERGE (n)-[:COVERS]->(t)";
    }

    public String integrateEntityDataIntoNeo4jQuery(){
        return "MATCH (n:News {link: $news_link}) "
                + "MERGE (e:Entity {value: $entity_name}) "
                + "MERGE (n)-[:MENTIONS]->(e) "
                + "WITH n, e "                             // 2. Find all OTHER entities mentioned in this SAME news article
                + "MATCH (n)-[:MENTIONS]->(other:Entity) "
                + "WHERE e <> other "                      //Don't link an entity to itself
                + "MERGE (e)-[r:CO_OCCURS_WITH]-(other) "
                + "ON CREATE SET r.weight = 1, r.last_seen = n.publish_date "
                + "ON MATCH SET r.weight = r.weight + 1, r.last_seen = n.publish_date";
    }

    public String getDiscoveryGraphQuery() {
        return "MATCH (n:News)-[:MENTIONS]->(e:Entity) " +
                "WHERE n.publish_date >= $startTime AND n.publish_date <= $endTime " +
                "WITH n, e " +
                "MATCH (n)-[:MENTIONS]->(other:Entity) " +
                "WHERE id(e) < id(other) " +
                "WITH e, other, count(n) AS weight, avg(n.sentiment) AS sentiment " +
                "WHERE weight > 1 " + // Only show connections appearing in 2+ articles
                "RETURN " +
                "  e.value AS source, " +
                "  labels(e)[0] AS sourceGroup, " +
                "  other.value AS target, " +
                "  labels(other)[0] AS targetGroup, " +
                "  weight, " +
                "  sentiment " +
                "ORDER BY weight DESC " +
                "LIMIT 300"; // Prevents "hairball" graph
    }
}
