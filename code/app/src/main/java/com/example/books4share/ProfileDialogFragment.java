package com.example.books4share;
/**
 * @author Wei Shanshan
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;

/**
 * This part allows users to edit their profile
 */
public class ProfileDialogFragment extends DialogFragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Users = db.collection("Users");
    FirebaseAuth myAuth = FirebaseAuth.getInstance();
    FirebaseUser user = myAuth.getCurrentUser();
    String UserId = user.getUid();

    private EditText name;
    private EditText phone;
    private EditText address;
    private ProfileUser Puser;
    /**
     * This method is used to get the object bundle data from the activity
     * @param Puser
     * @return
     */
    public static ProfileDialogFragment newInstance(ProfileUser Puser){//pass data from fragment to activity
        Bundle args = new Bundle();
        args.putSerializable("Profile", Puser);
        ProfileDialogFragment fragment = new ProfileDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * This method is used to build the fragment
     * @param savedInstanceState
     * @return this will return a fragment with object data got from the activity filled in the fragment edittext place.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {  //create a dialog fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.profile_fragment, null); // inflate the layout for this fragment
        name = view.findViewById(R.id.EditName);
        phone = view.findViewById(R.id.EditPhone);
        address = view.findViewById(R.id.EditAddress);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle arguments=getArguments();
        if(arguments != null){
            Puser = (ProfileUser) arguments.getSerializable("Profile");
            name.setText(Puser.getUserName());
            phone.setText(Puser.getPhone());
            address.setText(Puser.getAddress());
        }

        return builder
                .setView(view)
                .setTitle("Edit Information")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String NewName = name.getText().toString();
                                String NewPhone = phone.getText().toString();
                                String NewAddress = address.getText().toString();

                                if((NewName.isEmpty() && NewAddress.isEmpty() && NewPhone.isEmpty())) {
                                    dismiss();
                                }else if(NewPhone.length()!= 10){
                                    name.setText(Puser.getPhone());
                                    HashMap<String, String> Data = new HashMap<>();
                                    Data.put("Name", NewName);
                                    //if the new Phone Number is not 10 digits, it will just keep the former one
                                    Data.put("Phone", Puser.getPhone());
                                    Data.put("Address", NewAddress);
                                    Users.document(UserId).set(Data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Profile Fragment", "DocumentSnapshot successfully written!");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Profile Update", "Error writing document", e);
                                        }
                                    });
                                }else{
                                    HashMap<String, String> Data = new HashMap<>();
                                    Data.put("Name", NewName);
                                    Data.put("Phone", NewPhone);
                                    Data.put("Address", NewAddress);
                                    Users.document(UserId).collection("Profile").document("Information").set(Data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Profile Fragment", "DocumentSnapshot successfully written!");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Profile Update", "Error writing document", e);
                                        }
                                    });
                                }
                            }
                        }

                ).create();

    }
}
