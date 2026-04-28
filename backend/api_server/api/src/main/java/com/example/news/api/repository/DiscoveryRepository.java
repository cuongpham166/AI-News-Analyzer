package com.example.news.api.repository;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.QueryConfig;
import org.springframework.stereotype.Repository;

import com.example.news.api.util.DiscoveryQuery;;

@Repository
public class DiscoveryRepository {
    private final Driver neo4jDriver;
    private final DiscoveryQuery neo4jQuery;

    public DiscoveryRepository(Driver driver,DiscoveryQuery neo4jQuery){
        this.neo4jDriver = driver;
        this.neo4jQuery = neo4jQuery;
    }

    public Driver getDriver() {
        return this.neo4jDriver;
    }

    public void close() {
        if (this.neo4jDriver != null) {
            this.neo4jDriver.close();
        }
    }

    public void createInitConstraints() {
        String[] queries = {
            neo4jQuery.createSourceConstraintQuery(),
            neo4jQuery.createTopicConstraintQuery(),
            neo4jQuery.createEntityConstraintQuery(),
            neo4jQuery.createNewsConstraintQuery(),
        };

        for (String query : queries){
            this.neo4jDriver.executableQuery(query)
                .withConfig(QueryConfig.builder().withDatabase(System.getenv("NEO4J_DB")).build())
                .execute();            
        }
    }
}
