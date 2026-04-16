package com.example.utils;

import java.io.IOException;

import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval; 
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.json.JsonData;

public class AggregationRequest {
    public AggregationRequest(){}

    public GetRequest getInferenceNewsByIdRequest(String id) throws IOException {
        return GetRequest.of(g -> g
            .index("news")
            .id(id)
        );
    }

    public SearchRequest getAllInferenceNewsRequest()throws IOException {
        return SearchRequest.of(s -> s
            .index("news")
            .query(q -> q
                    .matchAll(m -> m)
            )
            .size(10)
        );
    }

    public SearchRequest findInterfaceNewsByTextRequest(String searchText) throws IOException {
        return SearchRequest.of(s -> s
            .index("news") 
            .query(q -> q      
                .match(t -> t   
                    .field("title")  
                    .query(searchText)
                )
            )
        );
    }

    public SearchRequest getGlobalTrendsRequest (long startEpoch,long endEpoch, CalendarInterval intervalEnum) throws IOException {
        return SearchRequest.of(s -> s
            .index("news")
            .size(0)
            .query(q -> q
                .bool(b -> b
                    .filter(f -> f.range(r -> r
                        .field("publish_date")
                        .format("epoch_second")
                        .gte(JsonData.of(startEpoch))
                        .lte(JsonData.of(endEpoch))
                        )
                    )
                )
            )
            .aggregations("trends_over_time", a -> a
                .dateHistogram(d -> d
                // USE A SCRIPT TO ENSURE MILLISECONDS
                    .script(sc -> sc.inline(i -> i.source("doc['publish_date'].value * 1000")))
                    .calendarInterval(intervalEnum)
                )   
                .aggregations("avg_sentiment", AggregationData.getAvgSentimentAgg())
                .aggregations("top_topics",AggregationData.getTopicAgg())
            )
        );
    }

    public SearchRequest getGlobalEntitiesTrendsRequest (long startEpoch,long endEpoch, CalendarInterval intervalEnum) throws IOException {
        return SearchRequest.of(s -> s
            .index("news")
            .size(0)
            .query(q -> q
                .bool(b -> b
                    .filter(f -> f.range(r -> r
                        .field("publish_date")
                        .format("epoch_second")
                        .gte(JsonData.of(startEpoch))
                        .lte(JsonData.of(endEpoch))
                        )
                    )
                )
            )
            .aggregations("trends_entities_over_time", a -> a            
                .dateHistogram(d -> d
                    .script(sc -> sc.inline(i -> i.source("doc['publish_date'].value * 1000")))
                    .calendarInterval(intervalEnum)
                )
                .aggregations("top_entities_per_period", subA -> subA
                    .terms(t -> t.field("entities.value.keyword").size(5)) // Top 5 entities per day/week
                    .aggregations("avg_sentiment", sa -> sa.avg(av -> av.field("sentiment")))
                )               
            )
        );
    }

    public SearchRequest getImpactArticlesRequest (long startEpoch,long endEpoch, int topN, boolean isPositive) throws IOException {
        return SearchRequest.of(s -> s
            .index("news")
            .size(topN)
            .query(q -> q.bool(b -> b
                .filter(f -> f.range(r -> r
                    .field("publish_date")
                    .gte(JsonData.of(startEpoch))
                    .lte(JsonData.of(endEpoch))
                ))
            ))
            .sort(so -> so.field(f -> f
                .field("sentiment")
                .order(isPositive ? SortOrder.Desc : SortOrder.Asc)
            ))
        );
    }
}
