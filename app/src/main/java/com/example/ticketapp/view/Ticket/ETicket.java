package com.example.ticketapp.view.Ticket;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ticketapp.R;
import com.example.ticketapp.databinding.FragmentETicketBinding;
import com.example.ticketapp.domain.model.Ticket;
import com.example.ticketapp.viewmodel.BookingViewModel;

import java.util.Locale;


public class ETicket extends Fragment {
    private FragmentETicketBinding binding;
    private BookingViewModel bookingViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentETicketBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpViewModel();
    }

    private void setUpViewModel() {
        bookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);
        bookingViewModel._currentTicketLiveData.observe(getViewLifecycleOwner(), ticket -> {
            if (ticket != null) {
                initView(ticket);
            }
        });
    }

    private void initView(Ticket ticket) {
        binding.detailDate.labelDetail.setText(getString(R.string.txt_time));
        binding.textFilmTitle.setText(ticket.getMovieName());
        binding.detailLocation.labelDetail.setText(getString(R.string.txt_cinema));
        binding.detailLocation.textDetailValue.setText(ticket.getCinemaName());
        binding.detailOrder.labelDetail.setText(getString(R.string.txt_ticket_id));
        binding.detailOrder.textDetailValue.setText(ticket.getId());
        binding.detailDate.textDetailValue.setText(ticket.getTime());
        binding.detailPayment.labelDetail.setText(getString(R.string.txt_status));
        binding.detailPayment.textDetailValue.setText(ticket.getStatus());
        binding.detailPrice.labelDetail.setText(getString(R.string.txt_price));
        String formattedPrice = String.format(Locale.getDefault(), "%,d đ",
                (int) ticket.getTotalPrice());
        binding.detailPrice.textDetailValue.setText(formattedPrice);
        binding.detailSeats.labelDetail.setText(getString(R.string.txt_seats));
        binding.detailSeats.textDetailValue.setText(ticket.getSeatNames().toString());
    }
}