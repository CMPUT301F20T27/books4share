// Shanshan Wei is responsible for this part
// This is to implement activities about Log in and Sign up

package com.example.books4share;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    TextView ProjectName;
    TextView Team;
    TextView Sentence;
    Button MainLogin;
    Button MainSignup;
    Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        ProjectName = findViewById(R.id.project_name);
        Team = findViewById(R.id.team_name);
        Sentence = findViewById(R.id.statement);
        MainLogin = findViewById(R.id.login_button);
        MainSignup = findViewById(R.id.signup_button);

        testButton = findViewById(R.id.new_button);

        MainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent LoginAct = new Intent(WelcomeActivity.this, Login.class);
                startActivityForResult(LoginAct, 1);
            }
        });

        MainSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent SignupAct = new Intent(WelcomeActivity.this, Signup.class);
                startActivity(SignupAct);
            }
        });

    }
}
