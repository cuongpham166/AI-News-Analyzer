package com.example.news.api.controller;

import java.util.List;
import java.sql.*;

import org.springframework.web.bind.annotation.*;
import com.example.news.api.dto.NewsDTO;
import com.example.news.api.service.MetaDataService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/news")
public class NewsController {
    private final MetaDataService metadataService;
    public NewsController(MetaDataService metadataService) {
        this.metadataService = metadataService;
    }

    @GetMapping("/all")
    public List<NewsDTO> getAllNews(
        @RequestParam(required = false, defaultValue = "10") int limit
    ) throws SQLException {
        List<NewsDTO> newsList = this.metadataService.getAllNews(limit);
        return newsList;
    }

    @GetMapping("/{link}")
    public NewsDTO getNewsByLink(
        @PathVariable String link
    ) throws SQLException {
        NewsDTO foundNews = this.metadataService.getNewsByLink(link);
        return foundNews;
    }

}