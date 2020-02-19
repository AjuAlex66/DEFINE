package com.example.define;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class INFO extends AppCompatActivity {

    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseAuth mAuth;

    TextView mes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("DefineApp");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mes = findViewById(R.id.message);



        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(INFO.this, LoginActivity.class));
                }
            }
        };
    }
    @Override
    protected void onStart() {
        super.onStart();
        check();
        mAuth.addAuthStateListener(mAuthListner);
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
