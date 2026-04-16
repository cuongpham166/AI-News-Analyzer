# AI News Analyzer

## Introduce

A scalable, event-driven AI platform that collects news from RSS feeds, enriches and analyzes articles in real-time using NLP models, and delivers fast, searchable insights.

### Key Capabilities

- **Event-driven Pipeline** — Decoupled microservices communicating via NATS for reliable, scalable processing.
- **Data Enrichment** — Language detection, deduplication, cleaning, and metadata extraction.
- **AI Analysis** — Inference using Hugging Face Transformers and ONNX Runtime for sentiment, NER, zero-shot classification, and article summarization.
- **Storage & Search** — Dual storage with Elasticsearch (full-text + semantic search + Kibana dashboards) and PostgreSQL.
- **Frontend** — React UI served behind NGINX for browsing AI-enriched news.

### Key Features (current version 1.0)

- **News Retrieval** — Efficiently ingest and fetch the latest news data across various global RSS sources. Currently the RSS news are fetched from UN.
- **Full-Text Search** — Powered by Elasticsearch. Currently, the user can find news using specific keywords. The next version will allow users to find specific entries, topics, etc.
- **News Analysis** — The platform is designed to perform analyses based on three levels of information density. These design help user move from broad petterns to specific evidence in three steps:
  - **Macro Layer (When?)** — It is visualized by Area Chart. This layer does more than just track sentiment changes and loudness spikes; it also automatically extracts the Top Topics for any given interval. When an event is breaking, users may quickly determine whether particular topics are driving the story.
  - **Meso Layer (Who?)** — Finding the "Main Characters." This layer identifies particular entities (such as organizations, person, location, etc) involved in a subject spike. The Treemap identifies the main characters by coloring items according to sentiment and scaling them according to the number of mentions.
  - **Micro Layer (Why?)** — Users can read the full text of the most impactful positive and negative articles for any given time period.
- **Dynamic Intervals** — Flexible time-window filtering (1 day, 1 week, 2 weeks, etc.) to switch between "Breaking News" and "Historical Trends."

## Overview

### Tech Stack

![React](https://img.shields.io/badge/React-61DAFB?logo=react&logoColor=white&style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/springboot-6DB33F?logo=springboot&logoColor=white&style=for-the-badge)
![Python](https://img.shields.io/badge/python-3776AB?logo=python&logoColor=white&style=for-the-badge)
![NATS](https://img.shields.io/badge/natsdotio-27AAE1?logo=natsdotio&logoColor=white&style=for-the-badge)

![PostgresSQL](https://img.shields.io/badge/postgresql-4169E1?logo=postgresql&logoColor=white&style=for-the-badge)
![ElasticSearch](https://img.shields.io/badge/elasticsearch-005571?logo=elasticsearch&logoColor=white&style=for-the-badge)
![Kibana](https://img.shields.io/badge/kibana-005571?logo=kibana&logoColor=white&style=for-the-badge)

![PyTorch](https://img.shields.io/badge/pytorch-EE4C2C?logo=pytorch&logoColor=white&style=for-the-badge)
![ONNX](https://img.shields.io/badge/onnx-005CED?logo=onnx&logoColor=white&style=for-the-badge)
![HuggingFace](https://img.shields.io/badge/huggingface-FFD21E?logo=huggingface&logoColor=white&style=for-the-badge)

![NGINX](https://img.shields.io/badge/nginx-009639?logo=nginx&logoColor=white&style=for-the-badge)
![Docker](https://img.shields.io/badge/docker-2496ED?logo=docker&logoColor=white&style=for-the-badge)

### Architecture Overview

![Overview Structure](/images/Final_Structure.png)

### Database Overview

#### PostgresSQL

![PostgresSQL Structure](/images/Final_DB_Diagram.png)

#### ElasticSearch

```
{
    "mappings": {
        "properties": {
            "@timestamp": {"type": "date"},
            "sentiment_label": {"type": "keyword"},
            "sentiment": {"type": "float"},
            "topic": {"type": "keyword"},
            "entities": {
                "type": "nested",
                "properties": {
                    "value": {"type": "text"},
                    "entity_type": {"type": "keyword"}
                }
            },
            "summary": {"type": "text"},
            "link": {"type": "keyword"},
            "publish_date": {"type": "date", "format": "epoch_second"},
            "title": {"type": "text"},
            "source": {"type": "keyword"}
        }
    }
}
```

#### Datanase Usage

- **PostgreSQL : The Relational Backbone of the application** — Use Cases:
  - Structured Data Storage
  - Complex Aggregations
  - Data Integrity
- **Elasticsearch : The Search & Discovery Engine** — Use Cases:
  - Full-Text Search
  - Unstructured Data Discovery

### Inference Overview

The project utilizes a custom inference approach using Hugging face Transformers (for PyTorch models) and ONNX Runtime (for ONNX models) for high performance model excution. The PyTorch model can be converted to ONNX by the written script. Rather than relying on high level pipeline and abstractions, the preprocssing (tokenization) and post-processing (logi transformation) are implemented manually to ensure the control over the inference lifecycle

| Task                     | Model                         | Task                                                                                      |
| :----------------------- | :---------------------------- | :---------------------------------------------------------------------------------------- |
| Zero-Shot Classification | mDeBERTa-v3-base-mnli-xnli    | Categorizing news into 8 topics (Politics, Economy, Tech, etc.)                           |
| Sentiment Analysis       | distilbert-base-uncased-sst-2 | Binary classification (Positive/Negative) to calculate "Global Temperature" metrics.      |
| Named Entity Recognition | gliner-bi-base-v2             | Identifying and categorize 9 entity types including People, Location, Events, and Titles. |
| Summarization            | distilbart-cnn-12-6           | Generating summary from long-form article text                                            |

## How It Works

### Data Flow Overview

The platform follows a clean, event-driven data pipeline that moves news articles from raw ingestion to fully analyzed and searchable content.

1. **Raw Ingestion** — RSS Scrapper pediodically fetches articles from RSS sources and publishes them as raw events to the `news.raw` NATS topic.
2. **Enrichment** — The Data Enrichment service consumes data from `news.raw` and performs language detection, deduplication, text cleaning, and metadata extraction, then publishes the improved articles to the `news.enriched` topic.
3. **AI Analysis** — The Inference Bridge consumes from `news.enriched` and sends data to the Inference Layer for processing. Analyzed results are published to the `news.ai` topic.
4. **Storage** — The PostgresBridge and ElasticBridge consume the results and persist them:
   - **PostgreSQL (Structured metadata)** — First, PostgresBridge consumes data from `news.enriched` and sends it to PostgresLayer for persistence. Later, it also consumes from `news.ai` to update the PostgreSQL data with new inference data.
   - **ElasticSearch (Inference data)** — ElasticBridge consumes data from `news.ai`and sends it to ElasticLayer for persistence. (Kibana dashboards for visualization)

The entire flow is **asynchronous and decoupled** thanks to **NATS** messaging, allowing independent scaling of each stage.

![Overview Structure](/images/Flow.png)
![Dashboard](/images/Project.png)
