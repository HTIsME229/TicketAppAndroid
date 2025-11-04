package com.example.ticketapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;


import com.example.ticketapp.databinding.ActivitySplashBinding;
import com.example.ticketapp.view.LoginFragment;
import com.example.ticketapp.viewmodel.CinemaViewModel;
import com.example.ticketapp.viewmodel.ProfileViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Splash extends AppCompatActivity {
        private ProfileViewModel profileViewModel;
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        decideRedirect();

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

    }



    private void setUpViewModel() {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        profileViewModel.observerAuthState().observe(this, isAuthenticated -> {;
            if (isAuthenticated) {
                // Nếu đã đăng nhập, chuyển đến MainActivity
                startActivity(new Intent(this, MainActivity.class));
            } else {
                // Nếu chưa đăng nhập, chuyển đến AuthenticationActivity
                startActivity(new Intent(this, AuthenticationActivity.class));
            }
            finish(); // Kết thúc Splash để không chạy tiếp
        });

    }

    private void decideRedirect() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isFirstTime = prefs.getBoolean("isFirstTime", true);

        if (isFirstTime) {
            // Chỉ chạy OnBoarding lần đầu
            startActivity(new Intent(this, OnBoarding.class));
            finish(); // Kết thúc Splash để không chạy tiếp
        } else {
            // Nếu không phải lần đầu thì mới check login
            setUpViewModel();
        }
    }
}