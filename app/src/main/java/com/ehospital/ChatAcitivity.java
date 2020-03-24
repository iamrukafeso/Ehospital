package com.ehospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAcitivity extends AppCompatActivity implements RecycleViewListener {

    private Toolbar mChatBar;

    private TextView mName;
    private EditText mInputMessage;
    private ImageView mSendBtn, mAudioBtn;

    private String audio;
    private Runnable runnable;
    private Handler handler;

    private CircleImageView mUserProfile;

    private DatabaseReference mUserRef, mDatabaseRef, mUserRoot, mAudioRef;
    private FirebaseAuth mAuth;
    private String mCurrentId, mMessager;

    private RecyclerView mMessageList;
    private MessageAdopter mAdopter;
    private SwipeRefreshLayout mRefresh;

    private final List<Message> listMessage = new ArrayList<>();
    private LinearLayoutManager mLineManager;


    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    //    private static String mFileName = null;
    private String mFileName = null;

    private StorageReference mStorage;
    private int num = 0;

    private static final String LOG_TAG = "AudioRecordTest";


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_acitivity);

        mStorage =FirebaseStorage.getInstance().getReference();

        mAudioBtn = findViewById(R.id.audioBtn);

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += mCurrentId + num + ".3gp";
        num++;


        mAudioBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startRecording();

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopRecording();
                    uploadToFireBase();
                    listMessage.clear();
                    loadMessage();

                }

                return false;
            }
        });



    mAuth =FirebaseAuth.getInstance();
    mCurrentId =mAuth.getCurrentUser().getUid();

    mUserRef =FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentId);
//
//    mChatBar = findViewById(R.id.toolBar);
//    setSupportActionBar(mChatBar);

    //setting the action  bar like the arrow bar
//
//    actionBar.setDisplayHomeAsUpEnabled(true);
//    actionBar.setDisplayShowCustomEnabled(true);

    // get the user details
    mMessager = getIntent().getStringExtra("user_id");

    String name = getIntent().getStringExtra("name");

//    getSupportActionBar().setTitle(name);

    LayoutInflater mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    // get the customise layout chat xml
    View mActionView = mInflater.inflate(R.layout.customise_chat_layout, null);

    // set the bar to customise layout
//        actionBar.setCustomView(mActionView);

//    mName = findViewById(R.id.chatter_name);

    mUserProfile = findViewById(R.id.chatter_image);

    mSendBtn = findViewById(R.id.senderBtn);
    mInputMessage = findViewById(R.id.sendMessageEdit);
    mRefresh = findViewById(R.id.swapMessageLayout);

    mAdopter =new MessageAdopter(listMessage,this);

    mMessageList = findViewById(R.id.chat_list);
    mLineManager =new LinearLayoutManager(this);
        mMessageList.setHasFixedSize(true);
        mMessageList.setLayoutManager(mLineManager);
        mMessageList.setAdapter(mAdopter);

//        mName.setText(name);
        loadMessage();

        mUserRef.addValueEventListener(new

    ValueEventListener() {
        @Override
        public void onDataChange (@NonNull DataSnapshot dataSnapshot){

        }

        @Override
        public void onCancelled (@NonNull DatabaseError databaseError){

        }
    });

    mPlayer =new MediaPlayer();

    mDatabaseRef =FirebaseDatabase.getInstance().getReference();
    mDatabaseRef.child("Conversion").child(mCurrentId).

    addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange (@NonNull DataSnapshot dataSnapshot){

            if (!dataSnapshot.hasChild(mMessager)) {
                Map convMap = new HashMap();
                convMap.put("seen", false);
                convMap.put("timestamp", ServerValue.TIMESTAMP);

                Map convUserMap = new HashMap();
                convUserMap.put("Conversion/" + mCurrentId + "/" + mMessager, convMap);
                convUserMap.put("Conversion/" + mMessager + "/" + mCurrentId, convMap);


                mDatabaseRef.updateChildren(convUserMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                    }
                });
            }
        }

        @Override
        public void onCancelled (@NonNull DatabaseError databaseError){

        }
    });
//        mAudioRef = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrentId).child(mMessager);
//
//
//        mAudioRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                Message ms  = dataSnapshot.getValue(Message.class);
//
//                String type = ms.getType().toString();
//
//                Toast.makeText(ChatAcitivity.this, type, Toast.LENGTH_SHORT).show();
//
////                if(msType.equals("audio")) {
////                    String messageType = dataSnapshot.getValue(Message.class).getMessage();
////
////                    audio = messageType;
////                }
//
//
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
        mSendBtn.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        sendMessage();
        mInputMessage.getText().clear();
    }
    });

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()

    {
        @Override
        public void onRefresh () {

        mRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mRefresh.isRefreshing()) {
                    mRefresh.setRefreshing(false);
                }

            }
        }, 1000);

    }

    });

    }


    private void sendMessage() {
        String message = mInputMessage.getText().toString();

        if(!message.isEmpty())
        {
            String current_userId = "messages/" + mCurrentId + "/" + mMessager;
            String from_userId = "messages/" + mMessager + "/" + mCurrentId;

            DatabaseReference path_message = mDatabaseRef.child("messages").child(mCurrentId).child(mMessager)
                    .push();
            String key_id_push = path_message.getKey();

            Map messageMap = new HashMap();


            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            String time = format.format(calendar.getTime());


            messageMap.put("message",message);
            messageMap.put("type","text");
            messageMap.put("from",mCurrentId);
            messageMap.put("time", time);
            messageMap.put("seen",false);

            Map userMap = new HashMap();
            userMap.put(current_userId + "/" + key_id_push,messageMap);
            userMap.put(from_userId + "/" + key_id_push,messageMap);

            mDatabaseRef.updateChildren(userMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                    if(databaseError != null)
                    {
                        Log.d("Chat", databaseError.getMessage().toString());
                    }
                }
            });


        }
    }

    public void loadMessage()
    {
        mUserRoot = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference messageRef = mUserRoot.child("messages").child(mCurrentId)
                .child(mMessager);

        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Message ms = dataSnapshot.getValue(Message.class);

                listMessage.add(ms);

                mAdopter.notifyDataSetChanged();

                mMessageList.scrollToPosition(listMessage.size() -1);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //Audio recorder
    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private void uploadToFireBase() {

        final StorageReference filePath = mStorage.child("Audio").child(mCurrentId + num + ".3gp");


        Uri uri = Uri.fromFile(new File(mFileName));

        filePath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if(task.isSuccessful())
                {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                            String current_userId = "messages/" + mCurrentId + "/" + mMessager;
                            String from_userId = "messages/" + mMessager + "/" + mCurrentId;

                            DatabaseReference path_message = mDatabaseRef.child("messages").child(mCurrentId).child(mMessager)
                                    .push();
                            String key_id_push = path_message.getKey();

                            Map messageMap = new HashMap();


                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                            String time = format.format(calendar.getTime());

                            String downloadUrl = uri.toString();

                            messageMap.put("message",downloadUrl);
                            messageMap.put("type","audio");
                            messageMap.put("from",mCurrentId);
                            messageMap.put("time", time);
                            messageMap.put("seen",false);

                            Map userMap = new HashMap();
                            userMap.put(current_userId + "/" + key_id_push,messageMap);
                            userMap.put(from_userId + "/" + key_id_push,messageMap);

                            mDatabaseRef.updateChildren(userMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    Toast.makeText(ChatAcitivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    });

                }

            }
        });

    }

//    public void play_audio(String audio)
//    {
//
//
//        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//        try{
//            mPlayer.reset();
//            mPlayer.setDataSource(audio);
//            mPlayer.prepareAsync();
//            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                   // mSeekBar.setMax(mPlayer.getDuration());
//                    mp.start();
//                  //  changeToSeekBar();
//
//                }
//            });
////            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
////                @Override
////                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////                    if(fromUser) {
////                        mPlayer.seekTo(progress);
////                    }
////                }
////
////                @Override
////                public void onStartTrackingTouch(SeekBar seekBar) {
////
////                }
////
////                @Override
////                public void onStopTrackingTouch(SeekBar seekBar) {
////
////                }
////            });
//
//           // mPlayer.prepare();
//        }catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//    }


    @Override
    public void onPlay(int position) {


          if(!mAdopter.mPlayer.isPlaying()) {
            String audioPlay = listMessage.get(position).getMessage();
            mAdopter.play_audio(audioPlay);


        }
        else{
            mPlayer.stop();
        }
    }

    @Override
    public void onPause(int position) {

    }
}
