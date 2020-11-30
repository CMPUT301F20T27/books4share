/**
 * @author Wei Shanshan
 */

package com.example.books4share;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.books4share.fragment.NotificationFragment;


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



        MainLogin.setOnClickListener(new View.OnClickListener() {
            /**
             * switch to Login page
             * @param v
             */
            @Override
            public void onClick(View v) {

                Intent LoginAct = new Intent(WelcomeActivity.this, Login.class);
                startActivityForResult(LoginAct, 1);
            }
        });

        MainSignup.setOnClickListener(new View.OnClickListener() {
            /**
             * switch to sign up page
             * @param v
             */
            @Override
            public void onClick(View v) {

                Intent SignupAct = new Intent(WelcomeActivity.this, Signup.class);
                startActivity(SignupAct);
            }
        });

    }
}
