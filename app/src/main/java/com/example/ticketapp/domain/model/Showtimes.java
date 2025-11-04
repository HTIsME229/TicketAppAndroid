package com.example.ticketapp.domain.model;

import android.os.Build;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


public class Showtimes {
    // ID của document trong Firestore, thường được gán sau khi lấy dữ liệu
    private String uid;

    // Các trường dữ liệu chính
    private String movieId;
    private String cinemaId;
    private String theaterId;
    private Date startTime; // Firestore lưu trữ dạng Timestamp, SDK tự động chuyển đổi sang java.util.Date
    private Date endTime;

    private String movieTitle;
    private String moviePosterUrl;
    private String cinemaName;
    private String city;
    private String theaterName;


    private int availableSeats;
    private int totalSeats;

    public Showtimes() {
    }

    // Constructor đầy đủ để dễ dàng tạo đối tượng trong code
    public Showtimes(String movieId, String cinemaId, String theaterId, Date startTime, Date endTime,
                     String movieTitle, String moviePosterUrl, String cinemaName, String city,
                     String theaterName, int availableSeats, int totalSeats) {
        this.movieId = movieId;
        this.cinemaId = cinemaId;
        this.theaterId = theaterId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.movieTitle = movieTitle;
        this.moviePosterUrl = moviePosterUrl;
        this.cinemaName = cinemaName;
        this.city = city;
        this.theaterName = theaterName;
        this.availableSeats = availableSeats;
        this.totalSeats = totalSeats;
    }

    public String getId() {
        return uid;
    }

    public void setId(String uid) {
        this.uid = uid;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(String theaterId) {
        this.theaterId = theaterId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMoviePosterUrl() {
        return moviePosterUrl;
    }

    public void setMoviePosterUrl(String moviePosterUrl) {
        this.moviePosterUrl = moviePosterUrl;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTheaterName() {
        return theaterName;
    }

    public void setTheaterName(String theaterName) {
        this.theaterName = theaterName;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    // --- Các phương thức tiện ích (tùy chọn) ---

    /**
     * Chuyển đổi startTime từ Date sang LocalDateTime để dễ xử lý hơn trong Java 8+
     */
    public LocalDateTime getStartTimeAsLocalDateTime() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return null;
    }

    /**
     * Chuyển đổi endTime từ Date sang LocalDateTime
     */
    public LocalDateTime getEndTimeAsLocalDateTime() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Showtimes{" +
                "uid='" + uid + '\'' +
                ", movieTitle='" + movieTitle + '\'' +
                ", cinemaName='" + cinemaName + '\'' +
                ", startTime=" + startTime +
                ", availableSeats=" + availableSeats +
                '}';
    }
}