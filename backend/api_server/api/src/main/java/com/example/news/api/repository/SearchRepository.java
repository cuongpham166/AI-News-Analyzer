package com.example.news.api.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.news.api.dto.*;
import com.example.news.api.util.*;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

@Repository
public class SearchRepository {

    private final ElasticsearchClient esClient;
    private final AggregationMapping aggMapping;
    private final AggregationRequest aggRequest;

    public SearchRepository(
        ElasticsearchClient esClient,
        AggregationMapping aggMapping,
        AggregationRequest aggRequest
    ){
        this.esClient = esClient;
        this.aggMapping = aggMapping;
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

    public GlobalTrendsDTO getGlobalTrendsWithRelativeInterval (String intervalUnit, int amount) throws IOException {
        long[] result = AggregationInterval.computeEpochRangeRelative(intervalUnit,amount);
        long startEpoch = result[0];
        long endEpoch   = result[1];
        SearchResponse<Void> response = getGlobalTrends(startEpoch,endEpoch,intervalUnit);
        GlobalTrendsDTO trendResult = aggMapping.mapGlobalTrends(response);
        return trendResult;
    }

    public SearchResponse<Void> getGlobalTrends (long startEpoch,long endEpoch, String intervalUnit) throws IOException {
        CalendarInterval intervalEnum = AggregationInterval.mapInterval(intervalUnit);
        SearchRequest searchRequest = aggRequest.getGlobalTrendsRequest(startEpoch,endEpoch,intervalEnum);
        SearchResponse<Void> response = esClient.search(searchRequest, Void.class); 
        return response;
    }


    public GlobalEntityTrendsDTO getGlobalEntityWithRelativeInterval (String intervalUnit, int amount) throws IOException {
        long[] result = AggregationInterval.computeEpochRangeRelative(intervalUnit,amount);
        long startEpoch = result[0];
        long endEpoch   = result[1];
        SearchResponse<Void> response = getGlobalEntityTrends(startEpoch,endEpoch,intervalUnit);
        GlobalEntityTrendsDTO trendResult = aggMapping.mapEntityAnalysis(response);
        return trendResult;
    }

    public SearchResponse<Void> getGlobalEntityTrends (long startEpoch,long endEpoch, String intervalUnit) throws IOException {
        CalendarInterval intervalEnum = AggregationInterval.mapInterval(intervalUnit);
        SearchRequest searchRequest = aggRequest.getGlobalEntitiesTrendsRequest(startEpoch,endEpoch,intervalEnum);
        SearchResponse<Void> response = esClient.search(searchRequest, Void.class);
        return response;
    }

    public List<InferenceNews> getImpactArticlesWithRelativeInterval(String intervalUnit, int amount, int topN, boolean isPositive) throws IOException {
        List<InferenceNews> allNews = new ArrayList<>();
        long[] result = AggregationInterval.computeEpochRangeRelative(intervalUnit,amount);
        long startEpoch = result[0];
        long endEpoch   = result[1];   
        SearchRequest searchRequest = aggRequest.getImpactArticlesRequest(startEpoch,endEpoch,topN, isPositive);
        SearchResponse<InferenceNews> response = esClient.search(searchRequest, InferenceNews.class);

        for (Hit<InferenceNews> hit : response.hits().hits()) {
            allNews.add(hit.source());
        }
        return allNews;
    }

    public TopRadarDTO getTopicRadarWithRelativeInterval (String intervalUnit, int amount) throws IOException {
        long[] result = AggregationInterval.computeEpochRangeRelative(intervalUnit,amount);
        long startEpoch = result[0];
        long endEpoch   = result[1];
        SearchResponse<Void> response = getTopicRadar(startEpoch,endEpoch);
        TopRadarDTO radarResult = aggMapping.mapTopicRadar(response);
        return radarResult;
    }

    public SearchResponse<Void> getTopicRadar(long startEpoch,long endEpoch) throws IOException {
        SearchRequest searchRequest = aggRequest.getTopicRadarRequest(startEpoch,endEpoch);
        SearchResponse<Void> response = esClient.search(searchRequest, Void.class);
        return response;
    }
}
