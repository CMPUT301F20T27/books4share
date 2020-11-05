package com.example.books4share;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);



        Intent intent = getIntent();

        final String TAG = "SignUp";

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");

        TextView SignTiltle = findViewById(R.id.signup_text);
        TextView SignEmailText = findViewById(R.id.text_email2);
        TextView SignPasswordText = findViewById((R.id.text_password2));

        final EditText SignupEmail = findViewById(R.id.editTextTextEmailAddress2);
        final EditText SignupPassword = findViewById(R.id.editTextTextPassword2);
        final EditText SignupConfirm = findViewById(R.id.editTextTextPassword3);


        FloatingActionButton SignupExit = findViewById(R.id.signupback);
        Button LoginButton = findViewById(R.id.signupButton);


        SignupExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            int flag = 0;
            @Override
            public void onClick(View v) {
                final String EnterEmail = SignupEmail.getText().toString();
                final String EnterPassword = SignupPassword.getText().toString();
                final String EnterConfirm = SignupConfirm.getText().toString();
                if(EnterEmail.length()==0 || EnterPassword.length()==0 || EnterConfirm.length()==0){
                    Toast.makeText(getApplicationContext(), "Information Missing", Toast.LENGTH_SHORT).show();
                }else{
                    if (!(EnterPassword).equals(EnterConfirm) ){
                        Toast.makeText(getApplicationContext(), "Password is not same", Toast.LENGTH_SHORT).show();
                        SignupConfirm.setText("");
                        SignupPassword.setText("");

                    } else{
                        HashMap<String,String> Data = new HashMap<>();
                        if(EnterEmail.length()>0 && EnterPassword.length()>0 && EnterConfirm.length()>0){
                            Data.put("User Password", EnterPassword);
                            collectionReference
                                    .document(EnterEmail)
                                    .set(Data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    flag = 1;
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });

                        }
                        if (flag == 1){
                            Intent MainPage = new Intent(Signup.this, NotificationActivity.class);
                            startActivityForResult(MainPage, 4);
                        }



                    }
                }


            }
        });


    }
}
