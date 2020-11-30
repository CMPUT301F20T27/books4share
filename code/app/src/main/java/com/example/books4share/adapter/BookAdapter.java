package com.example.books4share.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.books4share.R;
import com.example.books4share.bean.Book;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * This class is BookAdapter which extends the RecyclerView Adapter holding the basic books information
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> implements Filterable {
    ArrayList<Book> books ;
    ArrayList<Book> filtersBooks ;
    Context context ;

    /**
     * Constructor of BookAdapter
     * @param context
     * @param books
     */
    public BookAdapter(Context context, ArrayList<Book> books){
        this.books = books;
        this.filtersBooks = books;
        this.context = context;
    }


    /**
     * This method is used to inflate view of the book adapter
     * @param parent
     * @param viewType
     * @return
     */

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.search_book_item,parent,false);
        return new ViewHolder(view);
    }

    /**
     * Set up the contents of every item of the viewholder: name, ISBN, status, default images(occupied place for user to upload images)
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        Book book = books.get(position);
        holder.tvName.setText(book.getTitle()+"("+book.getAuthor()+")");
        holder.tvISBN.setText(book.getIsbn());
        holder.tvStatus.setText(book.getCurrentStatus());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.error_img)
                .fallback(R.drawable.error_img)
                .error(R.drawable.error_img);
        Glide.with(context).load(book.image).apply(options).into(holder.ivLogo);
    }


    /**
     * return the number of books on the view after filter
     * @return
     */
    @Override
    public int getItemCount() {
        return filtersBooks.size();
    }

    /**
     * set a filter to allow user to filter the book by options
     * @return
     */
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
                        if (book.getCurrentStatus().toUpperCase() .equals(c) ) {
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
                filtersBooks = (ArrayList<Book>) results.values;
                notifyDataSetChanged();

            }
        };

        return filter;
    }

    public  Book getBook(int position) {
        return  filtersBooks.get(position);

    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivLogo;
        TextView tvName;
        TextView tvISBN;
        TextView tvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLogo = itemView.findViewById(R.id.iv_logo);
            tvName = itemView.findViewById(R.id.tv_name);
            tvISBN = itemView.findViewById(R.id.tvISBN);
            tvStatus = itemView.findViewById(R.id.tvStatus);

        }
    }
    private OnItemClickListener onItemClickListener;

    /**
     * interface of clicking item listener
     */
    public  interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
