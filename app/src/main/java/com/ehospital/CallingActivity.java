package com.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CallingActivity extends AppCompatActivity {

    private TextView nameUser;
    private ImageView acceptCallBtn,profileImage,cancelBtn;

    private String userIdReceiver,userProfileImage,userName;
    private String userIdSender,userProfileImageSender,userNameSender,checker = "",callingID,ringingId;

    private DatabaseReference mUserRef,mCallingRef;

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        acceptCallBtn = findViewById(R.id.make_video);
        profileImage = findViewById(R.id.profileCallng);
        cancelBtn = findViewById(R.id.cancel_call);
        nameUser = findViewById(R.id.name_calling);


        mMediaPlayer = MediaPlayer.create(this,R.raw.ringing);
//        String data = getIntent().getStringExtra("user_id");
//        if(data == null)
//        {
//            userId = getIntent().getStringExtra("from_user_id");
//        }
//        else{
         //   userId = getIntent().getStringExtra("user_id");
       // }

        userIdReceiver = getIntent().getExtras().get("user_id").toString();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mCallingRef = FirebaseDatabase.getInstance().getReference().child("Call");
        userIdSender = FirebaseAuth.getInstance().getCurrentUser().getUid();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMediaPlayer.stop();
                checker = "clicked";

                cancelCallingUser();
            }


        });

        acceptCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMediaPlayer.stop();

                final HashMap<String,Object> callingPickUp = new HashMap<>();
                callingPickUp.put("picked","picked");

                mCallingRef.child(userIdSender).child("Ringing").updateChildren(callingPickUp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Intent videoIntent = new Intent(CallingActivity.this,VideoActivity.class);
                            videoIntent.putExtra("user_id",userIdReceiver);
                            startActivity(videoIntent);
                        }
                    }
                });
            }
        });

        getAndUserInfo();
    }



    private void getAndUserInfo() {

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(userIdReceiver).exists())
                {
                    userProfileImage = dataSnapshot.child(userIdReceiver).child("image").getValue().toString();
                    userName = dataSnapshot.child(userIdReceiver).child("firstname").getValue().toString();

                    nameUser.setText(userName);
                    Picasso.with(CallingActivity.this).load(userProfileImage).placeholder(R.drawable.defaultimage).into(profileImage);
                }

                if(dataSnapshot.child(userIdSender).exists()) {
                    userProfileImageSender = dataSnapshot.child(userIdSender).child("image").getValue().toString();
                    userNameSender = dataSnapshot.child(userIdSender).child("firstname").getValue().toString();


                    nameUser.setText(userNameSender);
                    Picasso.with(CallingActivity.this).load(userProfileImageSender).placeholder(R.drawable.defaultimage).into(profileImage);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        mMediaPlayer.start();

        mCallingRef.child(userIdReceiver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!checker.equals("clicked")&& !dataSnapshot.hasChild("Calling") && !dataSnapshot.hasChild("Ringing"))
                {

                    final HashMap<String,Object> callInfo = new HashMap<>();

                    callInfo.put("calling",userIdReceiver);

                    mCallingRef.child(userIdSender).child("Calling")
                            .updateChildren(callInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {

                                final HashMap<String,Object> ringingInfo = new HashMap<>();

                                ringingInfo.put("ringing",userIdSender);

                                mCallingRef.child(userIdReceiver).child("Ringing")
                                        .updateChildren(ringingInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(CallingActivity.this, "Video connected", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mCallingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userIdSender).hasChild("Ringing") && !dataSnapshot.child(userIdSender).hasChild("Calling"))
                {
                    acceptCallBtn.setVisibility(View.VISIBLE);
                }

                if (dataSnapshot.child(userIdReceiver).child("Ringing").hasChild("picked"))
                {
                    mMediaPlayer.stop();
                    Intent videoIntent = new Intent(CallingActivity.this,VideoActivity.class);
                    videoIntent.putExtra("user_id",userIdReceiver);
                    startActivity(videoIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void cancelCallingUser() {

        // from sender side
        mCallingRef.child(userIdSender).child("Calling").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("calling"))
                {
                    callingID = dataSnapshot.child("calling").getValue().toString();

                    mCallingRef.child(callingID).child("Ringing").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()) {
                                mCallingRef.child(userIdSender).child("Calling").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Intent chatIntent = new Intent(CallingActivity.this,ChatAcitivity.class);
                                        chatIntent.putExtra("user_id",userIdReceiver);
                                        chatIntent.putExtra("name",userName);

                                        startActivity(chatIntent);

                                        finish();
                                    }
                                });
                            }
                        }
                    });
                }
                else{
                    Intent chatIntent = new Intent(CallingActivity.this,ChatAcitivity.class);
                    chatIntent.putExtra("user_id",userIdReceiver);
                    chatIntent.putExtra("name",userName);

                    startActivity(chatIntent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // from receiver side

        mCallingRef.child(userIdSender).child("Ringing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("ringing"))
                {
                    ringingId = dataSnapshot.child("ringing").getValue().toString();

                    mCallingRef.child(ringingId).child("Calling").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()) {
                                mCallingRef.child(userIdSender).child("Ringing").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Intent chatIntent = new Intent(CallingActivity.this,ChatAcitivity.class);
                                        chatIntent.putExtra("user_id",userIdReceiver);
                                        chatIntent.putExtra("name",userName);
                                        startActivity(chatIntent);
                                        finish();
                                    }
                                });
                            }
                        }
                    });
                }
                else{
                    Intent chatIntent = new Intent(CallingActivity.this,ChatAcitivity.class);
                    chatIntent.putExtra("user_id",userIdReceiver);
                    chatIntent.putExtra("name",userName);

                    startActivity(chatIntent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
