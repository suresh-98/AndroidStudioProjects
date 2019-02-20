package com.example.suresh.review_king;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrationActivity extends AppCompatActivity {

    private EditText Name;
    private  EditText Password;
    private TextView Info;
    private Button Login;
    private  int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Name = (EditText)findViewById(R.id.editText);
        Password =(EditText)findViewById(R.id.editText2);
        Info =(TextView)findViewById(R.id.editText3);
        Login =(Button)findViewById(R.id.login1);
        Info.setText("NO of attempts remaining:5");
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validate(Name.getText().toString(),Password.getText().toString());
                Intent intent = new Intent (RegistrationActivity.this,menuActivity.class);
                startActivity(intent);
            }
        });


    }
    private  void  Validate(String Name,String Password){
        if((Name == "Admin")&&(Password =="12345")){
            Intent intent= new Intent (RegistrationActivity.this,MainActivity.class);
            startActivity(intent);

        }else {
            counter--;

            Info.setText("NO of attempts Remining" + String.valueOf(counter));
            if(counter==0){
                Login.setEnabled(false);
            }


        }
    }
}
