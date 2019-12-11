package com.ehospital;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    ImageView backButton;

    EditText emEdit;
    EditText passEdit;
    Button loginBt;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emEdit = findViewById(R.id.emlEditText);
        passEdit = findViewById(R.id.passTextEdit);
        loginBt = findViewById(R.id.loginBtn);
        database = FirebaseDatabase.getInstance();
       // myRef = database.getReference("members");




        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // loginBt.setBackgroundColor(Color.blue(20));
                //loginProcess();
                myRef = database.getReference().child("members").child("-Lvpo27yTdgbxQDZ0ACd");
               loginProcess();


            }
        });




        backButton = findViewById(R.id.backBtn);

//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                finish();
//
//            }
//        });


        //add account listener



    }


    public void loginProcess()
    {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String em = emEdit.getText().toString();
                String pass = passEdit.getText().toString();
                String emailDb = dataSnapshot.child("email").getValue().toString();
                String passDb = dataSnapshot.child("pwd").getValue().toString();

                if(em.equals(emailDb) && pass.equals(passDb)) {
                    Toast.makeText(LoginActivity.this, "Login successfull ", Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(LoginActivity.this, "Incorrect details", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




}



