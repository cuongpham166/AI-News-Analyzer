package com.example.news.api.service;

import com.example.news.api.dto.analytics.*;
import com.example.news.api.repository.analytics.RelationshipRepository;
import com.example.news.api.repository.analytics.SentimentRepository;
import com.example.news.api.repository.analytics.SpatialRepository;
import com.example.news.api.repository.analytics.TrendRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalysisServiceTest {

    @Mock
    private RelationshipRepository relationshipRepo;

    @Mock
    private SpatialRepository spatialRepo;

    @Mock
    private SentimentRepository sentimentRepo;

    @Mock
    private TrendRepository trendRepo;

    @InjectMocks
    private  AnalysisService analysisService;

    SpatialMapDTO spatialMapDTO1;
    SpatialMapDTO spatialMapDTO2;
    PowerCoupleDTO powerCoupleDTO;
    EventTrackerDTO eventTrackerDTO;
    VolatilityIndexDTO volatilityIndexDTO;
    String intervalUnit;
    int intervalAmount;
    IOException ioException;
    GlobalTrendsDTO globalTrendsDTO;
    GlobalEntityTrendsDTO globalEntityTrendsDTO;
    InferenceNews positiveInferenceNews1;
    InferenceNews positiveInferenceNews2;
    int topN;
    boolean isPositive;
    TopRadarDTO topRadarDTO;
    GraphResponseDTO graphResponseDTO;

    @BeforeEach
    void setUp() {
        spatialMapDTO1 = new SpatialMapDTO();
        spatialMapDTO2 = new SpatialMapDTO();
        powerCoupleDTO = new PowerCoupleDTO();
        eventTrackerDTO = new EventTrackerDTO();
        volatilityIndexDTO = new VolatilityIndexDTO();
        intervalUnit = "day";
        intervalAmount=1;
        ioException = new IOException("Repo Failure");
        globalTrendsDTO = new GlobalTrendsDTO();
        globalEntityTrendsDTO = new GlobalEntityTrendsDTO();
        positiveInferenceNews1 = new InferenceNews();
        positiveInferenceNews2 = new InferenceNews();
        topN = 2;
        isPositive = true;
        topRadarDTO = new TopRadarDTO();
        graphResponseDTO = new GraphResponseDTO();
    }

    @Nested
    class GetSpatialMapWithRelativeInterval{
        @Test
        void getSpatialMapWithRelativeInterval_shouldReturnSpatialMapDTOsFromRepo(){
            //Arrange
            when(spatialRepo.getSpatialMapWithRelativeInterval(intervalUnit,intervalAmount))
                    .thenReturn(List.of(spatialMapDTO1, spatialMapDTO2));
            //Act
            List<SpatialMapDTO> result = analysisService.getSpatialMapWithRelativeInterval(intervalUnit,intervalAmount);
            //Assert
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(spatialMapDTO1,spatialMapDTO2);
            verify(spatialRepo).getSpatialMapWithRelativeInterval(intervalUnit,intervalAmount);
        }

        @Test
        void getSpatialMapWithRelativeInterval_shouldReturnEmptyListFromRepo_whenNoData(){
            //Arrange
            when(spatialRepo.getSpatialMapWithRelativeInterval(intervalUnit,intervalAmount))
                    .thenReturn(List.of());
            //Act
            List<SpatialMapDTO> result = analysisService.getSpatialMapWithRelativeInterval(intervalUnit,intervalAmount);
            //Assert
            assertThat(result).isEmpty();
            verify(spatialRepo).getSpatialMapWithRelativeInterval(intervalUnit, intervalAmount);
        }
    }


    @Nested
    class GetPowerCoupleWithRelativeInterval{
        @Test
        void getPowerCoupleWithRelativeInterval_shouldReturnPowerCoupleDTOsFromRepo(){
            //Arrange
            when(relationshipRepo.getPowerCoupleWithRelativeInterval(intervalUnit,intervalAmount))
                    .thenReturn(List.of(powerCoupleDTO));
            //Act
            List<PowerCoupleDTO> result = analysisService.getPowerCoupleWithRelativeInterval(intervalUnit,intervalAmount);
            //Assert
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(powerCoupleDTO);
            verify(relationshipRepo).getPowerCoupleWithRelativeInterval(intervalUnit,intervalAmount);
        }

        @Test
        void getPowerCoupleWithRelativeInterval_shouldReturnEmptyListFromRepo_whenNoData(){
            //Arrange
            when(relationshipRepo.getPowerCoupleWithRelativeInterval(intervalUnit,intervalAmount))
                    .thenReturn(List.of());
            //Act
            List<PowerCoupleDTO> result = analysisService.getPowerCoupleWithRelativeInterval(intervalUnit,intervalAmount);
            //Assert
            assertThat(result).isEmpty();
            verify(relationshipRepo).getPowerCoupleWithRelativeInterval(intervalUnit, intervalAmount);
        }
    }

    @Nested
    class GetEventTrackerWithRelativeInterval{
        @Test
        void getEventTrackerWithRelativeInterval_shouldReturnEventTrackerDTOsFromRepo(){
            //Arrange
            when(relationshipRepo.getEventTrackerWithRelativeInterval(intervalUnit,intervalAmount))
                    .thenReturn(List.of(eventTrackerDTO));
            //Act
            List<EventTrackerDTO> result = analysisService.getEventTrackerWithRelativeInterval(intervalUnit,intervalAmount);
            //Assert
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(eventTrackerDTO);
            verify(relationshipRepo).getEventTrackerWithRelativeInterval(intervalUnit,intervalAmount);
        }

        @Test
        void getEventTrackerWithRelativeInterval_shouldReturnEmptyListFromRepo_whenNoData(){
            //Arrange
            when(relationshipRepo.getEventTrackerWithRelativeInterval(intervalUnit,intervalAmount))
                    .thenReturn(List.of());
            //Act
            List<EventTrackerDTO> result = analysisService.getEventTrackerWithRelativeInterval(intervalUnit,intervalAmount);
            //Assert
            assertThat(result).isEmpty();
            verify(relationshipRepo).getEventTrackerWithRelativeInterval(intervalUnit, intervalAmount);
        }
    }

    @Nested
    class GetVolatilityIndexWithRelativeInterval{
        @Test
        void getVolatilityIndexWithRelativeInterval_shouldReturnVolatilityIndexDTOsFromRepo(){
            //Arrange
            when(sentimentRepo.getVolatilityIndexWithRelativeInterval(intervalUnit,intervalAmount))
                    .thenReturn(List.of(volatilityIndexDTO));
            //Act
            List<VolatilityIndexDTO> result = analysisService.getVolatilityIndexWithRelativeInterval(intervalUnit,intervalAmount);
            //Assert
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(volatilityIndexDTO);
            verify(sentimentRepo).getVolatilityIndexWithRelativeInterval(intervalUnit,intervalAmount);
        }

        @Test
        void getVolatilityIndexWithRelativeInterval_shouldReturnEmptyListFromRepo_whenNoData(){
            //Arrange
            when(sentimentRepo.getVolatilityIndexWithRelativeInterval(intervalUnit,intervalAmount))
                    .thenReturn(List.of());
            //Act
            List<VolatilityIndexDTO> result = analysisService.getVolatilityIndexWithRelativeInterval(intervalUnit,intervalAmount);
            //Assert
            assertThat(result).isEmpty();
            verify(sentimentRepo).getVolatilityIndexWithRelativeInterval(intervalUnit, intervalAmount);
        }
    }

    @Nested
    class GetGlobalTrendsWithRelativeInterval{
        @Test
        void getGlobalTrendsWithRelativeInterval_shouldReturnGlobalTrendsDTOFromRepo()  throws IOException {
            when(trendRepo.getGlobalTrendsWithRelativeInterval(intervalUnit,intervalAmount))
                    .thenReturn(globalTrendsDTO);
            GlobalTrendsDTO result = analysisService.getGlobalTrendsWithRelativeInterval(intervalUnit,intervalAmount);
            assertEquals(globalTrendsDTO, result);
            verify(trendRepo).getGlobalTrendsWithRelativeInterval(intervalUnit,intervalAmount);
        }

        @Test
        void getGlobalTrendsWithRelativeInterval_shouldThrowIOException_whenRepoFails() throws IOException {
            when(trendRepo.getGlobalTrendsWithRelativeInterval(intervalUnit,intervalAmount))
                    .thenThrow(ioException);
            IOException thrown = assertThrows(IOException.class,
                    () -> analysisService.getGlobalTrendsWithRelativeInterval(intervalUnit, intervalAmount)
            );

            assertSame(ioException, thrown);
            verify(trendRepo).getGlobalTrendsWithRelativeInterval(intervalUnit,intervalAmount);
        }
    }

    @Nested
    class GetGlobalEntityWithRelativeInterval{
        @Test
        void getGlobalEntityWithRelativeInterval_shouldReturnGlobalEntityTrendsDTOFromRepo()  throws IOException {
            when(trendRepo.getGlobalEntityWithRelativeInterval(intervalUnit,intervalAmount))
                    .thenReturn(globalEntityTrendsDTO);
            GlobalEntityTrendsDTO result = analysisService.getGlobalEntityWithRelativeInterval(intervalUnit,intervalAmount);
            assertEquals(globalEntityTrendsDTO,result);
            verify(trendRepo).getGlobalEntityWithRelativeInterval(intervalUnit,intervalAmount);
        }

        @Test
        void getGlobalEntityWithRelativeInterval_shouldThrowIOException_whenRepoFails() throws  IOException{
            when(trendRepo.getGlobalEntityWithRelativeInterval(intervalUnit,intervalAmount))
                    .thenThrow(ioException);
            IOException thrown = assertThrows(IOException.class,
                    () -> analysisService.getGlobalEntityWithRelativeInterval(intervalUnit, intervalAmount)
            );
            assertSame(ioException, thrown);
            verify(trendRepo).getGlobalEntityWithRelativeInterval(intervalUnit,intervalAmount);
        }
    }

    @Nested
    class GetImpactArticlesWithRelativeInterval{
        @Test
        void getImpactArticlesWithRelativeInterval_shouldReturnListOfPositiveInferenceNewsFromRepo_whenIsPositiveTrue() throws  IOException{
            //Arrange
            positiveInferenceNews1.setSentiment(0.2f);
            positiveInferenceNews2.setSentiment(0.4f);
            List<InferenceNews> positiveArticles = List.of(positiveInferenceNews1, positiveInferenceNews2);
            when(trendRepo.getImpactArticlesWithRelativeInterval(intervalUnit,intervalAmount,topN, isPositive))
                    .thenReturn(positiveArticles);

            //Act
            List<InferenceNews> result = analysisService.getImpactArticlesWithRelativeInterval(
                    intervalUnit,intervalAmount,topN,isPositive
            );
            //Assert
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(positiveInferenceNews1, positiveInferenceNews2);
            assertTrue(result.stream().allMatch(inferenceNews -> inferenceNews.getSentiment() > 0));
            verify(trendRepo).getImpactArticlesWithRelativeInterval(intervalUnit,intervalAmount,topN,isPositive);
        }

        @Test
        void getImpactArticlesWithRelativeInterval_shouldReturnEmptyListOfPositiveInferenceNewsFromRepo_whenIsPositiveTrue() throws  IOException{
            //Arrange
            when(trendRepo.getImpactArticlesWithRelativeInterval(intervalUnit,intervalAmount,topN, isPositive))
                    .thenReturn(List.of());
            //Act
            List<InferenceNews> result = analysisService.getImpactArticlesWithRelativeInterval(
                    intervalUnit,intervalAmount,topN,isPositive
            );
            //Assert
            assertThat(result).isEmpty();
            verify(trendRepo).getImpactArticlesWithRelativeInterval(intervalUnit,intervalAmount,topN,isPositive);
        }

        @Test
        void getImpactArticlesWithRelativeInterval_shouldThrowIOException_whenRepoFails() throws  IOException{
            //Arrange
            when(trendRepo.getImpactArticlesWithRelativeInterval(intervalUnit,intervalAmount,topN, isPositive))
                    .thenThrow(ioException);
            //Act
            IOException thrownException = assertThrows(
                    IOException.class,
                    ()->analysisService.getImpactArticlesWithRelativeInterval(intervalUnit,intervalAmount, topN, isPositive)
            );
            //Assert
            assertSame(ioException, thrownException);
            verify(trendRepo).getImpactArticlesWithRelativeInterval(intervalUnit,intervalAmount, topN, isPositive);
        }
    }

    @Nested
    class GetTopicRadarWithRelativeInterval{
        @Test
        void getTopicRadarWithRelativeInterval_shouldReturnTopRadarDTOFromRepo() throws IOException {
            //Arrange
            when(trendRepo.getTopicRadarWithRelativeInterval(intervalUnit, intervalAmount))
                    .thenReturn(topRadarDTO);
            //Act
            TopRadarDTO result = analysisService.getTopicRadarWithRelativeInterval(intervalUnit,intervalAmount);
            //Assert
            assertEquals(topRadarDTO,result);
            verify(trendRepo).getTopicRadarWithRelativeInterval(intervalUnit, intervalAmount);
        }

        @Test
        void getTopicRadarWithRelativeInterval_shouldThrowIOException_whenRepoFails() throws IOException {
            //Arrange
            when(trendRepo.getTopicRadarWithRelativeInterval(intervalUnit, intervalAmount))
                    .thenThrow(ioException);
            //Act
            IOException thrownException = assertThrows(
                    IOException.class,
                    () -> analysisService.getTopicRadarWithRelativeInterval(intervalUnit, intervalAmount)
            );
            //Assert
            assertSame(ioException,thrownException);
            verify(trendRepo).getTopicRadarWithRelativeInterval(intervalUnit, intervalAmount);
        }
    }

    @Nested
    class GetDiscoveryDataWithRelativeInterval{
        @Test
        void getDiscoveryDataWithRelativeInterval_shouldReturnGraphResponseDTOFromRepo(){
            //Arrange
            when(relationshipRepo.getDiscoveryData(intervalUnit, intervalAmount))
                    .thenReturn(graphResponseDTO);
            //Act
            GraphResponseDTO result = analysisService.getDiscoveryDataWithRelativeInterval(intervalUnit,intervalAmount);

            //Assert
            assertEquals(graphResponseDTO, result);
            verify(relationshipRepo).getDiscoveryData(intervalUnit, intervalAmount);
        }
    }
}