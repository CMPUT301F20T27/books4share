package com.example.books4share;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;



public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    TextView ProjectName;
    TextView Team;
    TextView Sentence;
    Button MainLogin;
    Button MainSignup;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button newButton = findViewById(R.id.new_button);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
        ProjectName = findViewById(R.id.project_name);
        Team = findViewById(R.id.team_name);
        Sentence = findViewById(R.id.statement);
        MainLogin = findViewById(R.id.login_button);
        MainSignup = findViewById(R.id.signup_button);


        db = FirebaseFirestore.getInstance();

        MainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent LoginAct = new Intent(MainActivity.this, Login.class);
                startActivityForResult(LoginAct, 1);
            }
        });

        MainSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent SignupAct = new Intent(MainActivity.this, Signup.class);
                startActivityForResult(SignupAct, 2);
            }
        });


    }
}