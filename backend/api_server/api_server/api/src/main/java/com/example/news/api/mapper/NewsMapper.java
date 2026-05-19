package com.example.news.api.mapper;
import java.util.List;
import com.example.news.api.dto.analytics.DetailedEntityDTO;
import com.example.news.api.dto.jpa.DetailedNewsDTO;
import com.example.news.api.dto.jpa.NewsDTO;
import com.example.news.api.entity.NewsEntity;
import org.springframework.stereotype.Component;

@Component
public class NewsMapper {
    public NewsDTO toDTO(NewsEntity entity) {
        NewsDTO dto = new NewsDTO();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPublishDate(entity.getPublishDate());
        dto.setLink(entity.getLink());
        dto.setLanguage(entity.getLang());
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

    public DetailedNewsDTO toDetailedDTO (NewsEntity entity, List<DetailedEntityDTO> detailedEntity){
        DetailedNewsDTO detailedDTO = new DetailedNewsDTO();

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

        detailedDTO.setEntities(detailedEntity);
        return detailedDTO;
    }
}
