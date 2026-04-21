package com.example.service;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.example.dto.NewsDTO;
import com.example.dto.SpatialMapDTO;
import com.example.dto.PowerCoupleDTO;
import com.example.dto.EventTrackerDTO;
import com.example.dto.VolatilityIndexDTO;
import com.example.dto.Neo4jEntityDTO;

import com.example.utils.AggregationMapping;
import com.example.utils.AggregationQuery;
import com.example.utils.AggregationInterval;
import com.example.utils.Neo4jQuery;

import com.example.service.Neo4jClient;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import io.github.cdimascio.dotenv.Dotenv;

public class PostgresClient {
    private final Dotenv dotenv = Dotenv.configure().directory("../../").load();
    private final String url = this.dotenv.get("POSTGRES_JAVA_URL");
    private final String user = this.dotenv.get("POSTGRES_USER");
    private final String password = this.dotenv.get("POSTGRES_PASSWORD");
    private final Connection postgresClient;
    private final AggregationMapping aggMapping;
    private final AggregationQuery aggQuery;
    private final Neo4jQuery neo4jQuery;
    private final Neo4jClient neo4jClient;

    public PostgresClient() throws SQLException {
        this.postgresClient = DriverManager.getConnection(this.url, this.user, this.password);
        this.aggMapping = new AggregationMapping();
        this.aggQuery = new AggregationQuery();
        this.neo4jQuery = new Neo4jQuery();
        this.neo4jClient = new Neo4jClient();
    }

    public List<NewsDTO> getAllNews(int limit) throws SQLException {
        List<NewsDTO> newsList = new ArrayList<>();
        String sql = aggQuery.getAllNewsQuery(limit);
        try(PreparedStatement pstmt = this.postgresClient.prepareStatement(sql)){
            pstmt.setInt(1, limit);
            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    newsList.add(aggMapping.mapDetailedNews(rs));
                }
            }
        }
        return newsList;
    }

    public NewsDTO getNewsByLink(String link) throws SQLException {
        String sql = aggQuery.getNewsByLinkQuery(link);
        try(PreparedStatement pstmt = this.postgresClient.prepareStatement(sql)){
            pstmt.setString(1, link);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    return aggMapping.mapDetailedNews(rs);
                }else{
                    return null;
                }
            }
        }
    }

    public List<SpatialMapDTO> getSpatialMapWithRelativeInterval (String intervalUnit, int amount)throws SQLException {
        List<SpatialMapDTO> mapList = new ArrayList<>();

        Timestamp[] result = AggregationInterval.computeEpochRangeRelativeForSql(intervalUnit, amount);
        Timestamp startRange = result[0];
        Timestamp endRange = result[1];

        String sql = aggQuery.getSpatialMapQuery(startRange, endRange);
        try(PreparedStatement pstmt = this.postgresClient.prepareStatement(sql)){
            pstmt.setTimestamp(1, startRange);
            pstmt.setTimestamp(2, endRange);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    while (rs.next()) {
                        mapList.add(aggMapping.mapDetailedSpatialMap(rs));
                    }
                }
            }
        }
        return mapList;
    }

    public List<PowerCoupleDTO> getPowerCoupleWithRelativeInterval (String intervalUnit, int amount)throws SQLException {
        List<PowerCoupleDTO> powerCoupleList = new ArrayList<>();
        Timestamp[] result = AggregationInterval.computeEpochRangeRelativeForSql(intervalUnit, amount);
        Timestamp startRange = result[0];
        Timestamp endRange = result[1];
        String sql = aggQuery.getPowerCoupleQuery(startRange, endRange);
        try(PreparedStatement pstmt = this.postgresClient.prepareStatement(sql)){
            pstmt.setTimestamp(1, startRange);
            pstmt.setTimestamp(2, endRange);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    while (rs.next()) {
                        powerCoupleList.add(aggMapping.mapDetailedPowerCouple(rs));
                    }
                }
            }            
        }
        return powerCoupleList;
    }

    public List<EventTrackerDTO> getEventTrackerWithRelativeInterval (String intervalUnit, int amount)throws SQLException {
        List <EventTrackerDTO> eventList = new ArrayList<>();
        Timestamp[] result = AggregationInterval.computeEpochRangeRelativeForSql(intervalUnit, amount);
        Timestamp startRange = result[0];
        Timestamp endRange = result[1];
        String sql = aggQuery.getEventTrackerQuery(startRange, endRange);
        try(PreparedStatement pstmt = this.postgresClient.prepareStatement(sql)){
            pstmt.setTimestamp(1, startRange);
            pstmt.setTimestamp(2, endRange);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    while (rs.next()) {
                        eventList.add(aggMapping.mapDetailedMapTracker(rs));
                    }
                }
            }             
        }
        return eventList;
    }

    public List<VolatilityIndexDTO> getVolatilityIndexWithRelativeInterval (String intervalUnit, int amount)throws SQLException {
        List <VolatilityIndexDTO> volatilityIndexList = new ArrayList<>();
        Timestamp[] result = AggregationInterval.computeEpochRangeRelativeForSql(intervalUnit, amount);
        Timestamp startRange = result[0];
        Timestamp endRange = result[1];
        String sql = aggQuery.getVolatilityQuery(startRange, endRange);
        try(PreparedStatement pstmt = this.postgresClient.prepareStatement(sql)){
            pstmt.setTimestamp(1, startRange);
            pstmt.setTimestamp(2, endRange);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    while (rs.next()) {
                        volatilityIndexList.add(aggMapping.mapDetailedVolatilityIndex(rs));
                    }
                }
            }               
        }
        return volatilityIndexList;
    }

    public void syncNewsToNeo4j() throws SQLException {
        List<NewsDTO> newsList = new ArrayList<>();
        String sql = aggQuery.getAllNewsQueryWithoutLimit();
        String syncQuery = neo4jQuery.integrateDataIntoNeo4jQuery();

        try(PreparedStatement pstmt = this.postgresClient.prepareStatement(sql)){
            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    newsList.add(aggMapping.mapDetailedNews(rs));
                }
            }
        }

        Driver neo4jDriver = this.neo4jClient.getDriver();
        try (Session session = neo4jDriver.session()) {
            for(NewsDTO news : newsList){
                if (news.getTopic_name() != null && !news.getTopic_name().trim().isEmpty()) {           
                    Timestamp timestamp = news.getPublishDate();
                    long epochMillis = timestamp.getTime();

                    Map<String, Object> params = new HashMap<>();
                    params.put("source_name", news.getSource_name());
                    params.put("topic_name", news.getTopic_name());
                    params.put("link", news.getLink());
                    params.put("title", news.getTitle());
                    params.put("publish_date", epochMillis);
                    params.put("sentiment", news.getSentiment() != null ? news.getSentiment().doubleValue() : 0.0);

                    session.executeWrite(tx -> {
                        tx.run(syncQuery, params);
                            return null; 
                    });                   
                }else{
                    System.out.println("Skipping news with null topic: " + news.getLink());
                }
            }
        } 
  
    }

    public void syncNewsEntityToNeo4j() throws SQLException {
        List<Neo4jEntityDTO> newsEntitiesList = new ArrayList<>();
        String sql = aggQuery.syncEntityToNeo4jQuery();
        String syncQuery = neo4jQuery.integrateEntityDataIntoNeo4jQuery();

        try(PreparedStatement pstmt = this.postgresClient.prepareStatement(sql)){
            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    newsEntitiesList.add(aggMapping.mapNeo4jEntity(rs));
                }
            }
        }       
        
        Driver neo4jDriver = this.neo4jClient.getDriver();
        try (Session session = neo4jDriver.session()) { 
            for (Neo4jEntityDTO newsEntity : newsEntitiesList){
                Map<String, Object> params = new HashMap<>();
                params.put("entity_name", newsEntity.getEntity_name());
                params.put("news_link", newsEntity.getNews_link());    
                
                session.executeWrite(tx -> {
                    tx.run(syncQuery, params);
                        return null; 
                });  
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        PostgresClient postgresClient = new PostgresClient();
        List<NewsDTO> foundNewsList = postgresClient.getAllNews(1);
        List<SpatialMapDTO> result = postgresClient.getSpatialMapWithRelativeInterval("month",5);
        List<PowerCoupleDTO> powerCoupleResult = postgresClient.getPowerCoupleWithRelativeInterval("month",5);
        
        postgresClient.syncNewsEntityToNeo4j();
    }
}
