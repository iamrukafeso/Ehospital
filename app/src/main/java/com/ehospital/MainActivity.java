package com.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;


import android.view.Menu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    Button loginButton;
    TextView regBtn;

   private FirebaseAuth mAuth;
   private DatabaseReference mRef,mUserDatabase;
   private ProgressDialog mProgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loginButton = findViewById(R.id.loginBtn);
        regBtn = findViewById(R.id.regTextView);



        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        mUserDatabase = mRef.child("Users");

        //Set a listener that reacts when the login button is been clicked.
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        sendToMainActivities();

//        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
//
//        if (mCurrentUser != null) {
//            String userId = mCurrentUser.getUid();
//
//            mRef.child(userId).child("accounttype").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    String accType = dataSnapshot.getValue().toString();
//
//                    if (accType.equals("Patient")) {
//                        Intent patientIntent = new Intent(MainActivity.this, PatientMainActivity.class);
//                        startActivity(patientIntent);
//                        finish();
//                    } else {
//                        Intent doctPatient = new Intent(MainActivity.this, DoctorMainActivity.class);
//                        startActivity(doctPatient);
//                        finish();
//
//                    }
//
//                }
//
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
//
//        }
//
//    else {
//
//        }

    }

    private void sendToMainActivities()
    {
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();

        if (mCurrentUser != null) {
            String userId = mCurrentUser.getUid();

            mUserDatabase.child(userId).child("accounttype").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String accType = dataSnapshot.getValue().toString();

                    if (accType.equals("Patient")) {
                        Intent patientIntent = new Intent(MainActivity.this, PatientMainActivity.class);
                        startActivity(patientIntent);
                        finish();
                    } else if(accType.equals("Doctor")) {
                        Intent doctPatient = new Intent(MainActivity.this, DoctorMainActivity.class);
                        startActivity(doctPatient);
                        finish();

                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    public void onClick(View view)
    {
        Intent intent = new Intent(MainActivity.this, Registratrion.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Toast.makeText(this, "You clicked SETTINGS", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.help) {
            Toast.makeText(this, "You clicked HELP", Toast.LENGTH_SHORT).show();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }


}
