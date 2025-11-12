package com.example.ticketapp.domain.model;

import java.util.List;

public class Ticket {
    private String movieName;
    private String cinemaName;
    private List<String> seatNames;
    private String time;
    private String payment;
    private String OrderId;
    public Ticket(String movieName, String cinemaName, List<String> seatNames, String time, String payment, String orderId) {
        this.movieName = movieName;
        this.cinemaName = cinemaName;
        this.seatNames = seatNames;
        this.time = time;
        this.payment = payment;
        OrderId = orderId;
    }


}
