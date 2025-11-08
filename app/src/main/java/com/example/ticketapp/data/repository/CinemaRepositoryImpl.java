package com.example.ticketapp.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ticketapp.data.network.ApiService;
import com.example.ticketapp.domain.model.Cinema;
import com.example.ticketapp.domain.model.Movie;
import com.example.ticketapp.domain.repository.CinemaRepository;
import com.example.ticketapp.utils.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@Singleton
public class CinemaRepositoryImpl implements CinemaRepository {
    private final ApiService apiService;
@Inject
    public CinemaRepositoryImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public LiveData<Resource<List<Cinema>>> getCinemas(String selectedCity) {
        MutableLiveData<Resource<List<Cinema>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading());

        apiService.getCinemas(selectedCity).enqueue(new Callback<List<Cinema>>() {
            @Override
            public void onResponse(Call<List<Cinema>> call, Response<List<Cinema>> response) {
                if (response.isSuccessful()) {
                    data.setValue(Resource.success(response.body()));
                } else {
                    Log.d("CinemaRepositoryImpl", "Response error: " + response.message());
                    data.setValue(Resource.error("Lỗi: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<Cinema>> call, Throwable t) {
                Log.d("CinemaRepositoryImpl", "Response error: " + t.getMessage());

                data.setValue(Resource.error("Thất bại: " + t.getMessage()));
            }
        });

        return data;
    }


}
