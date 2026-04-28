package com.example.news.api.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.news.api.entity.NewsEntity;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Integer> {
    
    @Query("""
        SELECT n FROM NewsEntity n
        JOIN FETCH n.topic
        JOIN FETCH n.source
        """)
    List<NewsEntity> findAllWithRelations(Pageable pageable);

    @Query("""
        SELECT n FROM NewsEntity n
        JOIN FETCH n.topic
        JOIN FETCH n.source
        LEFT JOIN FETCH n.entities
        WHERE n.link = :link
    """)
    Optional<NewsEntity> findDetailByLink(@Param("link") String link);
}
