package com.example.news.api.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.news.api.dto.analytics.InferenceNews;
import com.example.news.api.repository.analytics.SearchRepository;


@Service
public class SearchService {
    private final SearchRepository searchRepo;
    
    public SearchService(SearchRepository searchRepo) {
        this.searchRepo = searchRepo;
    }

    public InferenceNews getInferenceNewsById(String id) throws IOException {
        return this.searchRepo.getInferenceNewsById(id);
    }

    public List<InferenceNews> getAllInferenceNews() throws IOException {
        return this.searchRepo.getAllInferenceNews();
    }

    public List<InferenceNews> findInterfaceNewsByText (String searchText) throws IOException {
        return this.searchRepo.findInterfaceNewsByText(searchText);
    }

}
