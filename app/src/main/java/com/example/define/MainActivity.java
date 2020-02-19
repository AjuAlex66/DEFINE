package com.example.define;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.define.R.*;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
//    RecyclerView recyclerView;
//    ListView listView;
//    ArrayList<Profile> list;
    private List<Profile> profileList;
//    private ArrayList<Profile> profileListFull;
    private DefineAdapter defineAdapter;
    private searchAdapter seAdapter;
//    FirebaseListAdapter adapter;
    FirebaseAuth mAuth;
//    boolean dbl =false;
    FirebaseAuth.AuthStateListener mAuthListner;
    RecyclerView recyclerView;
    int count;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    private AdView mAdView;
//    Date currentTime = Calendar.getInstance().getTime();

//    SignInButton googleBn;
//    FirebaseAuth mAuth;
//    private final static int RC_SIGN_IN = 2;
//
//    GoogleSignInClient mGoogleSignInClient;
//    FirebaseAuth.AuthStateListener mAuthListner;
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        mAuth.addAuthStateListener(mAuthListner);
//    }

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("DefineApp");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//        ca-app-pub-1921721310026993~4365136059
//        AdLoader adLoader = new AdLoader.Builder(context, "\n" + "ca-app-pub-1921721310026993~4365136059")
//                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
//                    @Override
//                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
//                        // Show the ad.
//                    }
//                })
//                .withAdListener(new AdListener() {
//                    @Override
//                    public void onAdFailedToLoad(int errorCode) {
//                        // Handle the failure by logging, altering the UI, and so on.
//                    }
//                })
//                .withNativeAdOptions(new NativeAdOptions.Builder()
//                        // Methods in the NativeAdOptions.Builder class can be
//                        // used here to specify individual options settings.
//                        .build())
//                .build();
//        adLoader.loadAd(new AdRequest.Builder().build());
//        progressDialog.setMessage("Please wait...");

//        swipeRefreshLayout = findViewById(id.swipe);

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                onStart();
//                swipeRefreshLayout.setRefreshing(false);
//                Toast.makeText(MainActivity.this, "Refresh Success", Toast.LENGTH_SHORT).show();
//            }
//        });

//        listView = (ListView) findViewById(R.id.myLisview);
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        };
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        list = new ArrayList<Profile>();
        recyclerView = findViewById(id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileList = new ArrayList<>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Posts");

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        //add data
                        Profile p = dataSnapshot1.getValue(Profile.class);
                        profileList.add(p);
                        //show totla children count
//                        integer a = dataSnapshot1.getChildrenCount();
//                        Log.e(dataSnapshot1.getKey(),dataSnapshot1.getChildrenCount() + "");
//                        Toast.makeText(MainActivity.this, "T"+e, Toast.LENGTH_SHORT).show();
                    }

                    defineAdapter = new DefineAdapter(profileList);
                    recyclerView.setAdapter(defineAdapter);

                }
                Collections.reverse(profileList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        ValueEventListener valueEventListener = mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                final List<ParentList> Parent = new ArrayList<>();
//                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    final String ParentKey = snapshot.getKey().toString();
//                    snapshot.child("titre").getValue();
//                    DatabaseReference childReference = FirebaseDatabase.getInstance().getReference().child(ParentKey);
//                    childReference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            final List<ChildList> Child = new ArrayList<>();
//                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
//                                final String ChildValue = snapshot1.getValue().toString();
//                                snapshot1.child("titre").getValue();
//                                Child.add(new ChildList(ChildValue));
//                            }
//                            Parent.add(new ParentList(ParentKey, Child));
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            }
//
//            class DocExpandableRecyclerAdapter<ExpandableRecyclerViewAdapter> extends ExpandableRecyclerViewAdapter<MyParentViewHolder, MyChildViewHolder> {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                    Profile p = dataSnapshot.getValue(Profile.class);
//                    list.add(p);
//                }
//                adapter = new MyAdapter(MainActivity.this, list);
//                recyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//            }
//        });


//        lv = (ListView) findViewById(R.id.listV);
//        progressDialog.show();
//        Query query = FirebaseDatabase.getInstance().getReference()
//                .child("Posts");
//        FirebaseListOptions<Profile> options = new FirebaseListOptions.Builder<Profile>()
//                .setLayout(R.layout.cardview)
//                .setQuery(query, Profile.class)
//                .build();
//        adapter = new FirebaseListAdapter(options) {
//            @Override
//            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
////                progressDialog.show();
//                TextView tit = v.findViewById(R.id.title);
////                TextView na = v.findViewById(R.id.MainName);
//                TextView des = v.findViewById(R.id.description);
//                TextView nm = v.findViewById(R.id.name);
//
//                Profile lis = (Profile) model;
//                tit.setText(lis.getpTitle());
//                des.setText(lis.getpDesc());
//                nm.setText(lis.getuEmail());
////                progressDialog.dismiss();
//            }
//        };
//        listView.setAdapter(adapter);
//        progressDialog.dismiss();

    }
//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        MenuItem me = menu.findItem(id.Add);
       me.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               Intent intent = new Intent(MainActivity.this, AddActivity.class);
               startActivity(intent);
               return false;
           }
       });

       MenuItem se = menu.findItem(id.Search);
       SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
       SearchView searchView = (SearchView) se.getActionView();
       searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               newText = newText.toLowerCase();
               List<Profile> myList = new ArrayList<>();
               for (Profile profile: profileList){
                   String search = profile.getpTitle().toLowerCase();
                   String name = profile.getuName().toLowerCase();
                   String content = profile.getpDesc().toLowerCase();
                   if (search.contains(newText) || name.contains(newText) || content.contains(newText)){
                       myList.add(profile);
                   }
               }
               defineAdapter.setSearchOperation(myList);
               return false;
           }
       });
//       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//           @Override
//           public boolean onQueryTextSubmit(String query) {
//               return false;
//           }
//
//           @Override
//           public boolean onQueryTextChange(String newText) {
////               defineAdapter = new DefineAdapter(profileListFull);
////               defineAdapter.getFilter().filter(newText);
//               defineAdapter = new DefineAdapter(profileList);
//               defineAdapter.getFilter().filter(newText );
//               return false;
//           }
//       });

       MenuItem ab = menu.findItem(id.logOut);
       ab.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               mAuth.signOut();
               finish();
               return false;
           }
       });
//       MenuItem id = menu.findItem(R.id.inFo);
//       id.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//           @Override
//           public boolean onMenuItemClick(MenuItem item) {
//               Intent intent = new Intent(MainActivity.this, INFO.class);
//               startActivity(intent);
//               return false;
//           }
//       });

       MenuItem set = menu.findItem(R.id.settingS);
       set.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               Toast.makeText(MainActivity.this, "Aju", Toast.LENGTH_SHORT).show();
               return false;
           }
       });

       MenuItem ref = menu.findItem(R.id.Refresh);
       ref.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               finish();
               overridePendingTransition(0,0);
               startActivity(getIntent());
               overridePendingTransition(0,0);
               Toast.makeText(MainActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
//               defineAdapter = new DefineAdapter(profileList);
//               recyclerView.setAdapter(defineAdapter);
//               defineAdapter.notifyDataSetChanged();
//               Toast.makeText(MainActivity.this, "Refreshed...", Toast.LENGTH_SHORT).show();
               return false;
           }
       });

        MenuItem cnt = menu.findItem(id.entrieS);
        cnt.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                defineAdapter = new DefineAdapter(profileList);
                recyclerView.setAdapter(defineAdapter);
                defineAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Total entries "+count, Toast.LENGTH_LONG).show();
                return false;
            }
        });

//        MenuItem log = menu.findItem(R.id.logIn);
//        log.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//                return false;
//            }
//        });

//        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView)myActionMenuItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//               ArrayList<String> arrayList = new ArrayList<>();
//
//               for (){
//                   if ()
//                }
//
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        check();
        mAuth.addAuthStateListener(mAuthListner);
//        FirebaseRecyclerOptions<Profile> options =
//                new FirebaseRecyclerOptions.Builder<Profile>()
//                .setQuery(mRef,Profile.class)
//                .build();
//        FirebaseRecyclerAdapter<Profile,ViewHolder> firebaseRecyclerAdapter =
//                new FirebaseRecyclerAdapter<Profile, ViewHolder>(
//                        options
//                ) {
//                    @NonNull
//                    @Override
//                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(layout.cardview,parent, false);
//                        return new ViewHolder(view);
//                    }
//
//                    @Override
//                    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Profile model) {
//                        holder.setDetails(getApplicationContext(),model.getpTitle(),model.getuEmail(),model.getpDesc());
//                    }
//                };
//        recyclerView.setAdapter(firebaseRecyclerAdapter);
//
    }

    @Override
    protected void onStop() {
        super.onStop();
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
//       if (dbl){
//           super.onBackPressed();
//           finish();
//           return;
//       }
//       this.dbl = true;
//        Toast.makeText(this, "Please press once to exit", Toast.LENGTH_SHORT).show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dbl = false;
//            }
//        },2000);
//    }
}
