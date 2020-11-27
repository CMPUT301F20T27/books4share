// Zexin Cai is responsible for this part
// This is to implement notification activities
// It includes a list of incoming request and a list of outgoing request
// If a status of incoming request is pending, you can click to enter the next activity.
// If a status of outgoing request is accepted, you can click to enter the detail page.

package com.example.books4share;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    ListView incomingList;
    ListView outgoingList;
    ArrayAdapter<IncomingRequest> inAdapter;
    ArrayList<IncomingRequest> inDataList;
    ArrayAdapter<OutgoingRequest> outAdapter;
    ArrayList<OutgoingRequest> outDataList;

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        incomingList = findViewById(R.id.incoming_list);
        outgoingList = findViewById(R.id.outgoing_list);

        String []bookName = {"book1", "book2", "book3", "book4", "book5"};
        String []borrowerName = {"borrower1", "borrower2", "borrower3", "borrower4", "borrower5"};
        String []bookStatus = {"Pending", "Accepted", "Declined", "Pending", "Accepted"};
        String []ownerName = {"owner1", "owner2", "owner3", "owner4", "owner5"};

        inDataList = new ArrayList<>();
        outDataList = new ArrayList<>();

        for (int i = 0; i < bookName.length; i++) {
            inDataList.add((new IncomingRequest(bookName[i], bookStatus[i], borrowerName[i])));
            outDataList.add((new OutgoingRequest(bookName[i], bookStatus[i], ownerName[i])));
        }

        inAdapter = new IncomingRequestList(this, inDataList);
        outAdapter = new OutgoingRequestList(this, outDataList);

        incomingList.setAdapter(inAdapter);
        outgoingList.setAdapter(outAdapter);



        incomingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = view.findViewById(R.id.book_status1);
                String status = text.getText().toString();

                if (status.equals("Pending")) {
                    Intent intent = new Intent(NotificationActivity.this, AcceptActivity.class);
                    startActivity(intent);
                }
            }
        });

        outgoingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = view.findViewById(R.id.book_status2);
                String status = text.getText().toString();

                if (status.equals("Accepted")) {
                    Intent intent = new Intent(NotificationActivity.this, ViewRequestActivity.class);
                    startActivity(intent);
                }
            }
        });

        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent a = new Intent(NotificationActivity.this, HomeActivity.class);
                        startActivity(a);
                        break;

                    case R.id.navigation_explore:
                        Intent b = new Intent(NotificationActivity.this, SearchActivity.class);
                        startActivity(b);
                        break;

                    case R.id.navigation_notification:
                        Intent c = new Intent(NotificationActivity.this, NotificationActivity.class);
                        startActivity(c);
                        break;

                    case R.id.navigation_Me:
                        Intent d = new Intent(NotificationActivity.this, ProfileFragment.class);
                        startActivity(d);
                        break;

                }

                return false;

            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();;
        CollectionReference BookList = db.collection("BookList");
        final String TAG =  "Requesting";

        //DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();



    }
}
