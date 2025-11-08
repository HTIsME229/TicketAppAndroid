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
import com.example.ticketapp.domain.model.Showtimes;
import com.example.ticketapp.utils.Resource;
import com.example.ticketapp.viewmodel.CinemaViewModel;
import com.example.ticketapp.viewmodel.ProfileViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SelectSeatFragment extends Fragment {
    private FragmentSelectSeatBinding binding;
    private final Calendar myCalendar = Calendar.getInstance();
    private CinemaViewModel cinemaViewModel;
    private Map<String, String> cinemaMap = new HashMap<>();
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

        setUpDatePicker();
        setupCitySpinner();
        setUpCinemaChoice();
        setUpViewModel();
    }

    private void setUpViewModel() {
        cinemaViewModel = new ViewModelProvider(requireActivity()).get(CinemaViewModel.class);
        cinemaViewModel.getCinemasByCity().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) {
                // Trường hợp cityInput là null (lúc mới khởi tạo)
                updateCinemaSpinner(new ArrayList<>()); // Xóa rỗng spinner rạp
                return;
            }

            if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
                updateCinemaSpinner(resource.getData());
            } else if (resource.getStatus() == Resource.Status.LOADING) {
                // Hiển thị loading...
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                // Xử lý lỗi
                updateCinemaSpinner(new ArrayList<>()); // Xóa rỗng spinner rạp
            }
        });
        cinemaViewModel.getShowTimes().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) {
                // Trường hợp cityInput là null (lúc mới khởi tạo)
                updateShowtimeSpinner(new ArrayList<>()); // Xóa rỗng spinner rạp
                return;
            }

            if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
                updateShowtimeSpinner(resource.getData());
            } else if (resource.getStatus() == Resource.Status.LOADING) {
                // Hiển thị loading...
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                // Xử lý lỗi
                updateShowtimeSpinner(new ArrayList<>()); // Xóa rỗng spinner rạp
            }

        });
    }

    private void setupCitySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),

                R.array.cities_array, // Nguồn dữ liệu
                R.layout.spinner_item// Layout mặc định cho mỗi item
        );

        // Chỉ định layout sẽ sử dụng khi danh sách lựa chọn xuất hiện
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                selectedCity = adapterView.getItemAtPosition(position).toString();
                if (!selectedCity.isEmpty()) {
                    cinemaViewModel.setCity(selectedCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Gán adapter cho Spinner
        binding.spinnerCity.setAdapter(adapter);
    }


    private void setUpDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel();
        };

        // Gán sự kiện click cho TextView
        binding.textViewDate.setOnClickListener(v -> {
            // Bước 2: Sử dụng requireContext() thay vì 'this'
            new DatePickerDialog(requireContext(), dateSetListener,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        updateLabel();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; // Định dạng ngày tháng
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.textViewDate.setText(sdf.format(myCalendar.getTime()));
        date = sdf.format(myCalendar.getTime());
    }

    private void updateCinemaSpinner(List<Cinema> cinemaList) {
        // Chuyển đổi List<Cinema> thành List<String> (tên rạp)
        cinemaMap.clear();
        List<String> cinemaNames = new ArrayList<>();
        for (Cinema cinema : cinemaList) {
            cinemaNames.add(cinema.getName());
            cinemaMap.put(cinema.getUid(), cinema.getName()); // Lưu cả object Cinema
        }

        // Tạo ArrayAdapter MỚI cho RẠP PHIM
        ArrayAdapter<String> cinemaAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item, // Layout cho item đã chọn
                cinemaNames           // Nguồn dữ liệu
        );

        cinemaAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        // Gán adapter mới cho SPINNER RẠP PHIM
        binding.spinnerCinema.setAdapter(cinemaAdapter);
    }

    private void setUpCinemaChoice() {
        binding.spinnerCinema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedCinemaName = adapterView.getItemAtPosition(position).toString();
                String selectedCinemaId = null;
                for (Map.Entry<String, String> entry : cinemaMap.entrySet()) {
                    if (entry.getValue().equals(selectedCinemaName)) {
                        selectedCinemaId = entry.getKey();

                        break;
                    }

                    // Sử dụng selectedCinemaId và date để lấy lịch chiếu
                    cinemaViewModel.setCinemaID(selectedCinemaId);
                    cinemaViewModel.setDate(date);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateShowtimeSpinner(List<Showtimes> showtimeList) {
        List<Date> listStartTime = new ArrayList<>();
        for (Showtimes showtime : showtimeList) {
            listStartTime.add(showtime.getStartTime());
            ArrayAdapter<Date> showtimeAdapter = new ArrayAdapter<>(
                    requireContext(),
                    R.layout.spinner_item, // Layout cho item đã chọn
                    listStartTime           // Nguồn dữ liệu
            );

            showtimeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
            binding.spinnerShowtime.setAdapter(showtimeAdapter);
        }
    }
}