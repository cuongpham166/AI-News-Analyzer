package com.example.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import com.example.dto.NewsDTO;
import com.example.dto.SpatialMapDTO;
import com.example.dto.PowerCoupleDTO;
import com.example.dto.EventTrackerDTO;
import com.example.dto.VolatilityIndexDTO;
import com.example.utils.AggregationMapping;
import com.example.utils.AggregationQuery;
import com.example.utils.AggregationInterval;

import io.github.cdimascio.dotenv.Dotenv;

public class PostgresClient {
    private final Dotenv dotenv = Dotenv.configure().directory("../../").load();
    private final String url = this.dotenv.get("POSTGRES_JAVA_URL");
    private final String user = this.dotenv.get("POSTGRES_USER");
    private final String password = this.dotenv.get("POSTGRES_PASSWORD");
    private final Connection postgresClient;
    private final AggregationMapping aggMapping;
    private final AggregationQuery aggQuery;

    public PostgresClient() throws SQLException {
        this.postgresClient = DriverManager.getConnection(this.url, this.user, this.password);
        this.aggMapping = new AggregationMapping();
        this.aggQuery = new AggregationQuery();
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

    public static void main(String[] args) throws SQLException {
        PostgresClient postgresClient = new PostgresClient();
        List<NewsDTO> foundNewsList = postgresClient.getAllNews(1);
        List<SpatialMapDTO> result = postgresClient.getSpatialMapWithRelativeInterval("month",5);
        List<PowerCoupleDTO> powerCoupleResult = postgresClient.getPowerCoupleWithRelativeInterval("month",5);
        
        System.out.print("Test: "+powerCoupleResult);
    }
}
