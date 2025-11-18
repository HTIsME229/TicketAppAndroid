package com.example.ticketapp.view.Ticket;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ticketapp.adapter.TicketAdapter;
import com.example.ticketapp.databinding.FragmentUpcomingTicketsBinding;
import com.example.ticketapp.databinding.FragmentWatchedTicketsBinding;
import com.example.ticketapp.domain.model.Ticket;
import com.example.ticketapp.viewmodel.BookingViewModel;
import java.util.List;
public class WatchedTicketsFragment extends Fragment {
    public interface OnTicketClickListener {
        void onTicketClick(Ticket ticket);
    }
    private BookingViewModel bookingViewModel; // Giả sử ViewModel quản lý việc lấy vé
    private TicketAdapter adapter;
    private FragmentWatchedTicketsBinding binding;
    private  OnTicketClickListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof  OnTicketClickListener) {
            listener = (OnTicketClickListener) getParentFragment();
        } else {
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWatchedTicketsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo ViewModel và Adapter/RecyclerView
        bookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

        RecyclerView recyclerView = binding.recyclerWatchedTickets;
        adapter = new TicketAdapter(false);
        adapter.setOnTicketActionListener(new TicketAdapter.OnTicketActionListener() {
            @Override
            public void onCancelTicket(Ticket ticket) {

            }

            @Override
            public void onRateTicket(Ticket ticket) {

            }

            @Override
            public void onTicketClick(Ticket ticket) {
                listener.onTicketClick(ticket);

            }
        });// Truyền cờ là vé sắp tới (Upcoming = true)
        recyclerView.setAdapter(adapter);
        
        // Setup SwipeRefresh
        setupSwipeRefresh();
        
        // Bắt đầu quan sát dữ liệu
        observeWatchedTickets();
    }

    private void observeWatchedTickets() {
        bookingViewModel.getWatchedTickets().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    binding.swipeRefreshLayout.setRefreshing(true);
                    binding.textEmptyList.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    binding.swipeRefreshLayout.setRefreshing(false);
                    List<Ticket> tickets = resource.getData();
                    if (tickets != null && !tickets.isEmpty()) {
                        adapter.submitList(tickets);
                        binding.textEmptyList.setVisibility(View.GONE);
                    } else {
                        binding.textEmptyList.setVisibility(View.VISIBLE);
                    }
                    break;
                case ERROR:
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.textEmptyList.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }
    
    private void setupSwipeRefresh() {
        binding.swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        );
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            bookingViewModel.refreshWatchedTickets();
        });
    }

}