package com.example.chatapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.databinding.CardComponetBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter <UserAdapter.ViewHolder>{

    List<String> userLists;
    String userName;
    Context context;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    public UserAdapter(List<String> userLists, Context context, String userName) {
        this.userLists = userLists;
        this.context = context;
        this.userName = userName;
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardComponetBinding binding = CardComponetBinding.inflate(LayoutInflater.from(context),parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        databaseReference.child("Users")
                .child(userLists.get(position))
                .addValueEventListener(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String otherName = snapshot.child("userName").getValue().toString();


                User user = snapshot.getValue(User.class);
                Picasso.get()
                        .load(user.getImage())
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(holder.circleImageView);

                holder.textViewUsers.setText(otherName);


                holder.cardView.setOnClickListener(view ->{
                    Intent intent = new Intent(context,ChatActivity.class);
                    intent.putExtra("userName",userName);
                    intent.putExtra("otherName",otherName);
                    context.startActivity(intent);

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return userLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewUsers;
        CircleImageView circleImageView;
        CardView  cardView;

        public ViewHolder(@NonNull CardComponetBinding binding) {
            super(binding.getRoot());
            textViewUsers = binding.txtUser;
            circleImageView = binding.imgProfile;
            cardView = binding.cardView;

        }
    }


}
