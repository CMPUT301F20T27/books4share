/**
 * Problem:
 * 1. (fixed) ListView doesn't refresh after adding a new book. Potential cause: filter doesn't refresh
 *    after data set has changed.
 *
 * 2. Cannot map OnClickListener to each button in the ListView.
 */
package com.example.books4share;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        AddBookFragment.OnFragmentInteractionListener {

    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;

    Spinner filterSpinner;
    ArrayAdapter<CharSequence> spinnerAdapter;

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
    public void onOkPressed(Book newBook) {
        if (bookAdapter.getPosition(newBook) == -1)
            bookAdapter.add(newBook);
    }

    @Override
    public void onDeletePressed(Book book) {
        bookAdapter.remove(book);
        book = null;
    }
}
