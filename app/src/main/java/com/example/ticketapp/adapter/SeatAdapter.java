package com.example.ticketapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ticketapp.R; // Thay bằng package của bạn
import com.example.ticketapp.domain.model.Seat; // Thay bằng package của bạn
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    private List<Seat> seatList = new ArrayList<>();
    private Set<String> selectedSeatIds = new HashSet<>();
    private OnSeatClickListener seatClickListener;

    // Interface để gửi sự kiện click về Fragment
    public interface OnSeatClickListener {
        void onSeatClick(Seat seat, int position);
    }

    public SeatAdapter(OnSeatClickListener listener) {
        this.seatClickListener = listener;
    }
    
    // Method để clear selected seats
    public void clearSelectedSeats() {
        selectedSeatIds.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seat = seatList.get(position);
        holder.bind(seat, position);
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    // Hàm để cập nhật danh sách ghế
    public void setSeats(List<Seat> newSeats) {
        this.seatList.clear();
        this.seatList.addAll(newSeats);
        notifyDataSetChanged();
    }

    // Lớp ViewHolder
    class SeatViewHolder extends RecyclerView.ViewHolder {
        CheckBox seatCheckBox;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            seatCheckBox = itemView.findViewById(R.id.check_box_seat);
        }

        public void bind(Seat seat, int position) {
            // Hiển thị tên ghế (A1, A2...)
            seatCheckBox.setText(seat.getSeatId());

            // Tự động đổi màu dựa trên trạng thái
            if ("available".equals(seat.getStatus().toString())) {
                seatCheckBox.setEnabled(true); // Cho phép bấm
                // Set checked state dựa trên selectedSeatIds
                seatCheckBox.setChecked(selectedSeatIds.contains(seat.getSeatId()));
            } else {
                seatCheckBox.setEnabled(false); // Vô hiệu hóa (màu đỏ)
                seatCheckBox.setChecked(false);
            }

            // Xử lý sự kiện click
            seatCheckBox.setOnClickListener(v -> {
                if (seatClickListener != null) {
                    // Toggle trong set
                    if (selectedSeatIds.contains(seat.getSeatId())) {
                        selectedSeatIds.remove(seat.getSeatId());
                    } else {
                        selectedSeatIds.add(seat.getSeatId());
                    }
                    // Notify Fragment
                    seatClickListener.onSeatClick(seat, position);
                }
            });
        }
    }
}