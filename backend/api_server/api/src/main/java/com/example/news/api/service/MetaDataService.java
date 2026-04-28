package com.example.news.api.service;


import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.news.api.dto.jpa.DetailedNewsDTO;
import com.example.news.api.dto.jpa.NewsDTO;
import com.example.news.api.mapper.NewsMapper;

import com.example.news.api.repository.jpa.*;

@Service
public class MetaDataService {
    private final NewsRepository newsRepo;

    public MetaDataService(
        NewsRepository newsRepo
    ){
        this.newsRepo = newsRepo;
    }


    public List<NewsDTO> getAllNews(int limit) {
        return this.newsRepo.findAllWithRelations(PageRequest.of(0, limit))
            .stream()
            .map(NewsMapper::toDTO)
            .toList();
    }

    public DetailedNewsDTO getDetailedNewsByLink(String link) {
        return this.newsRepo.findDetailByLink(link)
            .map(NewsMapper::toDetailedDTO)
            .orElseThrow(() -> new RuntimeException("Not found"));
    }

}
