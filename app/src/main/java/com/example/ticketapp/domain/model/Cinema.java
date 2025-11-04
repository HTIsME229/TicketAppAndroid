package com.example.ticketapp.domain.model;

public class Cinema {
    private  String name;
    private String logoUrl;
    private double rating;
   private String info;
   private  String address;
   private String city;
    public Cinema(String name, String logoUrl, double rating, String info, String address, String city) {
        this.name = name;
        this.logoUrl = logoUrl;
        this.rating = rating;
        this.info = info;
        this.address = address;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public double getRating() {
        return rating;
    }

    public String getInfo() {
        return info;
    }
}
