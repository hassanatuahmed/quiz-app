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

import com.example.chatapp.databinding.ActivityUpdateProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {
    ActivityUpdateProfileBinding binding;
    CircleImageView circleImageView;
    Button buttonUpdateProfile;
    EditText editTextUserName;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    String image;
    Uri imageUri;
    boolean imageControl = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        circleImageView = binding.profileImage;
        buttonUpdateProfile = binding.buttonUpdateProfile;
        editTextUserName = binding.editTextUsername;
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage =FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseUser = firebaseAuth.getCurrentUser();

        getUserInfo();

        circleImageView.setOnClickListener(view -> {
            imageChooser();

        });

        buttonUpdateProfile.setOnClickListener(view ->{
            updateProfile();

        });
    }

    public void getUserInfo(){
        databaseReference.child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("userName").getValue().toString();
                String image = snapshot.child("image").getValue().toString();
                editTextUserName.setText(name);
                if(image.equals("null")){
                    circleImageView.setImageResource(R.drawable.button_background);

                }else{
                    Picasso.get().load(image).into(circleImageView);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void updateProfile(){
        String userName = editTextUserName.getText().toString();
        databaseReference.child("Users").child(firebaseAuth.getUid()).child("userName").setValue(userName);
        if(imageControl){
            UUID randomID = UUID.randomUUID();
            String imageName = "images/"+randomID+".jpg";
            storageReference.child(imageName).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    StorageReference myStorageRef = firebaseStorage.getReference(imageName);
                    myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String filePath = uri.toString();
                            databaseReference.child("User").child(firebaseAuth.getUid()).child("image").setValue(filePath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(UpdateProfileActivity.this,"Write to database is successful",Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdateProfileActivity.this,"Write to database is not successful",Toast.LENGTH_SHORT).show();


                                }
                            });



                        }
                    });

                }
            });

        }else{
            databaseReference.child("User").child(firebaseAuth.getUid()).child("image").setValue(image);
        }
        Intent intent= new Intent(UpdateProfileActivity.this,HomeActivity.class);

        intent.putExtra("userName",userName);
        startActivity(intent);
        finish();

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