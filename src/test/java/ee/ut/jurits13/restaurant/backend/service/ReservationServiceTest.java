package ee.ut.jurits13.restaurant.backend.service;

import ee.ut.jurits13.restaurant.backend.dto.ReservationCreateRequest;
import ee.ut.jurits13.restaurant.backend.entity.Reservation;
import ee.ut.jurits13.restaurant.backend.entity.RestaurantTable;
import ee.ut.jurits13.restaurant.backend.repository.ReservationRepository;
import ee.ut.jurits13.restaurant.backend.repository.RestaurantTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RestaurantTableRepository restaurantTableRepository;

    @InjectMocks
    private ReservationService reservationService;

    private RestaurantTable table;
    private ReservationCreateRequest request;

    @BeforeEach
    void setUp() {
        table = new RestaurantTable("T1", 4, 100, 100, 60, 60,
                "HALL", true, true, true, false);
        table.setId(1L);

        request = new ReservationCreateRequest();
        request.setCustomerName("John");
        request.setDate(LocalDate.of(2026, 3, 12));
        request.setTime(LocalTime.of(18, 0));
        request.setPartySize(2);
        request.setTableId(1L);
    }

    @Test
    void createReservation_shouldFail_whenReservationOverlaps() {
        Reservation existingReservation = new Reservation(
                "Alice",
                LocalDate.of(2026, 3, 12),
                LocalTime.of(17, 0),
                LocalTime.of(19, 0),
                2,
                1L
        );

        when(restaurantTableRepository.findById(1L)).thenReturn(Optional.of(table));
        when(reservationRepository.findByDate(LocalDate.of(2026, 3, 12)))
                .thenReturn(List.of(existingReservation));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reservationService.createReservation(request)
        );

        assertEquals("Table is not available for the selected time", exception.getMessage());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservation_shouldSucceed_whenReservationDoesNotOverlap() {
        Reservation existingReservation = new Reservation(
                "Alice",
                LocalDate.of(2026, 3, 12),
                LocalTime.of(15, 0),
                LocalTime.of(17, 0),
                2,
                1L
        );

        when(restaurantTableRepository.findById(1L)).thenReturn(Optional.of(table));
        when(reservationRepository.findByDate(LocalDate.of(2026, 3, 12)))
                .thenReturn(List.of(existingReservation));
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation savedReservation = reservationService.createReservation(request);

        assertNotNull(savedReservation);
        assertEquals("John", savedReservation.getCustomerName());
        assertEquals(LocalTime.of(18, 0), savedReservation.getStartTime());
        assertEquals(LocalTime.of(20, 0), savedReservation.getEndTime());

        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void createReservation_shouldFail_whenPartySizeExceedsCapacity() {
        request.setPartySize(5);

        when(restaurantTableRepository.findById(1L)).thenReturn(Optional.of(table));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reservationService.createReservation(request)
        );

        assertEquals("Party size exceeds table capacity", exception.getMessage());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservation_shouldTrimCustomerName_beforeSaving() {
        request.setCustomerName("   John   ");

        when(restaurantTableRepository.findById(1L)).thenReturn(Optional.of(table));
        when(reservationRepository.findByDate(LocalDate.of(2026, 3, 12)))
                .thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        reservationService.createReservation(request);

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository).save(captor.capture());

        Reservation saved = captor.getValue();
        assertEquals("John", saved.getCustomerName());
    }
}