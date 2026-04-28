package com.example.news.api.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.news.api.dto.*;
import com.example.news.api.repository.SearchRepository;

@Service
public class SearchService {
    private final SearchRepository searchRepo;
    
    public SearchService(SearchRepository searchRepo) {
        this.searchRepo = searchRepo;
    }

    public InferenceNews getInferenceNewsById(String id) throws IOException {
       InferenceNews foundNews = this.searchRepo.getInferenceNewsById(id);
       return foundNews;
    }

    public List<InferenceNews> getAllInferenceNews() throws IOException {
        List<InferenceNews>foundNewsList = this.searchRepo.getAllInferenceNews();
        return foundNewsList;
    }

    public List<InferenceNews> findInterfaceNewsByText (String searchText) throws IOException {
        List<InferenceNews> foundNewsList = this.searchRepo.findInterfaceNewsByText(searchText);
        return foundNewsList;
    }

    public GlobalTrendsDTO getGlobalTrendsWithRelativeInterval (String intervalUnit, int amount) throws IOException {
        GlobalTrendsDTO trendResult = this.searchRepo.getGlobalTrendsWithRelativeInterval(intervalUnit,amount);
        return trendResult;
    }

    public GlobalEntityTrendsDTO getGlobalEntityWithRelativeInterval (String intervalUnit, int amount) throws IOException {
        GlobalEntityTrendsDTO trendResult = this.searchRepo.getGlobalEntityWithRelativeInterval(intervalUnit,amount);
        return trendResult;
    }

    public List<InferenceNews> getImpactArticlesWithRelativeInterval(String intervalUnit, int amount, int topN, boolean isPositive) throws IOException {
        List<InferenceNews> newsResult = this.searchRepo.getImpactArticlesWithRelativeInterval(intervalUnit,amount,topN,isPositive);
        return newsResult;
    }

    public TopRadarDTO getTopicRadarWithRelativeInterval (String intervalUnit, int amount) throws IOException {
        TopRadarDTO radarResult = this.searchRepo.getTopicRadarWithRelativeInterval(intervalUnit, amount);
        return radarResult;
    }

}
