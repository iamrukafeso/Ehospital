package com.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PatientFormActivity extends AppCompatActivity {

    private EditText mCardHolderName,mCardNumber,mExpireDate,mCVVNumber;
    private Spinner mCardSpinnner;
    private Button mCompleteBtn;

    private String cardHolderName,cardNumber,expireDate,cvvNumber;

    private DatabaseReference mRef,mMedCardRef,mBankCardRef;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_form);




        mRef = FirebaseDatabase.getInstance().getReference().child("Card");
        mAuth = FirebaseAuth.getInstance();

        mCardHolderName = findViewById(R.id.cardHoldername);
        mCardNumber = findViewById(R.id.cardNumber);
        mExpireDate = findViewById(R.id.expireDate);
        mCVVNumber = findViewById(R.id.ccvNumber);

        mCardSpinnner = findViewById(R.id.cardTypeSpinner);

        mCompleteBtn = findViewById(R.id.completeRegBtn);

       // String cardSpinner = mCardSpinnner.getSelectedItem().toString();


        mCardSpinnner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1)
                {
                    mCardHolderName.setVisibility(View.VISIBLE);
                    mCardNumber.setVisibility(View.VISIBLE);
                    mExpireDate.setVisibility(View.VISIBLE);
                    mCompleteBtn.setVisibility(View.VISIBLE);
                   // setContentView(R.layout.medical_card_form);
                }
                else if(position == 2)
                {
                    mCardHolderName.setVisibility(View.VISIBLE);
                    mCardNumber.setVisibility(View.VISIBLE);
                    mExpireDate.setVisibility(View.VISIBLE);
                    mCompleteBtn.setVisibility(View.VISIBLE);
                    mCVVNumber.setVisibility(View.VISIBLE);

//
                }
                else {
                    mCardHolderName.setVisibility(View.INVISIBLE);
                    mCardNumber.setVisibility(View.INVISIBLE);
                    mExpireDate.setVisibility(View.INVISIBLE);
                    mCompleteBtn.setVisibility(View.INVISIBLE);
                    mCVVNumber.setVisibility(View.INVISIBLE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


                mCompleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        formProcess();
                    }
                });




    }

    private void formProcess() {

        // get Current User id

        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        String patient_id = mCurrentUser.getUid();

        cardHolderName = mCardHolderName.getText().toString();
        cardNumber = mCardNumber.getText().toString();
        expireDate = mExpireDate.getText().toString();
        cvvNumber = mCVVNumber.getText().toString();

        String cardSelect = mCardSpinnner.getSelectedItem().toString();


        //Used hashmap for string key and value pairs

        if(cardHolderName.isEmpty())
        {
            mCardHolderName.setError("please enter this field");
        }

       else if(cardNumber.isEmpty())
        {
            mCardNumber.setError("please enter this field");
        }
        else if(cardNumber.length() < 16)
        {
            mCardNumber.setError("Card number must be 16 digits");
        }

       else if(expireDate.isEmpty())
        {
            mExpireDate.setError("please enter this field");
        }

       else {
            if (cardSelect.equals("Medical card")) {
                HashMap<String, String> medicalUser = new HashMap<>();
                medicalUser.put("cardholdername", cardHolderName);
                medicalUser.put("cardnumber", cardNumber);
                medicalUser.put("expiredate", expireDate);

                mMedCardRef = mRef.child("Medicalcard").child(patient_id);

                mMedCardRef.setValue(medicalUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Intent loadIntent = new Intent(PatientFormActivity.this, PatientMainActivity.class);
                            startActivity(loadIntent);
                        } else {
                            Toast.makeText(PatientFormActivity.this, "Please Enter valid details", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            } else if (cardSelect.equals("Bank card")) {

                if(cvvNumber.isEmpty())
                {
                    mCVVNumber.setError("Please enter this field ");
                }

               else if(cvvNumber.length() < 3)
                {
                    mCVVNumber.setError("CVV must be 3 digits");
                }

                else {
                    HashMap<String, String> bankUser = new HashMap<>();
                    bankUser.put("cardholdername", cardHolderName);
                    bankUser.put("cardnumber", cardNumber);
                    bankUser.put("expiredate", expireDate);

                    mBankCardRef = mRef.child("Bankcard").child(patient_id);

                    mBankCardRef.setValue(bankUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Intent loadIntent = new Intent(PatientFormActivity.this, PatientMainActivity.class);
                                startActivity(loadIntent);
                            } else {
                                Toast.makeText(PatientFormActivity.this, "Please Enter valid details", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        }






    }
}
