package com.example.define;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddActivity extends AppCompatActivity {

    ActionBar actionBar;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    DatabaseReference databaseReference;

    EditText e1,e2,e3;
    ImageView imgV;
    Button b1;

    String name,email, uid;

    //permission constants
    private static final int CAMERA_REQUEST_CODE =100;
    private static final int STORAGE_REQUEST_CODE =200;

    //image pick constants
    private static final int IMAGE_PICK_CAMREA_CODE =300;
    private static final int IMAGE_PICK_GELLERY_CODE =400;

    //permission array
    String[] cameraPermissions;
    String[] storagePermissions;

    //image picked will be same in this uri
    Uri image_rui = null;

    //progress bar
    ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        e1 = findViewById(R.id.title);
        e2 = findViewById(R.id.desc);
        b1 = findViewById(R.id.share);
//        e3 = findViewById(R.id.linkRef);
//        imgV = findViewById(R.id.imageV);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Define Something");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //init permission arrays
        cameraPermissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        storagePermissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        checkUserStatus();
        //get some info of current user to include in post
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    name = ""+ds.child("name").getValue();
                    email = ""+ds.child("email").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    alert();
                }
                else{

                }
            }
        };

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = e1.getText().toString().trim();
                final String define = e2.getText().toString().trim();
//                final String referel = e3.getText().toString().trim();
                check();
                if (TextUtils.isEmpty(title) && TextUtils.isEmpty(define)){
                    Toast.makeText(AddActivity.this, "What should i define?", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(define)){
                    Toast.makeText(AddActivity.this, "Define about that title...", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(title)){
                    Toast.makeText(AddActivity.this, "Give it a good title", Toast.LENGTH_LONG).show();
                    return;
                }
                if (image_rui == null){
                    //post without image
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddActivity.this);
                    builder.setTitle("Are you sure..!");
                    builder.setCancelable(false);
                    builder.setMessage("Once you submit, you can't edit later")
                            .setPositiveButton("Publish Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    uploadData(title, define,"noImage");
                                }
                            }).setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    final android.app.AlertDialog alert  = builder.create();
                    alert.show();

//                    uploadData(title, define, referel,"noImage");
                }
                else{
                    //post with image
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddActivity.this);
                    builder.setTitle("Are you sure!");
                    builder.setCancelable(false);
                    builder.setMessage("Once you submit, you can't edit later")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    uploadData(title, define, String.valueOf(image_rui));
                                }
                            }).setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    final android.app.AlertDialog alert  = builder.create();
                    alert.show();
//                    uploadData(title, define, referel, String.valueOf(image_rui));

                }
            }
        });
    }
    private void uploadData(final String title, String define, String uri) {
        progressDialog.setMessage("Publishing...");
        progressDialog.show();

        final String timeStamp = String.valueOf(System.currentTimeMillis());

        String filePathAndName = "Posts/" + "post" + timeStamp;


            HashMap<Object, String> hashMap = new HashMap<>();
            //put post info
//            hashMap.put("uid",uid);
            hashMap.put("uName",name);
            hashMap.put("uEmail",email);
            hashMap.put("pId",timeStamp);
            hashMap.put("pTitle",title);
            hashMap.put("pDesc",define);
            hashMap.put("pImage","noImage");
            hashMap.put("pTime",timeStamp);
//            hashMap.put("referelLink",referel);

            //path to store post data
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
            //put data in this ref
            reference.child(timeStamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //add in database
                            progressDialog.dismiss();
                            Toast.makeText(AddActivity.this, "Defined", Toast.LENGTH_SHORT).show();
                            //reset views
                            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddActivity.this);
                            builder.setTitle("Success");
                            builder.setCancelable(false);
                            builder.setMessage("Back to Home?")
                                    .setPositiveButton("Yes, please", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(AddActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                            Toast.makeText(AddActivity.this, "Thank You", Toast.LENGTH_SHORT).show();
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    e1.setText("");
                                    e2.setText("");
                                    e3.setText("");
                                    return;
                                }
                            });
                            final android.app.AlertDialog alert  = builder.create();
                            alert.show();
//                            e1.setText("");
//                            e2.setText("");
//                            imgV.setImageURI(null);
//                            image_rui = null;

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed adding post in database
                            progressDialog.dismiss();
                            Toast.makeText(AddActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

    }

    private void alert(){
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Login Required");
        builder.setCancelable(false);
        builder.setMessage("Looks like its an unauthorized access. Login first!")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AddActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
        final android.app.AlertDialog alert  = builder.create();
        alert.show();
    }

    private void alert1(){
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Login Required");
        builder.setCancelable(false);
        builder.setMessage("Looks like its an unauthorized access. Login first!")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AddActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
        final android.app.AlertDialog alert  = builder.create();
        alert.show();
    }

    private void checkUserStatus() {
        //get current user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            //user is signed in stay here
            //set email of logged user
            email = user.getEmail();
            name = user.getDisplayName();
            uid = user.getUid();
        }
        else{
        }
    }

    private void showImagePickDialog(){
        //options (camera, gallery) to show in dialog
        String [] options  = {"Camera","Gallery"};

        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");
        //set options to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //item click handle
                if (which==0){
                    //camera clicked
                    //we need to check permission first
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }
                }
                if (which==1){
                    //gallery clicked
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else{
                        pickFromGallery();
                    }
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }

    private void pickFromGallery() {
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GELLERY_CODE);
    }

    private void pickFromCamera() {
        //intent to pick images from camera
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
        image_rui = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_rui);
        startActivityForResult(intent, IMAGE_PICK_CAMREA_CODE);

    }

    private  boolean checkStoragePermission(){
        //check if storage permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private  void requestStoragePermission(){
        //request runtime storage permission
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private  boolean checkCameraPermission(){
        //check if camera permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private  void requestCameraPermission(){
        //request runtime camera permission
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    //handle permission results

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //this method is called when user press allows or deny from permission request dialog
        //here we will handle permission cases (allowed and denied)

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted){
                        //both permissions are granted
                        pickFromCamera();
                    }
                    else {
                        //both permissions are denied
                        Toast.makeText(this, "Camera & Storage both permissions are neccessary...", Toast.LENGTH_SHORT).show();
                    }
                }
                else{

                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted){
                        //storage permissions granted
                        pickFromGallery();
                    }
                    else {
                        //both permissions are denied
                        Toast.makeText(this, "Storage both permissions neccessary...", Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //this method will be called after picking image from camera or gallery
        if (requestCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GELLERY_CODE){
                //images is picked from gallery, get uri of image
                image_rui = data.getData();

                //set ti imageView
                imgV.setImageURI(image_rui);

            }
            else if (requestCode == IMAGE_PICK_CAMREA_CODE){
                //image is picked from camera, get uri of image

                imgV.setImageURI(image_rui);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void check(){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
//            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("No internet connection");
//            builder.setCancelable(false);
//            builder.setMessage("Please cross check your internet connection")
//                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            final AlertDialog alert  = builder.create();
//            alert.show();
        }

    }

}
