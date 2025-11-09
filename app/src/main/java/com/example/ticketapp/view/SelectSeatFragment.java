package com.example.ticketapp.view;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.example.ticketapp.R;
import com.example.ticketapp.databinding.FragmentSelectSeatBinding;
import com.example.ticketapp.domain.model.Cinema;
import com.example.ticketapp.domain.model.Showtimes; // Đảm bảo bạn đã import Showtimes
import com.example.ticketapp.utils.Resource;
import com.example.ticketapp.viewmodel.CinemaViewModel;
import com.example.ticketapp.viewmodel.MovieViewModel;
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

    // Sửa 1: Dùng List<Cinema> thay vì Map để lấy ID dễ hơn
    private List<Cinema> currentCinemaList = new ArrayList<>();
    private String date;
    private String selectedCity;

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

        // Setup các Observers
        setUpViewModelObservers();

        // Setup các Views
        setUpDatePicker();
        setupCitySpinner();
        setUpCinemaChoice();
    }

    private void setUpViewModelObservers() {
        // Lấy phim đã chọn (từ màn hình trước)
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
                updateCinemaSpinner(resource.getData());
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                updateCinemaSpinner(new ArrayList<>());
            }
        });

        // Tự động cập nhật suất chiếu khi 'setMovieSelected', 'setCinemaID', hoặc 'setDate' được gọi
        cinemaViewModel.getShowTimes().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) {
                updateShowtimeSpinner(new ArrayList<>());
                return;
            }
            if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
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
                selectedCity = adapterView.getItemAtPosition(position).toString();
                if (!selectedCity.isEmpty()) {
                    // Kích hoạt 'getCinemasByCity'
                    cinemaViewModel.setCity(selectedCity);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        binding.spinnerCity.setAdapter(adapter);
    }

    private void setUpDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(); // Cập nhật label VÀ kích hoạt ViewModel
        };
        binding.textViewDate.setOnClickListener(v -> {
            new DatePickerDialog(requireContext(), dateSetListener,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        updateLabel(); // Gọi lần đầu để set ngày hiện tại
    }

    private void updateLabel() {
        // Sửa 2: Lỗi định dạng 'DD'
        // Dùng "dd" (ngày trong tháng) thay vì "DD" (ngày trong năm)
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String formattedDate = sdf.format(myCalendar.getTime());

        binding.textViewDate.setText(formattedDate);
        date = formattedDate;

        // Sửa 3: Kích hoạt ViewModel khi ngày thay đổi
        // Phải kiểm tra null vì 'updateLabel' được gọi trước 'setUpViewModel'
        if (cinemaViewModel != null) {
            cinemaViewModel.setDate(date);
        }
    }

    private void updateCinemaSpinner(List<Cinema> cinemaList) {
        // Sửa 4a: Cập nhật List<Cinema>
        currentCinemaList.clear();
        currentCinemaList.addAll(cinemaList);

        // Chuyển đổi thành tên
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
                // Sửa 4b: Lấy ID từ List (an toàn và dễ hơn)
                if (position < currentCinemaList.size()) {
                    String selectedCinemaId = currentCinemaList.get(position).getUid();

                    if(selectedCinemaId != null) {
                        // Kích hoạt 'getShowTimes'
                        cinemaViewModel.setCinemaID(selectedCinemaId);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void updateShowtimeSpinner(List<Showtimes> showtimeList) {
        // Sửa 5: Định dạng lại giờ và tạo Adapter bên ngoài vòng lặp

        List<String> formattedShowtimes = new ArrayList<>();
        // Dùng SimpleDateFormat để format giờ: "19:30"
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);

        for (Showtimes showtime : showtimeList) {
            // Chuyển Date object thành chuỗi "HH:mm"
            formattedShowtimes.add(timeFormatter.format(showtime.getStartTime()));
        }

        // Tạo Adapter BÊN NGOÀI vòng lặp
        ArrayAdapter<String> showtimeAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item,
                formattedShowtimes // Dùng List<String> đã được định dạng
        );

        showtimeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.spinnerShowtime.setAdapter(showtimeAdapter);
    }
}