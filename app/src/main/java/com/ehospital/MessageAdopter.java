package com.ehospital;

import android.graphics.Color;

import android.icu.text.DateFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;


import java.io.IOException;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.icu.text.DateFormat.getDateTimeInstance;

public class MessageAdopter extends RecyclerView.Adapter<MessageAdopter.MessageViewHolder> {

    private RecycleViewListener recycleViewListener;
    private List<Message> mMesList;
    private FirebaseAuth mAuth;
    public   MediaPlayer mPlayer;
    private Handler handler;
    private Runnable runnable;
    private String audio;

   // public ImageView mPlayAudioSenderBtn,mPauseSenderBtn,mPlayAudioReceiverBtn,mPauseReceiverBtn;





    public MessageAdopter(List<Message> mMesList,RecycleViewListener recycleViewListener)
    {
        handler = new Handler();
        mPlayer = new MediaPlayer();
        this.mMesList = mMesList;
        this.recycleViewListener = recycleViewListener;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        // layoutInflater used to instantiate layout XML file into its corresponding view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_single_layout,parent,false);

        return new MessageViewHolder(view);
    }

    public  class  MessageViewHolder extends RecyclerView.ViewHolder
    {

        private TextView mReceiverText,mSenderText,mTimeViewSender,mTimeViewReceiver,mAudioText;
        private CircleImageView mProifleImageSender,mProifleImageReceiver;
        //private ImageView mPlayAudioSenderBtn,mPauseSenderBtn,mPlayAudioReceiverBtn,mPauseReceiverBtn;




        public MessageViewHolder(View view)
        {
            super(view);



            mSenderText = view.findViewById(R.id.senderText);
            mReceiverText = view.findViewById(R.id.receiverText);
            mTimeViewSender = view.findViewById(R.id.timeViewSender);
            mTimeViewReceiver = view.findViewById(R.id.timeViewReceiver);
            mProifleImageReceiver = view.findViewById(R.id.messageProfileImageReceiver);
            mProifleImageSender = view.findViewById(R.id.messageProfileImageSender);
           // mPlayAudioSenderBtn = view.findViewById(R.id.playAudioSenderBtn);
            //mPlayAudioReceiverBtn = view.findViewById(R.id.playAudioReceiverBtn);

           // mPauseReceiverBtn = view.findViewById(R.id.pauseAudioReceiverBtn);
           // mPauseSenderBtn = view.findViewById(R.id.pauseAudioSenderBtn);

//            mPlayAudioSenderBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                    public void onClick(View v) {
//                        recycleViewListener.onPlay(getAdapterPosition());
//
//                    }
//                });
//            mPauseSenderBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        recycleViewListener.onPause(getAdapterPosition());
//                    }
//                });
//            mPlayAudioReceiverBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        recycleViewListener.onPlay(getAdapterPosition());
//                    }
//                });
//            mPauseReceiverBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                    recycleViewListener.onPause(getAdapterPosition());
//                }
//            });

        }





    }



    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {

        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        Message ms = mMesList.get(position);
        String message_from = ms.getFrom();



       // String time = String.valueOf(ms.getTime());

        if(message_from != null)
        {
            if (message_from.equals(currentUserId)) {

//                if(ms.getType().equals("text")) {

                    //mPauseReceiverBtn.setVisibility(View.INVISIBLE);
//                    holder.mPlayAudioSenderBtn.setVisibility(View.INVISIBLE);
//                    holder.mPlayAudioReceiverBtn.setVisibility(View.INVISIBLE);
//                    mPauseSenderBtn.setVisibility(View.INVISIBLE);

                    holder.mSenderText.setVisibility(View.VISIBLE);
                    holder.mTimeViewSender.setVisibility(View.VISIBLE);

//
                    holder.mSenderText.setText(ms.getMessage());
                    holder.mTimeViewSender.setText(ms.getTime());


                    holder.mProifleImageSender.setVisibility(View.VISIBLE);
               // }
//                else if(ms.getType().equals("audio")){
//                   // holder.mSenderText.setVisibility(View.VISIBLE);
//                    holder.mSenderText.setText(ms.getMessage());
//                    holder.mTimeViewSender.setText(ms.getTime());
//                    holder.mTimeViewSender.setVisibility(View.VISIBLE);
//                    holder.mProifleImageSender.setVisibility(View.VISIBLE);
////                    holder.mAudioText.setVisibility(View.VISIBLE);
//                   // holder.mAudioText.setText("Audio");
//                   // mPauseSenderBtn.setVisibility(View.VISIBLE);
//                    holder.mPlayAudioSenderBtn.setVisibility(View.VISIBLE);
//
//
//                }


            }

        else{


//                if(ms.getType().equals("text")) {


//
                    holder.mReceiverText.setVisibility(View.VISIBLE);
                    holder.mTimeViewReceiver.setVisibility(View.VISIBLE);
                    holder.mReceiverText.setText(ms.getMessage());
                    holder.mTimeViewReceiver.setText(ms.getTime());
                    holder.mProifleImageReceiver.setVisibility(View.VISIBLE);
//                    holder.mSenderText.setVisibility(View.INVISIBLE);
//                    holder.mTimeViewSender.setVisibility(View.INVISIBLE);
//                    holder.mProifleImageSender.setVisibility(View.INVISIBLE);
//                }
//                else if(ms.getType().equals("audio")){
////
//                      holder.mTimeViewSender.setText(ms.getTime());
//                      holder.mTimeViewReceiver.setVisibility(View.VISIBLE);
////                    holder.mTimeViewReceiver.setVisibility(View.INVISIBLE);
////                    holder.mReceiverText.setVisibility(View.INVISIBLE);
//                      holder.mProifleImageReceiver.setVisibility(View.VISIBLE);
////
//                    holder.mPlayAudioReceiverBtn.setVisibility(View.VISIBLE);
////                    holder.mPlayAudioSenderBtn.setVisibility(View.INVISIBLE);
//
//                }


                }

            }

//        holder.mPlayAudioSenderBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                recycleViewListener.onPlay(position);
//
//                if(mPlayer.isPlaying())
//                {
//                    mPlayer.stop();
//                    holder.mPlayAudioSenderBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
//                }
//                else {
//
//                    holder.mPlayAudioSenderBtn.setImageResource(R.drawable.ic_pause_black_24dp);
//                }
//
//            }
//        });
//        holder.mPlayAudioReceiverBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                recycleViewListener.onPlay(position);
//
//                if(mPlayer.isPlaying())
//                {
//                    mPlayer.stop();
//                    holder.mPlayAudioReceiverBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
//                }
//                else {
//
//                    holder.mPlayAudioReceiverBtn.setImageResource(R.drawable.ic_pause_black_24dp);
//                }
//
//            }
//        });



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
//                   // seekBar.setMax(mPlayer.getDuration());
//                    mp.start();
//                   //  changeToSeekBar();
//
//                }
//            });
//               seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    if(fromUser) {
//                        mPlayer.seekTo(progress);
//                    }
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//
//                }
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//
//                }
//            });

            // mPlayer.prepare();
//        }catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//    }

//    private void changeToSeekBar() {
//
//        seekBar.setProgress(mPlayer.getCurrentPosition());
//
//        if(mPlayer.isPlaying())
//        {
//
//            runnable = new Runnable() {
//                @Override
//                public void run() {
//
//                    changeToSeekBar();
//                }
//            };
//            handler.postDelayed(runnable,1000);
//        }
//
//    }


    public  int getItemCount()
    {
        return mMesList.size();
    }




}
