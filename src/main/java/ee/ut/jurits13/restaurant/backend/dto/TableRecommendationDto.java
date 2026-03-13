package ee.ut.jurits13.restaurant.backend.dto;

import java.util.List;

public class TableRecommendationDto {

    private Long tableId;
    private int score;
    private String label;
    private List<String> reasons;

    public TableRecommendationDto() {
    }

    public TableRecommendationDto(Long tableId, int score, String label, List<String> reasons) {
        this.tableId = tableId;
        this.score = score;
        this.label = label;
        this.reasons = reasons;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }
}