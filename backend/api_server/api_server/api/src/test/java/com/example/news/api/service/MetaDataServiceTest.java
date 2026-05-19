package com.example.news.api.service;

import com.example.news.api.dto.analytics.DetailedEntityDTO;
import com.example.news.api.dto.jpa.DetailedNewsDTO;
import com.example.news.api.dto.jpa.NewsDTO;
import com.example.news.api.entity.NewsEntity;
import com.example.news.api.mapper.NewsMapper;
import com.example.news.api.repository.analytics.RelationshipRepository;
import com.example.news.api.repository.jpa.NewsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetaDataServiceTest {

    @Mock
    private NewsRepository newsRepo;

    @Mock
    private RelationshipRepository relationshipRepo;

    @Mock
    private NewsMapper newsMapper;

    @InjectMocks
    private MetaDataService metaDataService;

    private NewsEntity news1;
    private NewsEntity news2;

    private NewsDTO dto1;
    private NewsDTO dto2;

    private DetailedEntityDTO entityDTO;
    private DetailedNewsDTO detailedNewsDTO;

    private String link;
    private int sourceId;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        news1 = new NewsEntity();
        news2 = new NewsEntity();

        dto1 = new NewsDTO();
        dto2 = new NewsDTO();

        entityDTO = new DetailedEntityDTO();
        detailedNewsDTO = new DetailedNewsDTO();

        link = "link";

        sourceId = 1;
        pageable = PageRequest.of(
                0, // page index (0 = first page)
                10,
                Sort.by(Sort.Direction.DESC, "publishDate")
        );
    }

    @Test
    void getAllNews_shouldReturnMappedDTOs() {
        //Arrange
        int limit = 2;
        when(newsRepo.findAllWithRelations(PageRequest.of(0, limit)))
                .thenReturn(List.of(news1, news2));
        when(newsMapper.toDTO(news1))
                .thenReturn(dto1);
        when(newsMapper.toDTO(news2))
                .thenReturn(dto2);
        //Act
        List<NewsDTO>result = metaDataService.getAllNews(limit);

        //Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(dto1, dto2);

        verify(newsRepo).findAllWithRelations(PageRequest.of(0, limit));
        verify(newsMapper).toDTO(news1);
        verify(newsMapper).toDTO(news2);
        verifyNoMoreInteractions(newsRepo, newsMapper);
    }

    @Test
    void getAllNews_shouldReturnEmptyList_WhenNoData() {
        //Arrange
        int limit = 4;
        when(newsRepo.findAllWithRelations(PageRequest.of(0, limit)))
                .thenReturn(List.of());
        //Act
        List<NewsDTO>result = metaDataService.getAllNews(limit);

        //Assert
        assertThat(result).isEmpty();
        verify(newsRepo).findAllWithRelations(PageRequest.of(0, limit));
        verifyNoMoreInteractions(newsMapper);
    }

    @Test
    void getAllNewsBySourceId_shouldReturnMappedDTOs(){
        //Arrange
        when(this.newsRepo.findAllBySourceId(sourceId, pageable))
                .thenReturn(List.of(news1, news2));
        when(this.newsMapper.toDTO(news1))
                .thenReturn(dto1);
        when(this.newsMapper.toDTO(news2))
                .thenReturn(dto2);
        //Act
        List<NewsDTO> result = this.metaDataService.getAllNewsBySourceId(sourceId);

        //Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(dto1,dto2);
        verify(newsRepo).findAllBySourceId(sourceId, pageable);
        verify(newsMapper).toDTO(news1);
        verify(newsMapper).toDTO(news2);
        verifyNoMoreInteractions(newsRepo, newsMapper);
    }

    @Test
    void getAllNewsBySourceId_shouldReturnMappedDTOs_WhenNoData(){
        //Arrange
        when(this.newsRepo.findAllBySourceId(sourceId, pageable))
                .thenReturn(List.of());
        //Act
        List<NewsDTO> result = this.metaDataService.getAllNewsBySourceId(sourceId);

        //Assert
        assertThat(result).isEmpty();
        verify(newsRepo).findAllBySourceId(sourceId,pageable);
        verifyNoMoreInteractions(newsMapper);
    }

    @Test
    void getDetailedNewsByLink_shouldReturnDetailedNewsDTO_withDetailedEntityDTO() {
        //Arrange
        List<DetailedEntityDTO> entities = List.of(entityDTO);

        when(newsRepo.findDetailByLink(link)).thenReturn(Optional.of(news1));
        when(relationshipRepo.findEntitiesByNewsLink(link)).thenReturn(entities);
        when(newsMapper.toDetailedDTO(news1,entities)).thenReturn(detailedNewsDTO);

        //Action
        DetailedNewsDTO result = metaDataService.getDetailedNewsByLink(link);

        //Assert
        assertEquals(detailedNewsDTO,result);
        verify(newsRepo).findDetailByLink(link);
        verify(relationshipRepo).findEntitiesByNewsLink(link);
        verify(newsMapper).toDetailedDTO(news1,List.of(entityDTO));
        verifyNoMoreInteractions(newsRepo, relationshipRepo, newsMapper);
    }

    @Test
    void getDetailedNewsByLink_shouldReturnDetailedNewsDTO_withEmptyDetailedEntityDTO() {
        //Arrange
        when(newsRepo.findDetailByLink(link)).thenReturn(Optional.of(news1));
        when(relationshipRepo.findEntitiesByNewsLink(link)).thenReturn(List.of());
        when(newsMapper.toDetailedDTO(news1,List.of())).thenReturn(detailedNewsDTO);

        //Action
        DetailedNewsDTO result = metaDataService.getDetailedNewsByLink(link);

        //Assert
        assertEquals(detailedNewsDTO,result);
        verify(newsRepo).findDetailByLink(link);
        verify(relationshipRepo).findEntitiesByNewsLink(link);
        verify(newsMapper).toDetailedDTO(news1,List.of());
        verifyNoMoreInteractions(newsRepo, relationshipRepo, newsMapper);
    }

    @Test
    void getDetailedNewsByLink_shouldThrowRuntimeException() {
        //Arrange
        when(newsRepo.findDetailByLink(link)).thenReturn(Optional.empty());

        //Action + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> metaDataService.getDetailedNewsByLink(link)
        );
        assertEquals("News not found",ex.getMessage());
        verify(newsRepo).findDetailByLink(link);
        verifyNoMoreInteractions(relationshipRepo, newsMapper);
    }
}