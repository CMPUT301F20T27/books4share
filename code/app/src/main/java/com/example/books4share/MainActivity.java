package com.example.books4share;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView bookList;
    Button btnSearch;
    ArrayAdapter<Books> bookAdapter;
    ArrayList<Books> bookDataList;

    String[] titles = {"Harry Porter", "The Republic", "The Odyssey", "Lord of the Flies"};
    String[] authors = {"J.K. Rowling", "Plato", "Homer", "William"};
    String[] ISBNs = {"1", "2", "3", "4"};
    String[] status = {"Requested", "Requested", "Unrequested", "Requested"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Write all books into an ArrayList
        bookList=findViewById(R.id.bookList);
        bookDataList=new ArrayList<>();
        for(int i=0;i<titles.length;i++){
            bookDataList.add(new Books(titles[i], authors[i], ISBNs[i]));
        }

        bookAdapter=new BookList(this, bookDataList);
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
                        bookDataList.add(new Books(titles[i],authors[i],ISBNs[i]));
                    }
                }
                bookAdapter.notifyDataSetChanged();
            }
        });
        //When a book item is clicked, send a request
        //Unfinished, I will implement this next time
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "A mock request is sent!", Toast.LENGTH_SHORT).show();
            }
        });
        //Indicate the list is refreshed
        setTitle("4");
    }
}