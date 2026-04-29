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
        return this.analysisService.getGlobalTrendsWithRelativeInterval(intervalUnit,amount);
    }

    @GetMapping("/global_entity_trends")
    public GlobalEntityTrendsDTO getGlobalEntityWithRelativeInterval(
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    )throws IOException {
        return this.analysisService.getGlobalEntityWithRelativeInterval(intervalUnit,amount);
    }

    @GetMapping("/impact_articles")
    public List<InferenceNews> getImpactArticlesWithRelativeInterval(
        @RequestParam String intervalUnit, 
        @RequestParam int amount, 
        @RequestParam int topN, 
        @RequestParam boolean isPositive
    ) throws IOException{
        return this.analysisService.getImpactArticlesWithRelativeInterval(intervalUnit,amount,topN,isPositive);
    }

    @GetMapping("/spatial_map")
    public List<SpatialMapDTO> getSpatialMapWithRelativeInterval (
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    )throws SQLException {
        return this.analysisService.getSpatialMapWithRelativeInterval(intervalUnit, amount);
    }

    @GetMapping("/power_couple")
    public List<PowerCoupleDTO> getPowerCoupleWithRelativeInterval (
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    )throws SQLException {
        return this.analysisService.getPowerCoupleWithRelativeInterval(intervalUnit, amount);
    }

    @GetMapping("/event_tracker")
    public List<EventTrackerDTO> getEventTrackerWithRelativeInterval (
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    )throws SQLException {
        return this.analysisService.getEventTrackerWithRelativeInterval(intervalUnit, amount);
    }

    @GetMapping("/volatility_index")
    public List<VolatilityIndexDTO> getVolatilityIndexWithRelativeInterval (
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    )throws SQLException {
        return this.analysisService.getVolatilityIndexWithRelativeInterval(intervalUnit, amount);
    }
}
