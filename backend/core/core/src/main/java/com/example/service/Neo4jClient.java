package com.example.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.QueryConfig;

import io.github.cdimascio.dotenv.Dotenv;

import com.example.utils.Neo4jQuery;


public class Neo4jClient {
    private final Dotenv dotenv = Dotenv.configure().directory("../../").load();
    private final String neo4jUri = this.dotenv.get("NEO4J_URI");
    private final String neo4jUser = this.dotenv.get("NEO4J_USER");
    private final String neo4jPassword = this.dotenv.get("NEO4J_PASSWORD");
    private final String neo4j_DB = this.dotenv.get("NEO4J_DB"); 
    private Driver driver = null;
    private final Neo4jQuery neo4jQuery;


    public Neo4jClient(){
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
        Neo4jClient neo4jClient = new Neo4jClient();
        try {
            neo4jClient.createInitConstraints();
        } finally {
            neo4jClient.close(); 
        }
    }
}
