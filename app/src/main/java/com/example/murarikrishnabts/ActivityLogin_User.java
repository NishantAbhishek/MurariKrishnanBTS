package com.example.murarikrishnabts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLogin_User extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnLogin,btnCancel;

    private EditText et_Email,et_Passowrd;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__user);
        Initialize();
        Buttons();
    }

    private void Initialize(){

        mAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.LG_Confirm);
        btnCancel = findViewById(R.id.LG_Cancel);

        et_Email = findViewById(R.id.LG_Email);
        et_Passowrd = findViewById(R.id.LG_Password);
        progressDialog = new ProgressDialog(this);



    }
    private void Buttons(){

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_Email.setText("");
                et_Passowrd.setText("");

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Password = et_Passowrd.getText().toString();
                String Email = et_Email.getText().toString();


                if(!Password.isEmpty() && !Email.isEmpty()){
                    progressDialog.setTitle("Processing");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    Login(Email,Password);

                }else {
                    Toast.makeText(ActivityLogin_User.this,"Please fill each box",Toast.LENGTH_SHORT).show();
                }







            }
        });




    }
    private void Login(String email,String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(mAuth.getCurrentUser().isEmailVerified()){
                        progressDialog.dismiss();
                        Intent in = new Intent(ActivityLogin_User.this, MainActivity.class);
                        startActivity(in);
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(ActivityLogin_User.this,"Please verify your email",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(ActivityLogin_User.this,"Wrong password",Toast.LENGTH_SHORT).show();


                }

            }
        });


    }













}
