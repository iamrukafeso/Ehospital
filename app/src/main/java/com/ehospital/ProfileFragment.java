package com.ehospital;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.RouteInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    private EditText mFirstName,mSurname,mEmail;
    private Button mUpdateBtn,mChangeProfileImgBtn;
    private  View mMainView;
    private CircleImageView mProfileImage;

    private DatabaseReference mUserRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference mStorageRef;
    private ProgressDialog mProgDialog;
    private static final int IMAGE_PICK = 1;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.profile_fragment,container,false);

        mFirstName = mMainView.findViewById(R.id.firstNameEdit);
        mSurname = mMainView.findViewById(R.id.surnameEdit);
        mEmail = mMainView.findViewById(R.id.emailEdit);
        mUpdateBtn = mMainView.findViewById(R.id.updateBtn);
        mProfileImage = mMainView.findViewById(R.id.profile_image);
        mChangeProfileImgBtn = mMainView.findViewById(R.id.changeImage);
        mProgDialog = new ProgressDialog(getActivity());

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final String userId = mUser.getUid();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String firstName = dataSnapshot.child("firstname").getValue().toString();
                String surname = dataSnapshot.child("surname").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                final String email = mUser.getEmail();

                mFirstName.setText(firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase());
                mSurname.setText(surname.substring(0, 1).toUpperCase() + surname.substring(1).toLowerCase());
                mEmail.setText(email.substring(0, 1).toUpperCase() + email.substring(1).toLowerCase());
                mEmail.setEnabled(false);
                mUpdateBtn.setEnabled(false);

                Toast.makeText(getContext(), image, Toast.LENGTH_SHORT).show();
                if(!mProfileImage.equals("default"))
                {
                    Picasso.with(getActivity()).load(image).placeholder(R.drawable.defaultimage).into(mProfileImage);
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

        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgDialog.setTitle("Updating details");
                mProgDialog.setMessage("Please wait");
                mProgDialog.show();
                String firstName = mFirstName.getText().toString();
                String surname = mSurname.getText().toString();

                if(firstName.isEmpty())
                {
                    mFirstName.setError("Please enter first name");
                }
               else if(surname.isEmpty())
                {
                    mSurname.setError("Please enter  surname");
                }
               else
                {
                    HashMap<String,Object> userMap = new HashMap<>();
                    userMap.put("firstname",firstName);
                    userMap.put("surname",surname);
                    mUserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                mProgDialog.dismiss();
                                ProfileFragment rSum = new ProfileFragment();
                                getFragmentManager().beginTransaction().remove(rSum).commit();

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


        return mMainView;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMAGE_PICK && resultCode == RESULT_OK) {
            Uri imageURL = data.getData();
            // to select the image as square
            CropImage.activity(imageURL).setAspectRatio(1, 1).start(getContext(), this);
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


                                    mUserRef.child("image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mProgDialog.dismiss();

                                            } else {
                                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();

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
