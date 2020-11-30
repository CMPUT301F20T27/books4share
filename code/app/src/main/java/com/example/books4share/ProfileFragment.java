// Shanshan Wei is responsible for this part
// This is to implement the user profile

package com.example.books4share;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.books4share.ProfileDialogFragment;
import com.example.books4share.ProfileUser;
import com.example.books4share.R;
import com.example.books4share.WelcomeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFragment extends Fragment {

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

    private String UserId;

    private View rootView;
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.profile_activity,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        showInfo();
        updateInfo();

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * this method is used to initialize the layout views
     */
    public void initView(){
        fullName = rootView.findViewById(R.id.ShowName);
        PhoneNum = rootView.findViewById(R.id.ShowPhone);
        AddressLoc = rootView.findViewById(R.id.ShowAddress);
        image =rootView. findViewById(R.id.HeadPhoto);
        Edit = rootView.findViewById(R.id.EditProfile);
        ProfileText = rootView.findViewById(R.id.MyProfileText);
        ProfileText.setText("My Profile");
        Logout =rootView. findViewById(R.id.btn_SignOut);


    }


    /**
     * this method is used to show the profile information from Cloud Firestore
     */

    public void showInfo() {

        FirebaseUser user = myAuth.getCurrentUser();
        if (user != null) {
            UserId = user.getUid();
            Users.document(UserId).collection("Profile").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                ProfileDialogFragment.newInstance(FragmentUser).show(getChildFragmentManager(), "Edit Profile");
                showInfo();
            }
        });
    }

}