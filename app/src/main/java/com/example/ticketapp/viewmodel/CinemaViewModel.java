package com.example.ticketapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ticketapp.domain.model.Cinema;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CinemaViewModel extends ViewModel {

    private MutableLiveData<List<Cinema>> listCinema = new MutableLiveData<>();
    public  LiveData<List<Cinema>> _listCinema = listCinema;

    public void setListCinema(List<Cinema> _list){
        listCinema.postValue(_list);

    }
    @Inject
    public CinemaViewModel (){

        listCinema.postValue(new ArrayList<>());
    }

}
