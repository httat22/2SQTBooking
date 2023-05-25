package com.example.abc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.abc.fragmentManager.AccountFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.abc.databinding.ActivityManagerBinding;

public class ManagerActivity extends AppCompatActivity {

    private ActivityManagerBinding binding;
    private String email = "";
    private long backPressedTime;
    private Toast mToast;

    public String getEmail() {
        return email;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        binding = ActivityManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view_manager);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home_manager, R.id.navigation_statistics, R.id.navigation_notifications,
                R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_manager);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navViewManager, navController);

        Intent intent = getIntent();
        if (intent != null) {
            email = intent.getStringExtra("email");
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            AccountFragment fragment = new AccountFragment();
            fragment.setArguments(bundle);
        }
    }
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            mToast.cancel();
            super.onBackPressed();
            return;
        } else {
            mToast = Toast.makeText(ManagerActivity.this, "Press back again to exit the application", Toast.LENGTH_SHORT);
            mToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}