package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.quizgame.databinding.ActivityForgotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    ActivityForgotPasswordBinding binding;
    Button buttonContinue;
    EditText editTextEmail;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        buttonContinue = binding.buttonContinue;
        progressBar = binding.progressBar3;
        editTextEmail = binding.editTextTextPersonEmail;
        progressBar.setVisibility(View.GONE);

        buttonContinue.setOnClickListener(view ->{
            resetPassword(editTextEmail.getText().toString());


        });
    }

    public void resetPassword(String userEmail){
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(userEmail)
        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(ForgotPasswordActivity.this,"We Have Sent You your New Password Via Email",Toast.LENGTH_SHORT).show();
                    buttonContinue.setClickable(false);
                    progressBar.setVisibility(View.GONE);
                    finish();
                }else {
                    Toast.makeText(ForgotPasswordActivity.this,"Sorry,Try Again later",Toast.LENGTH_SHORT).show();


                }
            }
        });

    }
}