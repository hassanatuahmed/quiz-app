package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizgame.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding  binding;
    EditText editTextEmail,editTextPassword;
    Button buttonLogin;
    SignInButton buttonSignWithGoogle;
    TextView textViewSignUp,textViewForgotPassword;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editTextPassword = binding.editTextPassword;
        editTextEmail= binding.editTextEmail;
        buttonLogin = binding.buttonLogin;
        buttonSignWithGoogle = binding.signInButton;
        textViewSignUp = binding.textViewSignUp;
        progressBar = binding.progressBar;
        textViewForgotPassword = binding.textViewForgotPassword;
        progressBar.setVisibility(View.GONE);

        textViewSignUp.setOnClickListener(view -> {
            Intent intent= new Intent(LoginActivity.this,SignUpActivity.class);
            startActivity(intent);
        });

        buttonSignWithGoogle.setOnClickListener(view ->{
            signInGoogle();

        });
        buttonLogin.setOnClickListener(view -> {
            signInWithGoogle(editTextEmail.getText().toString(),editTextPassword.getText().toString());

        });
        textViewForgotPassword.setOnClickListener(view -> {
            Intent intent =new Intent(LoginActivity.this,ForgotPasswordActivity.class);
            startActivity(intent);
        });



    }

    private void signInGoogle() {
        GoogleSignInOptions googleSignInOptions = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        signIn();
    }

    private void signIn() {
           Intent i = googleSignInClient.getSignInIntent();
           startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);
            firebaseSigninWithGoogle(task);
        }
    }

    private void firebaseSigninWithGoogle(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this,"signed in success",Toast.LENGTH_SHORT).show();
            Intent i =new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
            finish();
            firebaseGoogleAccount(account);
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this,"signed in not successful",Toast.LENGTH_SHORT).show();

        }
    }

    private void firebaseGoogleAccount(GoogleSignInAccount account) {

        AuthCredential authCredential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user =firebaseAuth.getCurrentUser();

                }

            }
        });
    }

    public void signInWithGoogle(String userEmail, String userPassword){
        progressBar.setVisibility(View.VISIBLE);
        buttonSignWithGoogle.setClickable(false);
        firebaseAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this,"Sign in successfully",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this,"Sign in not successfully",Toast.LENGTH_SHORT).show();


                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            Intent intent =new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();

        }
    }
}