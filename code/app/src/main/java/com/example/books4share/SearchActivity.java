// Dazhi Zhang is responsible for this part
// This is to implement search book and send a request

package com.example.books4share;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements RequestFragment.OnFragmentInteractionListener {

    ListView bookList;
    Button btnSearch;
    ArrayAdapter<SearchBooks> bookAdapter;
    ArrayList<SearchBooks> bookDataList;

    String[] titles = {"Harry Porter", "The Republic", "The Odyssey", "Lord of the Flies"};
    String[] authors = {"J.K. Rowling", "Plato", "Homer", "William"};
    String[] ISBNs = {"1", "2", "3", "4"};
    String[] status = {"Borrowed", "Requested", "Available", "Available"};

    String word = "";

    Button b1;
    Button b2;
    Button b3;
    Button b4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


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

        b1 = findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        b3 = findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        b4 = findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, Profile.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onOkPressed(String message){

    }
}