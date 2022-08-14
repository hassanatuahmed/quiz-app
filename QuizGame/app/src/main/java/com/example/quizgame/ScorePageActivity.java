package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.quizgame.databinding.ActivityScorePageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScorePageActivity extends AppCompatActivity {

    ActivityScorePageBinding binding;
    TextView textViewCorrect,textViewWrong;
    Button buttonExit,buttonPlayAgain;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference= database.getReference("scores");

    FirebaseAuth auth =FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String userCorrect;
    String userWrong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScorePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        buttonExit = binding.buttonExit;
        buttonPlayAgain = binding.buttonPlayAgain;
        textViewCorrect = binding.txtCorrectAnswer;
        textViewWrong = binding.txtWrong;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userUID = user.getUid();
                userCorrect = snapshot.child(userUID).child("correct").getValue().toString();
                userWrong = snapshot.child(userUID).child("wrong").getValue().toString();
                textViewCorrect.setText(userCorrect);
                textViewWrong.setText(userWrong);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonExit.setOnClickListener(view ->{
            finish();

        });

        buttonPlayAgain.setOnClickListener(view ->{
            Intent i =new Intent(ScorePageActivity.this,MainActivity.class);
            startActivity(i);
        });
    }
}