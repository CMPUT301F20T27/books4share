package com.example.books4share;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ScanDetailActivity extends AppCompatActivity {

    private Notification notification;

    private ImageView iv_logo;
    private TextView tv_author;
    private TextView tv_isbn;
    private TextView tv_title;
    Button btnBorrowed;
    Button btnAvailable;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Intent intent = getIntent();
        notification = (Notification) intent.getSerializableExtra("item");
        initView();
        getBookInfoByBookId();
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
                            Book book = document.toObject(Book.class);
                            book.setId(document.getId());
                            if(book!=null){
                                Glide.with(ScanDetailActivity.this).load(book.image).into(iv_logo);

                                tv_author.setText(book.author);
                                tv_isbn.setText(book.isbn);
                                tv_title.setText(book.title);
                            }
                        } else {

                        }
                    }
                });
    }

    private void initView() {

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Book Detail");
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
        tv_title = findViewById(R.id.Borrowed);
        btnBorrowed =  findViewById(R.id.Borrowed);
        btnAvailable =  findViewById(R.id.Available);


        btnBorrowed  .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("Books").document(notification.getBookId()).update(
                        "currentStatus", "Borrowed"
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });
            }
        });
        btnAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Books").document(notification.getBookId()).update(
                        "currentStatus", "Available"
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });
            }
        });


    }




}