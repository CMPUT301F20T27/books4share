package com.example.books4share;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.HashMap;

public class Signup extends AppCompatActivity {

    public static final String SignUpUser = "SignUp";
    public String TAG = "SignUp";

    private TextView SignTiltle;
    private TextView SignEmailText;
    private TextView SignPasswordText;
    private EditText SignupEmail;
    private EditText SignupPassword;
    private EditText SignupConfirm;
    private FloatingActionButton SignupExit;
    private Button LoginButton;

    FirebaseFirestore db;
    CollectionReference collectionReference;
    FirebaseAuth myAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        myAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users");


        Intent intent = getIntent();
        final String TAG = "SignUp";

        initView();

        TestValidEmail();

        SignupExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * This method is used to initialize the layout views.
     */

    public void initView() {
        SignTiltle = findViewById(R.id.signup_text);
        SignEmailText = findViewById(R.id.text_email2);
        SignPasswordText = findViewById((R.id.text_password2));

        SignupEmail = findViewById(R.id.editTextTextEmailAddress2);
        SignupPassword = findViewById(R.id.editTextTextPassword2);
        SignupConfirm = findViewById(R.id.editTextTextPassword3);
        SignupExit = findViewById(R.id.signupback);
        LoginButton = findViewById(R.id.signupButton);
    }

    /**
     * this method is used to test whether the input email is correct format
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        if (TextUtils.isEmpty(emailRegex)) {
            return false;
        } else {
            return strEmail.matches(emailRegex);
        }
    }


    /**
     * If the input email and password were all correct, this method will create a new user doc
     */
    public void TestValidEmail(){
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EnterEmail = SignupEmail.getText().toString();
                String EnterPassword = SignupPassword.getText().toString();
                String EnterConfirm = SignupConfirm.getText().toString();

                if (EnterEmail.length() == 0 || EnterPassword.length() == 0 || EnterConfirm.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Information Missing", Toast.LENGTH_SHORT).show();
                } else if (!(EnterPassword).equals(EnterConfirm)) {
                    Toast.makeText(getApplicationContext(), "Password is not same", Toast.LENGTH_SHORT).show();
                    SignupConfirm.setText("");
                    SignupPassword.setText("");
                }else if(EnterEmail.length() < 6){
                    Toast.makeText(getApplicationContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    SignupConfirm.setText("");
                    SignupPassword.setText("");
                } else if (isEmail(EnterEmail) == false) {
                    Toast.makeText(getApplicationContext(), "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    SignupEmail.setText("");

                } else {
                    myAuth.createUserWithEmailAndPassword(EnterEmail, EnterPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = myAuth.getCurrentUser();

                                        Intent intent = new Intent(Signup.this, SignInfo.class);
                                        startActivity(intent);

                                    }else{
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Signup.this, "Authentication failed. Email is already registered",
                                                Toast.LENGTH_SHORT).show();
                                        SignupConfirm.setText("");
                                        SignupPassword.setText("");
                                        SignupEmail.setText("");

                                    }

                                }
                            });
                }
            }
        });
    }
}