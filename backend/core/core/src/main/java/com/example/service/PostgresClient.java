package com.example.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import com.example.dto.NewsDTO;
import io.github.cdimascio.dotenv.Dotenv;

public class PostgresClient {
    private final Dotenv dotenv = Dotenv.configure().directory("../../").load();
    private final String url = this.dotenv.get("POSTGRES_JAVA_URL");
    private final String user = this.dotenv.get("POSTGRES_USER");
    private final String password = this.dotenv.get("POSTGRES_PASSWORD");
    private final Connection postgresClient;

    public PostgresClient() throws SQLException {
        this.postgresClient = DriverManager.getConnection(this.url, this.user, this.password);
    }

    public List<NewsDTO> getAllNews(int limit) throws SQLException {
        List<NewsDTO> newsList = new ArrayList<>();
        String sql = "SELECT news.*, topic.name AS topic_name, source.name AS source_name "
                + "FROM news "
                + "LEFT JOIN topic ON news.topic_id = topic.id "
                + "LEFT JOIN source ON news.source_id = source.id "
                + "ORDER BY publish_date DESC "
                + "LIMIT ?";
        try(PreparedStatement pstmt = this.postgresClient.prepareStatement(sql)){
            pstmt.setInt(1, limit);
            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    newsList.add(mapResultSetToNews(rs));
                }
            }
        }
        return newsList;
    }

    public NewsDTO getNewsByLink(String link) throws SQLException {
        String sql = "SELECT news.*, topic.name AS topic_name, source.name AS source_name "
                + "FROM news "
                + "LEFT JOIN topic ON news.topic_id = topic.id "
                + "LEFT JOIN source ON news.source_id = source.id "
                + "WHERE news.link = ?";

        try(PreparedStatement pstmt = this.postgresClient.prepareStatement(sql)){
            pstmt.setString(1, link);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    return mapResultSetToNews(rs);
                }else{
                    return null;
                }
            }
        }
    }

    public NewsDTO mapResultSetToNews(ResultSet rs) throws SQLException {
        NewsDTO news = new NewsDTO();
        news.setId(rs.getInt("id"));
        news.setTitle(rs.getString("title"));
        news.setPublishDate(rs.getTimestamp("publish_date"));
        news.setLink(rs.getString("link"));
        news.setLanguage(rs.getString("lang"));
        news.setFullText(rs.getString("full_text"));
        news.setSummary(rs.getString("summary"));
        news.setSentimentLabel(rs.getString("sentiment_label"));
        news.setSentiment(rs.getBigDecimal("sentiment"));
        news.setTopicId(rs.getInt("topic_id"));
        news.setSourceId(rs.getInt("source_id"));
        news.setTopic_name(rs.getString("topic_name"));
        news.setSource_name(rs.getString("source_name"));
        return news;
    }

    public static void main(String[] args) throws SQLException {
        PostgresClient postgresClient = new PostgresClient();
        //NewsDTO foundNews = postgresClient.getNewsByLink("https://news.un.org/feed/view/en/story/2026/01/1166814");
        //System.out.println("News: "+foundNews);
        //List<NewsDTO> foundNewsList = postgresClient.getAllNews(1);
        //System.out.println("News List : "+foundNewsList);
    }
}
