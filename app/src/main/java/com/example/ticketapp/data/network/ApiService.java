package com.example.ticketapp.data.network;

import com.example.ticketapp.domain.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("getMovies") // endpoint Firebase Functions
    Call<List<Movie>> getMovies();
}

