package com.example.service;

import java.sql.SQLException;
import java.util.List;

import com.example.dto.EventTrackerDTO;
import com.example.dto.NewsDTO;
import com.example.dto.PowerCoupleDTO;
import com.example.dto.SpatialMapDTO;
import com.example.dto.VolatilityIndexDTO;
import com.example.repository.MetaDataRepository;

public class MetaDataService {
    private final MetaDataRepository metadataRepo;

    public MetaDataService() {
        MetaDataRepository tempRepo;
        try{
            tempRepo = new MetaDataRepository();
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to connect to PostgreSQL", e);
        }
        this.metadataRepo = tempRepo;
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
