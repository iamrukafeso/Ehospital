package com.ehospital;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatAdopter extends RecyclerView.Adapter<ChatAdopter.ChatViewHolder> {
    private static final int MSG_TYPE_RIGHT =0;
    private static final int MSG_TYPE_LEFT = 0;
    private Context mContext;
    private List<Mess> mMessList;

    private FirebaseUser mUser;
    public ChatAdopter(Context mContext,List<Mess> mMessList)
    {
        this.mContext = mContext;
        this.mMessList = mMessList;

    }

    @NonNull
    @Override
    public ChatAdopter.ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // layoutInflater used to instantiate layout XML file into its corresponding view

        if(viewType == MSG_TYPE_RIGHT)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right,parent,false);
            return new ChatAdopter.ChatViewHolder(view);
        }
        else{

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left,parent,false);
            return new ChatAdopter.ChatViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdopter.ChatViewHolder holder, int position) {
        Mess mess = mMessList.get(position);

        holder.show_message.setText(mess.getMessage());
    }




    @Override
    public int getItemCount() {
        return 0;
    }


    public static class ChatViewHolder extends  RecyclerView.ViewHolder
    {
        public TextView show_message;

        public ChatViewHolder(View itemView)
        {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mMessList.get(position).getSender().equals(mUser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }


}
