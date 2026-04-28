package com.example.news.api.util;

import org.springframework.stereotype.Component;

@Component
public class DiscoveryQuery {
    public DiscoveryQuery(){};

    public String createSourceConstraintQuery(){
        String query = "CREATE CONSTRAINT source_name_unique IF NOT EXISTS "
                + "FOR (s:Source) "
                + "REQUIRE s.name IS UNIQUE;";
        return query;
    }

    public String createTopicConstraintQuery(){
        String query = "CREATE CONSTRAINT topic_name_unique IF NOT EXISTS "
                + "FOR (t:Topic) "
                + "REQUIRE t.name IS UNIQUE;";
        return query;
    }

    public String createEntityConstraintQuery(){
        String query = "CREATE CONSTRAINT entity_name_unique IF NOT EXISTS "
                + "FOR (e:Entity) "
                + "REQUIRE e.name IS UNIQUE;";
        return query;
    }


    public String createNewsConstraintQuery(){
        String query = "CREATE CONSTRAINT news_link_unique IF NOT EXISTS "
                + "FOR (n:News) "
                + "REQUIRE n.link IS UNIQUE;";
        return query;
    }

    public String integrateDataIntoNeo4jQuery(){
        String query = "MERGE (s:Source {name: $source_name}) "
                + "MERGE (t:Topic {name: $topic_name}) "
                + "MERGE (n:News {link: $link}) "
                + "ON CREATE SET "
                + " n.title = $title, "
                + " n.publish_date = $publish_date, "
                + " n.sentiment = $sentiment "
                + "MERGE (s)-[:PUBLISHED]->(n) "
                + "MERGE (n)-[:COVERS]->(t)";
        return query;
    }

    public String integrateEntityDataIntoNeo4jQuery(){
        String query = "MATCH (n:News {link: $news_link}) "
                + "MERGE (e:Entity {value: $entity_name}) "
                + "MERGE (n)-[:MENTIONS]->(e) "
                + "WITH n, e "                             // 2. Find all OTHER entities mentioned in this SAME news article
                + "MATCH (n)-[:MENTIONS]->(other:Entity) " 
                + "WHERE e <> other "                      //Don't link an entity to itself
                + "MERGE (e)-[r:CO_OCCURS_WITH]-(other) "
                + "ON CREATE SET r.weight = 1, r.last_seen = n.publish_date "
                + "ON MATCH SET r.weight = r.weight + 1, r.last_seen = n.publish_date";
        return query;
    }

}
