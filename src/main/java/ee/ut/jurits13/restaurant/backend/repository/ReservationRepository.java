package ee.ut.jurits13.restaurant.backend.repository;

import ee.ut.jurits13.restaurant.backend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDate(LocalDate date);
}