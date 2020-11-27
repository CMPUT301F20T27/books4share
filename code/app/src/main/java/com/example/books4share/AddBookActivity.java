package com.example.books4share;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.books4share.Book;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddBookActivity extends AppCompatActivity {
    private EditText titleBox;
    private EditText authorBox;
    private EditText isbnBox;

    FirebaseFirestore db = FirebaseFirestore.getInstance();;
    final String TAG =  "Add";
    final String Flag = "Delete";
    private Book book;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book_layout);
        book = (Book) getIntent().getSerializableExtra("item");
        titleBox = findViewById(R.id.editText_title);
        authorBox =  findViewById(R.id.editText_author);
        isbnBox =  findViewById(R.id.editText_isbn);
        Button  btnAdd = findViewById(R.id.btn_add);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        if (book!=null){
            btnAdd.setText("Edit");
            mToolbar.setTitle("Edit Book");
            titleBox.setText(book.title);
            authorBox.setText(book.author);
            isbnBox.setText(book.isbn);
        }else{
            mToolbar.setTitle("Add Book");
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnAdd.    setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }



    private void submit() {
        if (book==null) {
            if (checkInput() == "") {
                String title = titleBox.getText().toString();
                String author = authorBox.getText().toString();
                String description = isbnBox.getText().toString();

                Book book = new Book();
                book.title = title;
                book.author = author;
                book.isbn = description;
                book.currentStatus = "Available";
                book.usersId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                book.image = "";
                db
                        .collection("Books")
                        .document()
                        .set(book)
                        .addOnSuccessListener(new OnSuccessListener<Void>(){

                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Data has been added successfully!");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d(TAG, "Data could not be added!" + e.toString());

                            }
                        });

                titleBox.setText("");
                authorBox.setText("");
                isbnBox.setText("");



            } else {
                new AlertDialog.Builder(AddBookActivity.this).setMessage(checkInput())
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        } else {
            String title = titleBox.getText().toString();
            String author = authorBox.getText().toString();
            String isbn = isbnBox.getText().toString();

            db  .collection("Books") .document(book.getId())
                    .update(
                            "title", title,
                            "author", author,
                            "isbn", isbn
                    )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Flag,"DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener(){

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(Flag,"Error deleting document", e);

                    }
                });
        }
    }


    /* Check if all inputs are valid*/
    public String checkInput() {
        if (titleBox.getText().toString().length() == 0)
            return "Title cannot be empty.";
        if (authorBox.getText().toString().length() == 0)
            return "Author cannot be empty.";
        if (isbnBox.getText().toString().length() == 0)
            return "ISBN cannot be empty.";
        return "";
    }


}
