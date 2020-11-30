package com.example.books4share;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.books4share.bean.Book;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {

    private Book book;

    private ImageView iv_logo;
    private TextView tv_author;
    private TextView tv_isbn;
    private TextView tv_title;
    private ProgressDialog mProgressDialog;
    String[] permissions = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        mProgressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("item");
        initView();


    }

    /**
     * Initialize the activity view
     */
    private void initView() {

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Book Detail");
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

        Glide.with(this).load(book.image).into(iv_logo);

        tv_author.setText(book.author);
        tv_isbn.setText(book.isbn);
        tv_title.setText(book.title);
        findViewById(R.id.btImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPermissionList.clear();
                for (int i = 0; i < permissions.length; i++) {
                    if (ContextCompat.checkSelfPermission(BookDetailActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                        mPermissionList.add(permissions[i]);
                    }
                }
                if (mPermissionList.isEmpty()) {
                    loadImage();
                } else {
                    String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
                    ActivityCompat.requestPermissions(BookDetailActivity.this, permissions, 101);
                }
            }
        });
        findViewById(R.id.Edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailActivity.this, AddBookActivity.class);
                intent.putExtra("item",book);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.Delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(BookDetailActivity.this)
                        .setTitle("Delete")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("Books").document(book.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();
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
        iv_logo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // US 08.02.01
               if (book.image!=null){
                   new AlertDialog.Builder(BookDetailActivity.this)
                           .setTitle("Delete Image")
                           .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.dismiss();
                                   FirebaseFirestore db = FirebaseFirestore.getInstance();
                                   db.collection("Books").document(book.getId()).update(
                                           "image", ""
                                   ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                           finish();
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
                return true;
            }
        });
    }

    /**
     * open the image gallery
     */
    public void loadImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 99);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 101){
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(BookDetailActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        Toast.makeText(BookDetailActivity.this,"Permission not requested",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * get the image data  and upload image if the resultCode is Result_OK
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case 99:
                    final Uri selectedUri = data.getData();
                    upload(selectedUri);
                    break;

                default:
                    break;
            }
        }

    }

    /**
     * Upload the image data into Storage, since firestore doesn't support image type
     * https://github.com/bumptech/glide
     * glide is a image loading library
     * @param selectedUri
     */
    private void upload(Uri selectedUri) {
        mProgressDialog.show();
        StorageReference mStoreReference = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = mStoreReference.child(System.currentTimeMillis()+".jpg");
        UploadTask uploadTask = riversRef.putFile(selectedUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                mProgressDialog.dismiss();
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String imageUrl = downloadUri.toString();
                    book.setImage(imageUrl);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Books").document(book.getId()).set(book);
                    Glide.with(BookDetailActivity.this).load(imageUrl).into(iv_logo);
                }
            }
        });
    }

}