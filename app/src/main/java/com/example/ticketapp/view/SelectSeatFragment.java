package com.example.ticketapp.view;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.ticketapp.R;
import com.example.ticketapp.adapter.SeatAdapter;
import com.example.ticketapp.databinding.FragmentSelectSeatBinding;
import com.example.ticketapp.domain.model.Cinema;
import com.example.ticketapp.domain.model.Res.BookingData;
import com.example.ticketapp.domain.model.Room;
import com.example.ticketapp.domain.model.Seat;
import com.example.ticketapp.domain.model.Showtimes; // Đảm bảo bạn đã import Showtimes
import com.example.ticketapp.utils.Resource;
import com.example.ticketapp.viewmodel.BookingViewModel;
import com.example.ticketapp.viewmodel.CinemaViewModel;
import com.example.ticketapp.viewmodel.MovieViewModel;
import com.example.ticketapp.viewmodel.ProfileViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SelectSeatFragment extends Fragment {
    private FragmentSelectSeatBinding binding;
    private final Calendar myCalendar = Calendar.getInstance();
    private CinemaViewModel cinemaViewModel;
    private MovieViewModel movieViewModel;
    private SeatAdapter seatAdapter;
    private RecyclerView recyclerViewSeats;
    private ProfileViewModel profileViewModel;
    private BookingViewModel bookingViewModel;
    private Showtimes selectedShowtime;
    private int selectedCinemaPosition = AdapterView.INVALID_POSITION;
    private List<Cinema> currentCinemaList = new ArrayList<>();
    private List<Showtimes> currentShowtimeList = new ArrayList<>();
    private String date;
    private String selectedCity;
    private List<String> selectdSeats = new ArrayList<>();
    private BookingData bookingData = new BookingData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSelectSeatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Khởi tạo ViewModel trước
        cinemaViewModel = new ViewModelProvider(requireActivity()).get(CinemaViewModel.class);
        movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        bookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);
        seatAdapter = new SeatAdapter((seat, position) -> {
            // Toggle seat: nếu đã chọn thì bỏ, chưa chọn thì thêm
            String seatId = seat.getSeatId();
            if (selectdSeats.contains(seatId)) {
                selectdSeats.remove(seatId);
            } else {
                selectdSeats.add(seatId);
            }
        });
        recyclerViewSeats = binding.recyclerViewSeats;
        recyclerViewSeats.setAdapter(seatAdapter);
        binding.buttonCheckout.setOnClickListener(view1 -> {
            bookingData.setShowTimeId(selectedShowtime.getUid());
            if (!selectdSeats.isEmpty() && bookingData.getUserId() != null && selectedShowtime != null) {
                bookingData.setSelectedSeats(selectdSeats);
                bookingData.setShowTimeId(selectedShowtime.getUid());
                bookingViewModel.setBookingData(bookingData);

                NavController navController = NavHostFragment.findNavController(SelectSeatFragment.this);
                navController.navigate(R.id.action_selectSeatFragment_to_paymentMethod);
            }
        });
        // Setup các Observers
        setUpViewModelObservers();
        // Setup các Views
        setUpDatePicker();
        setupCitySpinner();
        setUpCinemaChoice();
        setUpShowtimeChoice();
    }


    @Override
    public void onResume() {
        super.onResume();
        selectdSeats.clear();
        if (seatAdapter != null) {
            seatAdapter.setSeats(new ArrayList<>());
            seatAdapter.clearSelectedSeats(); // Clear selected seats trong adapter
        }
        bookingData = new BookingData();
        
        // Lấy userId từ profile và set vào bookingData
        if (profileViewModel.getUserProfile().getValue() != null) {
            String userId = profileViewModel.getUserProfile().getValue().getUid();
            if (userId != null) {
                bookingData.setUserId(userId);
            }
        } else {
            // Nếu user profile null, load lại từ Firestore
            profileViewModel.geUserById().observe(getViewLifecycleOwner(), resource -> {
                if (resource != null && resource.getData() != null) {
                    String userId = resource.getData().getUid();
                    if (userId != null) {
                        bookingData.setUserId(userId);
                    }
                }
            });
        }

        if (binding != null) {
            updateLabel();
        }
    }
    private void setUpViewModelObservers() {
        profileViewModel.getUserProfile().observe(getViewLifecycleOwner(), account -> {
            if (account.getUid() != null) {
                bookingData.setUserId(account.getUid());
            }

        });
        movieViewModel.selectedMovie.observe(getViewLifecycleOwner(), movie -> {
            if (movie != null) {
                cinemaViewModel.setMovieSelected(movie.getId());
            }
        });

        // Tự động cập nhật rạp khi 'setCity' được gọi
        cinemaViewModel.getCinemasByCity().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) {
                updateCinemaSpinner(new ArrayList<>());

                return;
            }
            if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
                if (resource.getData().isEmpty()) {
                    updateCinemaSpinner(new ArrayList<>());
                    updateShowtimeSpinner(new ArrayList<>());

                }
                updateCinemaSpinner(resource.getData());
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                updateCinemaSpinner(new ArrayList<>());
            }
        });
        cinemaViewModel.getShowTimes().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) {
                updateShowtimeSpinner(new ArrayList<>());
                return;
            }
            if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
                if(resource.getData().isEmpty() )
                {
                    seatAdapter.setSeats(new ArrayList<>());
                }
                  updateShowtimeSpinner(resource.getData());


            } else if (resource.getStatus() == Resource.Status.ERROR) {
                updateShowtimeSpinner(new ArrayList<>());
            }
        });
    }

    private void setupCitySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.cities_array,
                R.layout.spinner_item
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                seatAdapter.setSeats(new ArrayList<>());
                selectedCity = adapterView.getItemAtPosition(position).toString();
                if (!selectedCity.isEmpty()) {
                    cinemaViewModel.setCity(selectedCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        binding.spinnerCity.setAdapter(adapter);
    }

    private void setUpDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        binding.textViewDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), dateSetListener,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            Calendar today = Calendar.getInstance();
            datePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());
            datePickerDialog.show();
        });

        updateLabel();
    }
    private void updateLabel() {
        seatAdapter.setSeats(new ArrayList<>());
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String formattedDate = sdf.format(myCalendar.getTime());

        binding.textViewDate.setText(formattedDate);
        date = formattedDate;

        if (cinemaViewModel != null) {
            cinemaViewModel.setDate(date);
        }
    }

    private void updateCinemaSpinner(List<Cinema> cinemaList) {
        currentCinemaList.clear();
        currentCinemaList.addAll(cinemaList);
        List<String> cinemaNames = new ArrayList<>();
        for (Cinema cinema : cinemaList) {
            cinemaNames.add(cinema.getName());
        }

        ArrayAdapter<String> cinemaAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item,
                cinemaNames
        );
        cinemaAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.spinnerCinema.setAdapter(cinemaAdapter);
    }

    private void setUpCinemaChoice() {

        binding.spinnerCinema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position < currentCinemaList.size()) {
                    String selectedCinemaId = currentCinemaList.get(position).getUid();
                    selectedCinemaPosition = position;
                    if (selectedCinemaId != null) {
                        cinemaViewModel.setCinemaID(selectedCinemaId);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpShowtimeChoice() {

        binding.spinnerShowtime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position < currentShowtimeList.size() &&
                        selectedCinemaPosition != AdapterView.INVALID_POSITION && // Kiểm tra vị trí hợp lệ
                        selectedCinemaPosition < currentCinemaList.size()) {

                    // 1. Lấy Suất chiếu và Rạp phim
                    selectedShowtime = currentShowtimeList.get(position);
                    Cinema selectedCinema = currentCinemaList.get(selectedCinemaPosition);

                    // 2. Tìm thông tin phòng chiếu (Room)
                    Room selectedRoom = null;
                    for (Room room : selectedCinema.getRooms()) { // Giả sử Cinema có getRooms()
                        if (room.getRoomName().equals(selectedShowtime.getRoomName())) {
                            selectedRoom = room;
                            break;
                        }
                    }

                    int spanCount = 10; // Mặc định 10 cột nếu không tìm thấy
                    if (selectedRoom != null) {
                        spanCount = selectedRoom.getSeatsPerRow();
                    }

                    GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), spanCount);
                    recyclerViewSeats.setLayoutManager(layoutManager);
                    List<Seat> seats = selectedShowtime.getSeats();
                    seatAdapter.setSeats(seats);
                } else {
                    seatAdapter.setSeats(new ArrayList<>());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void updateShowtimeSpinner(List<Showtimes> showtimeList) {
        currentShowtimeList.clear();
        currentShowtimeList.addAll(showtimeList);

        List<String> formattedShowtimes = new ArrayList<>();
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);

        for (Showtimes showtime : showtimeList) {
            formattedShowtimes.add(timeFormatter.format(showtime.getStartTime()));
        }
        ArrayAdapter<String> showtimeAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item,
                formattedShowtimes
        );

        showtimeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.spinnerShowtime.setAdapter(showtimeAdapter);
    }
}