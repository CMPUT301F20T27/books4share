package com.example.books4share;
/**
 * @author Wei Shanshan
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.books4share.fragment.ProfileFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignInfo extends AppCompatActivity {

    public static final String SignUpInfo = "SignInfo";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Users = db.collection("Users");
    FirebaseAuth myAuth = FirebaseAuth.getInstance();



    private ImageView photo;
    private TextView title;
    private TextView NameText;
    private EditText inputName;
    private TextView phoneText;
    private EditText inputPhone;
    private TextView AddressText;
    private EditText inputAddress;
    private Button Confirm;
    private Button Clear;
    private String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_info);

        Intent intent = getIntent();

        initView();
        inputValidation();


    }
    /**
     * This method is to initialize the layout views
     */
    public void initView(){
        photo = findViewById(R.id.imageView2);
        title = findViewById(R.id.fillin);
        NameText = findViewById(R.id.text_fullname);
        inputName = findViewById(R.id.editfullname);
        phoneText = findViewById(R.id.text_phone);
        inputPhone = findViewById(R.id.editTextPhone);
        AddressText = findViewById(R.id.text_address);
        inputAddress = findViewById(R.id.editTextAddress);
        Confirm = findViewById(R.id.ConfirmButton);
        Clear = findViewById(R.id.ClearButton);
    }
    /**
     * This method is used to write the profile information on Firestore Cloud
     * @param strName
     * @param strPhone
     * @param strAddress
     */
    public void updateProfile(String strName, String strPhone, String strAddress){
        FirebaseUser user = myAuth.getCurrentUser();
        UserId = user.getUid();
        HashMap <String, String> ProfileData = new HashMap<>();
        ProfileData.put("Name", strName);
        ProfileData.put("Phone", strPhone);
        ProfileData.put("Address", strAddress);
        Users.document(UserId).collection("Profile")
                .document("Information")
                .set(ProfileData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Profile Update", "DocumentSnapshot successfully written!");
                Intent intent = new Intent(SignInfo.this, MainActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Profile Update", "Error writing document", e);
            }
        });

    }
    /**
     * This method is used to check whether the input is right format
     */
    public void inputValidation(){
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = inputName.getText().toString();
                String Phone = inputPhone.getText().toString();
                String Address = inputAddress.getText().toString();

                if (Name.length() == 0 || Phone.length() == 0 || Address.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Profile Information Missing", Toast.LENGTH_SHORT);
                }
                else if (Name.length() != 0 && Phone.length() != 0 && Address.length() != 0) {
                    if (Phone.length() > 10 || Phone.length() < 10) {
                        Toast.makeText(getApplicationContext(), "Phone NUmber is invalid", Toast.LENGTH_SHORT);
                        inputPhone.setText("");
                    } else {
                        updateProfile(Name, Phone, Address);
                    }

                }

            }
        });
        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPhone.setText("");
                inputAddress.setText("");
                inputName.setText("");
            }
        });

    }

}