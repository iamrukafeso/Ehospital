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
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    ImageView backButton;

    EditText emEdit;
    EditText passEdit;
    Button loginBt;
    String id;
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
        myRef = database.getReference("members");

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // loginBt.setBackgroundColor(Color.blue(20));
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


    String pass;
    public void loginProcess() {
        //get the input and store in string
        String email = emEdit.getText().toString();
        pass = passEdit.getText().toString();


        //check if the fields are empty
        if(email.isEmpty())
        {
            //display this message
            emEdit.setError("Please enter email");
        }
        else if(pass.isEmpty())
        {
            passEdit.setError("Please enter password");
        }
        else{
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                    Members member = dataSnapshot.getValue(Members.class);
////
////                    if(pass.equals(member.getPwd()))
////                    {
////                        Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
////
////                    }
////                    else
////                    {
////                        Toast.makeText(LoginActivity.this,"Details invalid",Toast.LENGTH_LONG).show();
////
////                    }
//                    if(dataSnapshot.child(id).exists())
//                    {
//                            if (!id.isEmpty()) {
//                                Members member = dataSnapshot.child(id).getValue(Members.class);
//
//                                if(member.getPwd().equals(pass))
//                                {
//                                     Toast.makeText(LoginActivity.this,"Login Success",Toast.LENGTH_LONG).show();
//
//                                }
//
//                            }
//
//
//
//
//                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
          //  Toast.makeText(this,"Your registration was successful",Toast.LENGTH_LONG).show();

        }

    }
}



