package com.example.ticketapp.di;

import com.example.ticketapp.data.repository.MovieRepositoryImpl;
import com.example.ticketapp.domain.repository.MovieRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {

    @Binds
    @Singleton
    public abstract MovieRepository bindMovieRepository(MovieRepositoryImpl impl);
}
