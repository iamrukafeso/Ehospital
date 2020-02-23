package com.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;


public class DoctorMainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar mNavBar;
    private DrawerLayout mDrawer;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        mAuth = FirebaseAuth.getInstance();
        mNavBar = findViewById(R.id.toolBar);
        setSupportActionBar(mNavBar);
        getSupportActionBar().setTitle("Doctor page");





        mDrawer = findViewById(R.id.doctor_layout);

        // to tie DrawerLayout

        ActionBarDrawerToggle mBarToggle = new ActionBarDrawerToggle(this, mDrawer, mNavBar,
                R.string.nav_draw_open, R.string.nav_draw_close);

        mDrawer.addDrawerListener(mBarToggle);
        mBarToggle.syncState();

        NavigationView mNavView = findViewById(R.id.nav_view);

        mNavView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId())
        {
            case R.id.profile_nav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new ProfileFragment()).commit();
                break;

            case R.id.logout_nav:
                mAuth.signOut();
                Intent homeIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(homeIntent);


        }

        //close the navigation
        mDrawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
