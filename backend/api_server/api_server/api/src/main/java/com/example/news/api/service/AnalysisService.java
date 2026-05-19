package com.example.news.api.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.example.news.api.dto.analytics.*;
import org.springframework.stereotype.Service;

import com.example.news.api.repository.analytics.RelationshipRepository;
import com.example.news.api.repository.analytics.SentimentRepository;
import com.example.news.api.repository.analytics.SpatialRepository;
import com.example.news.api.repository.analytics.TrendRepository;
import com.example.news.api.repository.jpa.NewsRepository;

@Service
public class AnalysisService {
    private final RelationshipRepository relationshipRepo;
    private final SpatialRepository spatialRepo;
    private final SentimentRepository sentimentRepo;
    private final TrendRepository trendRepo;

    public AnalysisService(
        RelationshipRepository relationshipRepo,
        SpatialRepository spatialRepo,
        SentimentRepository sentimentRepo,
        TrendRepository trendRepo
    ){
        this.relationshipRepo = relationshipRepo;
        this.spatialRepo = spatialRepo;
        this.sentimentRepo = sentimentRepo;
        this.trendRepo = trendRepo;
    }

    public List<SpatialMapDTO> getSpatialMapWithRelativeInterval (String intervalUnit, int amount) {
        return this.spatialRepo.getSpatialMapWithRelativeInterval(intervalUnit, amount);
    }

    public List<PowerCoupleDTO> getPowerCoupleWithRelativeInterval (String intervalUnit, int amount) {
        return this.relationshipRepo.getPowerCoupleWithRelativeInterval(intervalUnit, amount);
    }

    public List<EventTrackerDTO> getEventTrackerWithRelativeInterval (String intervalUnit, int amount) {
        return this.relationshipRepo.getEventTrackerWithRelativeInterval(intervalUnit, amount);
    }

    public List<VolatilityIndexDTO> getVolatilityIndexWithRelativeInterval (String intervalUnit, int amount) {
        return this.sentimentRepo.getVolatilityIndexWithRelativeInterval(intervalUnit, amount);
    }

    public GlobalTrendsDTO getGlobalTrendsWithRelativeInterval (String intervalUnit, int amount) throws IOException {
        return this.trendRepo.getGlobalTrendsWithRelativeInterval(intervalUnit,amount);
    }

    public GlobalEntityTrendsDTO getGlobalEntityWithRelativeInterval (String intervalUnit, int amount) throws IOException {
        return this.trendRepo.getGlobalEntityWithRelativeInterval(intervalUnit,amount);
    }

    public List<InferenceNews> getImpactArticlesWithRelativeInterval(String intervalUnit, int amount, int topN, boolean isPositive) throws IOException {
        return this.trendRepo.getImpactArticlesWithRelativeInterval(intervalUnit,amount,topN,isPositive);
    }

    public TopRadarDTO getTopicRadarWithRelativeInterval (String intervalUnit, int amount) throws IOException {
        return this.trendRepo.getTopicRadarWithRelativeInterval(intervalUnit, amount);
    }

    public GraphResponseDTO getDiscoveryDataWithRelativeInterval(String intervalUnit, int amount){
        return this.relationshipRepo.getDiscoveryData(intervalUnit, amount);
    }
}
