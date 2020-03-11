package com.ehospital;

import android.graphics.Color;

import android.icu.text.DateFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private List<Message> mMesList;

    private FirebaseAuth mAuth;




    public MessageAdopter(List<Message> mMesList)
    {
        this.mMesList = mMesList;
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
        private TextView mReceiverText,mSenderText,mTimeViewSender,mTimeViewReceiver;
        private CircleImageView mProifleImageSender,mProifleImageReceiver;

        private Button mPlayBtn;
        private SeekBar mSeekBar;



        public MessageViewHolder(View view)
        {
            super(view);



            mSenderText = view.findViewById(R.id.senderText);
            mReceiverText = view.findViewById(R.id.receiverText);
            mTimeViewSender = view.findViewById(R.id.timeViewSender);
            mTimeViewReceiver = view.findViewById(R.id.timeViewReceiver);
            mProifleImageReceiver = view.findViewById(R.id.messageProfileImageReceiver);
            mProifleImageSender = view.findViewById(R.id.messageProfileImageSender);



        }





    }



    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {

        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        Message ms = mMesList.get(position);
        String message_from = ms.getFrom();



       // String time = String.valueOf(ms.getTime());

        if(message_from != null)
        {
            if (message_from.equals(currentUserId)) {
//                holder.messageText.setBackgroundColor(Color.WHITE);
//                holder.messageText.setTextColor(Color.BLACK);
//                holder.messageText.setGravity(Gravity.RIGHT);
                holder.mSenderText.setText(ms.getMessage());
                holder.mTimeViewSender.setText(ms.getTime());
                holder.mTimeViewReceiver.setVisibility(View.INVISIBLE);
                holder.mReceiverText.setVisibility(View.INVISIBLE);
                holder.mProifleImageReceiver.setVisibility(View.INVISIBLE);


            }

        else{
                holder.mReceiverText.setText(ms.getMessage());
                holder.mTimeViewReceiver.setText(ms.getTime());
                holder.mSenderText.setVisibility(View.INVISIBLE);
                holder.mTimeViewSender.setVisibility(View.INVISIBLE);
                holder.mProifleImageSender.setVisibility(View.INVISIBLE);

                }
            }
//        if(ms.getType().equals("text")) {
//            holder.messageText.setText(ms.getMessage());
//            holder.timeText.setText(ms.getTime());
//            holder.mPlayBtn.setVisibility(View.INVISIBLE);
//            holder.mSeekBar.setVisibility(View.INVISIBLE);
//        }
//        else if(ms.getType().equals("audio")){
//            holder.messageText.setVisibility(View.INVISIBLE);
//            holder.timeText.setText(ms.getTime());






    }
    public  int getItemCount()
    {
        return mMesList.size();
    }




}
