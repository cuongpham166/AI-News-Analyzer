package com.example.news.api.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.news.api.dto.*;
import com.example.news.api.repository.*;

@Service
public class MetaDataService {
    private final MetaDataRepository metadataRepo;
    
    public MetaDataService(MetaDataRepository metadataRepo){
        this.metadataRepo = metadataRepo;
    }

    public List<NewsDTO> getAllNews(int limit) throws SQLException {
        List<NewsDTO> newsList = this.metadataRepo.getAllNews(limit);
        return newsList;
    }

    public NewsDTO getNewsByLink(String link) throws SQLException {
        NewsDTO foundNews = this.metadataRepo.getNewsByLink(link);
        return foundNews;
    }

    public List<SpatialMapDTO> getSpatialMapWithRelativeInterval (String intervalUnit, int amount)throws SQLException {
        List<SpatialMapDTO> result = this.metadataRepo.getSpatialMapWithRelativeInterval(intervalUnit, amount);
        return result;
    }

    public List<PowerCoupleDTO> getPowerCoupleWithRelativeInterval (String intervalUnit, int amount)throws SQLException {
        List<PowerCoupleDTO> result = this.metadataRepo.getPowerCoupleWithRelativeInterval(intervalUnit, amount);
        return result;
    }

    public List<EventTrackerDTO> getEventTrackerWithRelativeInterval (String intervalUnit, int amount)throws SQLException {
        List <EventTrackerDTO> result = this.metadataRepo.getEventTrackerWithRelativeInterval(intervalUnit, amount);
        return result;
    }

    public List<VolatilityIndexDTO> getVolatilityIndexWithRelativeInterval (String intervalUnit, int amount)throws SQLException {
        List <VolatilityIndexDTO> result = this.metadataRepo.getVolatilityIndexWithRelativeInterval(intervalUnit, amount);
        return result;
    }

}
