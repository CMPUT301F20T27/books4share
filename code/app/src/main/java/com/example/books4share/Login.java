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

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class Login extends AppCompatActivity {

    public static final String LoginUserEmail = "LoginEmail";


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Users = db.collection("Users");

    private String TAG = "LoginActivity";

    private TextView LoginTiltle;
    private TextView LoginEmailText;
    private TextView LoginPasswordText;
    private EditText LoginEmail;
    private EditText LoginPassword;
    private FloatingActionButton loginExit;
    private Button Login;

    private FirebaseAuth myAuth = FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        Intent intent = getIntent();

        initView();
        LoginValidation();

        loginExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * This method is to initialize the views
     */
    public void initView(){
        LoginTiltle = findViewById(R.id.login_text);
        LoginEmailText = findViewById(R.id.text_email);
        LoginPasswordText = findViewById((R.id.text_password));
        LoginEmail = findViewById(R.id.editTextTextEmailAddress);
        LoginPassword = findViewById(R.id.editTextTextPassword);
        loginExit = findViewById(R.id.loginback);
        Login = findViewById(R.id.login);
    }

    /**
     * Test whether the input format is all correct
     */
    public void LoginValidation(){
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EnterEmail = LoginEmail.getText().toString();
                String EnterPassword = LoginPassword.getText().toString();

                if (EnterEmail.length() == 0 || EnterPassword.length() == 0){
                    Toast.makeText(getApplicationContext(), "Information Missing", Toast.LENGTH_SHORT).show();
                    LoginEmail.setText("");
                    LoginPassword.setText("");
                }else{
                    Login(EnterEmail, EnterPassword);
                }
            }
        });
    }

    /**
     * Login in through the Firebase Authorization
     * @param strEmail
     * @param strPassword
     */
    public void Login(String strEmail, String strPassword){
        myAuth.signInWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = myAuth.getCurrentUser();
                            Intent intent = new Intent(Login.this, Profile.class);
                            startActivity(intent);
                        }else{
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Email or Password is invalid. Failed to login",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}

