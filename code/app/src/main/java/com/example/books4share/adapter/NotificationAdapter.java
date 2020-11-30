package com.example.books4share.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.books4share.R;
import com.example.books4share.bean.Book;
import com.example.books4share.bean.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * NotificationAdapter extending RecyclerViewAdapter is used to show the notifications under notificationFragment activity
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>  {
    private String TAG = NotificationAdapter.class.getSimpleName();
    ArrayList<Notification> books ;
    ArrayList<Notification> filtersBooks ;
    Context context ;

    /**
     * Notification constructor
     * @param context
     * @param books
     */

    public NotificationAdapter(Context context, ArrayList<Notification> books){
        this.books = books;
        this.filtersBooks = books;
        this.context = context;
    }

    /**
     * This method inflates the recycle view  of the adapter
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
     * set up contents for displaying in the item view
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        Notification notification = books.get(position);
        holder.tvStatus.setText(notification.status);
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

        FirebaseFirestore db = FirebaseFirestore.getInstance();;
        DocumentReference docRef = db.collection("Books").document(notification.bookId);
        docRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    /**
                     * This override method tells whether getting information from firestore is completed successfully
                     * and if we get book object details successfully, we will set the item content on adapter
                     * @param task
                     */
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                            Book  book =   document.toObject(Book.class);
                            if(book!=null){
                                holder.tvName.setText(book.getTitle()+"("+book.getAuthor()+")");
                                holder.tvISBN.setText(book.getIsbn());
                                notification.book = book;
                                Glide.with(context).load(book.image).apply(options).into(holder.ivLogo);
                            }
                        } else {

                        }
                    }
                })

        ;
    }

    /**
     * return the number of  notifications
     * @return
     */

    @Override
    public int getItemCount() {
        return filtersBooks.size();
    }
    /**
     * viewholder class
     */
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

    public  interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
