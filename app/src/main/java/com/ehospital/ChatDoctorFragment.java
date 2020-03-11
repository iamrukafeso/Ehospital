package com.ehospital;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.ContentHandler;
import java.net.MalformedURLException;


public class ChatDoctorFragment extends Fragment {

    private RecyclerView mChatList;
    private DatabaseReference mChatRef,mMessageRef,mUserDatabase;
    private Query mQuery;

    private FirebaseAuth mAuth;
    private String mCurrentUserId,fromUserId;
    private View mView;

    public ChatDoctorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_chat_doctor, container, false);

        mChatList = mView.findViewById(R.id.chatListId);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();


        mChatRef = FirebaseDatabase.getInstance().getReference().child("Conversion").child(mCurrentUserId);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        mMessageRef = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrentUserId);


        mQuery = mChatRef.orderByChild("timestamp");

       mChatList.setLayoutManager(new LinearLayoutManager(getContext()));

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

       FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Chat>()
               .setQuery(mQuery,Chat.class).build();

       FirebaseRecyclerAdapter<Chat,ChatViewHolder> adapter = new FirebaseRecyclerAdapter<Chat, ChatViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull final ChatViewHolder holder, int position, @NonNull final Chat model) {

               final String list_user_id = getRef(position).getKey();

               Query lastMessage = mMessageRef.child(list_user_id).limitToLast(1);

               lastMessage.addChildEventListener(new ChildEventListener() {
                   @Override
                   public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        final String messageType = dataSnapshot.child("type").getValue().toString();

                      final String data = dataSnapshot.child("message").getValue().toString();

                       mUserDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                              final String firstName = dataSnapshot.child("firstname").getValue().toString();
                              final String surname = dataSnapshot.child("surname").getValue().toString();



                               holder.setName("Dr" + firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase() + " " + surname.substring(0, 1).toUpperCase() + surname.substring(1).toLowerCase());
                              if(messageType.equals("text")) {
                                  holder.setMessage(data, model.isSeen());
                              }

                               holder.mView.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       Intent chatIntent = new Intent(getContext(),ChatAcitivity.class);
                                       chatIntent.putExtra("user_id",list_user_id);
                                       chatIntent.putExtra("name",firstName + surname);

                                       startActivity(chatIntent);

                                   }
                               });

                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });



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

           @NonNull
           @Override
           public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext())
                       .inflate(R.layout.user_single_layout, parent, false);
               return new ChatViewHolder(view);
           }
       };

        adapter.startListening();
        mChatList.setAdapter(adapter);
    }


    public static class ChatViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public  ChatViewHolder(View itemView)
        {
            super(itemView);

            mView = itemView;

        }

        public void setMessage(String message,boolean isSeen)
        {
            TextView messageText = mView.findViewById(R.id.doctSpecialist);

            messageText.setText(message);

            if(!isSeen)
            {
                messageText.setTypeface(messageText.getTypeface(), Typeface.NORMAL);
            }
            else {
                messageText.setTypeface(messageText.getTypeface(), Typeface.NORMAL);
            }
        }

        public void setName(String name)
        {
            TextView mName = mView.findViewById(R.id.doctName);

            mName.setText(name);

        }
    }

}
