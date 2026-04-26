package com.example.service;

import java.io.IOException;
import java.util.*;

import com.example.dto.InferenceNews;
import com.example.dto.TopRadarDTO;
import com.example.dto.GlobalTrendsDTO;
import com.example.dto.GlobalEntityTrendsDTO;
import com.example.repository.SearchRepository;

public class SearchService {
    private final SearchRepository searchRepo;

    public SearchService() {
        this.searchRepo = new SearchRepository();
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
