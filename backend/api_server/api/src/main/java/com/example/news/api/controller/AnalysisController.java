package com.example.news.api.controller;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.news.api.dto.analytics.EventTrackerDTO;
import com.example.news.api.dto.analytics.GlobalEntityTrendsDTO;
import com.example.news.api.dto.analytics.GlobalTrendsDTO;
import com.example.news.api.dto.analytics.InferenceNews;
import com.example.news.api.dto.analytics.PowerCoupleDTO;
import com.example.news.api.dto.analytics.SpatialMapDTO;
import com.example.news.api.dto.analytics.VolatilityIndexDTO;

import com.example.news.api.service.AnalysisService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService ) {
        this.analysisService = analysisService;
    }

    @GetMapping("/global_trends")
    public GlobalTrendsDTO getGlobalTrendsWithRelativeInterval(
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    ) throws IOException {
        GlobalTrendsDTO trendResult = this.analysisService.getGlobalTrendsWithRelativeInterval(intervalUnit,amount);
        return trendResult;
    }

    @GetMapping("/global_entity_trends")
    public GlobalEntityTrendsDTO getGlobalEntityWithRelativeInterval(
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    )throws IOException {
        GlobalEntityTrendsDTO trendResult = this.analysisService.getGlobalEntityWithRelativeInterval(intervalUnit,amount);
        return trendResult;
    }

    @GetMapping("/impact_articles")
    public List<InferenceNews> getImpactArticlesWithRelativeInterval(
        @RequestParam String intervalUnit, 
        @RequestParam int amount, 
        @RequestParam int topN, 
        @RequestParam boolean isPositive
    ) throws IOException{
        List<InferenceNews> newsResult = this.analysisService.getImpactArticlesWithRelativeInterval(intervalUnit,amount,topN,isPositive);
        return newsResult;        
    }

    @GetMapping("/spatial_map")
    public List<SpatialMapDTO> getSpatialMapWithRelativeInterval (
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    )throws SQLException {
        List<SpatialMapDTO> result = this.analysisService.getSpatialMapWithRelativeInterval(intervalUnit, amount);
        return result;
    }

    @GetMapping("/power_couple")
    public List<PowerCoupleDTO> getPowerCoupleWithRelativeInterval (
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    )throws SQLException {
        List<PowerCoupleDTO> result = this.analysisService.getPowerCoupleWithRelativeInterval(intervalUnit, amount);
        return result;
    }

    @GetMapping("/event_tracker")
    public List<EventTrackerDTO> getEventTrackerWithRelativeInterval (
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    )throws SQLException {
        List <EventTrackerDTO> result = this.analysisService.getEventTrackerWithRelativeInterval(intervalUnit, amount);
        return result;
    }

    @GetMapping("/volatility_index")
    public List<VolatilityIndexDTO> getVolatilityIndexWithRelativeInterval (
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    )throws SQLException {
        List <VolatilityIndexDTO> result = this.analysisService.getVolatilityIndexWithRelativeInterval(intervalUnit, amount);
        return result;
    }
}
