// Dazhi Zhang is responsible for this part
// This is to implement search book and send a request

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements RequestFragment.OnFragmentInteractionListener {

    private static final String TAG ="dazhi" ;
    ListView bookList;
    Button btnSearch;
    ArrayAdapter<SearchBooks> bookAdapter;
    ArrayList<SearchBooks> bookDataList;

    ArrayList<String> tt=new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();;
    CollectionReference BookList = db.collection("BookList");

    String[] titles ={"Harry"};
    String[] authors = {"no"};
    String[] ISBNs = {"123"};
    String[] status = {"available"};

    String word = "";

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Add a new book to cloud
        HashMap<String, String> BookData = new HashMap<>();
        BookData.put("Title","test1");
        BookData.put("Author","test1");
        BookData.put("Isbn","test1");
        BookList
                .document("test1")
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
        //set the book as title
        db.collection("BookList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(DocumentSnapshot document:task.getResult()){
                                tt.add(document.getId());
                                Toast.makeText(SearchActivity.this, tt.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        //Put all books into an ArrayList
        bookList=findViewById(R.id.bookList);
        bookDataList=new ArrayList<>();
        for(int i=0;i<titles.length;i++){
            bookDataList.add(new SearchBooks(titles[i], authors[i], ISBNs[i], status[i]));
        }
        bookAdapter=new SearchBookList(this, bookDataList);
        bookList.setAdapter(bookAdapter);

        btnSearch = findViewById(R.id.btn_search);
        //Once the button is clicked, do search
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            //Detect if the search button is clicked
            public void onClick(View v) {
                //Get the content of search box
                EditText editText = (EditText) findViewById(R.id.edit_text);
                String inputText = editText.getText().toString();
                word = inputText;
                //Delete the book which name is not the input
                if (!inputText.equals("")){
                    for(int i=0;i<bookDataList.size();i++) {
                        if (!bookDataList.get(i).getTitle().equals(inputText)) {
                            bookDataList.remove(i);
                            i--;
                            //Indicate some book(s) is deleted
                            setTitle("1");
                        }
                    }
                }
                //Return all books if there is no input
                else{
                    bookDataList.clear();
                    for(int i=0;i<titles.length;i++){
                        bookDataList.add(new SearchBooks(titles[i],authors[i],ISBNs[i], status[i]));
                    }
                }
                bookAdapter.notifyDataSetChanged();
            }
        });

        //When a book item is clicked, send a request
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!word.equals("")) {
                    int tmp=0;
                    while(!titles[tmp].equals(word)){
                        tmp++;
                    }
                    if (status[tmp].equals("Available")) {
                        new RequestFragment().show(getSupportFragmentManager(), "detail");
                        //Toast.makeText(MainActivity.this, "The request has been send", Toast.LENGTH_SHORT).show();
                    } else if (status[tmp].equals("Requested")) {
                        Toast.makeText(SearchActivity.this, "You have requested this book, please try another one", Toast.LENGTH_SHORT).show();
                    } else if (status[tmp].equals("Borrowed")) {
                        Toast.makeText(SearchActivity.this, "This book has been borrowed by others", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    if (status[i].equals("Available")) {
                        new RequestFragment().show(getSupportFragmentManager(), "detail");
                        //Toast.makeText(MainActivity.this, "The request has been send", Toast.LENGTH_SHORT).show();
                    } else if (status[i].equals("Requested")) {
                        Toast.makeText(SearchActivity.this, "You have requested this book, please try another one", Toast.LENGTH_SHORT).show();
                    } else if (status[i].equals("Borrowed")) {
                        Toast.makeText(SearchActivity.this, "This book has been borrowed by others", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent a = new Intent(SearchActivity.this, HomeActivity.class);
                        startActivity(a);
                        break;

                    case R.id.navigation_explore:
                        Intent b = new Intent(SearchActivity.this, SearchActivity.class);
                        startActivity(b);
                        break;

                    case R.id.navigation_notification:
                        Intent c = new Intent(SearchActivity.this, NotificationActivity.class);
                        startActivity(c);
                        break;

                    case R.id.navigation_Me:
                        Intent d = new Intent(SearchActivity.this, Profile.class);
                        startActivity(d);
                        break;

                }

                return false;

            }
        });

    }

    @Override
    public void onOkPressed(String message){

    }
}