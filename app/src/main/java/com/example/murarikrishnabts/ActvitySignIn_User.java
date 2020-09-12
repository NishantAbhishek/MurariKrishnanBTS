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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ActvitySignIn_User extends AppCompatActivity {
    private Button btnLogin,btnCancel;

    private EditText et_Email,et_Passowrd,et_Phone,et_Name;
    private FirebaseAuth mauth;
    private ProgressDialog progressDialog;
    private FirebaseUser currentuser;
    private DatabaseReference database;
    private String Phone,Name,Password,Email;
    private EditText Confirm_Password;
    private Button SN_Login;
    private String emaillg,passwordlg;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actvity_sign_in__user);

        Intilialize();
        buttons();
        Login();


    }


    private void Intilialize(){
        progressDialog = new ProgressDialog(this);
        mauth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.SN_Confirm);
        btnCancel = findViewById(R.id.SN_Cancel);
        et_Email = findViewById(R.id.SN_Email);
        et_Passowrd = findViewById(R.id.SN_Password);
        et_Phone = findViewById(R.id.SN_Phone);
        et_Name = findViewById(R.id.SN_Name);
        Confirm_Password  = findViewById(R.id.Confirm_Password);
        SN_Login = findViewById(R.id.SN_Login);
        currentuser = FirebaseAuth.getInstance().getCurrentUser();


    }


    private void  buttons(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        et_Email.setText("");
                        et_Phone.setText("");
                        et_Passowrd.setText("");
                        Confirm_Password.setText("");

                    }
                });
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = et_Email.getText().toString();
                Password = et_Passowrd.getText().toString();
                Phone = et_Phone.getText().toString();
                Name = et_Name.getText().toString();
                String STConfirm_Password = Confirm_Password.getText().toString();


                if(STConfirm_Password.equals(Password)){
                    if(!Phone.isEmpty() && !Password.isEmpty() && !Email.isEmpty()){
                        progressDialog.setTitle("Registering");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        Signin(Email,Password);
                    }else {
                        Toast.makeText(ActvitySignIn_User.this,"Please fill each box",Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Toast.makeText(ActvitySignIn_User.this,"Password do not match",Toast.LENGTH_LONG).show();

                }



                }
        });



    }


    private void Signin(String email,String password){
        mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ActvitySignIn_User.this,"Please verify your email address",Toast.LENGTH_SHORT).show();
                                RegisterPhone(Phone,Name);
                            }else {
                                Toast.makeText(ActvitySignIn_User.this,"Wrong email address",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }else {
                    progressDialog.dismiss();
                    Toast.makeText(ActvitySignIn_User.this,"Please try again",Toast.LENGTH_SHORT).show();

                }

            }
        });




    }
    private void RegisterPhone(String Phone,String Name){
        //firebase
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        String Current_Uid = currentuser.getUid();
        database = FirebaseDatabase.getInstance().getReference().child("Users").child(Current_Uid);

        HashMap<String, String> user = new HashMap<>();
        user.put("Name",Name);
        user.put("Phone",Phone);
        database.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ActvitySignIn_User.this,"Success registering name and phone",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(ActvitySignIn_User.this,"Filed to register info",Toast.LENGTH_SHORT).show();


                }
            }


        });


    }


    private void Login(){

             SN_Login.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     emaillg = et_Email.getText().toString();
                     passwordlg = et_Passowrd.getText().toString();

                     mauth.signInWithEmailAndPassword(emaillg, passwordlg).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isSuccessful()){
                                 if(mauth.getCurrentUser().isEmailVerified()){
                                     Intent in = new Intent(ActvitySignIn_User.this, MainActivity.class);
                                     startActivity(in);
                                 }else {
                                     Toast.makeText(ActvitySignIn_User.this,"Please verify your email",Toast.LENGTH_SHORT).show();
                                 }

                             }else {
                                 Toast.makeText(ActvitySignIn_User.this,"Wrong password",Toast.LENGTH_SHORT).show();


                             }

                         }
                     });






                 }
             });




    }








}
