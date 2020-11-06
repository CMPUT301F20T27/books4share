package com.example.books4share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BookList extends ArrayAdapter<Book> implements Filterable {
    private ArrayList<Book> books;
    private Context context;
    private ArrayList<Book> filteredBooks;

    public BookList(Context context, ArrayList<Book> books) {
        super(context,0,books);
        this.books = books;
        this.filteredBooks = books;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.mybooks_content, parent, false);
        }
        Book book = filteredBooks.get(position);


        TextView bookName = view.findViewById(R.id.textView_book_title);
        TextView bookAuthor = view.findViewById(R.id.textView_book_author);
        TextView bookIsbn = view.findViewById(R.id.textView_book_isbn);
        TextView bookStatus = view.findViewById(R.id.textView_book_status);
        TextView bookBorrower = view.findViewById(R.id.textView_book_burrower);
        Button editButton = view.findViewById(R.id.button_edit_book);


        bookName.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookIsbn.setText(book.getIsbn());
        bookStatus.setText(book.getCurrentStatus().name());
        bookBorrower.setText("Burrower: In progress");

        // Testing
        editButton.setVisibility(View.GONE);



        return view;
    }

    public int getCount() {
        return filteredBooks.size();
    }

    public Book getItem(int position) {
        return filteredBooks.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Book> tempList = new ArrayList<>();
                String c = constraint.toString();

                if (c.equals("ALL")) {
                    tempList = books;
                } else {
                    for (Book book : books) {
                        if (book.getCurrentStatus() == Book.Status.valueOf(c)) {
                            tempList.add(book);
                        }
                    }
                }

                results.values = tempList;
                results.count = tempList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredBooks = (ArrayList<Book>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }

            }
        };

        return filter;
    }
}
