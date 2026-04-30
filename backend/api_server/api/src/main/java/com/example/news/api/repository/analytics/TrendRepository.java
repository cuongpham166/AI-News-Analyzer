package com.example.news.api.repository.analytics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.news.api.dto.analytics.GlobalEntityTrendsDTO;
import com.example.news.api.dto.analytics.GlobalTrendsDTO;
import com.example.news.api.dto.analytics.InferenceNews;
import com.example.news.api.dto.analytics.TopRadarDTO;
import com.example.news.api.util.AggregationInterval;
import com.example.news.api.util.AggregationMapping;
import com.example.news.api.util.AggregationRequest;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

@Repository
public class TrendRepository {
    private final ElasticsearchClient esClient;
    private final AggregationMapping aggMapping;
    private final AggregationRequest aggRequest;
    private final AggregationInterval aggInterval;

    public TrendRepository(
        ElasticsearchClient esClient,
        AggregationMapping aggMapping,
        AggregationRequest aggRequest,
        AggregationInterval aggInterval
    ){
        this.esClient = esClient;
        this.aggMapping = aggMapping;
        this.aggRequest = aggRequest;
        this.aggInterval = aggInterval;
    }

    public GlobalTrendsDTO getGlobalTrendsWithRelativeInterval (String intervalUnit, int amount) throws IOException {
        long[] result = this.aggInterval.computeEpochRangeRelative(intervalUnit,amount);
        long startEpoch = result[0];
        long endEpoch   = result[1];
        SearchResponse<Void> response = getGlobalTrends(startEpoch,endEpoch,intervalUnit);
        return aggMapping.mapGlobalTrends(response);
    }

    public SearchResponse<Void> getGlobalTrends (long startEpoch,long endEpoch, String intervalUnit) throws IOException {
        CalendarInterval intervalEnum = this.aggInterval.mapInterval(intervalUnit);
        SearchRequest searchRequest = aggRequest.getGlobalTrendsRequest(startEpoch,endEpoch,intervalEnum);
        return esClient.search(searchRequest, Void.class);
    }


    public GlobalEntityTrendsDTO getGlobalEntityWithRelativeInterval (String intervalUnit, int amount) throws IOException {
        long[] result = this.aggInterval.computeEpochRangeRelative(intervalUnit,amount);
        long startEpoch = result[0];
        long endEpoch   = result[1];
        SearchResponse<Void> response = getGlobalEntityTrends(startEpoch,endEpoch,intervalUnit);
        return aggMapping.mapEntityAnalysis(response);
    }

    public SearchResponse<Void> getGlobalEntityTrends (long startEpoch,long endEpoch, String intervalUnit) throws IOException {
        CalendarInterval intervalEnum = this.aggInterval.mapInterval(intervalUnit);
        SearchRequest searchRequest = aggRequest.getGlobalEntitiesTrendsRequest(startEpoch,endEpoch,intervalEnum);
        return esClient.search(searchRequest, Void.class);
    }

    public List<InferenceNews> getImpactArticlesWithRelativeInterval(String intervalUnit, int amount, int topN, boolean isPositive) throws IOException {
        List<InferenceNews> allNews = new ArrayList<>();
        long[] result = this.aggInterval.computeEpochRangeRelative(intervalUnit,amount);
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
        long[] result = this.aggInterval.computeEpochRangeRelative(intervalUnit,amount);
        long startEpoch = result[0];
        long endEpoch   = result[1];
        SearchResponse<Void> response = getTopicRadar(startEpoch,endEpoch);
        return aggMapping.mapTopicRadar(response);
    }

    public SearchResponse<Void> getTopicRadar(long startEpoch,long endEpoch) throws IOException {
        SearchRequest searchRequest = aggRequest.getTopicRadarRequest(startEpoch,endEpoch);
        return esClient.search(searchRequest, Void.class);
    }
}
