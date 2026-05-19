package com.example.news.api.repository.analytics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.news.api.dto.analytics.InferenceNews;
import com.example.news.api.util.AggregationMapping;
import com.example.news.api.util.AggregationRequest;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

@Repository
public class SearchRepository {
    private final ElasticsearchClient esClient;
    private final AggregationRequest aggRequest;

    public SearchRepository(
        ElasticsearchClient esClient,
        AggregationRequest aggRequest
    ){
        this.esClient = esClient;
        this.aggRequest = aggRequest;
    }

    public InferenceNews getInferenceNewsById(String id) throws IOException {
        GetRequest getRequest = aggRequest.getInferenceNewsByIdRequest(id);
        GetResponse<InferenceNews> response = esClient.get(getRequest, InferenceNews.class);
        if (response.found()) {
            InferenceNews news = response.source();
            return news;
        } else {
            return null;
        }
    }

    public List<InferenceNews> getAllInferenceNews() throws IOException {
        List<InferenceNews> allNews = new ArrayList<>();
        SearchRequest searchRequest = aggRequest.getAllInferenceNewsRequest();
        SearchResponse<InferenceNews> response = esClient.search(searchRequest, InferenceNews.class);
        for (Hit<InferenceNews> hit : response.hits().hits()) {
            allNews.add(hit.source());
        }
        return allNews;
    }

    public List<InferenceNews> findInterfaceNewsByText (String searchText) throws IOException {
        List<InferenceNews> allNews = new ArrayList<>();
        SearchRequest searchRequest = aggRequest.findInterfaceNewsByTextRequest(searchText);
        SearchResponse<InferenceNews> response = esClient.search(searchRequest, InferenceNews.class);     
        for (Hit<InferenceNews> hit : response.hits().hits()) {
            allNews.add(hit.source());
        }
        return allNews;   
    }
}
