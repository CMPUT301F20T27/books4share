// Zexin Cai is responsible for this part
// This is to show detailed information about the user's accepted request.

package com.example.books4share;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.books4share.Book;
import com.example.books4share.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewRequestActivity extends AppCompatActivity {

    private Notification notification;

    private ImageView iv_logo;
    private TextView tv_author;
    private TextView tv_isbn;
    private TextView tv_title;
    private TextView tvOwnName;
    private TextView tvLocation;
    private ProgressDialog mProgressDialog;
    private Book book;
    FirebaseFirestore db = FirebaseFirestore.getInstance();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);
        mProgressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        notification = (Notification) intent.getSerializableExtra("item");
        initView();


    }

    private void initView() {

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("View request");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_logo = findViewById(R.id.iv_logo);
        tv_author = findViewById(R.id.tv_author);
        tv_isbn = findViewById(R.id.tv_isbn);
        tv_title = findViewById(R.id.tv_title);
        tvOwnName = findViewById(R.id.tvOwnName);
        tvLocation = findViewById(R.id.tvLocation);
        TextView tvDetail = findViewById(R.id.tv_detail);
        if (notification.book!=null){
            book = notification.book;
            setBookInfo();
            getBookOwnInfo();
        }else{
            getBookInfoByBookId();
        }
        if (notification.receiveLocation!=null){
            tvLocation.setVisibility(View.VISIBLE);
            tvLocation.setText(notification.receiveLocation);
        }
        tvDetail.setText(notification.status);


    }

    private void getBookInfoByBookId() {

        DocumentReference docRef = db.collection("Books").document(notification.bookId);
        docRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            book =   document.toObject(Book.class);
                            book.setId(document.getId());
                            getBookOwnInfo();
                            if(book!=null){
                                setBookInfo();
                            }
                        } else {

                        }
                    }
                });
    }

    private void getBookOwnInfo() {

        db.collection("Users").document(book.usersId).collection("Profile")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                            Log.d("Success", String.valueOf(doc.getData().get("Name")));
                            String Name = (String) doc.getData().get("Name");
                            tvOwnName.setText(Name);
                        }
                    }
                });
    }

    private void setBookInfo(){
        Glide.with(this).load(book.image).into(iv_logo);

        tv_author.setText(book.author);
        tv_isbn.setText(book.isbn);
        tv_title.setText(book.title);
    }
}
