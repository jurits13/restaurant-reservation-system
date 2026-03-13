package ee.ut.jurits13.restaurant.backend.dto;

import ee.ut.jurits13.restaurant.backend.entity.Reservation;
import ee.ut.jurits13.restaurant.backend.entity.RestaurantTable;

import java.util.List;

public class SearchResult {

    private List<RestaurantTable> tables;
    private List<Reservation> reservations;

    private Long bestTableId;
    private List<Long> goodTableIds;

    private List<TableRecommendationDto> recommendations;

    public SearchResult() {
    }

    public List<RestaurantTable> getTables() {
        return tables;
    }

    public void setTables(List<RestaurantTable> tables) {
        this.tables = tables;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Long getBestTableId() {
        return bestTableId;
    }

    public void setBestTableId(Long bestTableId) {
        this.bestTableId = bestTableId;
    }

    public List<Long> getGoodTableIds() {
        return goodTableIds;
    }

    public void setGoodTableIds(List<Long> goodTableIds) {
        this.goodTableIds = goodTableIds;
    }

    public List<TableRecommendationDto> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<TableRecommendationDto> recommendations) {
        this.recommendations = recommendations;
    }
}