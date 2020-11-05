// Zexin Cai is responsible for this part
// Just like the customList in lab

package com.example.books4share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class IncomingRequestList extends ArrayAdapter<IncomingRequest> {

    private ArrayList<IncomingRequest> irequests;
    private Context icontext;

    public IncomingRequestList(Context icontext, ArrayList<IncomingRequest> irequests) {
        super(icontext, 0, irequests);
        this.irequests = irequests;
        this.icontext = icontext;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(icontext).inflate(R.layout.incoming_content, parent, false);
        }

        IncomingRequest incomingRequest = irequests.get(position);

        TextView bookName1 = view.findViewById(R.id.book_name1);
        TextView bookStatus = view.findViewById(R.id.book_status1);
        TextView requestFrom = view.findViewById(R.id.request_from);
        TextView borrowerName = view.findViewById(R.id.borrower_name);

        bookName1.setText(incomingRequest.getBookName());
        bookStatus.setText(incomingRequest.getBookStatus());
        borrowerName.setText(incomingRequest.getBorrowerName());

        return view;
    }
}
