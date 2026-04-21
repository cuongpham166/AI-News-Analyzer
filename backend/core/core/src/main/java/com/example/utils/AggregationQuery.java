package com.example.utils;

import java.sql.Timestamp;

public class AggregationQuery {
    public AggregationQuery(){}

    public String getAllNewsQuery (int limit) {
        String query = "SELECT news.*, topic.name AS topic_name, source.name AS source_name "
                + "FROM news "
                + "LEFT JOIN topic ON news.topic_id = topic.id "
                + "LEFT JOIN source ON news.source_id = source.id "
                + "ORDER BY publish_date DESC "
                + "LIMIT ?";
        return query;
    }

    public String getAllNewsQueryWithoutLimit() {
        String query = "SELECT news.*, topic.name AS topic_name, source.name AS source_name "
                + "FROM news "
                + "LEFT JOIN topic ON news.topic_id = topic.id "
                + "LEFT JOIN source ON news.source_id = source.id";
        return query;        
    }

    public String getNewsByLinkQuery (String link) {
        String query = "SELECT news.*, topic.name AS topic_name, source.name AS source_name "
                + "FROM news "
                + "LEFT JOIN topic ON news.topic_id = topic.id "
                + "LEFT JOIN source ON news.source_id = source.id "
                + "WHERE news.link = ?";
        return query;        
    }

    public String getSpatialMapQuery (Timestamp startRange,Timestamp endRange){
        // Get top location
        String query = "SELECT e.value as location, COUNT(*) as count "
                + "FROM news n "
                + "JOIN news_entity ne ON n.id = ne.news_id "
                + "JOIN entity e ON ne.entity_id = e.id "
                + "JOIN entity_type et ON e.entity_type_id = et.id "
                + "WHERE et.name = 'location' "
                + "AND n.publish_date BETWEEN ? AND ? "
                + "GROUP BY e.value "
                + "ORDER BY count DESC LIMIT 50";
        return query;
    }

    public String getVolatilityQuery(Timestamp startRange,Timestamp endRange){
        String query = "SELECT e.value as entity_name, AVG(n.sentiment) as avg_sentiment, STDDEV(n.sentiment) as volatility, COUNT(*) as mentions "
                + "FROM news n "
                + "JOIN news_entity ne ON n.id = ne.news_id "
                + "JOIN entity e ON ne.entity_id = e.id "
                + "WHERE n.publish_date BETWEEN ? AND ? "
                + "GROUP BY e.value "
                + "HAVING COUNT(*) > 5 "
                + "ORDER BY volatility DESC";
        return query;
    }

    public String getPowerCoupleQuery(Timestamp startRange,Timestamp endRange){
        //Get Person-Organization connection
        String query = "SELECT e1.value AS person, e2.value AS organization, COUNT(*) AS strength "
                + "FROM news_entity ne1 "
                + "JOIN entity e1 ON ne1.entity_id = e1.id "
                + "JOIN entity_type et1 ON e1.entity_type_id = et1.id "
                + "JOIN news_entity ne2 ON ne1.news_id = ne2.news_id "
                + "JOIN entity e2 ON ne2.entity_id = e2.id "
                + "JOIN entity_type et2 ON e2.entity_type_id = et2.id "
                + "JOIN news n ON ne1.news_id = n.id "
                + "WHERE n.publish_date BETWEEN ? AND ? "
                + "AND et1.name = 'person' AND et2.name = 'organization' "
                + "GROUP BY e1.value, e2.value "
                + "ORDER BY strength DESC LIMIT 50 ";
        return query;
    }

    public String getEventTrackerQuery(Timestamp startRange,Timestamp endRange){
        //Get Event-Location connection
        String query = "SELECT e1.value AS event, e2.value AS location, COUNT(*) AS strength "
                + "FROM news_entity ne1 "
                + "JOIN entity e1 ON ne1.entity_id = e1.id "
                + "JOIN entity_type et1 ON e1.entity_type_id = et1.id "
                + "JOIN news_entity ne2 ON ne1.news_id = ne2.news_id "
                + "JOIN entity e2 ON ne2.entity_id = e2.id "
                + "JOIN entity_type et2 ON e2.entity_type_id = et2.id "
                + "JOIN news n ON ne1.news_id = n.id "
                + "WHERE n.publish_date BETWEEN ? AND ? "
                + "AND et1.name = 'event' AND et2.name = 'location' "
                + "GROUP BY e1.value, e2.value "
                + "ORDER BY strength DESC LIMIT 50 ";
        return query;
    }

    public String syncEntityToNeo4jQuery(){
        String query = "Select n.link as news_link, e.value AS entity_name FROM news n "
            + "JOIN news_entity ne ON n.id = ne.news_id "
            + "JOIN entity e ON ne.entity_id = e.id ";
        return query;
    }
}
