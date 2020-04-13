package com.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DoctorFormActivity extends AppCompatActivity {

    private EditText mdoctorSpecification, mDoctUniqueNumber, mYearExp;


    private Button mDoctCompleteBtn;

    private DatabaseReference mRef,mUserRef;
    private FirebaseAuth mAuth;

    private ProgressDialog mDoctorProgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_form);

        mdoctorSpecification = findViewById(R.id.specialistFieldEdit);
        mDoctUniqueNumber = findViewById(R.id.regNumberEdit);
        mYearExp = findViewById(R.id.yearOfExpEdit);

        mDoctCompleteBtn = findViewById(R.id.docCompleteBtn);

        mRef = FirebaseDatabase.getInstance().getReference().child("DoctorForm");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        mDoctorProgDialog = new ProgressDialog(this);

        mDoctCompleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formProcess();
            }
        });
    }

    private void formProcess() {

        mDoctorProgDialog.setTitle("Process Form ");
        mDoctorProgDialog.setMessage("Please wait for a moment");
        mDoctorProgDialog.setCanceledOnTouchOutside(false);
        mDoctorProgDialog.show();


        String doctorSpecification = mdoctorSpecification.getText().toString();
        String doctUniqueNumber = mDoctUniqueNumber.getText().toString();
        String yearExp = mYearExp.getText().toString();

        if(doctorSpecification.isEmpty())
        {
            mDoctorProgDialog.hide();
            mdoctorSpecification.setError("Please enter this field");
        }

        else if(doctUniqueNumber.isEmpty())
        {
            mDoctorProgDialog.hide();
            mDoctUniqueNumber.setError("Please enter this field");
        }

        else if(yearExp.isEmpty())
        {
            mDoctorProgDialog.hide();
            mYearExp.setError("Please enter this field");
        }

        else{

            // get the id of current Doctor;

            FirebaseUser mCurrentUser = mAuth.getCurrentUser();

            final String doctor_id = mCurrentUser.getUid();

            // used the values in hashMap

            HashMap<String,String> doctUser = new HashMap<>();

            doctUser.put("uniquenumber",doctUniqueNumber);
            doctUser.put("specialistfield",doctorSpecification);
            doctUser.put("yearExperience",yearExp);


            mRef.child(doctor_id).setValue(doctUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful())
                    {

                        mUserRef.child(doctor_id).child("fillForm").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()) {
                                    mDoctorProgDialog.dismiss();
                                    Intent doctMainIntent = new Intent(DoctorFormActivity.this, DoctorMainActivity.class);
                                    startActivity(doctMainIntent);
                                    finish();
                                }
                            }
                        });


                    }
                    else
                    {
                        mDoctorProgDialog.hide();
                        Toast.makeText(DoctorFormActivity.this, "There was some errors please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }


    }


}


