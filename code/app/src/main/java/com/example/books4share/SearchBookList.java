package com.example.books4share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SearchBookList extends ArrayAdapter<SearchBooks> {
    private ArrayList<SearchBooks> books;
    private Context context;

    public SearchBookList(Context context, ArrayList<SearchBooks> books) {
        super(context,0,books);
        this.books = books;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.search_book_item,parent,false);
        }

        SearchBooks book = books.get(position);

        TextView bookTitle = view.findViewById(R.id.book_title);
        TextView bookAuthor = view.findViewById(R.id.book_author);
        TextView bookISBN = view.findViewById(R.id.book_ISBN);
        TextView bookStatus = view.findViewById(R.id.book_status);


        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookISBN.setText(book.getISBN());
        bookStatus.setText(book.getStatus());

        return view;
    }
}
