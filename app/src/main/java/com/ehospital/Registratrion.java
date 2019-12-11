package com.ehospital;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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

    FirebaseDatabase database;

    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registratrion);

        //get all element id
        firstNameEdit = findViewById(R.id.firstNameTextEdit);
        surnameEdit = findViewById(R.id.surnameTextEdit);
        pwdEdit = findViewById(R.id.pwdTextEdit);
        email = findViewById(R.id.emailEditText);
        dobTextEdit = findViewById(R.id.dobTextEdit);
        signUpBtn = findViewById(R.id.signupbtn);
        spinner = findViewById(R.id.accTypeSpinner);
        //        //get the year, month, day calender
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day =  calendar.get(Calendar.DAY_OF_MONTH);
//
//        //get instance of firebase
        database = FirebaseDatabase.getInstance();
        //insert data in members
        myRef = database.getReference("members");



        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRegistration();

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

    public void addRegistration()
    {
        //get input field and store in String
        String firstName = firstNameEdit.getText().toString();
        String surname = surnameEdit.getText().toString();
        String eml = email.getText().toString();
        String pwd = pwdEdit.getText().toString();
        String dob = dobTextEdit.getText().toString();
        String acctype = spinner.getSelectedItem().toString();
        // textView = (TextView)spinner.getSelectedItem();

        //check if the fields are empty
        if(firstName.isEmpty())
        {
            firstNameEdit.setError("Please enter first name");
        }

        else  if(surname.isEmpty())
        {
            surnameEdit.setError("Please enter surname");
        }

        else if(eml.isEmpty())
        {
            email.setError("Please enter email");
        }

        else if(pwd.isEmpty())
        {
            pwdEdit.setError("Please enter password");
        }

        else if(dob.isEmpty())
        {
            dobTextEdit.setError("Please enter date of birth");
        }

        else if(acctype.equals("Select"))
        {
            Toast.makeText(this,"Please select acount type",Toast.LENGTH_LONG).show();
        }


        //if its not  empty insert data into database
        else
        {

            String id = myRef.push().getKey();

            Members member  = new Members(id,firstName,surname,eml,pwd,dob,acctype);
            // set the value in members table
            myRef.child(id).setValue(member);
            Toast.makeText(this,"Your registration was successful",Toast.LENGTH_LONG).show();
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
}
