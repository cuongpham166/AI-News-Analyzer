package com.example.repository;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.QueryConfig;

import com.example.utils.Neo4jQuery;

import io.github.cdimascio.dotenv.Dotenv;

public class DiscoveryRepository {
   private final Dotenv dotenv = Dotenv.configure().directory("../../").load();
    private final String neo4jUri = this.dotenv.get("NEO4J_URI");
    private final String neo4jUser = this.dotenv.get("NEO4J_USER");
    private final String neo4jPassword = this.dotenv.get("NEO4J_PASSWORD");
    private final String neo4j_DB = this.dotenv.get("NEO4J_DB"); 
    private Driver driver = null;
    private final Neo4jQuery neo4jQuery;


    public DiscoveryRepository(){
        this.driver = GraphDatabase.driver(this.neo4jUri, AuthTokens.basic(this.neo4jUser, this.neo4jPassword));
        this.neo4jQuery = new Neo4jQuery();
    }


    public Driver getDriver() {
        return this.driver;
    }

    public void close() {
        if (this.driver != null) {
            this.driver.close();
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
            this.driver.executableQuery(query)
                .withConfig(QueryConfig.builder().withDatabase(this.neo4j_DB).build())
                .execute();            
        }
    }
    
    public static void main(String... args) {
        DiscoveryRepository discoveryRepo = new DiscoveryRepository();
        try {
            discoveryRepo.createInitConstraints();
        } finally {
            discoveryRepo.close(); 
        }
    }
}
