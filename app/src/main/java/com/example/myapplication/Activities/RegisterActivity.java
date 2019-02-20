package com.example.myapplication.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    ImageView ImgUserPhoto;
    static int Preqcode =1 ;
    static int REQUESCODE =1;
    Uri pickedImgurl;
    private Button regbtn;
    private EditText userName,userPassword,userPassword2,userMail;
    private ProgressBar Loadingprogress;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.regName);
        userMail = findViewById(R.id.regMail);
        userPassword =findViewById(R.id.regPassword);
        userPassword2 =findViewById(R.id.regpassword2);
        regbtn = findViewById(R.id.button);
        ImgUserPhoto = findViewById(R.id.user_reg_photo);
        Loadingprogress=findViewById(R.id.progressBar);
        Loadingprogress.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regbtn.setVisibility(View.INVISIBLE);
                Loadingprogress.setVisibility(View.VISIBLE);
                final String email = userMail.getText().toString();
                final String name = userName.getText().toString();
                final String password=userPassword.getText().toString();
                final String password2 = userPassword2.getText().toString();

                if(email.isEmpty() ||name.isEmpty() || password.isEmpty()||  !password.equals(password2) ){

                    SHOWMESSAGE("please verify all fields");
                    regbtn.setVisibility(View.VISIBLE);
                    Loadingprogress.setVisibility(View.INVISIBLE);
                }

                else {

                    CreateUserAcoount(name,email,password);
                }

            }
        });

        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22)
                {

                    checkAndRequestforpermission();
                }

            else
                {
                    openGallery();
                }
            }
        });
    }

    private void CreateUserAcoount(final String name, String email, String password) {

     mAuth.createUserWithEmailAndPassword(email,password)
             .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful()){
                         SHOWMESSAGE("Account Create Success");

                         updateUserInfor(name,pickedImgurl,mAuth.getCurrentUser());
                     }
                     else {
                         SHOWMESSAGE("Account craete Failed"+task.getException().getMessage());
                         regbtn.setVisibility(View.VISIBLE);
                         Loadingprogress.setVisibility(View.INVISIBLE);
                     }
                 }
             });
    }

    private void updateUserInfor(final String name, Uri pickedImgurl, final FirebaseUser currentUser) {

        StorageReference mstorage = FirebaseStorage.getInstance().getReference().child("use_photo");
        final StorageReference imagefilpath = mstorage.child(pickedImgurl.getLastPathSegment());
        imagefilpath.putFile(pickedImgurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                imagefilpath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        UserProfileChangeRequest  profileupdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileupdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isComplete()){
                                            SHOWMESSAGE("Register  SuccessFull");
                                            updateUi();
                                        }

                                    }
                                });
                    }
                });
            }
        });
    }

    private void updateUi() {
        Intent homeactivity = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(homeactivity);
        finish();
    }


    private void SHOWMESSAGE(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT ).show();

    }

    private void openGallery() {

        Intent gallaryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        gallaryIntent.setType("image/*");
        startActivityForResult(gallaryIntent,REQUESCODE);
    }

    private void checkAndRequestforpermission() {
        if(ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
           != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(RegisterActivity.this,"Plaese accept for required permission",Toast.LENGTH_SHORT).show();
            }

            else{
                ActivityCompat.requestPermissions(RegisterActivity.this,
                                                              new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                Preqcode);
            }
        }
         openGallery();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESCODE && data != null);

        pickedImgurl = data.getData();
        ImgUserPhoto.setImageURI(pickedImgurl);
    }
}

