package com.ehospital;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    ImageView backButton;

    private EditText mEmailEdit, mPassEdit;
    private Button loginBt;
    private FirebaseAuth mAuth;

    private DatabaseReference mRef, mPatientRef, mDoctRef;
    private String patientUserId, doctUserId;

    private ProgressDialog mProgDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mProgDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mEmailEdit = findViewById(R.id.emlEditText);
        mPassEdit = findViewById(R.id.passTextEdit);
        loginBt = findViewById(R.id.loginBtn);


        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // loginBt.setBackgroundColor(Color.blue(20));
                //loginProcess();

                loginProcess();


            }
        });


    }

    public void loginProcess() {
        mProgDialog.setTitle("Registration Process ");
        mProgDialog.setMessage("Please wait for a moment");
        mProgDialog.setCanceledOnTouchOutside(false);
        mProgDialog.show();

        String eml = mEmailEdit.getText().toString();
        String pass = mPassEdit.getText().toString();

        if (eml.isEmpty()) {
            mProgDialog.hide();
            mEmailEdit.setError("Please enter email");
        } else if (pass.isEmpty()) {
            mProgDialog.hide();
            mPassEdit.setError("Please enter password");
        } else {
            FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            final String userId = mCurrentUser.getUid();

            mPatientRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Patient").child(userId);
            mDoctRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor").child(userId);

            patientUserId = mPatientRef.getKey();
            doctUserId = mDoctRef.getKey();


            if(patientUserId.equals(userId)){

            mAuth.signInWithEmailAndPassword(eml, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {


                            mProgDialog.dismiss();
                            Intent loadIntent = new Intent(LoginActivity.this, LoadingActivity.class);
                            startActivity(loadIntent);

                        }
                    else {
                        mProgDialog.hide();
                        Toast.makeText(LoginActivity.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
                    }





                }
            });
        }
            else {
                mAuth.signInWithEmailAndPassword(eml, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            mProgDialog.dismiss();
                            Intent loadIntent = new Intent(LoginActivity.this, PatientMainActivity.class);
                            startActivity(loadIntent);

                        }
                        else {
                            mProgDialog.hide();
                            Toast.makeText(LoginActivity.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
                        }





                    }
                });
            }
        }
    }






     private void setPath()
     {
         FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
         final String userId = mCurrentUser.getUid();

         mPatientRef = mRef.child("Patient");
         mDoctRef = mRef.child("Doctor");


         String patientUserId = mPatientRef.getKey();
         String doctUserId = mDoctRef.getKey();

         if(patientUserId.equals("Patient"))
         {
             Toast.makeText(this, doctUserId, Toast.LENGTH_SHORT).show();
             mProgDialog.dismiss();
             Intent loadIntent = new Intent(LoginActivity.this, PatientMainActivity.class);
             startActivity(loadIntent);

         }
         if (doctUserId.equals("Doctor"))
         {
             Toast.makeText(this, doctUserId, Toast.LENGTH_SHORT).show();
            // Intent docIntent = new Intent(LoginActivity.this, LoadingActivity.class);
             //startActivity(docIntent);
         }

        // Toast.makeText(this, us, Toast.LENGTH_SHORT).show();
//         mPatientRef.addListenerForSingleValueEvent(new ValueEventListener() {
//             @Override
//             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                 String us = dataSnapshot.getValue().toString();
//
//                 if(us.equals("Patient")) {
//
//
//                     mProgDialog.dismiss();
//                     Intent loadIntent = new Intent(LoginActivity.this, PatientMainActivity.class);
//                     startActivity(loadIntent);
//                 }
//
//
//
//             }
//
//             @Override
//             public void onCancelled(@NonNull DatabaseError databaseError) {
//
//             }
//         });







     }


}



