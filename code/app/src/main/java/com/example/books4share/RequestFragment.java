package com.example.books4share;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RequestFragment extends DialogFragment {
    private TextView titleName;
    private TextView authorName;
    private TextView ISBNName;
    private TextView statusName;
    private RequestFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener{
        void onOkPressed(String message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RequestFragment.OnFragmentInteractionListener){
            listener = (RequestFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View aview = LayoutInflater.from(getActivity()).inflate(R.layout.search_book_detail, null);
        titleName=aview.findViewById(R.id.title_detail);
        authorName=aview.findViewById(R.id.author_detail);
        ISBNName=aview.findViewById(R.id.ISBN_detail);
        statusName=aview.findViewById(R.id.status_detail);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(aview)
                .setTitle("Info")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        listener.onOkPressed("request");
                    }
                }).create();
    }
}
