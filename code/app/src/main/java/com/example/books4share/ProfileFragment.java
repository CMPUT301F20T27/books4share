package com.example.books4share;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends DialogFragment {



    private OnFragmentInteractionListener listener;

    private EditText name;
    private EditText phone;
    private EditText address;


    public interface OnFragmentInteractionListener {
        void onOkPressed(ProfileUser Puser);

    }

    @Override
    public void onAttach(Context context) { //first step of fragment. make connection to activity and get data from activity
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {  //create a dialog fragment


        View view = LayoutInflater.from(getActivity()).inflate(R.layout.profile_fragment, null); // inflate the layout for this fragment

        name = view.findViewById(R.id.EditName);
        phone = view.findViewById(R.id.EditPhone);
        address = view.findViewById(R.id.EditAddress);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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

                                if((NewName.isEmpty() && NewAddress.isEmpty() && NewPhone.isEmpty())){
                                    listener = null;
                                }else{
                                    listener.onOkPressed(new ProfileUser(NewName, NewPhone, NewAddress));


                                }}
                        }

                ).create();

    }
}
