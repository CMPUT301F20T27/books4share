// Zexin Cai is responsible for this part
// Just like the customList in lab

package com.example.books4share;
// Zexin Cai is responsible for this part
// Just like the customList in lab

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class OutgoingRequestList extends ArrayAdapter<OutgoingRequest> {

    private ArrayList<OutgoingRequest> orequests;
    private Context ocontext;

    public OutgoingRequestList(Context ocontext, ArrayList<OutgoingRequest> orequests) {
        super(ocontext, 0, orequests);
        this.orequests = orequests;
        this.ocontext = ocontext;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(ocontext).inflate(R.layout.outgoing_content, parent, false);
        }

        OutgoingRequest outgoingRequest = orequests.get(position);

        TextView bookName2 = view.findViewById(R.id.book_name2);
        TextView bookStatus = view.findViewById(R.id.book_status2);
        TextView owner = view.findViewById(R.id.owner1);
        TextView ownerName = view.findViewById(R.id.owner_name);

        bookName2.setText(outgoingRequest.getBookName());
        bookStatus.setText(outgoingRequest.getBookStatus());
        ownerName.setText(outgoingRequest.getOwnerName());

        return view;
    }
}
