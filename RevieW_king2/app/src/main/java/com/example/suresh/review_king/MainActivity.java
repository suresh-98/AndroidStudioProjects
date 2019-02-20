package com.example.suresh.review_king;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    private EditText useremail, conformpassword, userpassword;
    private Button regButton;
    private TextView UserLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        useremail = (EditText) findViewById(R.id.etxt1);
        userpassword = (EditText) findViewById(R.id.etxt2);
        conformpassword = (EditText) findViewById(R.id.etxt3);
        regButton = (Button) findViewById(R.id.button1);
        UserLogin = (TextView) findViewById(R.id.userlogin);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
  Intent intent = new Intent(MainActivity.this,RegistrationActivity.class);
  startActivity(intent);
            String s1 = useremail.getText().toString();
            String s2 =  userpassword.getText().toString();
            String s3 = conformpassword.getText().toString();
            if(s1.equals("")&& s2.equals("")&& s3.equals("")){
                Toast.makeText(getApplicationContext(),"the fields are empty",Toast.LENGTH_SHORT).show();
            }
            else {
                if(s2.equals(s3)){
                    Boolean chkemail = db.chkemail(s2);
                    if(chkemail==true){
                     Boolean insert = db.insert(s2,s3);
                     if(insert==true){
                         Toast.makeText(getApplicationContext(),"register succesfully",Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }
            }

        });
UserLogin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this,RegistrationActivity.class);
        startActivity(intent);

    }
});
    }

}