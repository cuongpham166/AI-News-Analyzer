package com.example;

import com.example.dto.InferenceNews;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        // URL and API key
        String serverUrl = "http://localhost:9200";

        // Create the low-level client
        RestClient restClient = RestClient
            .builder(HttpHost.create(serverUrl))
            .build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        GetResponse<InferenceNews> response = esClient.get(g -> g
                .index("news")
                .id("https://news.un.org/feed/view/en/story/2026/04/1167268"),
            InferenceNews.class
        );

        if (response.found()) {
            InferenceNews news = response.source();
            System.out.println("Product name " + news);
        } else {
            System.out.println("Product not found");
        }
    }
}