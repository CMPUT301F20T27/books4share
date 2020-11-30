/**
 * @author Zexin Cai
 */
package com.example.books4share.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books4share.AcceptActivity;
import com.example.books4share.R;
import com.example.books4share.ViewRequestActivity;
import com.example.books4share.adapter.NotificationAdapter;
import com.example.books4share.bean.Book;
import com.example.books4share.bean.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
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

    /**
     * inflate the NotificationFragment layout view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notification,container,false);
        return rootView;
    }

    /**
     * initialize the view under the NotificationFragment
     * @param view
     * @param savedInstanceState
     */
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
            /**
             * switch to AcceptActivity which allow user to choose whether accept or declined
             * @param position
             */
            @Override
            public void onItemClick(int position) {
                if (inDataList.get(position).status.equals("pending")){
                    Intent intent = new Intent(getActivity(), AcceptActivity.class);
                    intent.putExtra("item",inDataList.get(position));
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(),inDataList.get(position).status,Toast.LENGTH_SHORT).show();
                }

            }
        });
        outAdapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            /**
             * switch to ViewRequestActivity
             */
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), ViewRequestActivity.class);
                intent.putExtra("item",outDataList.get(position));
                startActivity(intent);
            }
        });

        initInData();
        initOutData();
    }

    /**
     * Initialize outgoing notification
     */
    private void initOutData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();;
        CollectionReference bookRef = db.collection("Notification");
        bookRef.whereEqualTo("borrowId", FirebaseAuth.getInstance().getCurrentUser().getUid())
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                outDataList.clear();
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

    /**
     * initialize incoming notification
     */
    private void initInData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();;
        CollectionReference bookRef = db.collection("Notification");
        bookRef.whereEqualTo("usersId", FirebaseAuth.getInstance().getCurrentUser().getUid())
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                inDataList.clear();
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
