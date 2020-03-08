package com.ehospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class DoctorMainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar mNavBar;
    private DrawerLayout mDrawer;
    private ImageView mProfileImage;

    private RecyclerView mChatList;

    private DatabaseReference mChatRef,mMessageRef,mUserDatabase;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private FirebaseRecyclerAdapter adapter;
    private Query chatQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);


        mNavBar = findViewById(R.id.toolBar);
        setSupportActionBar(mNavBar);
        getSupportActionBar().setTitle("Doctor page");





        mChatList = findViewById(R.id.chatListIdPatient);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();


        mChatRef = FirebaseDatabase.getInstance().getReference().child("Conversion").child(mCurrentUserId);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");





        mMessageRef = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrentUserId);


        chatQuery = mChatRef.orderByChild("timestamp");

        mChatList.setLayoutManager(new LinearLayoutManager(this));


        mDrawer = findViewById(R.id.doctor_layout);

        // to tie DrawerLayout

        ActionBarDrawerToggle mBarToggle = new ActionBarDrawerToggle(this, mDrawer, mNavBar,
                R.string.nav_draw_open, R.string.nav_draw_close);

        mDrawer.addDrawerListener(mBarToggle);
        mBarToggle.syncState();

        NavigationView mNavView = findViewById(R.id.nav_view);

        mProfileImage = mNavView.getHeaderView(0).findViewById(R.id.user_proifle_image);

        mNavView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mUserDatabase.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("image").getValue().toString();



                Picasso.with(getApplicationContext()).load(image).placeholder(R.drawable.ic_profile).into(mProfileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Chat>(
                ).setQuery(chatQuery,Chat.class).build();

        FirebaseRecyclerAdapter<Chat,ChatViewHolder> adapter = new FirebaseRecyclerAdapter<Chat, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ChatViewHolder holder, int position, @NonNull final Chat model) {

                final String list_user_id = getRef(position).getKey();

                Query lastMessage = mMessageRef.child(list_user_id).limitToLast(1);

                lastMessage.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        final String data = dataSnapshot.child("message").getValue().toString();

                        mUserDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final String firstName = dataSnapshot.child("firstname").getValue().toString();
                                final String surname = dataSnapshot.child("surname").getValue().toString();

                                holder.setName(firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase() + " " + surname.substring(0, 1).toUpperCase() + surname.substring(1).toLowerCase());
                                holder.setMessage(data,model.isSeen());

                                holder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent chatIntent = new Intent(DoctorMainActivity.this,ChatAcitivity.class);
                                        chatIntent.putExtra("user_id",list_user_id);
                                        chatIntent.putExtra("name",firstName + " " +surname);

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
                        .inflate(R.layout.user_single_layout,parent,false);
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
                messageText.setTypeface(messageText.getTypeface(), Typeface.BOLD);
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

    @Override
    public void onBackPressed() {

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId())
        {
            case R.id.profile_nav:
                Intent profileIntent = new Intent(getApplicationContext(), DoctorProfileActivity.class);
                startActivity(profileIntent);

              //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new ProfileFragment()).commit();
                break;

            case R.id.logout_nav:
                mAuth.signOut();
                Intent homeIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(homeIntent);
                finish();





        }

        //close the navigation
        mDrawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
