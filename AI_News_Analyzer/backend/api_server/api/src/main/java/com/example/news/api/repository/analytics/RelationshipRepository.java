package com.example.news.api.repository.analytics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.news.api.dto.analytics.*;

import org.neo4j.driver.Driver;
import org.neo4j.driver.QueryConfig;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import org.springframework.stereotype.Repository;

import com.example.news.api.dto.jpa.NewsDTO;
import com.example.news.api.util.AggregationInterval;
import com.example.news.api.util.AggregationMapping;
import com.example.news.api.util.AggregationQuery;
import com.example.news.api.util.DiscoveryQuery;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import com.example.news.api.jooq.Tables;

@Repository
public class RelationshipRepository {
    private final Connection pConnection;
    private final AggregationMapping aggMapping;
    private final AggregationQuery aggQuery;
    private final DiscoveryQuery neo4jQuery;
    private final Driver neo4jDriver;
    private final DSLContext dsl;
    private final AggregationInterval aggInterval;

    public RelationshipRepository(
        Connection pConnection,
        AggregationMapping aggMapping,
        AggregationQuery aggQuery,
        DiscoveryQuery neo4jQuery,
        Driver neo4jDriver,
        DSLContext dsl,
        AggregationInterval aggInterval
    ){
        this.pConnection = pConnection;
        this.aggMapping = aggMapping;
        this.aggQuery = aggQuery;
        this.neo4jQuery = neo4jQuery;
        this.neo4jDriver = neo4jDriver;
        this.dsl = dsl;
        this.aggInterval = aggInterval;
    }


    public List<PowerCoupleDTO> getPowerCoupleWithRelativeInterval (String intervalUnit, int amount){
        Timestamp[] result = this.aggInterval.computeEpochRangeRelativeForSql(intervalUnit, amount);
        Timestamp startRange = result[0];
        Timestamp endRange = result[1];
        
        // Convert Timestamp → LocalDateTime (since jOOQ uses javaTimeTypes)
        LocalDateTime start = startRange.toLocalDateTime();
        LocalDateTime end = endRange.toLocalDateTime();
        return dsl.select(
                Tables.ENTITY.as("e1").VALUE.as("person"),
                Tables.ENTITY.as("e2").VALUE.as("organization"),
                DSL.count().as("strength")
            )
            .from(Tables.NEWS_ENTITY.as("ne1"))
            .join(Tables.ENTITY.as("e1")).on(Tables.NEWS_ENTITY.as("ne1").ENTITY_ID.eq(Tables.ENTITY.as("e1").ID))
            .join(Tables.ENTITY_TYPE.as("et1")).on(Tables.ENTITY.as("e1").ENTITY_TYPE_ID.eq(Tables.ENTITY_TYPE.as("et1").ID))
            .join(Tables.NEWS_ENTITY.as("ne2")).on(Tables.NEWS_ENTITY.as("ne1").NEWS_ID.eq(Tables.NEWS_ENTITY.as("ne2").NEWS_ID))
            .join(Tables.ENTITY.as("e2")).on(Tables.NEWS_ENTITY.as("ne2").ENTITY_ID.eq(Tables.ENTITY.as("e2").ID))
            .join(Tables.ENTITY_TYPE.as("et2")).on(Tables.ENTITY.as("e2").ENTITY_TYPE_ID.eq(Tables.ENTITY_TYPE.as("et2").ID))
            .join(Tables.NEWS).on( Tables.NEWS_ENTITY.as("ne1").NEWS_ID.eq(Tables.NEWS.ID))
            .where(Tables.NEWS.PUBLISH_DATE.between(start, end))
            .and(Tables.ENTITY_TYPE.as("et1").NAME.eq("person"))
            .and(Tables.ENTITY_TYPE.as("et2").NAME.eq("organization"))
            .groupBy(Tables.ENTITY.as("e1").VALUE,Tables.ENTITY.as("e2").VALUE)
            .orderBy(DSL.count().desc())
            .limit(50)
            .fetchInto(PowerCoupleDTO.class);

        
    }
    public List<EventTrackerDTO> getEventTrackerWithRelativeInterval (String intervalUnit, int amount){
        Timestamp[] result = this.aggInterval.computeEpochRangeRelativeForSql(intervalUnit, amount);
        Timestamp startRange = result[0];
        Timestamp endRange = result[1];

        // Convert Timestamp → LocalDateTime (since jOOQ uses javaTimeTypes)
        LocalDateTime start = startRange.toLocalDateTime();
        LocalDateTime end = endRange.toLocalDateTime();

        return dsl.select(
                Tables.ENTITY.as("e1").VALUE.as("event"),
                Tables.ENTITY.as("e2").VALUE.as("location"),
                DSL.count().as("strength")
            )
            .from(Tables.NEWS_ENTITY.as("ne1"))
            .join(Tables.ENTITY.as("e1")).on(Tables.NEWS_ENTITY.as("ne1").ENTITY_ID.eq(Tables.ENTITY.as("e1").ID))
            .join(Tables.ENTITY_TYPE.as("et1")).on(Tables.ENTITY.as("e1").ENTITY_TYPE_ID.eq(Tables.ENTITY_TYPE.as("et1").ID))
            .join(Tables.NEWS_ENTITY.as("ne2")).on(Tables.NEWS_ENTITY.as("ne1").NEWS_ID.eq(Tables.NEWS_ENTITY.as("ne2").NEWS_ID))
            .join(Tables.ENTITY.as("e2")).on(Tables.NEWS_ENTITY.as("ne2").ENTITY_ID.eq(Tables.ENTITY.as("e2").ID))
            .join(Tables.ENTITY_TYPE.as("et2")).on(Tables.ENTITY.as("e2").ENTITY_TYPE_ID.eq(Tables.ENTITY_TYPE.as("et2").ID))
            .join(Tables.NEWS).on(Tables.NEWS_ENTITY.as("ne1").NEWS_ID.eq(Tables.NEWS.ID))
            .where(Tables.NEWS.PUBLISH_DATE.between(start, end))
            .and(Tables.ENTITY_TYPE.as("et1").NAME.eq("event"))
            .and(Tables.ENTITY_TYPE.as("et2").NAME.eq("location"))
            .groupBy(Tables.ENTITY.as("e1").VALUE, Tables.ENTITY.as("e2").VALUE)
            .orderBy(DSL.count().desc())
            .limit(50)
            .fetchInto(EventTrackerDTO.class);
    }

    public List<DetailedEntityDTO> findEntitiesByNewsLink(String newsLink) {
        return dsl.select(
                Tables.ENTITY.ID.as("entity_id"),
                Tables.ENTITY.VALUE.as("entity_name"),
                Tables.ENTITY_TYPE.NAME.as("entity_type_name"),
                Tables.ENTITY_TYPE.ID.as("entity_type_id")
        )
                .from(Tables.ENTITY)
                .leftJoin(Tables.ENTITY_TYPE)
                .on(Tables.ENTITY.ENTITY_TYPE_ID.eq(Tables.ENTITY_TYPE.ID))
                .leftJoin(Tables.NEWS_ENTITY)
                .on(Tables.ENTITY.ID.eq(Tables.NEWS_ENTITY.ENTITY_ID))
                .leftJoin(Tables.NEWS)
                .on(Tables.NEWS_ENTITY.NEWS_ID.eq(Tables.NEWS.ID))
                .where(Tables.NEWS.LINK.eq(newsLink))
                .fetchInto(DetailedEntityDTO.class);

    }
    public void syncNewsToNeo4j() throws SQLException {
        List<NewsDTO> newsList = new ArrayList<>();
        String sql = aggQuery.getAllNewsQueryWithoutLimit();
        String syncQuery = neo4jQuery.integrateDataIntoNeo4jQuery();

        try(PreparedStatement pstmt = this.pConnection.prepareStatement(sql)){
            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    newsList.add(aggMapping.mapDetailedNews(rs));
                }
            }
        }

        try (Session session = this.neo4jDriver.session()) {
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

        try(PreparedStatement pstmt = this.pConnection.prepareStatement(sql)){
            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    newsEntitiesList.add(aggMapping.mapNeo4jEntity(rs));
                }
            }
        }       
        
        try (Session session = this.neo4jDriver.session()) { 
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
    
    public Driver getDriver() {
        return this.neo4jDriver;
    }

    public void close() {
        if (this.neo4jDriver != null) {
            this.neo4jDriver.close();
        }
    }

    public void createInitConstraints() {
        String[] queries = {
            neo4jQuery.createSourceConstraintQuery(),
            neo4jQuery.createTopicConstraintQuery(),
            neo4jQuery.createEntityConstraintQuery(),
            neo4jQuery.createNewsConstraintQuery(),
        };

        for (String query : queries){
            this.neo4jDriver.executableQuery(query)
                .withConfig(QueryConfig.builder().withDatabase(System.getenv("NEO4J_DB")).build())
                .execute();            
        }
    }

    public GraphResponseDTO getDiscoveryData(String intervalUnit, int amount) {
        GraphResponseDTO response = new GraphResponseDTO();
        Map<String, GraphNodeDTO> nodeMap = new HashMap<>();

        long[] rangeResult = this.aggInterval.computeEpochRangeRelativeForNeo4j(intervalUnit, amount);

        try (Session session = this.neo4jDriver.session()) {
            session.executeRead(tx -> {
                Result result = tx.run(this.neo4jQuery.getDiscoveryGraphQuery(),
                        Map.of("startTime", rangeResult[0], "endTime", rangeResult[1]));

                while (result.hasNext()) {
                    Record record = result.next();
                    String sName = record.get("source").asString();
                    String sGroup = record.get("sourceGroup").asString();
                    String tName = record.get("target").asString();
                    String tGroup = record.get("targetGroup").asString();
                    double w = record.get("weight").asDouble();
                    double s = record.get("sentiment").asDouble();

                    // Build unique nodes
                    // Update Source Node
                    nodeMap.putIfAbsent(sName, new GraphNodeDTO(sName, sName, sGroup, 0, 0));
                    GraphNodeDTO sNode = nodeMap.get(sName);
                    sNode.setSize(sNode.getSize() + w); // Pass the new calculated size
                    sNode.setSentiment((sNode.getSentiment() + s) / 2);

                    // 2. Update Target Node
                    nodeMap.putIfAbsent(tName, new GraphNodeDTO(tName, tName, tGroup, 0, 0));
                    GraphNodeDTO tNode = nodeMap.get(tName);
                    tNode.setSize(tNode.getSize() + w); // Pass the new calculated size
                    tNode.setSentiment((tNode.getSentiment() + s) / 2);

                    // Add link (Order: source, target, value, sentiment)
                    response.getLinks().add(new GraphLinkDTO(sName, tName, w, s));
                }
                return null;
            });
        }
        response.setNodes(new ArrayList<>(nodeMap.values()));
        return response;
    }
}
