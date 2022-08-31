package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    TextView textViewChat;
    EditText editTextMessage;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    ImageView imageViewBack;

    String userName,otherName;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    MessageAdapter messageAdapter;
    List<ModelClass> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textViewChat = binding.txtChat;
        editTextMessage = binding.editTextTextMultiLine3;
        recyclerView = binding.recyclerView;
        floatingActionButton = binding.floating;
        imageViewBack = binding.imgViewBack;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        userName = getIntent().getStringExtra("userName");
        otherName = getIntent().getStringExtra("otherName");
        textViewChat.setText(otherName);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        getMessage();

        imageViewBack.setOnClickListener(view ->{
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);

        });

        floatingActionButton.setOnClickListener(view -> {
            String message = editTextMessage.getText().toString();
            if(!message.equals("")){
                sendMessage(message);
                editTextMessage.setText("");
            }

        });
    }

    private void getMessage() {
        databaseReference.child("Messages").child(userName).child(otherName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ModelClass modelClass = snapshot.getValue(ModelClass.class);
                list.add(modelClass);
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(list.size()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        messageAdapter = new MessageAdapter(list,userName);
        recyclerView.setAdapter(messageAdapter);
    }

    private void sendMessage(String message) {
        String key = databaseReference.child("Messages").child(userName).child(otherName).push().getKey();
        Map<String,Object> messageMap = new HashMap<>();
        messageMap.put("message",message);
        messageMap.put("from",userName);
        databaseReference.child("Messages").child(userName)
                .child(otherName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    databaseReference.child("Messages").child(otherName).child(userName)
                            .child(key).setValue(messageMap);
                }

            }
        });

    }
}