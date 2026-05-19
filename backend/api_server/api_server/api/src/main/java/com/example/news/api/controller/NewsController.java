package com.example.news.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.news.api.dto.jpa.DetailedNewsDTO;
import com.example.news.api.dto.jpa.NewsDTO;
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
    public List<NewsDTO> getAllNews(@RequestParam(required = false, defaultValue = "10") int limit) {
        return this.metadataService.getAllNews(limit);
    }

    @GetMapping("/all/source")
    public List<NewsDTO> getAllNewsBySourceId(@RequestParam int sourceId) {
        return this.metadataService.getAllNewsBySourceId(sourceId);
    }

    @GetMapping("/detail")
    public DetailedNewsDTO getNewsByLink( @RequestParam String link) {
        return this.metadataService.getDetailedNewsByLink(link);
    }

}