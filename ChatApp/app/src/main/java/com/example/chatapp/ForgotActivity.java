package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.databinding.ActivityForgotBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {
    ActivityForgotBinding binding;
    EditText editTextEmail;
    Button buttonForgot;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        editTextEmail = binding.editEmail;
        buttonForgot = binding.buttonForgot;
        firebaseAuth = FirebaseAuth.getInstance();

        buttonForgot.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString();
            if(!email.equals("")){
                passwordReset(email);

            }
        });

    }

    public void passwordReset(String email){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotActivity.this,"Please check your email",Toast.LENGTH_SHORT).show();

                }else{


                    Toast.makeText(ForgotActivity.this,"There is a problem",Toast.LENGTH_SHORT).show();


                }

            }
        });
    }
}