package com.example.ticketapp.view;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.ticketapp.R;
import com.example.ticketapp.databinding.FragmentSelectSeatBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SelectSeatFragment extends Fragment {
    private FragmentSelectSeatBinding binding;
    private final Calendar myCalendar = Calendar.getInstance();

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
    }

    private void setupCitySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),

                R.array.cities_array, // Nguồn dữ liệu
                R.layout.spinner_item// Layout mặc định cho mỗi item
        );

        // Chỉ định layout sẽ sử dụng khi danh sách lựa chọn xuất hiện
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

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
    }
}