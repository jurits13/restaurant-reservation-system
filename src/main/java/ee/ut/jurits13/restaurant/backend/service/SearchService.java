package ee.ut.jurits13.restaurant.backend.service;

import ee.ut.jurits13.restaurant.backend.dto.SearchRequest;
import ee.ut.jurits13.restaurant.backend.dto.SearchResult;
import ee.ut.jurits13.restaurant.backend.dto.TableRecommendationDto;
import ee.ut.jurits13.restaurant.backend.entity.Reservation;
import ee.ut.jurits13.restaurant.backend.entity.RestaurantTable;
import ee.ut.jurits13.restaurant.backend.repository.ReservationRepository;
import ee.ut.jurits13.restaurant.backend.repository.RestaurantTableRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SearchService {

    private final RestaurantTableRepository restaurantTableRepository;
    private final ReservationRepository reservationRepository;

    public SearchService(RestaurantTableRepository restaurantTableRepository,
                         ReservationRepository reservationRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
        this.reservationRepository = reservationRepository;
    }

    public SearchResult search(SearchRequest request) {
        List<RestaurantTable> allTables = restaurantTableRepository.findAll();
        List<Reservation> reservationsForDate = reservationRepository.findByDate(request.getDate());

        List<RestaurantTable> availableTables = new ArrayList<>();

        for (RestaurantTable table : allTables) {
            if (matchesFilters(table, request) && isTableAvailable(table, reservationsForDate, request)) {
                availableTables.add(table);
            }
        }

        List<TableRecommendationDto> recommendations = availableTables.stream()
                .map(table -> new TableRecommendationDto(
                        table.getId(),
                        calculateScore(table, request),
                        "",
                        buildReasons(table, request)
                ))
                .sorted(Comparator.comparingInt(TableRecommendationDto::getScore).reversed())
                .toList();

        List<TableRecommendationDto> labeledRecommendations = assignLabels(recommendations);

        Long bestTableId = labeledRecommendations.isEmpty()
                ? null
                : labeledRecommendations.get(0).getTableId();

        List<Long> goodTableIds = labeledRecommendations.stream()
                .filter(dto -> "GOOD".equals(dto.getLabel()))
                .map(TableRecommendationDto::getTableId)
                .toList();

        SearchResult result = new SearchResult();
        result.setTables(allTables);
        result.setReservations(reservationsForDate);
        result.setBestTableId(bestTableId);
        result.setGoodTableIds(goodTableIds);
        result.setRecommendations(labeledRecommendations);

        return result;
    }

    private boolean matchesFilters(RestaurantTable table, SearchRequest request) {
        if (table.getCapacity() < request.getPartySize()) {
            return false;
        }

        if (request.getZone() != null
                && !request.getZone().isBlank()
                && !table.getZone().equalsIgnoreCase(request.getZone())) {
            return false;
        }

        return true;
    }

    private boolean isTableAvailable(RestaurantTable table, List<Reservation> reservations, SearchRequest request) {
        LocalTime requestedStart = request.getTime();
        LocalTime requestedEnd = request.getTime().plusHours(2);

        for (Reservation reservation : reservations) {
            if (!reservation.getTableId().equals(table.getId())) {
                continue;
            }

            boolean overlaps =
                    requestedStart.isBefore(reservation.getEndTime()) &&
                            requestedEnd.isAfter(reservation.getStartTime());

            if (overlaps) {
                return false;
            }
        }

        return true;
    }

    private int calculateScore(RestaurantTable table, SearchRequest request) {
        int score = 0;

        int extraSeats = table.getCapacity() - request.getPartySize();

        if (extraSeats == 0) {
            score += 50;
        } else if (extraSeats == 1) {
            score += 40;
        } else if (extraSeats == 2) {
            score += 30;
        } else {
            score += Math.max(0, 20 - extraSeats * 5);
        }

        score += preferenceScore(request.isWindowSeat(), table.isWindowSeat(), 15);
        score += preferenceScore(request.isQuiet(), table.isQuiet(), 15);
        score += preferenceScore(request.isAccessible(), table.isAccessible(), 20);
        score += preferenceScore(request.isNearKidsArea(), table.isNearKidsArea(), 10);

        return score;
    }

    private int preferenceScore(boolean requested, boolean actual, int points) {
        if (!requested) {
            return 0;
        }

        return actual ? points : -points;
    }

    private List<String> buildReasons(RestaurantTable table, SearchRequest request) {
        List<String> reasons = new ArrayList<>();

        int extraSeats = table.getCapacity() - request.getPartySize();

        if (extraSeats == 0) {
            reasons.add("Exact capacity match");
        } else if (extraSeats == 1) {
            reasons.add("Very good size fit");
        } else {
            reasons.add(extraSeats + " extra seat(s)");
        }

        if (request.isWindowSeat() && table.isWindowSeat()) {
            reasons.add("Matches window seat preference");
        }

        if (request.isQuiet() && table.isQuiet()) {
            reasons.add("Matches quiet area preference");
        }

        if (request.isAccessible() && table.isAccessible()) {
            reasons.add("Matches accessibility preference");
        }

        if (request.isNearKidsArea() && table.isNearKidsArea()) {
            reasons.add("Matches near kids area preference");
        }

        if (reasons.size() == 1) {
            reasons.add("Available for selected time");
        }

        return reasons;
    }

    private List<TableRecommendationDto> assignLabels(List<TableRecommendationDto> sortedRecommendations) {
        List<TableRecommendationDto> labeled = new ArrayList<>();

        for (int i = 0; i < sortedRecommendations.size(); i++) {
            TableRecommendationDto dto = sortedRecommendations.get(i);

            String label;
            if (i == 0) {
                label = "BEST";
            } else if (i < 3) {
                label = "GOOD";
            } else {
                label = "AVAILABLE";
            }

            labeled.add(new TableRecommendationDto(
                    dto.getTableId(),
                    dto.getScore(),
                    label,
                    dto.getReasons()
            ));
        }

        return labeled;
    }
}