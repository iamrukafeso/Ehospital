package com.ehospital;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

    private TextView forgotPasswordTextView;
    private DatabaseReference mRef, mUserRef, mDoctRef;
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

        forgotPasswordTextView = findViewById(R.id.forgetPasswordText);

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent passwordIntent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);

                startActivity(passwordIntent);
            }
        });

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // loginBt.setBackgroundColor(Color.blue(20));
                //loginProcess();

//                FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
//                final String userId = mCurrentUser.getUid();
//
//                mPatientRef = mRef.child("Patient").child(userId);
//                mDoctRef = mRef.child("Doctor");
//
//
//                String patientUserId = mPatientRef.getKey();
//                String doctUserId = mDoctRef.getKey();
//
//                if(userId.equals(patientUserId)) {

                loginProcess();
            }


        });


    }

    public void loginProcess() {
        mProgDialog.setTitle("Login Process ");
        mProgDialog.setMessage("Please wait for a moment");
        mProgDialog.setCanceledOnTouchOutside(false);
        mProgDialog.show();

        final String eml = mEmailEdit.getText().toString();
        final String pass = mPassEdit.getText().toString();


        if (eml.isEmpty()) {
            mProgDialog.hide();
            mEmailEdit.setError("Please enter email");
        }

        else if (pass.isEmpty()) {
            mProgDialog.hide();
            mPassEdit.setError("Please enter password");
        }
        else {


            mAuth.signInWithEmailAndPassword(eml,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()) {
                        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String userId = mCurrentUser.getUid();

                        mUserRef = mRef.child(userId);

                        mUserRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String accountType = dataSnapshot.child("accounttype").getValue().toString();
                                String fillForm = dataSnapshot.child("fillForm").getValue().toString();

                                if (accountType.equals("Patient"))
                                {
                                    if(fillForm.equals("false")) {
                                        if(mAuth.getCurrentUser().isEmailVerified())
                                        {
                                            mProgDialog.dismiss();
                                            Intent patientFormIntent = new Intent(LoginActivity.this, PatientFormActivity.class);
                                            startActivity(patientFormIntent);
                                            finish();
                                        }
                                        else{
                                            mProgDialog.hide();
                                            Toast.makeText(LoginActivity.this, R.string.unVerifiedEmail, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    else
                                    {
                                        mProgDialog.dismiss();
                                        Intent patientIntent = new Intent(LoginActivity.this, PatientMainActivity.class);
                                        startActivity(patientIntent);
                                        finish();

                                    }
                                }
                                else if(accountType.equals("Doctor")) {
                                    if(fillForm.equals("false")) {
                                        if(mAuth.getCurrentUser().isEmailVerified()) {
                                            mProgDialog.dismiss();
                                            Intent doctFormIntent = new Intent(LoginActivity.this, DoctorFormActivity.class);
                                            startActivity(doctFormIntent);
                                            finish();
                                        }
                                        else{
                                            mProgDialog.hide();
                                            Toast.makeText(LoginActivity.this, R.string.unVerifiedEmail, Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                    else{
                                        Intent doctMainIntent = new Intent(LoginActivity.this, DoctorMainActivity.class);
                                        startActivity(doctMainIntent);
                                        finish();
                                    }
                                }
                                else {
                                    mProgDialog.dismiss();
                                    Intent mainIntent = new Intent(LoginActivity.this, LoginActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else {
                        mProgDialog.dismiss();
                        Toast.makeText(LoginActivity.this, R.string.inValidDetails, Toast.LENGTH_SHORT).show();
                    }



                }
            });
        }


   }

    public void onClick(View view)
    {
        Intent intent = new Intent(LoginActivity.this, Registratrion.class);
        startActivity(intent);

    }
}










