package com.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;


public class Registratrion extends AppCompatActivity {

    //create all fields attributes
    private EditText firstNameEdit;
    private EditText surnameEdit;
    private EditText pwdEdit;
    private EditText email;
    private EditText dobTextEdit;
    private Button signUpBtn;
    private TextView textView;
    private Spinner spinner;
    private Calendar calendar = Calendar.getInstance();
    private int year,month,day;


    private DatabaseReference mRef,mUserRef;
    private FirebaseAuth mAuth;

    private ProgressDialog mProgDialog;

    private String mFirstName,mSurname,mDob,mAccountType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registratrion);

        //get all element id
        firstNameEdit = findViewById(R.id.firstNameTextEdit);
        surnameEdit = findViewById(R.id.surnameTextEdit);
        pwdEdit = findViewById(R.id.passEdit);
        email = findViewById(R.id.emailEdit);
        dobTextEdit = findViewById(R.id.dateOfBirth);
        signUpBtn = findViewById(R.id.signupbtn);
        spinner = findViewById(R.id.accTypeSpinner);
        //        //get the year, month, day calender
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day =  calendar.get(Calendar.DAY_OF_MONTH);



        mRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();

        mProgDialog = new ProgressDialog(this);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regProcess();

            }
        });
        dobTextEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show the Calender when Date of Birth clicked
                showCalenderDialog();



            }
        });

    }

    //Create method to insert data

    public void regProcess()
    {
        mProgDialog.setTitle("Registration Process ");
        mProgDialog.setMessage("Please wait for a moment");
        mProgDialog.setCanceledOnTouchOutside(false);
        mProgDialog.show();

        //get input field and store as String
        mFirstName = firstNameEdit.getText().toString();
        mSurname = surnameEdit.getText().toString();
        final String eml = email.getText().toString();
        final String pwd = pwdEdit.getText().toString();
        mDob = dobTextEdit.getText().toString();
        mAccountType= spinner.getSelectedItem().toString();
        // textView = (TextView)spinner.getSelectedItem();
       // String[] age = mDob.split("/");
      //  String ageNumber = age[2];


        //check if the fields are empty
        if(mFirstName.isEmpty())
        {
            mProgDialog.hide();
            firstNameEdit.setError("Please enter first name");
        }

        else  if(mSurname.isEmpty())
        {
            mProgDialog.hide();
            surnameEdit.setError("Please enter surname");
        }

        else if(eml.isEmpty())
        {
            mProgDialog.hide();
            email.setError("Please enter email");
        }

        else if(pwd.isEmpty())
        {
            mProgDialog.hide();
            pwdEdit.setError("Please enter password");
        }
        else if(pwd.length() < 7)
        {
            mProgDialog.hide();
            pwdEdit.setError("Password must be 7 or more characters");
        }


        else if(mDob.isEmpty())
        {
            mProgDialog.hide();
            dobTextEdit.setError("Please enter date of birth");
        }


        else if(mAccountType.equals("Select"))
        {
            mProgDialog.hide();
            Toast.makeText(this,"Please select account type",Toast.LENGTH_LONG).show();
        }

        //if its not  empty insert data into database
        else {


                mAuth.fetchSignInMethodsForEmail(eml).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean check = !task.getResult().getSignInMethods().isEmpty();

                        if (!check) {
                            mAuth.createUserWithEmailAndPassword(eml, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    //get userId and store as string
                                    FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    String userId = mCurrentUser.getUid();

                                    //Used hashmap for string key and value pairs

                                    HashMap<String, String> userMap = new HashMap<>();
                                    userMap.put("firstname", mFirstName);
                                    userMap.put("surname", mSurname);
                                    userMap.put("dateofbirth", mDob);
                                    userMap.put("accounttype", mAccountType);
                                    userMap.put("fillForm", "false");
                                    userMap.put("image", "default-image");

                                    if (mAccountType.equals("Doctor")) {


                                        mUserRef = mRef.child(userId);
                                        mUserRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    final FirebaseUser user = mAuth.getCurrentUser();
                                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                mProgDialog.dismiss();
                                                                Toast.makeText(Registratrion.this,
                                                                        "Verification email sent to " + user.getEmail(),
                                                                        Toast.LENGTH_SHORT).show();
                                                                //Toast.makeText(Registratrion.this, "Your registration was successful", Toast.LENGTH_LONG).show();

                                                                Intent intent = new Intent(Registratrion.this, LoginActivity.class);
                                                                startActivity(intent);
                                                            } else {
                                                                mProgDialog.dismiss();
                                                                // Log.e(TAG, "sendEmailVerification", task.getException());
                                                                Toast.makeText(Registratrion.this,
                                                                        "Failed to send verification email.",
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });


                                                }

                                            }
                                        });
                                    } else if (mAccountType.equals("Patient")) {
                                        mUserRef = mRef.child(userId);
                                        mUserRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

//
                                                    final FirebaseUser user = mAuth.getCurrentUser();
                                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                mProgDialog.dismiss();
                                                                Toast.makeText(Registratrion.this,
                                                                        "Verification email sent to " + user.getEmail(),
                                                                        Toast.LENGTH_SHORT).show();
                                                                //Toast.makeText(Registratrion.this, "Your registration was successful", Toast.LENGTH_LONG).show();
                                                                Intent formIntent = new Intent(Registratrion.this, LoginActivity.class);
                                                                startActivity(formIntent);
                                                            } else {
                                                                mProgDialog.dismiss();
                                                                // Log.e(TAG, "sendEmailVerification", task.getException());
                                                                Toast.makeText(Registratrion.this,
                                                                        "Failed to send verification email.",
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                }

                                            }
                                        });

                                    }
                                }

                            });
                        } else {
                            mProgDialog.hide();
                            Toast.makeText(Registratrion.this, R.string.toast, Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        }

    }

    //Create Calender dialog
    public void  showCalenderDialog()
    {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //set text edit date format

                dobTextEdit.setText(dayOfMonth + "/" + month + "/" + year);

            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,listener,year,month,day);

        datePickerDialog.show();
    }



    public void onClickTo(View view)
    {
        Intent intent = new Intent(Registratrion.this, LoginActivity.class);
        startActivity(intent);

    }

    public String buildToastDisplay(String toastName)
    {
        return toastName;
    }
}
