package ee.ut.jurits13.restaurant.backend.service;

import ee.ut.jurits13.restaurant.backend.dto.SearchRequest;
import ee.ut.jurits13.restaurant.backend.dto.SearchResult;
import ee.ut.jurits13.restaurant.backend.dto.TableRecommendationDto;
import ee.ut.jurits13.restaurant.backend.entity.Reservation;
import ee.ut.jurits13.restaurant.backend.entity.RestaurantTable;
import ee.ut.jurits13.restaurant.backend.repository.ReservationRepository;
import ee.ut.jurits13.restaurant.backend.repository.RestaurantTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private RestaurantTableRepository restaurantTableRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private SearchService searchService;

    private SearchRequest request;

    @BeforeEach
    void setUp() {
        request = new SearchRequest();
        request.setDate(LocalDate.of(2026, 3, 12));
        request.setTime(LocalTime.of(18, 0));
        request.setPartySize(2);
        request.setZone("HALL");
        request.setQuiet(true);
        request.setWindowSeat(false);
        request.setAccessible(false);
        request.setNearKidsArea(false);
    }

    @Test
    void search_shouldRecommendExactCapacityMatch_asBest() {
        RestaurantTable exactFit = new RestaurantTable("T1", 2, 50, 50, 60, 60,
                "HALL", false, true, true, false);
        exactFit.setId(1L);

        RestaurantTable largerTable = new RestaurantTable("T2", 4, 150, 50, 70, 70,
                "HALL", false, true, true, false);
        largerTable.setId(2L);

        when(restaurantTableRepository.findAll()).thenReturn(List.of(exactFit, largerTable));
        when(reservationRepository.findByDate(request.getDate())).thenReturn(List.of());

        SearchResult result = searchService.search(request);

        assertEquals(1L, result.getBestTableId());
        assertNotNull(result.getRecommendations());
        assertFalse(result.getRecommendations().isEmpty());

        TableRecommendationDto best = result.getRecommendations().get(0);
        assertEquals(1L, best.getTableId());
        assertEquals("BEST", best.getLabel());
    }

    @Test
    void search_shouldExcludeReservedTable_fromRecommendations() {
        RestaurantTable reservedTable = new RestaurantTable("T1", 2, 50, 50, 60, 60,
                "HALL", false, true, true, false);
        reservedTable.setId(1L);

        RestaurantTable freeTable = new RestaurantTable("T2", 2, 150, 50, 60, 60,
                "HALL", false, true, true, false);
        freeTable.setId(2L);

        Reservation reservation = new Reservation(
                "Alice",
                request.getDate(),
                LocalTime.of(17, 30),
                LocalTime.of(19, 30),
                2,
                1L
        );

        when(restaurantTableRepository.findAll()).thenReturn(List.of(reservedTable, freeTable));
        when(reservationRepository.findByDate(request.getDate())).thenReturn(List.of(reservation));

        SearchResult result = searchService.search(request);

        assertEquals(2L, result.getBestTableId());
        assertTrue(
                result.getRecommendations().stream()
                        .noneMatch(recommendation -> recommendation.getTableId().equals(1L))
        );
    }

    @Test
    void search_shouldRespectZoneFilter() {
        RestaurantTable hallTable = new RestaurantTable("T1", 2, 50, 50, 60, 60,
                "HALL", false, true, true, false);
        hallTable.setId(1L);

        RestaurantTable terraceTable = new RestaurantTable("T2", 2, 150, 50, 60, 60,
                "TERRACE", false, true, true, false);
        terraceTable.setId(2L);

        when(restaurantTableRepository.findAll()).thenReturn(List.of(hallTable, terraceTable));
        when(reservationRepository.findByDate(request.getDate())).thenReturn(List.of());

        SearchResult result = searchService.search(request);

        assertTrue(
                result.getRecommendations().stream()
                        .allMatch(recommendation -> recommendation.getTableId().equals(1L))
        );
    }

    @Test
    void search_shouldPreferMatchingPreference_whenSizesAreEqual() {
        RestaurantTable quietTable = new RestaurantTable("T1", 2, 50, 50, 60, 60,
                "HALL", false, true, true, false);
        quietTable.setId(1L);

        RestaurantTable nonQuietTable = new RestaurantTable("T2", 2, 150, 50, 60, 60,
                "HALL", false, false, true, false);
        nonQuietTable.setId(2L);

        when(restaurantTableRepository.findAll()).thenReturn(List.of(quietTable, nonQuietTable));
        when(reservationRepository.findByDate(request.getDate())).thenReturn(List.of());

        SearchResult result = searchService.search(request);

        assertEquals(1L, result.getBestTableId());
    }
}