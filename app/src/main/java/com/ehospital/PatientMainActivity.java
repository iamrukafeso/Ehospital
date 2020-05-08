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
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar mNavBar;
    private DrawerLayout mDrawer;
    private FirebaseAuth mAuth;
    private CircleImageView mProfileImage;
    private DatabaseReference mUserRef,mUserDatabase;
    private String mCurrentUserId;
    private ViewPager mPager;
    private TabLayout mTabLayout;
    private SectionAdopter mSectionPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        mAuth = FirebaseAuth.getInstance();
        mNavBar = findViewById(R.id.toolBar);
        setSupportActionBar(mNavBar);
        getSupportActionBar().setTitle(R.string.patientPage);

        mDrawer = findViewById(R.id.doctor_layout);

        mTabLayout = findViewById(R.id.mainTabLayout);
        mPager = findViewById(R.id.mainTabPager);

        mSectionPager = new SectionAdopter(getSupportFragmentManager());

        mPager.setAdapter(mSectionPager);

        mTabLayout.setupWithViewPager(mPager);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUserId);
        // to tie DrawerLayout

        ActionBarDrawerToggle mBarToggle = new ActionBarDrawerToggle(this, mDrawer, mNavBar,
                R.string.nav_draw_open, R.string.nav_draw_close);

        mDrawer.addDrawerListener(mBarToggle);
        mBarToggle.syncState();

        NavigationView mNavView = findViewById(R.id.nav_view);
        mProfileImage = mNavView.getHeaderView(0).findViewById(R.id.user_proifle_image);
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
            case R.id.covid19_nav:

//
                Intent covidIntent = new Intent(getApplicationContext(), MobileNavigation.class);
                startActivity(covidIntent);
               
                break;

            case R.id.profile_nav:

//
                            Intent profileIntent = new Intent(getApplicationContext(), PatientProfileActivity.class);
                            startActivity(profileIntent);

                            break;
            case R.id.logout_nav:
                mAuth.signOut();
                Intent homeIntent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(homeIntent);
                finish();


        }

        //close the navigation
        mDrawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        mUserDatabase.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("image").getValue().toString();



                Picasso.with(getApplicationContext()).load(image).placeholder(R.drawable.ic_profile).into(mProfileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}