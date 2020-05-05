package com.ehospital;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MobileNavigation extends AppCompatActivity {

    private Toolbar mCovidBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mCovidBar = findViewById(R.id.covidBar);
//        setSupportActionBar(mCovidBar);
     //   getSupportActionBar().setTitle("Covid 19 Tracker");

     //   ActionBar actionBar = getSupportActionBar();
      //  actionBar.setDisplayHomeAsUpEnabled(true);
//        navView.setItemIconTintList(null);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

//        NavigationUI.setupWithNavController(navView, navController);
    }

}
