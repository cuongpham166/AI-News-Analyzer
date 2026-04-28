package com.example.news.api.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.news.api.dto.analytics.EventTrackerDTO;
import com.example.news.api.dto.analytics.GlobalEntityTrendsDTO;
import com.example.news.api.dto.analytics.GlobalTrendsDTO;
import com.example.news.api.dto.analytics.InferenceNews;
import com.example.news.api.dto.analytics.PowerCoupleDTO;
import com.example.news.api.dto.analytics.SpatialMapDTO;
import com.example.news.api.dto.analytics.TopRadarDTO;
import com.example.news.api.dto.analytics.VolatilityIndexDTO;
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

    public List<SpatialMapDTO> getSpatialMapWithRelativeInterval (String intervalUnit, int amount)throws SQLException {
        List<SpatialMapDTO> result = this.spatialRepo.getSpatialMapWithRelativeInterval(intervalUnit, amount);
        return result;
    }


    public List<PowerCoupleDTO> getPowerCoupleWithRelativeInterval (String intervalUnit, int amount) {
        List<PowerCoupleDTO> result = this.relationshipRepo.getPowerCoupleWithRelativeInterval(intervalUnit, amount);
        return result;
    }

    public List<EventTrackerDTO> getEventTrackerWithRelativeInterval (String intervalUnit, int amount)throws SQLException {
        List <EventTrackerDTO> result = this.relationshipRepo.getEventTrackerWithRelativeInterval(intervalUnit, amount);
        return result;
    }

    public List<VolatilityIndexDTO> getVolatilityIndexWithRelativeInterval (String intervalUnit, int amount)throws SQLException {
        List <VolatilityIndexDTO> result = this.sentimentRepo.getVolatilityIndexWithRelativeInterval(intervalUnit, amount);
        return result;
    }

    public GlobalTrendsDTO getGlobalTrendsWithRelativeInterval (String intervalUnit, int amount) throws IOException {
        GlobalTrendsDTO trendResult = this.trendRepo.getGlobalTrendsWithRelativeInterval(intervalUnit,amount);
        return trendResult;
    }

    public GlobalEntityTrendsDTO getGlobalEntityWithRelativeInterval (String intervalUnit, int amount) throws IOException {
        GlobalEntityTrendsDTO trendResult = this.trendRepo.getGlobalEntityWithRelativeInterval(intervalUnit,amount);
        return trendResult;
    }

    public List<InferenceNews> getImpactArticlesWithRelativeInterval(String intervalUnit, int amount, int topN, boolean isPositive) throws IOException {
        List<InferenceNews> newsResult = this.trendRepo.getImpactArticlesWithRelativeInterval(intervalUnit,amount,topN,isPositive);
        return newsResult;
    }

    public TopRadarDTO getTopicRadarWithRelativeInterval (String intervalUnit, int amount) throws IOException {
        TopRadarDTO radarResult = this.trendRepo.getTopicRadarWithRelativeInterval(intervalUnit, amount);
        return radarResult;
    }
}
