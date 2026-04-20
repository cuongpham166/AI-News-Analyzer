package com.example.service;


import org.apache.http.HttpHost;
import java.io.IOException;
import java.util.*;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.elasticsearch.client.RestClient;

import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval; 
import co.elastic.clients.json.JsonData;

import com.example.dto.InferenceNews;
import com.example.dto.TopRadarDTO;
import com.example.dto.GlobalTrendsDTO;
import com.example.dto.GlobalEntityTrendsDTO;

import com.example.utils.AggregationInterval;
import com.example.utils.AggregationData;
import com.example.utils.AggregationMapping;
import com.example.utils.AggregationRequest;
import io.github.cdimascio.dotenv.Dotenv;

public class ElasticClient {
    private final Dotenv dotenv = Dotenv.configure().directory("../../").load();
    private final String serverUrl = this.dotenv.get("ELASTIC_URL");

    private final RestClient restClient;
    private final ElasticsearchTransport transport;
    private final ElasticsearchClient esClient;
    private final AggregationMapping aggMapping;
    private final AggregationRequest aggRequest;

    public ElasticClient() {
        this.restClient = RestClient.builder(HttpHost.create(this.serverUrl)).build();
        this.transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        this.esClient = new ElasticsearchClient(this.transport);
        this.aggMapping = new AggregationMapping();
        this.aggRequest = new AggregationRequest();
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

    public static void main(String[] args) throws IOException {
        ElasticClient esClient = new ElasticClient();

        GlobalTrendsDTO trendsResult = esClient.getGlobalTrendsWithRelativeInterval("week",1);
        //System.out.println("Global Trends: " + trendsResult);
        //System.out.println("---------------------------------------------------------------------");
        GlobalEntityTrendsDTO entityTrendsResult = esClient.getGlobalEntityWithRelativeInterval("week",2);
        //System.out.println("Global Entity Trends: " + entityTrendsResult);
        List<InferenceNews> allPositveImpactNews = esClient.getImpactArticlesWithRelativeInterval("week",2,3,true);
        //System.out.println("Positive Impact News: " + allPositveImpactNews);
        List<InferenceNews> allNegativeImpactNews = esClient.getImpactArticlesWithRelativeInterval("week",2,3,false);
        //System.out.println("Negative Impact News: " + allNegativeImpactNews);
        
    }
}
