package com.example.news.api.service;

import java.util.List;

import com.example.news.api.dto.analytics.DetailedEntityDTO;
import com.example.news.api.entity.NewsEntity;
import com.example.news.api.repository.analytics.RelationshipRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.news.api.dto.jpa.DetailedNewsDTO;
import com.example.news.api.dto.jpa.NewsDTO;
import com.example.news.api.mapper.NewsMapper;

import com.example.news.api.repository.jpa.*;

@Service
public class MetaDataService {
    private final NewsRepository newsRepo;
    private final RelationshipRepository relationshipRepo;
    private final NewsMapper newsMapper;

    public MetaDataService(
        NewsRepository newsRepo,
        RelationshipRepository relationshipRepo,
        NewsMapper newsMapper
    ){
        this.newsRepo = newsRepo;
        this.relationshipRepo = relationshipRepo;
        this.newsMapper = newsMapper;
    }


    public List<NewsDTO> getAllNews(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return this.newsRepo.findAllWithRelations(pageable)
            .stream()
            .map(this.newsMapper::toDTO)
            .toList();
    }

    public List<NewsDTO> getAllNewsBySourceId(int sourceId){
        Pageable pageable = PageRequest.of(
                0, // page index (0 = first page)
                10,
                Sort.by(Sort.Direction.DESC, "publishDate")
        );
        return this.newsRepo.findAllBySourceId(sourceId, pageable)
                .stream()
                .map(this.newsMapper::toDTO)
                .toList();
    }

    public DetailedNewsDTO getDetailedNewsByLink(String link) {
        NewsEntity foundNews = newsRepo.findDetailByLink(link)
                .orElseThrow(() -> new RuntimeException("News not found"));
        // Fetch entities as DTOs (with entityType fully populated)
        List<DetailedEntityDTO> detailedEntity = relationshipRepo.findEntitiesByNewsLink(link);
        return this.newsMapper.toDetailedDTO(foundNews,detailedEntity);
    }
}
