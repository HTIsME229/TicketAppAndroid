package com.example.ticketapp.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ticketapp.R;
import com.example.ticketapp.databinding.ItemMovieBinding;
import com.example.ticketapp.domain.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movieList  ;
    private ItemMovieBinding binding;
    private  OnItemClickListener onItemClickListener;
    public  MovieAdapter( ){
        movieList = new ArrayList<>();


    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public void updateListMovie(List<Movie> _movieList){
        this.movieList = _movieList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = ItemMovieBinding.inflate(
                android.view.LayoutInflater.from(parent.getContext()), parent, false
        );
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
       holder.bind(movieList.get(position));
       holder.itemView.setOnClickListener(v -> {
              if (onItemClickListener != null) {
                onItemClickListener.onItemClick(movieList.get(position));
              }
         });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private  ItemMovieBinding binding;
       public MovieViewHolder ( ItemMovieBinding binding) {
           super(binding.getRoot());
           this.binding = binding;
         }
        void bind(Movie movie) {
            binding.tvMovieTitle.setText(movie.getTitle());
            Glide.with(binding.getRoot().getContext())
                    .load(R.drawable.sample_movie)
                    .into(binding.ivMoviePoster);

        }

    }
    public interface  OnItemClickListener {
        void onItemClick(Movie movie);
    }
}

