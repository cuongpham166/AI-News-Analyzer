package com.example.news.api.config;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Neo4jConfig {
    @Bean
    public Driver neo4jConnection(){
        Driver driver = GraphDatabase.driver(
            System.getenv("NEO4J_URI"), 
            AuthTokens.basic(
                System.getenv("NEO4J_USER"), 
                System.getenv("NEO4J_PASSWORD")
            ));
        return driver;
    }
}
