package com.example.ticketapp.data.network;

import com.example.ticketapp.domain.model.Cinema;
import com.example.ticketapp.domain.model.Movie;
import com.example.ticketapp.domain.model.Showtimes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("getMovies")
        // endpoint Firebase Functions
    Call<List<Movie>> getMovies();

    @GET("getCinemas")
    Call<List<Cinema>> getCinemas(@Query("city") String city);

    @GET("getShowTimes")
    Call<List<Showtimes>> getShowTimes(@Query("date") String date, @Query("cinemaId") String cinemaID);
}

