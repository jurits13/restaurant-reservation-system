package ee.ut.jurits13.restaurant.backend.service;

import ee.ut.jurits13.restaurant.backend.dto.ReservationCreateRequest;
import ee.ut.jurits13.restaurant.backend.entity.Reservation;
import ee.ut.jurits13.restaurant.backend.entity.RestaurantTable;
import ee.ut.jurits13.restaurant.backend.repository.ReservationRepository;
import ee.ut.jurits13.restaurant.backend.repository.RestaurantTableRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              RestaurantTableRepository restaurantTableRepository) {
        this.reservationRepository = reservationRepository;
        this.restaurantTableRepository = restaurantTableRepository;
    }

    public Reservation createReservation(ReservationCreateRequest request) {
        RestaurantTable table = restaurantTableRepository.findById(request.getTableId())
                .orElseThrow(() -> new IllegalArgumentException("Table not found"));

        if (request.getPartySize() > table.getCapacity()) {
            throw new IllegalArgumentException("Party size exceeds table capacity");
        }

        LocalTime startTime = request.getTime();
        LocalTime endTime = startTime.plusHours(2);

        List<Reservation> reservationsForDate = reservationRepository.findByDate(request.getDate());

        for (Reservation reservation : reservationsForDate) {
            if (!reservation.getTableId().equals(table.getId())) {
                continue;
            }

            boolean overlaps =
                    startTime.isBefore(reservation.getEndTime()) &&
                            endTime.isAfter(reservation.getStartTime());

            if (overlaps) {
                throw new IllegalArgumentException("Table is not available for the selected time");
            }
        }

        Reservation reservation = new Reservation(
                request.getCustomerName().trim(),
                request.getDate(),
                startTime,
                endTime,
                request.getPartySize(),
                request.getTableId()
        );

        return reservationRepository.save(reservation);
    }
}