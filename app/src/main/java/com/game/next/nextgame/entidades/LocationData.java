package com.game.next.nextgame.entidades;

public class LocationData {

    private String userId;
    private String time;
    private String latitude;
    private String longitude;
    private String entregaLatitude;
    private String entregaLongitude;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getEntregaLatitude() {
        return entregaLatitude;
    }

    public void setEntregaLatitude(String entregaLatitude) {
        this.entregaLatitude = entregaLatitude;
    }

    public String getEntregaLongitude() {
        return entregaLongitude;
    }

    public void setEntregaLongitude(String entregaLongitude) {
        this.entregaLongitude = entregaLongitude;
    }
}
