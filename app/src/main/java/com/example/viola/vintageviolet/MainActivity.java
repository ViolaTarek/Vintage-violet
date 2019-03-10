package com.example.viola.vintageviolet;

import android.app.usage.NetworkStats;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, SwipeRefreshLayout.OnRefreshListener {

    ImageView profile_pic;
    TextView username;
    TextView userEmail;
    String userID;
    GoogleApiClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    MenuItem signing;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    String savedState;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            homeFragment home = new homeFragment();
            transaction.replace(R.id.fragment_styles, home);
            transaction.commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        profile_pic = navigationView.getHeaderView(0).findViewById(R.id.profile_pic);
        username = navigationView.getHeaderView(0).findViewById(R.id.userName_tv);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.userEmail_tv);
        signing = navigationView.getMenu().getItem(0);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            signIn();
            signing.setTitle("Sign out");
        } else {
            userID = null;
            signing.setTitle("Sign In");

        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle bundle = new Bundle();
        stylesFragment myFrag = new stylesFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        homeFragment home = new homeFragment();
        switch (id) {
            case (R.id.sign_in):
                if (item.getTitle() == "Sign In") {
                    signIn();
                    item.setTitle("Sign out");
                } else {
                    signOut();
                    userID = null;
                    item.setTitle("Sign In");
                }
                break;

            case (R.id.dress):
                bundle.putString("category", "dress");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                savedState = "styles";
                break;

            case (R.id.casual):
                bundle.putString("category", "casual");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();

                break;

            case (R.id.classy):
                bundle.putString("category", "classy");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();

                break;
            case (R.id.autumn):
                bundle.putString("category", "autumn");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                break;
            case (R.id.winter):
                bundle.putString("category", "winter");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                break;
            case (R.id.summer):
                bundle.putString("category", "summer");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                break;
            case (R.id.spring):
                bundle.putString("category", "spring");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                break;
            case (R.id.home_nav):
                bundle.putString("userid", userID);
                home.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, home);
                transaction.commit();
                savedState = "home";

                break;
            case (R.id.favorite_nav):
                if (userID != null) {
                    bundle.putString("userid", userID);
                    FavoriteFragment fav = new FavoriteFragment();
                    fav.setArguments(bundle);
                    transaction.replace(R.id.fragment_styles, fav);
                    transaction.commit();
                } else {
                    Toast.makeText(this, "Sign in to show your Styles", Toast.LENGTH_LONG).show();
                }
                savedState = "favorite";

                break;
            default:
                return true;

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        username.setVisibility(View.VISIBLE);
        userEmail.setVisibility(View.VISIBLE);

    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleSignInClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Glide.with(getApplicationContext())
                        .load(R.mipmap.ic_user)
                        .into(profile_pic);
                username.setVisibility(View.GONE);
                userEmail.setVisibility(View.GONE);

            }

        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                String personPhoto = account.getPhotoUrl().toString();
                String personEmail = account.getEmail();
                String personName = account.getDisplayName();
                userID = account.getId();


                if (personPhoto != null) {
                    Glide.with(this)
                            .load(personPhoto)
                            .into(profile_pic);
                }

                userEmail.setText(personEmail);
                username.setText(personName);
            }

        }
    }


    @Override
    public void onRefresh() {
    }

}

