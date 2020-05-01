package com.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfileActivity extends AppCompatActivity {

    private TextView mEmailText;
    private Toolbar mProfileBar;
    private EditText mFirstName,mSurname,mEmail,mYearOfExp,mSpecialist,mDesc;
    private Button mUpdateBtn,mChangeProfileImgBtn;

    private CircleImageView mProfileImage;

    private DatabaseReference mUserRef,mDoctFormRef,mPatientRef;
    private FirebaseAuth mAuth;
    private String mDoctorId;
    private FirebaseUser mUser;
    private StorageReference mStorageRef;
    private ProgressDialog mProgDialog;
    private static final int IMAGE_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        mProfileBar = findViewById(R.id.profileBar);
        setSupportActionBar(mProfileBar);
        getSupportActionBar().setTitle("Doctor Profile");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowCustomEnabled(true);

        mFirstName = findViewById(R.id.doct_FirstNameEdit);
        mSurname = findViewById(R.id.doct_SurnameEdit);
//        mEmail = findViewById(R.id.doct_EmailEdit);
        mYearOfExp = findViewById(R.id.doctYearOfExpEdit);
        mSpecialist = findViewById(R.id.doctSpecialiTextEdit);
        mDesc = findViewById(R.id.doctDescriptionEdit);

//        mEmailText = findViewById(R.id.doct_Email);
        mUpdateBtn =findViewById(R.id.doctupdateBtn);
        mProfileImage =findViewById(R.id.doct_profile_image);
        mChangeProfileImgBtn = findViewById(R.id.doct_ChangeImage);
        mProgDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        final String userId = mUser.getUid();
        mDoctorId = getIntent().getStringExtra("user_id");

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mDoctFormRef = FirebaseDatabase.getInstance().getReference().child("DoctorForm");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mPatientRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);



        mPatientRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String accounttype = dataSnapshot.child("accounttype").getValue().toString();

                if(accounttype.equals("Patient")) {

                    if (!mDoctorId.equals(userId)) {
                        mUserRef.child(mDoctorId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                String firstName = dataSnapshot.child("firstname").getValue().toString();
                                String surname = dataSnapshot.child("surname").getValue().toString();
                                String image = dataSnapshot.child("image").getValue().toString();

                                mFirstName.setEnabled(false);
                                mSurname.setEnabled(false);
//                                mEmail.setEnabled(false);
                                mYearOfExp.setEnabled(false);
                                mSpecialist.setEnabled(false);
                                mDesc.setEnabled(false);
                                mUpdateBtn.setVisibility(View.INVISIBLE);
                                mChangeProfileImgBtn.setVisibility(View.INVISIBLE);

//                                mEmail.setVisibility(View.INVISIBLE);
//                                mEmailText.setVisibility(View.INVISIBLE);

                               // final String email = dataSnapshot.child("email").getValue().toString();

                                mFirstName.setText(firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase());
                                mSurname.setText(surname.substring(0, 1).toUpperCase() + surname.substring(1).toLowerCase());
                              //  mEmail.setText(email.substring(0, 1).toUpperCase() + email.substring(1).toLowerCase());
//                                mEmail.setEnabled(false);
                                mUpdateBtn.setEnabled(false);

                                if (!mProfileImage.equals("default")) {
                                    Picasso.with(getApplicationContext()).load(image).placeholder(R.drawable.defaultimage).into(mProfileImage);
                                }

                                mDoctFormRef.child(mDoctorId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        String yearExp = dataSnapshot.child("yearExperience").getValue().toString();
                                        String specField = dataSnapshot.child("specialistfield").getValue().toString();
                                        if (dataSnapshot.hasChild("description")) {
                                            String desc = dataSnapshot.child("description").getValue().toString();
                                            mDesc.setText(desc.substring(0, 1).toUpperCase() + desc.substring(1).toLowerCase());
                                        }

                                        mYearOfExp.setText(yearExp.substring(0, 1).toUpperCase() + yearExp.substring(1).toLowerCase());
                                        mSpecialist.setText(specField.substring(0, 1).toUpperCase() + specField.substring(1).toLowerCase());


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                else{

                    mUserRef.child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                            String firstName = dataSnapshot.child("firstname").getValue().toString();
                            String surname = dataSnapshot.child("surname").getValue().toString();
                            String image = dataSnapshot.child("image").getValue().toString();



                            final String email = mUser.getEmail();

                            mFirstName.setText(firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase());
                            mSurname.setText(surname.substring(0, 1).toUpperCase() + surname.substring(1).toLowerCase());
//                            mEmail.setText(email.substring(0, 1).toUpperCase() + email.substring(1).toLowerCase());
//                            mEmail.setEnabled(false);
                            mUpdateBtn.setEnabled(false);

                            if (!mProfileImage.equals("default")) {
                                Picasso.with(getApplicationContext()).load(image).placeholder(R.drawable.defaultimage).into(mProfileImage);
                            }

                            mDoctFormRef.child(userId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    String yearExp = dataSnapshot.child("yearExperience").getValue().toString();
                                    String specField = dataSnapshot.child("specialistfield").getValue().toString();
                                    if (dataSnapshot.hasChild("description")) {
                                        String desc = dataSnapshot.child("description").getValue().toString();
                                        mDesc.setText(desc.substring(0, 1).toUpperCase() + desc.substring(1).toLowerCase());
                                    }

                                    mYearOfExp.setText(yearExp.substring(0, 1).toUpperCase() + yearExp.substring(1).toLowerCase());
                                    mSpecialist.setText(specField.substring(0, 1).toUpperCase() + specField.substring(1).toLowerCase());


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }




                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        mFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                mUpdateBtn.setEnabled(true);

            }
        });

        mSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                mUpdateBtn.setEnabled(true);

            }
        });

//        mEmail.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//
//                mUpdateBtn.setEnabled(true);
//
//            }
//        });

        mYearOfExp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                mUpdateBtn.setEnabled(true);

            }
        });

        mSpecialist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                mUpdateBtn.setEnabled(true);

            }
        });
        mDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                mUpdateBtn.setEnabled(true);

            }
        });

        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgDialog.setTitle("Updating details");
                mProgDialog.setMessage("Please wait");
                mProgDialog.show();
                String firstName = mFirstName.getText().toString();
                String surname = mSurname.getText().toString();
                final String yearExp = mYearOfExp.getText().toString();
                final String spec = mSpecialist.getText().toString();
                final String desc = mDesc.getText().toString();

                if(firstName.isEmpty())
                {
                    mFirstName.setError("Please enter first name");
                }
                else if(surname.isEmpty())
                {
                    mSurname.setError("Please enter  surname");
                }

                else if(yearExp.isEmpty())
                {
                    mYearOfExp.setError("Please enter  surname");
                }

                else if(spec.isEmpty())
                {
                    mSpecialist.setError("Please enter  surname");
                }

                else if(desc.isEmpty())
                {
                    mDesc.setError("Please enter  surname");
                }
                else
                {
                    HashMap<String,Object> userMap = new HashMap<>();
                    userMap.put("firstname",firstName);
                    userMap.put("surname",surname);

                    mUserRef.child(userId).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                HashMap<String,Object> doctMap = new HashMap<>();

                                doctMap.put("yearExperience",yearExp);
                                doctMap.put("specialistfield",spec);
                                doctMap.put("description",desc);

                                mDoctFormRef.child(userId).updateChildren(doctMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()) {
                                            mProgDialog.dismiss();
                                            mUpdateBtn.setEnabled(false);
                                            // ProfileFragment rSum = new ProfileFragment();
                                            //getFragmentManager().beginTransaction().remove(rSum).commit();
                                        }

                                    }
                                });


                            }
                        }
                    });





                }

            }
        });

        // add click listen to open gallery

        mChangeProfileImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"), IMAGE_PICK);




            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMAGE_PICK && resultCode == RESULT_OK) {
            Uri imageURL = data.getData();
            // to select the image as square
            CropImage.activity(imageURL).setAspectRatio(1, 1).start( this);
            // Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();

        }




        // check if the requestcode is passed through crop activity

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            //store in result variable
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                // set the progress dialog while uploading the image
                mProgDialog.setTitle("Uploading image ...");
                mProgDialog.setMessage("Please wait");
                mProgDialog.setCanceledOnTouchOutside(false);
                mProgDialog.show();


                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

                final String currentUserId = mAuth.getUid();




                // store the image in firebase storage
                final StorageReference filePath = mStorageRef.child("profile-images").child(currentUserId + ".jpg");


                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {


                            mStorageRef.child("profile-images").child(currentUserId + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override
                                public void onSuccess(final Uri uri) {

                                    final String downloadUrl = uri.toString();


                                    mPatientRef.child("image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mProgDialog.dismiss();

                                            } else {
                                                Toast.makeText(DoctorProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });


                                }


                            });


                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {


                mProgDialog.hide();
                Exception error = result.getError();


            }

        }
    }
}
