package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    EditText editTextEmail,editTextPassword;
    Button buttonSignup,buttonSignIn;
    TextView textViewForgotPassword;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser !=null){
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        buttonSignIn = binding.buttonSignin;
        buttonSignup = binding.buttonSignup;
        editTextEmail = binding.editTextEmail;
        editTextPassword = binding.editTextPassword;
        textViewForgotPassword = binding.txtForgotPassword;
        firebaseAuth=FirebaseAuth.getInstance();

        buttonSignIn.setOnClickListener(view ->{
            String email=editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            if(!email.equals("") && !password.equals("")){
                signIn(email,password);

            }
            else{
                Toast.makeText(MainActivity.this,"Please fill all fields",Toast.LENGTH_SHORT).show();

            }

        });

        buttonSignup.setOnClickListener(view -> {
            Intent intent = new Intent(this,SignUpActivity.class);
            startActivity(intent);

        });

        textViewForgotPassword.setOnClickListener(view ->{
            Intent intent = new Intent(this,ForgotActivity.class);
            startActivity(intent);

        });

    }
    public void signIn(String email,String password){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent= new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"Login Not Successful",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}