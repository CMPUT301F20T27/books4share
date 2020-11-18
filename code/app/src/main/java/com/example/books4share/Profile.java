// Shanshan Wei is responsible for this part
// This is to implement the user profile

package com.example.books4share;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static com.example.books4share.SignInfo.SignUpInfo;
public class Profile extends AppCompatActivity{

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Users = db.collection("Users");
    FirebaseAuth myAuth = FirebaseAuth.getInstance();

    private TextView fullName;
    private TextView PhoneNum;
    private TextView AddressLoc;
    private ImageView image;
    private TextView ProfileText;
    Button Edit;
    Button Logout;

    private ProfileUser FragmentUser = new ProfileUser();

    Button home;
    Button notif;
    Button explore;

    private String UserId;

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        Intent intent = getIntent();

        initView();
        showInfo();
        updateInfo();

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Profile.this, MainActivity.class);
                startActivity(intent);
            }
        });

        home = findViewById(R.id.Home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        explore = findViewById(R.id.Explore);
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        notif = findViewById(R.id.Notification);
        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent a = new Intent(Profile.this, HomeActivity.class);
                        startActivity(a);
                        break;

                    case R.id.navigation_explore:
                        Intent b = new Intent(Profile.this, SearchActivity.class);
                        startActivity(b);
                        break;

                    case R.id.navigation_notification:
                        Intent c = new Intent(Profile.this, NotificationActivity.class);
                        startActivity(c);
                        break;

                    case R.id.navigation_Me:
                        Intent d = new Intent(Profile.this, Profile.class);
                        startActivity(d);
                        break;

                }

                return false;

            }
        });
    }

    /**
     * this method is used to initialize the layout views
     */
    public void initView(){
        fullName = findViewById(R.id.ShowName);
        PhoneNum = findViewById(R.id.ShowPhone);
        AddressLoc = findViewById(R.id.ShowAddress);
        image = findViewById(R.id.HeadPhoto);
        Edit = findViewById(R.id.EditProfile);
        ProfileText = findViewById(R.id.MyProfileText);
        ProfileText.setText("My Profile");
        Logout = findViewById(R.id.btn_SignOut);
        /*
        home = findViewById(R.id.Home);
        notif = findViewById(R.id.Notification);
        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        explore = findViewById(R.id.Explore);
        */
    }


    /**
     * this method is used to show the profile information from Cloud Firestore
     */

    public void showInfo() {

       FirebaseUser user = myAuth.getCurrentUser();
       if (user != null) {
           UserId = user.getUid();
           Users.addSnapshotListener(new EventListener<QuerySnapshot>() {
               @Override
               public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                   for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                   {
                       Log.d("Success", String.valueOf(doc.getData().get("Name")));
                       String Name = (String) doc.getData().get("Name");
                       String Phone = (String) doc.getData().get("Phone");
                       String Address = (String) doc.getData().get("Address");
                       fullName.setText(Name);
                       PhoneNum.setText(Phone);
                       AddressLoc.setText(Address);

                   }
               }
           });
       }
    }


    /**
     * This method is used to set a click listener on Edit button.
     * If the user click on the button. The activity will call a Fragment to prompt user input new profile information
     */

    public void updateInfo(){
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = fullName.getText().toString();
                String UserPhone = PhoneNum.getText().toString();
                String UserAddress = AddressLoc.getText().toString();
                FragmentUser.setUserName(Username);
                FragmentUser.setPhone(UserPhone);
                FragmentUser.setAddress(UserAddress);
                ProfileFragment.newInstance(FragmentUser).show(getSupportFragmentManager(), "Edit Profile");
                showInfo();
            }
        });
    }








}