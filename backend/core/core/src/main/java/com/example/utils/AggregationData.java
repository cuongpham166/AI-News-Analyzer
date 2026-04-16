package com.example.utils;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;

public class AggregationData {

    public static Aggregation getAvgSentimentAgg() {
        return Aggregation.of(a -> a
            .avg(v -> v.field("sentiment").missing(0.0))
        );
    }

    public static Aggregation getTopicAgg() {
        return Aggregation.of(a -> a
            .terms(t -> t.field("topic.keyword").size(3))
        );
    }
}
