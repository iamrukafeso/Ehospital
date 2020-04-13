//spla
package com.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SlashScreenActivity extends AppCompatActivity {

    //1500 Equals 1.5 seconds.
    private static int SPLASH_SCREEN = 1500;

    //Variables
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo, slogan;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef, mUserDatabase;
    private ProgressDialog mProgDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_slash_screen);


        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        mUserDatabase = mRef.child("Users");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(SlashScreenActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
                sendToMainActivities();
            }
        },SPLASH_SCREEN);

    }

    private void sendToMainActivities() {
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();


         if(mCurrentUser == null) {
            Intent intent = new Intent(SlashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (mCurrentUser != null) {
            String userId = mCurrentUser.getUid();

            mUserDatabase.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String accType = dataSnapshot.child("accounttype").getValue().toString();
                        String fillForm = dataSnapshot.child("fillForm").getValue().toString();

                        if (accType.equals("Patient") && fillForm.equals("true")) {
                            Intent patientIntent = new Intent(SlashScreenActivity.this, PatientMainActivity.class);
                            startActivity(patientIntent);
                            finish();
                        } else if (accType.equals("Doctor") && fillForm.equals("true")) {
                            Intent doctPatient = new Intent(SlashScreenActivity.this, DoctorMainActivity.class);
                            startActivity(doctPatient);
                            finish();

                        }
                        else{
                            Intent loginIntent = new Intent(SlashScreenActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                            finish();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }
}
