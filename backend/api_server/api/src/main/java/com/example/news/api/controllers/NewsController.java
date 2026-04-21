package com.example.news.api.controllers;

import java.util.List;
import java.sql.*;

import org.springframework.web.bind.annotation.*;
import com.example.dto.NewsDTO;
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

}