// Weijia Zhang and Philip Wang are responsible for this part
// This is to implement all functions about book

package com.example.books4share.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.books4share.AddBookActivity;
import com.example.books4share.BookDetailActivity;
import com.example.books4share.ScanDetailActivity;
import com.example.books4share.adapter.BookAdapter;
import com.example.books4share.bean.Book;
import com.example.books4share.R;
import com.example.books4share.bean.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    RecyclerView bookList;
    BookAdapter bookAdapter;
    ArrayList<Book> bookDataList;
    Spinner filterSpinner;
    ArrayAdapter<CharSequence> spinnerAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();;


    private View rootView;


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    /**
     * initialize the HomeFragment Layout view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home,container,false);
        return rootView;
    }

    /**
     * initialize the home fragment content view
     * @param view
     * @param savedInstanceState
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Drop down menu for filters
        filterSpinner = (Spinner) rootView.findViewById(R.id.spinner_filter);
        spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.status_array,
                android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(spinnerAdapter);
        filterSpinner.setOnItemSelectedListener(this);

        bookList = rootView.findViewById(R.id.my_book_list);
        bookDataList = new ArrayList<>();

        bookAdapter = new BookAdapter(getActivity(), bookDataList);
        bookList.setLayoutManager(new LinearLayoutManager(getContext()));
        bookList.setAdapter(bookAdapter);


        Button addBook = rootView. findViewById(R.id.button_add_book);
        Button btnScan = rootView. findViewById(R.id.btnScan);


        btnScan.setOnClickListener(new View.OnClickListener() {
            /**
             * click to start the scan function
             * @param v
             */
            @Override
            public void onClick(View v) {
                scan();
            }
        });

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * switch to AddBookActivity
                 */

                Intent intent =   new Intent(getActivity(), AddBookActivity.class);
                startActivity(intent);
            }
        });

        initData();
        bookAdapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
            /**
             * Switch to BookDtailActivity
             * @param position
             */
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                Book book = bookAdapter.getBook(position);
                intent.putExtra("item",book);
                startActivity(intent);
            }
        });
    }

    /**
     * This method is a function which allows user to scan the isbn
     */
    private void scan(){
        if (!hasPermission()){
            return;
        }
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, 100);
    }

    /**
     * Check whether there are permissions allowed. If permissions are allowed, the scan function will be allowed to work
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    scan();
                }
                break;
        }
    }

    /**
     * check whether the user have allowed the relative permissions. If not allowed, this will request user to grant permissions
     * @return
     */
    private boolean hasPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 101);
            return false;
        }else {
            return true;
        }
    }

    /**
     * get the data bundle from the intent activity
     * if the bundle is null, no action performed
     * if bundle is not null and the scan-camera/CodeUtills(the scan tool from zxinglibrary) returns a true result
     * we call getBookbyISBN function
     * else this will toast a message noticing user scan failed
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    getBookByISBN(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(),"failed", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * This is used to override the onItemSelected interface
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString().toUpperCase();
        bookAdapter.getFilter().filter(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * get the book information by ISBN from firestore
     * @param isbn
     */
    private void getBookByISBN(String isbn){

        CollectionReference bookRef = db.collection("Books");
        bookRef
                .whereEqualTo("isbn",isbn)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Book book =    document.toObject(Book.class);
                            book.setId(document.getId());
                            getNotificationByBook(book);
                            break;
                        }
                    }
                });



    }

    /**
     * get the books by status under Notification collection
     * @param book
     */
    private void getNotificationByBook(Book book) {
        CollectionReference bookRef = db.collection("Notification");
        bookRef.whereEqualTo("status", "accepted")
                .whereEqualTo("bookId", book.getId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    /**
                     * If successfully get the book, switch to ScanDetailActivity
                     * @param queryDocumentSnapshots
                     */
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Notification book =    document.toObject(Notification.class);
                            if (book.borrowId.equals(FirebaseAuth.getInstance().getUid())||book.usersId.equals(FirebaseAuth.getInstance().getUid())){
                                Intent intent = new Intent(getActivity(), ScanDetailActivity.class);
                                intent.putExtra("item",book);
                                startActivity(intent);
                            }

                            break;
                        }
                    }
                });
    }

    /**
     * Initialize the data of books
     */

    private void initData(){

       CollectionReference bookRef = db.collection("Books");
       bookRef.whereEqualTo("usersId", FirebaseAuth.getInstance().getCurrentUser().getUid())
               .addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
               bookDataList.clear();
               for (QueryDocumentSnapshot document : value) {
                   Book book =    document.toObject(Book.class);
                   book.setId(document.getId());
                   if (book!=null){
                       bookDataList.add(book);
                   }
               }

               bookAdapter.notifyDataSetChanged();
           }
       });
   }
}
