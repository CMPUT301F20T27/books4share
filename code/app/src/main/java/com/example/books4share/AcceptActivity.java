// Zexin Cai is responsible for this part
// It is to handle the incoming request of the user
// The user can choose to accept, decline or back
// There are no follow-up activities for accept and decline

package com.example.books4share;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.books4share.Book;
import com.example.books4share.Notification;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AcceptActivity extends AppCompatActivity {
    private Notification notification;

    private ImageView iv_logo;
    private TextView tv_author;
    private TextView tv_isbn;
    private TextView tv_title;
    private TextView tvOwnName;
    private ProgressDialog mProgressDialog;
    private Book book;
    FirebaseFirestore db = FirebaseFirestore.getInstance();;
    private TextView tvBorrowerName;
    private TextView tvBorrowerPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);
        mProgressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        notification = (Notification) intent.getSerializableExtra("item");
        initView();


    }

    private void initView() {

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Pending request");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_logo = findViewById(R.id.iv_logo);
        tv_author = findViewById(R.id.tv_author);
        tv_isbn = findViewById(R.id.tv_isbn);
        tv_title = findViewById(R.id.tv_title);
        tvOwnName = findViewById(R.id.tvOwnName);
        tvBorrowerName = findViewById(R.id.tvBorrowerName);
        tvBorrowerPhone = findViewById(R.id.tvBorrowerPhone);
        if (notification.book!=null){
            book = notification.book;
            setBookInfo();
            getBookOwnInfo();
        }else{
            getBookInfoByBookId();
        }
        getBookBorrowerInfo();
        findViewById(R.id.btImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(AcceptActivity.this,MapActivity.class);
                startActivityForResult(intent,100);
            }
        });

        findViewById(R.id.Delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AcceptActivity.this)
                        .setTitle("Alert")
                        .setMessage("Are you sure to declined ?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                db  .collection("Books") .document(book.getId())
                                        .update(
                                                "currentStatus", "Available"
                                        )
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                db  .collection("Notification") .document(notification.getId())
                                                        .update(
                                                                "status", "declined"

                                                        )
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener(){

                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener(){

                                            @Override
                                            public void onFailure(@NonNull Exception e) {


                                            }
                                        });


                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK){
            String receiveLocation=  data.getStringExtra("address");
            if (!TextUtils.isEmpty(receiveLocation)){
                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage("The receiving place for borrowing books is in "+receiveLocation)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                db  .collection("Books") .document(book.getId())
                                        .update(
                                                "currentStatus", "accepted"
                                        )
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                db  .collection("Notification")
                                                        .document(notification.getId())
                                                        .update(
                                                                "status", "accepted",
                                                                "receiveLocation", receiveLocation
                                                        )
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(AcceptActivity.this,"Success",Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener(){

                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener(){

                                            @Override
                                            public void onFailure(@NonNull Exception e) {


                                            }
                                        });

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
            }
        }
    }

    private void getBookBorrowerInfo() {
        db.collection("Users").document(notification.borrowId).collection("Profile")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                            Log.d("Success", String.valueOf(doc.getData().get("Name")));
                            String Name = (String) doc.getData().get("Name");
                            String Phone = (String) doc.getData().get("Phone");
                            tvBorrowerName.setText(Name);
                            tvBorrowerPhone.setText(Phone);
                        }
                    }
                });
    }

    private void getBookInfoByBookId() {

        DocumentReference docRef = db.collection("Books").document(notification.bookId);
        docRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            book =   document.toObject(Book.class);
                            book.setId(document.getId());
                            getBookOwnInfo();
                            if(book!=null){
                                setBookInfo();
                            }
                        } else {

                        }
                    }
                });
    }

    private void getBookOwnInfo() {

        db.collection("Users").document(book.usersId).collection("Profile")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                            Log.d("Success", String.valueOf(doc.getData().get("Name")));
                            String Name = (String) doc.getData().get("Name");
                            tvOwnName.setText(Name);
                        }
                    }
                });
    }

    private void setBookInfo(){
        Glide.with(this).load(book.image).into(iv_logo);

        tv_author.setText(book.author);
        tv_isbn.setText(book.isbn);
        tv_title.setText(book.title);
    }

}
