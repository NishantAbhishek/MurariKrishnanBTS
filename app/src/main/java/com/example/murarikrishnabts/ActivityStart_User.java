package com.example.murarikrishnabts;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityStart_User extends AppCompatActivity  {

    private Button btn_Need_New_Account;
    private Button btn_ALready_Have_Account;
    private FirebaseUser currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start__user);
        btn_Need_New_Account = findViewById(R.id.ST_new_Account);
        btn_ALready_Have_Account = findViewById(R.id.ST_have_account);
        Button();

    }

    private void Button(){

        btn_ALready_Have_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ActivityStart_User.this,ActivityLogin_User.class);
                startActivity(in);

            }
        });

        btn_Need_New_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ActivityStart_User.this,ActvitySignIn_User.class);
                startActivity(in);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentuser==null){

            Toast.makeText(ActivityStart_User.this,"No user",Toast.LENGTH_SHORT).show();

        }else if(currentuser!=null  && currentuser.isEmailVerified()){
            Toast.makeText(ActivityStart_User.this,"User",Toast.LENGTH_SHORT).show();
            Intent in = new Intent(ActivityStart_User.this,MainActivity.class);
            startActivity(in);
        }





    }

}
