package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    CircleImageView circleImageView;
    Button buttonSignUp;
    EditText editTextEmail,editTextPassword,editTextUserName;
    Boolean imageControl= false;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        reference = database.getReference();
        firebaseStorage =FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        circleImageView = binding.profileImage;
        buttonSignUp = binding.buttonSignup;
        editTextEmail = binding.editTextEmail;
        editTextPassword = binding.editTextPassword;
        editTextUserName = binding.editUsername;

        circleImageView.setOnClickListener(view ->{
            imageChooser();

        });

        buttonSignUp.setOnClickListener(view ->{
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            String userName = editTextUserName.getText().toString();

            if(!email.equals("") && !password.equals("") && !userName.equals("")){
                signUp(email,password,userName);
            }

        });

    }


    private void signUp(String email, String password, String userName) {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    reference.child("Users").child(firebaseAuth.getUid()).child("userName").setValue(userName);
                    if(imageControl){
                        UUID randomID = UUID.randomUUID();

                        String imageName = "images/"+randomID+".jpg";
                        storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                StorageReference myStorageRef = firebaseStorage.getReference(imageName);
                                myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filePath = uri.toString();
                                        reference.child("Users").child(firebaseAuth.getUid()).child("image").setValue(filePath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(SignUpActivity.this,"Write to database is successful",Toast.LENGTH_SHORT).show();


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SignUpActivity.this,"Write to database is not successful",Toast.LENGTH_SHORT).show();

                                            }
                                        });


                                    }
                                });


                            }
                        });


                    }else{
                        reference.child("Users").child(firebaseAuth.getUid()).child("image").setValue("null");
                    }
                    Intent intent= new Intent(SignUpActivity.this,HomeActivity.class);

                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(SignUpActivity.this,"There is a problem creating an account",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void imageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data != null){
            imageUri =data.getData();
            Picasso.get().load(imageUri).into(circleImageView);
            imageControl = true;

        }
        else{
            imageControl = false;

        }
    }
}