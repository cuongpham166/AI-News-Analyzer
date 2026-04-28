package com.example.news.api.mapper;

import java.util.HashSet;
import java.util.Set;

import com.example.news.api.dto.jpa.DetailedNewsDTO;
import com.example.news.api.dto.jpa.EntityDTO;
import com.example.news.api.dto.jpa.EntityTypeDTO;
import com.example.news.api.dto.jpa.NewsDTO;
import com.example.news.api.entity.EntityEntity;
import com.example.news.api.entity.NewsEntity;

public class NewsMapper {
    public static NewsDTO toDTO(NewsEntity entity) {
        NewsDTO dto = new NewsDTO();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPublishDate(entity.getPublishDate());
        dto.setLink(entity.getLink());
        dto.setLanguage(entity.getLang());
        dto.setFullText(entity.getFullText());
        dto.setSummary(entity.getSummary());
        dto.setSentimentLabel(entity.getSentimentLabel());
        dto.setSentiment(entity.getSentiment());

        if(entity.getTopic() != null){
            dto.setTopicId(entity.getTopic().getId());
            dto.setTopic_name(entity.getTopic().getName());
        }

        if(entity.getSource() != null){
            dto.setSourceId(entity.getSource().getId());
            dto.setSource_name(entity.getSource().getName());
        }

        return dto;
    }

    public static DetailedNewsDTO toDetailedDTO (NewsEntity entity){
        DetailedNewsDTO detailedDTO = new DetailedNewsDTO();
        Set<EntityDTO> entitiesSet = new HashSet<EntityDTO>();
        
        detailedDTO.setId(entity.getId());
        detailedDTO.setTitle(entity.getTitle());
        detailedDTO.setPublishDate(entity.getPublishDate());
        detailedDTO.setLink(entity.getLink());
        detailedDTO.setLanguage(entity.getLang());
        detailedDTO.setFullText(entity.getFullText());
        detailedDTO.setSummary(entity.getSummary());
        detailedDTO.setSentimentLabel(entity.getSentimentLabel());
        detailedDTO.setSentiment(entity.getSentiment());

        if(entity.getTopic() != null){
            detailedDTO.setTopicId(entity.getTopic().getId());
            detailedDTO.setTopic_name(entity.getTopic().getName());
        }

        if(entity.getSource() != null){
            detailedDTO.setSourceId(entity.getSource().getId());
            detailedDTO.setSource_name(entity.getSource().getName());
        }

        if(entity.getEntities().size() > 0){
            entity.getEntities().forEach(newsEntity-> {
                EntityDTO entityDTO = new EntityDTO();
                entityDTO.setId(newsEntity.getId());
                entityDTO.setValue(newsEntity.getName());
                
                EntityTypeDTO entityTypeDTO= new EntityTypeDTO();
                entityTypeDTO.setId(newsEntity.getEntityType().getId());
                entityTypeDTO.setName(newsEntity.getEntityType().getName());

                entityDTO.setEntityTpe(entityTypeDTO);

                entitiesSet.add(entityDTO);
            });
        }

        detailedDTO.setEntities(entitiesSet);
        return detailedDTO;
    }
}
