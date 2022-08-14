package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.quizgame.databinding.ActivityMainBinding;
import com.example.quizgame.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    EditText editTextEmail, editTextPassword;
    Button buttonSignUp;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        editTextEmail = binding.editTextEmail;
        editTextPassword = binding.editTextPassword;
        buttonSignUp = binding.buttonSignUp;
        progressBar = binding.progressBar;
        progressBar.setVisibility(View.GONE);

        buttonSignUp.setOnClickListener(view -> {
            buttonSignUp.setClickable(false);
            signUpFirebase(editTextEmail.getText().toString(),editTextPassword.getText().toString());

        });
    }

    public void signUpFirebase(String userEmail, String userPassword) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Your account is created successfully",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    progressBar.setVisibility(View.GONE);
                    editTextEmail.setText(null);
                    editTextPassword.setText(null);
                }
                else {
                    Toast.makeText(SignUpActivity.this, "An error occurred,Please try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}