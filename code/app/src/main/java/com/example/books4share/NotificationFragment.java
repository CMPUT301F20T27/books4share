// Zexin Cai is responsible for this part
// This is to implement notification activities
// It includes a list of incoming request and a list of outgoing request
// If a status of incoming request is pending, you can click to enter the next activity.
// If a status of outgoing request is accepted, you can click to enter the detail page.

package com.example.books4share;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books4share.AcceptActivity;
import com.example.books4share.R;
import com.example.books4share.ViewRequestActivity;
import com.example.books4share.NotificationAdapter;
import com.example.books4share.Book;
import com.example.books4share.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    RecyclerView incomingList;
    RecyclerView outgoingList;
    NotificationAdapter inAdapter;
    ArrayList<Notification> inDataList;
    NotificationAdapter outAdapter;
    ArrayList<Notification> outDataList;

    private View rootView;

    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notification,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        incomingList =rootView. findViewById(R.id.incoming_list);
        outgoingList =rootView. findViewById(R.id.outgoing_list);


        inDataList = new ArrayList<>();
        outDataList = new ArrayList<>();

        incomingList.setLayoutManager(new LinearLayoutManager(getContext()));
        outgoingList.setLayoutManager(new LinearLayoutManager(getContext()));
        inAdapter = new NotificationAdapter(getActivity(), inDataList);
        outAdapter = new NotificationAdapter(getActivity(), outDataList);

        incomingList.setAdapter(inAdapter);
        outgoingList.setAdapter(outAdapter);

        inAdapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), AcceptActivity.class);
                startActivity(intent);
            }
        });
        outAdapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), ViewRequestActivity.class);
                startActivity(intent);
            }
        });

        initInData();
        initOutData();
    }
    private void initOutData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();;
        CollectionReference bookRef = db.collection("Notification");
        bookRef.whereEqualTo("borrowId", FirebaseAuth.getInstance().getCurrentUser().getUid())
        .addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (QueryDocumentSnapshot document : value) {
                    Notification book =    document.toObject(Notification.class);
                    book.setId(document.getId());
                    if (book!=null){
                        outDataList.add(book);
                    }
                }
                outAdapter.notifyDataSetChanged();
            }
        });

    }
    private void initInData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();;
        CollectionReference bookRef = db.collection("Notification");
        bookRef.whereEqualTo("usersId", FirebaseAuth.getInstance().getCurrentUser().getUid())
    .addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (QueryDocumentSnapshot document : value) {
                    Notification book =    document.toObject(Notification.class);
                    book.setId(document.getId());
                    if (book!=null){
                        inDataList.add(book);
                    }
                }

                inAdapter.notifyDataSetChanged();
            }
        });

    }

}
