# AI News Analyzer

## Introduce

A scalable, event-driven AI platform that collects news from RSS feeds, enriches and analyzes articles in real-time using NLP models, and delivers fast, searchable insights.

### Key Capabilities

- **Event-driven Pipeline** — Decoupled microservices communicating via NATS for reliable, scalable processing.
- **Data Enrichment** — Language detection, deduplication, cleaning, and metadata extraction.
- **AI Analysis** — Inference using Hugging Face Transformers and ONNX Runtime for sentiment, NER, zero-shot classification, and article summarization.
- **Triple-Engine Storage** —
  - **Elasticsearch** — Full-text search and time-series macro-trends.
  - **PostgreSQL** — Relational metadata, statistical profiling, and data integrity.
  - **Neo4j** — **[New]** Knowledge Graph for deep relationship mining and actor-network analysis.
- **Asynchronous Enrichment** - A modular pipeline that establishes data "anchors" (Source/News) and asynchronously backfills AI-extracted intelligence (Entities/Topics) into the graph.
- **Frontend** — React UI served behind NGINX for browsing AI-enriched news.

### Key Features (Version 2.0)

- **News Retrieval** — Efficiently ingest and fetch the latest news data across various global RSS sources. Currently the RSS news are fetched from UN.
- **Full-Text Search** — Powered by Elasticsearch. Currently, the user can find news using specific keywords. The next version will allow users to find specific entries, topics, etc.
- **Multi-Dimensional Analysis** — The platform is designed to perform analyses based on three levels of information density. These design help user move from broad petterns to specific evidence in four steps:
  - **The Pulse Layer (The "What happened?")** — This is the High-Level View. It uses big charts to show you how much news is happening and if the "mood" (sentiment) of the world is happy or sad.
    - **Goal:** Spot a sudden spike in news so you know exactly when a big event started.
  - **The Character Layer (The "Who is involved?")** — This is the Zoomed-In View. It uses a "Treemap" (a box chart) to show you the main people, companies, or countries mentioned in the news.
    - **Goal:** See who the "Main Characters" are. The bigger their box, the more they are being talked about.
  - **The Evidence Layer (The "Why is it happening?")** — This is the Deep-Dive View. Here, you can read the actual articles and AI-generated summaries.
    - **Goal:** Read the facts. It helps you understand the specific reasons behind the news without having to read hundreds of long stories.
  - **The Connection Layer (Relational)** — **[New]** This is the Network View (powered by the Knowledge Graph). It shows lines connecting different people and topics.
    - **Goal:** Find hidden links. For example, it can show you that "Person A" and "Person B" are connected because they were both mentioned in the same five news stories, even if they aren't in the same headline.
- **Dynamic Intervals** — Flexible time-window filtering (1 day, 1 week, 2 weeks, etc.) to switch between "Breaking News" and "Historical Trends."

## Overview

### Tech Stack

![React](https://img.shields.io/badge/React-61DAFB?logo=react&logoColor=white&style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/springboot-6DB33F?logo=springboot&logoColor=white&style=for-the-badge)
![Python](https://img.shields.io/badge/python-3776AB?logo=python&logoColor=white&style=for-the-badge)
![NATS](https://img.shields.io/badge/natsdotio-27AAE1?logo=natsdotio&logoColor=white&style=for-the-badge)

![PostgresSQL](https://img.shields.io/badge/postgresql-4169E1?logo=postgresql&logoColor=white&style=for-the-badge)
![ElasticSearch](https://img.shields.io/badge/elasticsearch-005571?logo=elasticsearch&logoColor=white&style=for-the-badge)
![Neo4j](https://img.shields.io/badge/neo4j-005571?logo=neo4j&logoColor=white&style=for-the-badge)

![PyTorch](https://img.shields.io/badge/pytorch-EE4C2C?logo=pytorch&logoColor=white&style=for-the-badge)
![ONNX](https://img.shields.io/badge/onnx-005CED?logo=onnx&logoColor=white&style=for-the-badge)
![HuggingFace](https://img.shields.io/badge/huggingface-FFD21E?logo=huggingface&logoColor=white&style=for-the-badge)

![NGINX](https://img.shields.io/badge/nginx-009639?logo=nginx&logoColor=white&style=for-the-badge)
![Docker](https://img.shields.io/badge/docker-2496ED?logo=docker&logoColor=white&style=for-the-badge)
![Kibana](https://img.shields.io/badge/kibana-005571?logo=kibana&logoColor=white&style=for-the-badge)

### Architecture Overview

![Overview Structure](/images/Final_Structure_v2.png)

### Database Overview

#### PostgresSQL

![PostgresSQL Structure](/images/Final_DB_Diagram.png)

#### ElasticSearch - Mapping

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

#### Neo4j - Graph Data Model

- **Nodes:**
  - **News**: The central anchor containing title, sentiment, and temporal data.
  - **Entity**: Real-world actors (People, Organizations, Locations) extracted via NER.
  - **Source**: Media outlets and data origins.
  - **Topic**: Thematic classifications (e.g., Politic, Technology, Economy).

- **Relationships:**
  - **(:Source)-[:PUBLISHED]->(:News)**: The central anchor containing title, sentiment, and temporal data.
  - **(:News)-[:COVERS]->(:Topic)**: The central anchor containing title, sentiment, and temporal data.
  - **(:News)-[:MENTIONS]->(:Entity)**: The "Link" providing traceability for every connection.
  - **(:Entity)-[:CO_OCCURS_WITH]->(:Entity)**: A weighted relationship (weight, last_seen) representing the strength of the bond between actors found in shared contexts.
  - **(:Entity)-[:RELATED_TO_TOPIC]->(:Topic)**: Semantic profiling of entities.

#### Database Usage

- **PostgreSQL : The Relational Backbone of the application** — Use Cases:
  - Structured Data Storage
  - Complex Aggregations
  - Data Integrity
- **Elasticsearch : The Search & Discovery Engine** — Use Cases:
  - Full-Text Search
  - Unstructured Data Discovery
- **Neo4j : The Connection Engine** — Use Cases:
  - This is our Knowledge Graph. Instead of rows and columns, it stores data as "Nodes" (People, Topics) and "Lines" (Connections).
  - Mapping the "Discovery Layer." It finds hidden links between different people or organizations by tracking how often they appear together in the same news stories.

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
   - **Neo4j (Relational data)** — First, Neo4jBridge consumes data from `news.enriched` and sends it to Neo4jLayer for persistence. Later, it also consumes from `news.ai` to update the Neo4j data with new inference data.
   - **ElasticSearch (Inference data)** — ElasticBridge consumes data from `news.ai`and sends it to ElasticLayer for persistence. (Kibana dashboards for visualization)

The entire flow is **asynchronous and decoupled** thanks to **NATS** messaging, allowing independent scaling of each stage.

![Overview Structure](/images/Flow_v2.png)
_The Flow Chart_

![Dashboard](/images/Project.png)
_The Example of UI_

![Neo4j](/images/Neo4j_Example.png)

_The Example of Data in Neo4j_
