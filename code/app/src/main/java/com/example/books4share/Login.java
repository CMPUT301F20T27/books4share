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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.Map;

public class Login extends AppCompatActivity {


    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        Intent intent = getIntent();

        final String TAG = "LoginActivity";

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference Users = db.collection("Users");


        TextView LoginTiltle = findViewById(R.id.login_text);
        TextView LoginEmailText = findViewById(R.id.text_email);
        TextView LoginPasswordText = findViewById((R.id.text_password));

        final EditText LoginEmail = findViewById(R.id.editTextTextEmailAddress);
        final EditText LoginPassword = findViewById(R.id.editTextTextPassword);

        FloatingActionButton loginExit = findViewById(R.id.loginback);
        Button Login = findViewById(R.id.login);



        Login.setOnClickListener(new View.OnClickListener() {
            int flag;
            @Override
            public void onClick(View v) {
                String EnterEmail = LoginEmail.getText().toString();
                String EnterPassword = LoginPassword.getText().toString();
                if (EnterEmail.length() == 0 || EnterPassword.length() == 0){
                    Toast.makeText(getApplicationContext(), "Information Missing", Toast.LENGTH_SHORT).show();
                }else{
                    Users.document(EnterEmail).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                            flag = 1;
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });

                    if (flag == 1){

                        Intent MainPage = new Intent(Login.this, NotificationActivity.class);
                        startActivityForResult(MainPage, 3);

                    }


                }

            }
        });
        loginExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

