    package com.example.ticketapp.view;

    import android.os.Bundle;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.navigation.NavController;
    import androidx.navigation.Navigation;
    import androidx.navigation.fragment.NavHostFragment;
    import androidx.recyclerview.widget.RecyclerView;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;

    import com.example.ticketapp.adapter.CinemaAdapter;
    import com.example.ticketapp.adapter.MovieAdapter;
    import com.example.ticketapp.databinding.FragmentHomeBinding;
    import com.example.ticketapp.viewmodel.CinemaViewModel;
    import com.example.ticketapp.viewmodel.MovieViewModel;

    import dagger.hilt.android.AndroidEntryPoint;

    @AndroidEntryPoint
    public class HomeFragment extends Fragment {
        private FragmentHomeBinding binding;
        private RecyclerView listMovieSoon;
        private RecyclerView listCinema;
        private CinemaViewModel cinemaViewModel;
        private MovieViewModel movieViewModel;
        private MovieAdapter movieAdapter;
        private CinemaAdapter cinemaAdapter;

        private void setUpAdater() {
            movieAdapter = new MovieAdapter(
            );

            cinemaAdapter = new CinemaAdapter();
            listCinema.setAdapter(cinemaAdapter);
            listMovieSoon.setAdapter(movieAdapter);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            binding = FragmentHomeBinding.inflate(inflater, container, false);
            initView();
            setUpAdater();
            setUpViewModel();

            setUpSwipeRefresh();
            return binding.getRoot();
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            NavController navController = NavHostFragment.findNavController(this);
            setUpNavigation(navController);
        }

        private void setUpNavigation(NavController navController) {
         movieAdapter.setOnItemClickListener(movie -> {
             movieViewModel.setSelectMovie(movie);
             navController.navigate(
                     HomeFragmentDirections.actionNavHomeToDetailsFragment()
             );
         });

        }

        private void setUpSwipeRefresh() {
            binding.swipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light
            );
            binding.swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        }

        private void refreshData() {
            movieViewModel.getMovies();

        }

        private void initView() {
            listMovieSoon = binding.rvComingSoon;
            listCinema = binding.rvCinemas;
        }

        private void setUpViewModel() {
            cinemaViewModel = new ViewModelProvider(requireActivity()).get(CinemaViewModel.class);
            movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);
            cinemaViewModel._listCinema.observe(getViewLifecycleOwner(), listCinema -> {
                cinemaAdapter.updateListCinema(listCinema);

            });
            movieViewModel.movies.observe(getViewLifecycleOwner(), resource -> {
                switch (resource.getStatus()) {
                    case LOADING:
                        binding.swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        binding.swipeRefreshLayout.setRefreshing(false);
                        if (resource.getData() != null) {
                            movieAdapter.updateListMovie(resource.getData());
                        }
                        break;
                    case ERROR:
                        binding.swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            });
          if(movieViewModel.movies.getValue()== null){
              movieViewModel.getMovies();
          }
        }


    }