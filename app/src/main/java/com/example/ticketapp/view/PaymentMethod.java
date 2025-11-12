package com.example.ticketapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ticketapp.R;
import com.example.ticketapp.adapter.CardAdapter;
import com.example.ticketapp.databinding.FragmentPaymentMethodBinding;
import com.example.ticketapp.domain.model.PaymentCard;
import com.example.ticketapp.view.dialog.PaymentSuccessDialog;

import java.util.List;


public class PaymentMethod extends Fragment {

    private FragmentPaymentMethodBinding binding;
    private RecyclerView recyclerViewPaymentMethods;
    private CardAdapter cardAdapter ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPaymentMethodBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewPaymentMethods = binding.recyclerViewCards;
        List<PaymentCard> cardList = List.of(
                new PaymentCard("1234 5678 9012 3456", "John Doe", "12/24","123"),
                new PaymentCard("9876 5432 1098 7654", "Jane Smith", "11/23","456"),
                new PaymentCard("4567 8901 2345 6789", "Alice Johnson", "10/25","789")
        );
        cardAdapter = new CardAdapter(cardList);
        recyclerViewPaymentMethods.setAdapter(cardAdapter);
        binding.layoutPayButton.setOnClickListener(v -> {
            showSuccessDialog();
        });
    }
    private void showSuccessDialog() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(PaymentMethodDirections.actionPaymentMethodToPaymentSuccessDialog2());

    }
}