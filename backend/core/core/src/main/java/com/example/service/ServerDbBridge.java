package com.example.service;

import java.sql.*;
import java.io.IOException;
import com.example.service.ElasticClient;
import com.example.service.PostgresClient;
import java.util.ArrayList;
import java.util.List;

import com.example.dto.GlobalTrendsDTO;
import com.example.dto.GlobalEntityTrendsDTO;
import com.example.dto.NewsDTO;
import com.example.dto.InferenceNews;

public class ServerDbBridge {
    private final ElasticClient elasticClient;
    private final PostgresClient postgresClient;

    public ServerDbBridge() {
        this.elasticClient = new ElasticClient();
        PostgresClient tempClient;
        try{
            tempClient = new PostgresClient();
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to connect to PostgreSQL", e);
        }
        this.postgresClient = tempClient;
    }

    public List<NewsDTO> getAllNews(int limit) throws SQLException {
        List<NewsDTO> newsList = this.postgresClient.getAllNews(limit);
        return newsList;
    }

    public NewsDTO getNewsByLink(String link) throws SQLException {
        NewsDTO foundNews = this.postgresClient.getNewsByLink(link);
        return foundNews;
    }

    public GlobalTrendsDTO getGlobalTrendsWithRelativeInterval (String intervalUnit, int amount) throws IOException {
        GlobalTrendsDTO trendResult = this.elasticClient.getGlobalTrendsWithRelativeInterval(intervalUnit,amount);
        return trendResult;
    }

    public GlobalEntityTrendsDTO getGlobalEntityWithRelativeInterval (String intervalUnit, int amount) throws IOException {
        GlobalEntityTrendsDTO trendResult = this.elasticClient.getGlobalEntityWithRelativeInterval(intervalUnit,amount);
        return trendResult;
    }

    public List<InferenceNews> getImpactArticlesWithRelativeInterval(String intervalUnit, int amount, int topN, boolean isPositive) throws IOException {
        List<InferenceNews> newsResult = this.elasticClient.getImpactArticlesWithRelativeInterval(intervalUnit,amount,topN,isPositive);
        return newsResult;
    }   
}