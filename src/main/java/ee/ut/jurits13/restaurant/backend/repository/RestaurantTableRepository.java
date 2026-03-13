package ee.ut.jurits13.restaurant.backend.repository;

import ee.ut.jurits13.restaurant.backend.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
}
