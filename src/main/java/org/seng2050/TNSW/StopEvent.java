package org.seng2050.TNSW;

public class StopEvent {
    private String routeNumber;
    private String destination;
    private String location;
    private String departureTimePlanned;

    // Getters and setters

    public String getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(String routeNumber) {
        this.routeNumber = routeNumber;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartureTimePlanned() {
        return departureTimePlanned;
    }

    public void setDepartureTimePlanned(String departureTimePlanned) {
        this.departureTimePlanned = departureTimePlanned;
    }
}
