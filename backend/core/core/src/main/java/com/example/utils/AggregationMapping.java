package com.example.utils;

import java.util.*;
import java.util.Collections;
import java.text.SimpleDateFormat;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch._types.aggregations.DateHistogramBucket;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;

import com.example.dto.GlobalTrendsDTO;
import com.example.dto.TrendBucketDTO;

import com.example.dto.GlobalEntityTrendsDTO;
import com.example.dto.EntityCountDTO;

public class AggregationMapping {

    public AggregationMapping(){}

    public GlobalTrendsDTO mapGlobalTrends(SearchResponse<Void> response) {
        List<TrendBucketDTO> trendBuckets = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        if (response.aggregations() == null || !response.aggregations().containsKey("trends_over_time")) {
            return new GlobalTrendsDTO(Collections.emptyList());
        }

        List<DateHistogramBucket> buckets = response.aggregations()
            .get("trends_over_time")
            .dateHistogram()
            .buckets()
            .array();

        for (DateHistogramBucket bucket : buckets) {
            TrendBucketDTO dtoBucket = new TrendBucketDTO();

            // Now bucket.key() is in MILLISECONDS because of our script above
            long epochMillis = bucket.key(); 
            
            // Format normally
            dtoBucket.setDate(sdf.format(new Date(epochMillis)));
            
            dtoBucket.setArticleCount(bucket.docCount());

            if (bucket.aggregations().containsKey("avg_sentiment")) {
                double avg = bucket.aggregations().get("avg_sentiment").avg().value();
                dtoBucket.setAverageSentiment(Double.isNaN(avg) ? 0.0 : avg);
            }

            if (bucket.aggregations().containsKey("top_topics")) {
                Map<String, Long> topicsMap = new HashMap<String, Long>();
                List<StringTermsBucket> topicBuckets = bucket.aggregations()
                    .get("top_topics")
                    .sterms()
                    .buckets()
                    .array();
                for (StringTermsBucket topicBucket : topicBuckets) {
                    topicsMap.put(topicBucket.key().stringValue(), topicBucket.docCount());
                }
                dtoBucket.setTopTopics(topicsMap);
            }
            trendBuckets.add(dtoBucket);
        }
        GlobalTrendsDTO dto = new GlobalTrendsDTO();
        dto.setTimeline(trendBuckets);
        return dto;
    }

    public GlobalEntityTrendsDTO mapEntityAnalysis(SearchResponse<Void> response) {
        GlobalEntityTrendsDTO result = new GlobalEntityTrendsDTO();
        Map<String, List<EntityCountDTO>> timeline = new LinkedHashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        List<DateHistogramBucket> timeBuckets = response.aggregations()
            .get("trends_entities_over_time")
            .dateHistogram()
            .buckets()
            .array();

        for (DateHistogramBucket timeBucket : timeBuckets) {
            // Convert the bucket key (millis) to a date string
            String dateKey = sdf.format(new Date(timeBucket.key()));
            List<EntityCountDTO> entitiesForThisDate = new ArrayList<>();    
            
            List<StringTermsBucket> entityBuckets = timeBucket.aggregations()
                .get("top_entities_per_period")
                .sterms()
                .buckets()
                .array();

            for (StringTermsBucket entityBucket : entityBuckets) {
                EntityCountDTO entityDTO = new EntityCountDTO();
                entityDTO.setName(entityBucket.key().stringValue());
                entityDTO.setCount(entityBucket.docCount());

                if (entityBucket.aggregations().containsKey("avg_sentiment")) {
                    double avg = entityBucket.aggregations().get("avg_sentiment").avg().value();
                    entityDTO.setAverageSentiment(Double.isNaN(avg) ? 0.0 : avg);
                }

                entitiesForThisDate.add(entityDTO);
            }
            timeline.put(dateKey, entitiesForThisDate);
        }
        result.setTimeline(timeline);
        return result;
    }
    
}
