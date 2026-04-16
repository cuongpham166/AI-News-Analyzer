package com.example.news.api.controllers;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.io.IOException;
import java.sql.*;

import com.example.dto.GlobalTrendsDTO;
import com.example.dto.GlobalEntityTrendsDTO;
import com.example.dto.NewsDTO;
import com.example.dto.InferenceNews;

import com.example.service.ServerDbBridge;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/news")
public class NewsController {
    private final ServerDbBridge bridgeClient;
    public NewsController() {
        this.bridgeClient = new ServerDbBridge();
    }

    @GetMapping("/all")
    public List<NewsDTO> getAllNews(
        @RequestParam(required = false, defaultValue = "10") int limit
    ) throws SQLException {
        List<NewsDTO> newsList = this.bridgeClient.getAllNews(limit);
        return newsList;
    }

    @GetMapping("/{link}")
    public NewsDTO getNewsByLink(
        @PathVariable String link
    ) throws SQLException {
        NewsDTO foundNews = this.bridgeClient.getNewsByLink(link);
        return foundNews;
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

}