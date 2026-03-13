package ee.ut.jurits13.restaurant.backend.controller;

import ee.ut.jurits13.restaurant.backend.dto.ReservationCreateRequest;
import ee.ut.jurits13.restaurant.backend.entity.Reservation;
import ee.ut.jurits13.restaurant.backend.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public Reservation createReservation(@Valid @RequestBody ReservationCreateRequest request) {
        return reservationService.createReservation(request);
    }
}