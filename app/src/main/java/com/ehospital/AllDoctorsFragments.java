package com.ehospital;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class AllDoctorsFragments extends Fragment {

    //to display list of  data
    private RecyclerView mDoctorList;
    private View mainView;

    private DatabaseReference mRef,mQuery,mDoctRef;
    private FirebaseAuth mAuth;
    private String currentUserId,accounttype;
    private  FirebaseRecyclerAdapter adapter;

    public AllDoctorsFragments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_all_doctors_fragments, container, false);

        mDoctorList = mainView.findViewById(R.id.doctorListId);
        mDoctorList.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        currentUserId = mAuth.getCurrentUser().getUid();
        mQuery = mRef.child("DoctorForm");
        mDoctRef = mRef.child("Users");




        mDoctorList.setLayoutManager(new LinearLayoutManager(getContext()));



        return  mainView;
    }

    @Override
    public void onStart() {
        super.onStart();


                    FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Doctor>().setQuery(
                            mQuery, Doctor.class).build();

                     adapter = new FirebaseRecyclerAdapter<Doctor, DoctorViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull final DoctorViewHolder holder, int position, @NonNull final Doctor model) {

                            final String user_id = getRef(position).getKey();

                            mDoctRef.child(user_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final String firstName = dataSnapshot.child("firstname").getValue().toString();
                                    final String surname = dataSnapshot.child("surname").getValue().toString();
                                    String image = dataSnapshot.child("image").getValue().toString();
                                    holder.setName(firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase() + " " + surname.substring(0, 1).toUpperCase() + surname.substring(1).toLowerCase());
                                    holder.setSpecialist(model.getSpecialistfield());
                                    holder.setImage(image,getContext());

                                    holder.mView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            CharSequence opt[] = new CharSequence[]{
                                                    "Profile",
                                                    "Chat"
                                            };

                                            // create alertDialog for popup
                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

                                            alertDialog.setTitle("Select options");
                                            alertDialog.setItems(opt, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int pos) {
                                                    // if it's profile
                                                    if(pos == 0)
                                                    {

                                                        String name_user = firstName;

                                                        Intent profileIntent = new Intent(getContext(),DoctorProfileActivity.class);
                                                        profileIntent.putExtra("user_id",user_id);
                                                        profileIntent.putExtra("name",name_user);
                                                        startActivity(profileIntent);
                                                    }
                                                    else if(pos == 1)
                                                    {
                                                        String name_user = firstName;

                                                        Intent chatIntent = new Intent(getContext(),ChatAcitivity.class);
                                                        chatIntent.putExtra("user_id",user_id);
                                                        chatIntent.putExtra("name",name_user);
                                                        startActivity(chatIntent);


                                                    }
                                                }
                                            });
                                            alertDialog.show();

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                        }

                        @NonNull
                        @Override
                        public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.user_single_layout, parent, false);
                            return new DoctorViewHolder(view);
                        }
                    };






        adapter.startListening();
        mDoctorList.setAdapter(adapter);
    }


    public static class DoctorViewHolder extends RecyclerView.ViewHolder
    {
        //allows to get the id of the fields
        View mView;
        public DoctorViewHolder(View itemView)
        {
            super(itemView);

            mView = itemView;


        }

        public void setSpecialist(String specialist)
        {
            TextView mSpecial = mView.findViewById(R.id.doctSpecialist);

            mSpecial.setText(specialist);
        }

        public void setName(String name)
        {
            TextView fulName = mView.findViewById(R.id.doctName);

            fulName.setText("DR. " + name);
        }
        public void setImage(String image, Context cnt)
        {
            CircleImageView mImage = mView.findViewById(R.id.user_proifle_image);

            Picasso.with(cnt).load(image).placeholder(R.drawable.ic_profile).into(mImage);

        }

    }
}
