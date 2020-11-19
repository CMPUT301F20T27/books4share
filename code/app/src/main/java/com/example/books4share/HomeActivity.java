// Weijia Zhang and Philip Wang are responsible for this part
// This is to implement all functions about book

package com.example.books4share;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        AddBookFragment.OnFragmentInteractionListener {

    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;

    Spinner filterSpinner;
    ArrayAdapter<CharSequence> spinnerAdapter;
    BottomNavigationView bottomNavigation;

    FirebaseFirestore db = FirebaseFirestore.getInstance();;
    CollectionReference BookList = db.collection("BookList");
    FirebaseAuth BookAuth = FirebaseAuth.getInstance();

    final String TAG =  "Add";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Drop down menu for filters
        filterSpinner = (Spinner) findViewById(R.id.spinner_filter);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.status_array,
                android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(spinnerAdapter);
        filterSpinner.setOnItemSelectedListener(this);

        bookList = findViewById(R.id.my_book_list);
        bookDataList = new ArrayList<>();

        bookAdapter = new BookList(this, bookDataList);

        bookList.setAdapter(bookAdapter);


        Book somebook = new Book("The Power of Now",
                "Eckhart Tolle",
                "978-1-57731-152-2");

        Book somebook2 = new Book("The Power of Now",
                "Eckhart Tolle",
                "978-1-57731-152-2", Book.Status.ACCEPTED);

        Book somebook3 = new Book("The Power of Now",
                "Eckhart Tolle",
                "978-1-57731-152-2", Book.Status.REQUESTED);

        Book somebook4 = new Book("The Power of Now",
                "Eckhart Tolle",
                "978-1-57731-152-2", Book.Status.BORROWED);

        bookDataList.add(somebook);
        bookDataList.add(somebook2);
        bookDataList.add(somebook3);
        bookDataList.add(somebook4);

        Button addBook = findViewById(R.id.button_add_book);



        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddBookFragment(false).show(getSupportFragmentManager(), "ADD_BOOK");



            }
        });

        bookList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                new AddBookFragment(true, bookAdapter.getItem(pos)).show(getSupportFragmentManager(), "EDIT_BOOK");
                return true;
            }
        });
      
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent a = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(a);
                        break;

                    case R.id.navigation_explore:
                        Intent b = new Intent(HomeActivity.this, SearchActivity.class);
                        startActivity(b);
                        break;

                    case R.id.navigation_notification:
                        Intent c = new Intent(HomeActivity.this, NotificationActivity.class);
                        startActivity(c);
                        break;

                    case R.id.navigation_Me:
                        Intent d = new Intent(HomeActivity.this, Profile.class);
                        startActivity(d);
                        break;

                }

                return false;

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString().toUpperCase();
        bookAdapter.getFilter().filter(item);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onOkPressed(final Book newBook) {
        if (bookAdapter.getPosition(newBook) == -1){

                BookList.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                            FirebaseFirestoreException error) {
                        bookDataList.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                            Log.d(TAG, String.valueOf(doc.getData().get("Title")));

                            String title = doc.getId();
                            String author = (String) doc.getData().get("Author");
                            String description = (String) doc.getData().get("Isbn");

                            bookDataList.add(new Book(title,author,description));

                        }
                        bookAdapter.notifyDataSetChanged();

                    }
                });
            }



    }



    @Override
    public void onDeletePressed(Book book) {
        bookAdapter.remove(book);
        book = null;
    }


}
