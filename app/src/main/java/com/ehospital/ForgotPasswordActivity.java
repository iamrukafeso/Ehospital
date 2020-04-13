package com.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailField;
    private Button sendEmailBtn;
    private TextView mInfoText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.fieldTextEditEml);

        sendEmailBtn = findViewById(R.id.emailSendBtn);

        sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eml = emailField.getText().toString();

                if(eml.isEmpty())
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter email address", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    mAuth.sendPasswordResetEmail(eml).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(ForgotPasswordActivity.this, "A link has been sent to you for resetting your password", Toast.LENGTH_SHORT).show();
                                Intent logIntent = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                                startActivity(logIntent);
                            }
                            else{
                                String message = task.getException().getMessage();

                                Toast.makeText(ForgotPasswordActivity.this,"Error occurred " + message + "\nPlease try again", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }
}
