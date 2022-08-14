package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.quizgame.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    TextView textViewSignOut;
    TextView textViewStart;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        textViewSignOut=binding.txtSignOut;
        textViewStart = binding.textView3;
        textViewSignOut.setOnClickListener(view ->{
            firebaseAuth.signOut();
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();

        });

        textViewStart.setOnClickListener(view ->{
            Intent i = new Intent(MainActivity.this,Quiz_PageActivity.class);
            startActivity(i);
        });
    }
}