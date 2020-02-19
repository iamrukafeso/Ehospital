package com.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

    private EditText mdoctorSpecification, muniqueID, myearOfExp;

    private String doctorSpecification, uniqueID, yearOfExp;

    private Button mCompleteBtn2;

//    private DatabaseReference mdoctorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_form);

     //
    }

    private void formProcess() {

        doctorSpecification = mdoctorSpecification.getText().toString();

        //get userId and store as string
       // FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
       // String DocId = mCurrentUser.getDocid();

        //Used hashmap for string key and value pairs


    }


}


