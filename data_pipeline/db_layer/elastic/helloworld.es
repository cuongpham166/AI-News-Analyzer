#GET _cluster/health

{
  "aggs": {
    "sentiment_counts": {
      "terms": {"field": "sentiment_label"}
    }
  }
}