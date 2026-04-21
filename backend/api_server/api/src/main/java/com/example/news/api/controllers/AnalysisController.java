package com.example.news.api.controllers;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.dto.EventTrackerDTO;
import com.example.dto.GlobalEntityTrendsDTO;
import com.example.dto.GlobalTrendsDTO;
import com.example.dto.InferenceNews;
import com.example.dto.PowerCoupleDTO;
import com.example.dto.SpatialMapDTO;
import com.example.dto.VolatilityIndexDTO;
import com.example.service.ServerDbBridge;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final ServerDbBridge bridgeClient;
    public AnalysisController() {
        this.bridgeClient = new ServerDbBridge();
    }

    @GetMapping("/global_trends")
    public GlobalTrendsDTO getGlobalTrendsWithRelativeInterval(
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    ) throws IOException {
        GlobalTrendsDTO trendResult = this.bridgeClient.getGlobalTrendsWithRelativeInterval(intervalUnit,amount);
        return trendResult;
    }

    @GetMapping("/global_entity_trends")
    public GlobalEntityTrendsDTO getGlobalEntityWithRelativeInterval(
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    )throws IOException {
        GlobalEntityTrendsDTO trendResult = this.bridgeClient.getGlobalEntityWithRelativeInterval(intervalUnit,amount);
        return trendResult;
    }

    @GetMapping("/impact_articles")
    public List<InferenceNews> getImpactArticlesWithRelativeInterval(
        @RequestParam String intervalUnit, 
        @RequestParam int amount, 
        @RequestParam int topN, 
        @RequestParam boolean isPositive
    ) throws IOException{
        List<InferenceNews> newsResult = this.bridgeClient.getImpactArticlesWithRelativeInterval(intervalUnit,amount,topN,isPositive);
        return newsResult;        
    }

    @GetMapping("/spatial_map")
    public List<SpatialMapDTO> getSpatialMapWithRelativeInterval (
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    )throws SQLException {
        List<SpatialMapDTO> result = this.bridgeClient.getSpatialMapWithRelativeInterval(intervalUnit, amount);
        return result;
    }

    @GetMapping("/power_couple")
    public List<PowerCoupleDTO> getPowerCoupleWithRelativeInterval (
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    )throws SQLException {
        List<PowerCoupleDTO> result = this.bridgeClient.getPowerCoupleWithRelativeInterval(intervalUnit, amount);
        return result;
    }

    @GetMapping("/event_tracker")
    public List<EventTrackerDTO> getEventTrackerWithRelativeInterval (
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    )throws SQLException {
        List <EventTrackerDTO> result = this.bridgeClient.getEventTrackerWithRelativeInterval(intervalUnit, amount);
        return result;
    }

    @GetMapping("/volatility_index")
    public List<VolatilityIndexDTO> getVolatilityIndexWithRelativeInterval (
        @RequestParam String intervalUnit, 
        @RequestParam int amount
    )throws SQLException {
        List <VolatilityIndexDTO> result = this.bridgeClient.getVolatilityIndexWithRelativeInterval(intervalUnit, amount);
        return result;
    }
}
