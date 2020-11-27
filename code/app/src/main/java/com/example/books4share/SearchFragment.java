// Dazhi Zhang is responsible for this part
// This is to implement search book and send a request

package com.example.books4share;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books4share.R;
import com.example.books4share.BookAdapter;
import com.example.books4share.Book;
import com.example.books4share.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchFragment extends Fragment  {

    RecyclerView bookList;
    Button btnSearch;
    BookAdapter bookAdapter;
    ArrayList<Book> bookDataList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //String[] status = {"Borrowed", "Requested", "Available", "Pending"};

    private View rootView;
    private EditText editText;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Put all books into an ArrayList
        bookList=rootView.findViewById(R.id.bookList);
        bookDataList=new ArrayList<>();
        bookList.setLayoutManager(new LinearLayoutManager(getActivity()));

        bookAdapter=new BookAdapter(getActivity(), bookDataList);
        bookList.setAdapter(bookAdapter);
         editText = (EditText)rootView. findViewById(R.id.edit_text);
        btnSearch = rootView.findViewById(R.id.btn_search);
        //Once the button is clicked, do search
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            //Detect if the search button is clicked
            public void onClick(View v) {
                //Get the content of search box


                search();
            }
        });

        //When a book item is clicked, send a request
        bookAdapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Book book = bookDataList.get(position);
                if (book.getCurrentStatus().equals("Available")) {
                    if (book.usersId!= FirebaseAuth.getInstance().getCurrentUser().getUid()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                         builder
                                .setTitle("Request")
                                .setNegativeButton("Cancel", null)
                                .setPositiveButton("Request", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                       // book.setRequestId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        Notification notification = new Notification();
                                        notification.bookId = book.getId();
                                        notification.borrowId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        notification.usersId = book.getUsersId();
                                        notification.status ="pending";
                                        book.setCurrentStatus("Requested");
                                        db.collection("Books").document(book.getId()).set(book);
                                        db.collection("Notification").document().set(notification);
                                        dialog.dismiss();
                                        search();
                                    }
                                }).create();
                        builder.show();
                    }else{
                        Toast.makeText(getActivity(), "This book is your own, please try another one", Toast.LENGTH_SHORT).show();
                    }

                } else if (book.getCurrentStatus().equals("Requested")) {
                    Toast.makeText(getActivity(), "You have requested this book, please try another one", Toast.LENGTH_SHORT).show();
                } else if (book.getCurrentStatus().equals("Borrowed")) {
                    Toast.makeText(getActivity(), "This book has been borrowed by others", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void search() {
        String inputText = editText.getText().toString();
        if (TextUtils.isEmpty(inputText)){
            return;
        }
        bookDataList.clear();
        db.collection("Books")
                .whereEqualTo("title", inputText)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Book book =    document.toObject(Book.class);
                                book.setId(document.getId());
                                if (book!=null){
                                    bookDataList.add(book);

                                }
                            }
                            bookAdapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                });
    }



}