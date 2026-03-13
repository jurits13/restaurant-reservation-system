package ee.ut.jurits13.restaurant.backend.config;

import ee.ut.jurits13.restaurant.backend.entity.Reservation;
import ee.ut.jurits13.restaurant.backend.entity.RestaurantTable;
import ee.ut.jurits13.restaurant.backend.repository.ReservationRepository;
import ee.ut.jurits13.restaurant.backend.repository.RestaurantTableRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RestaurantTableRepository restaurantTableRepository;
    private final ReservationRepository reservationRepository;

    public DataSeeder(RestaurantTableRepository restaurantTableRepository,
                      ReservationRepository reservationRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... args) {
        if (restaurantTableRepository.count() > 0) {
            return;
        }

        List<RestaurantTable> tables = List.of(

                // HALL - smaller tables
                new RestaurantTable("H1", 2, 50, 50, 60, 60, "HALL", true, false, true, false),
                new RestaurantTable("H2", 2, 130, 50, 60, 60, "HALL", false, true, true, false),
                new RestaurantTable("H3", 2, 210, 50, 60, 60, "HALL", true, true, false, false),
                new RestaurantTable("H4", 2, 290, 50, 60, 60, "HALL", false, false, true, true),

                // HALL - medium tables
                new RestaurantTable("H5", 4, 50, 150, 70, 70, "HALL", true, true, true, false),
                new RestaurantTable("H6", 4, 140, 150, 70, 70, "HALL", false, true, false, true),
                new RestaurantTable("H7", 4, 230, 150, 70, 70, "HALL", true, false, true, true),
                new RestaurantTable("H8", 4, 320, 150, 70, 70, "HALL", false, false, true, false),

                // TERRACE
                new RestaurantTable("T9", 2, 450, 50, 60, 60, "TERRACE", true, false, false, false),
                new RestaurantTable("T10", 4, 530, 50, 70, 70, "TERRACE", true, false, false, false),
                new RestaurantTable("T11", 4, 620, 50, 70, 70, "TERRACE", false, false, true, false),
                new RestaurantTable("T12", 6, 500, 150, 80, 80, "TERRACE", true, false, false, true),

                // PRIVATE
                new RestaurantTable("P13", 4, 700, 250, 70, 70, "PRIVATE", false, true, true, false),
                new RestaurantTable("P14", 6, 790, 250, 80, 80, "PRIVATE", false, true, true, false),
                new RestaurantTable("P15", 8, 890, 240, 90, 90, "PRIVATE", false, true, true, false)
        );

        restaurantTableRepository.saveAll(tables);

        List<RestaurantTable> savedTables = restaurantTableRepository.findAll();

        Random random = new Random(42);
        LocalDate today = LocalDate.now();

        for (RestaurantTable table : savedTables) {
            if (random.nextDouble() < 0.4) {
                LocalTime startTime = LocalTime.of(12 + random.nextInt(9), 0); // 12:00 - 20:00
                LocalTime endTime = startTime.plusHours(2);

                int partySize = Math.min(
                        table.getCapacity(),
                        Math.max(2, table.getCapacity() - random.nextInt(2))
                );

                Reservation reservation = new Reservation(
                        "Customer " + table.getName(),
                        today,
                        startTime,
                        endTime,
                        partySize,
                        table.getId()
                );

                reservationRepository.save(reservation);
            }
        }
    }
}