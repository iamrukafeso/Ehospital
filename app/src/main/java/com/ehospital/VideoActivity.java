package com.ehospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener  {

    private static String API_Key = "46624612";
    private static String SESSION_ID = "2_MX40NjYyNDYxMn5-MTU4NTUwNjUzNzU1MH5INEcrSE9GbGJKQkRUTkZCTWMxV2t6MXV-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjYyNDYxMiZzaWc9MzM1MGExNjU0YmQ2YzRhOTQ0OTM4MDQwYmEzY2EyYWY5NTNkZDI4NDpzZXNzaW9uX2lkPTJfTVg0ME5qWXlORFl4TW41LU1UVTROVFV3TmpVek56VTFNSDVJTkVjclNFOUdiR0pLUWtSVVRrWkNUV014VjJ0Nk1YVi1mZyZjcmVhdGVfdGltZT0xNTg1NTA2NjEwJm5vbmNlPTAuNzMyNzQ2NDg0MTYxMDAyNSZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTg4MDk4NjEwJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static final String LOG_TAG = VideoActivity.class.getSimpleName();
    private static final int RC_VIDEO_APP_PERM = 124;

    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private FrameLayout mPublisherView,mSubscriberContainerView;
    private ImageView closeBtn;

    private DatabaseReference userRef;

    private String userId = "";
    private String mUserIdReceiver,mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        closeBtn = findViewById(R.id.endVideoChat);


        mUserIdReceiver = getIntent().getStringExtra("user_id");
        mUserName =  getIntent().getStringExtra("name");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Call");

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(userId).hasChild("Ringing"))
                        {

                            userRef.child(userId).child("Ringing").removeValue();

                            if(mPublisher != null)
                            {
                                mPublisher.destroy();
                            }
                            if(mSubscriber != null)
                            {
                                mSubscriber.destroy();
                            }
                            Intent chatIntent = new Intent(VideoActivity.this,ChatAcitivity.class);
                            chatIntent.putExtra("user_id",mUserIdReceiver);
                            chatIntent.putExtra("name",mUserName);
                            startActivity(chatIntent);
                            finish();
                        }

                        if(dataSnapshot.child(userId).hasChild("Calling"))
                        {
                            userRef.child(userId).child("Calling").removeValue();

                            if(mPublisher != null)
                            {
                                mPublisher.destroy();
                            }
                            if(mSubscriber != null)
                            {
                                mSubscriber.destroy();
                            }

                            Intent chatIntent = new Intent(VideoActivity.this,ChatAcitivity.class);
                            chatIntent.putExtra("user_id",mUserIdReceiver);
                            chatIntent.putExtra("name",mUserName);

                            startActivity(chatIntent);
                            finish();
                        }
                        else{
                            if(mPublisher != null)
                            {
                                mPublisher.destroy();
                            }
                            if(mSubscriber != null)
                            {
                                mSubscriber.destroy();
                            }
                            Intent chatIntent = new Intent(VideoActivity.this,ChatAcitivity.class);
                            chatIntent.putExtra("user_id",mUserIdReceiver);
                            chatIntent.putExtra("name",mUserName);

                            startActivity(chatIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        requestPermissions();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,VideoActivity.this);


    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)

    private void requestPermissions()
    {
        String [] perm = {Manifest.permission.INTERNET,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};

        if(EasyPermissions.hasPermissions(this,perm))
        {
            mPublisherView = findViewById(R.id.publisherContainerVideo);
            mSubscriberContainerView = findViewById(R.id.subscriberVideo);

            // connect to the Session

            mSession = new Session.Builder(this,API_Key,SESSION_ID).build();
            mSession.setSessionListener(VideoActivity.this);
            mSession.connect(TOKEN);
        }
        else{
            EasyPermissions.requestPermissions(this,"App needs to access the camera and audio, please allow it",RC_VIDEO_APP_PERM, perm);
        }
    }


    // publihser a steam to the session

    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG,"Session connected");

        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(VideoActivity.this);

        mPublisherView.addView(mPublisher.getView());

        if(mPublisher.getView() instanceof GLSurfaceView)
        {
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }

        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {

        Log.i(LOG_TAG,"Stream Disconnacted");
    }

    // subscribing  to the stream

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG,"Stream received");

        if(mSubscriber == null)
        {
            mSubscriber = new Subscriber.Builder(this,stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberContainerView.addView(mSubscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        Log.i(LOG_TAG,"Stream droppper");

        if(mSubscriber != null)
        {
            mSubscriber = null;
            mSubscriberContainerView.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.i(LOG_TAG,"Stream error");
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

        Log.i(LOG_TAG,"Stream error");
    }



//    @Override
//    public void onError(Session session, OpentokError opentokError) {
//
//    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

//    @Override
//    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
//
//    }

}
