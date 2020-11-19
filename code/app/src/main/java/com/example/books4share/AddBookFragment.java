package com.example.books4share;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class AddBookFragment extends DialogFragment {
    private EditText titleBox;
    private EditText authorBox;
    private EditText isbnBox;
    private OnFragmentInteractionListener listener;
    private boolean editing;
    private Book bookEditing;

    ArrayList<Book> NewDataList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();;
    CollectionReference BookList = db.collection("BookList");
    final String TAG =  "Add";
    final String Flag = "Delete";


    public AddBookFragment(boolean editing) {
        super();
        this.editing = editing;
    }

    public AddBookFragment(boolean editing, Book book) {
        super();
        this.editing = editing;
        bookEditing = book;
    }


    public interface OnFragmentInteractionListener {
        void onOkPressed(Book newBook);
        void onDeletePressed(Book book);
        Book getChosenBook();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
            listener = (OnFragmentInteractionListener) context;
        else
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_book_layout, null);
        titleBox = view.findViewById(R.id.editText_title);
        authorBox =  view.findViewById(R.id.editText_author);
        isbnBox =  view.findViewById(R.id.editText_isbn);


        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (!editing) {
            /* Adding a new book*/
            return builder
                    .setView(view)
                    .setTitle("Add a book")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (checkInput() == "") {
                                String title = titleBox.getText().toString();
                                String author = authorBox.getText().toString();
                                String description = isbnBox.getText().toString();

                                HashMap<String, String> BookData = new HashMap<>();

                                BookData.put("Title",title);
                                BookData.put("Author",author);
                                BookData.put("Isbn",description);

                                BookList
                                        .document(title)
                                        .set(BookData)
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

                                listener.onOkPressed(new Book(title,author,description));

                                titleBox.setText("");
                                authorBox.setText("");
                                isbnBox.setText("");



                            } else {
                                new AlertDialog.Builder(getContext()).setMessage(checkInput())
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).show();
                            }
                        }
                    }).create();
        } else {
            /* Editing an existing book*/
            titleBox.setText(bookEditing.getTitle());
            authorBox.setText(bookEditing.getAuthor());
            isbnBox.setText(bookEditing.getIsbn());

            return builder
                    .setView(view)
                    .setTitle("Edit this book")
                    .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Book deleteBook = listener.getChosenBook();

                            BookList
                                    .document(deleteBook.getTitle())
                                    .delete()
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


                            listener.onDeletePressed(deleteBook);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (checkInput() == "") {
                                bookEditing.setTitle(titleBox.getText().toString());
                                bookEditing.setIsbn(isbnBox.getText().toString());
                                bookEditing.setAuthor(authorBox.getText().toString());

                                listener.onOkPressed(bookEditing);
                            } else {
                                new AlertDialog.Builder(getContext()).setMessage(checkInput())
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).show();
                            }
                        }
                    }).create();
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
