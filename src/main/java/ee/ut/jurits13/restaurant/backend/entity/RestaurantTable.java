package ee.ut.jurits13.restaurant.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int capacity;
    private int x;
    private int y;
    private int width;
    private int height;
    private String zone;
    private boolean windowSeat;
    private boolean quiet;
    private boolean accessible;
    private boolean nearKidsArea;

    public RestaurantTable() {
    }

    public RestaurantTable(String name, int capacity, int x, int y, int width, int height,
                           String zone, boolean windowSeat, boolean quiet,
                           boolean accessible, boolean nearKidsArea) {
        this.name = name;
        this.capacity = capacity;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.zone = zone;
        this.windowSeat = windowSeat;
        this.quiet = quiet;
        this.accessible = accessible;
        this.nearKidsArea = nearKidsArea;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public boolean isWindowSeat() {
        return windowSeat;
    }

    public void setWindowSeat(boolean windowSeat) {
        this.windowSeat = windowSeat;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    public boolean isNearKidsArea() {
        return nearKidsArea;
    }

    public void setNearKidsArea(boolean nearKidsArea) {
        this.nearKidsArea = nearKidsArea;
    }

    @Override
    public String toString() {
        return "RestaurantTable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", zone='" + zone + '\'' +
                '}';
    }
}
