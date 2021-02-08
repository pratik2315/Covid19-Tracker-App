package com.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.covid19tracker.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    private Fragment homeFragment, statsFragment;
    private Fragment activeFragment;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());//inflates the view?
        setContentView(binding.getRoot());//getRoot method being important in viewBinding.

        initFragments();

        binding.refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragment.onResume();
                statsFragment.onResume();
            }
        });

        binding.nav.setOnNavigationItemSelectedListener(this);

    }

    private void initFragments(){
        homeFragment = new HomeFragment();
        statsFragment = new StatsFragment();

        fragmentManager = getSupportFragmentManager();
        activeFragment = homeFragment;

        fragmentManager.beginTransaction()
                .add(R.id.frame, homeFragment, "homeFragment")
                .commit();
        fragmentManager.beginTransaction()
                .add(R.id.frame, statsFragment, "statsFragment")
                .hide(statsFragment)
                .commit();
    }

    private void loadHomeData(){
        binding.title.setText("Home");
        fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
        activeFragment = homeFragment;
    }

    private void loadStatsData(){
        binding.title.setText("Stats");
        fragmentManager.beginTransaction().hide(activeFragment).show(statsFragment).commit();
        activeFragment = statsFragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                loadHomeData();
                return true;

            case R.id.nav_stats:
                loadStatsData();
                return true;
        }
        return false;
    }
}