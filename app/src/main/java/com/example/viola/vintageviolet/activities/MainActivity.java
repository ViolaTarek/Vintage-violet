package com.example.viola.vintageviolet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.viola.vintageviolet.R;
import com.example.viola.vintageviolet.fragments.FavoriteFragment;
import com.example.viola.vintageviolet.fragments.homeFragment;
import com.example.viola.vintageviolet.fragments.stylesFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, SwipeRefreshLayout.OnRefreshListener {

    ImageView profile_pic;
    TextView username;
    TextView userEmail;
    String userID;
    GoogleApiClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    MenuItem signing;
    String savedState = "home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
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
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            homeFragment home = new homeFragment();
            transaction.replace(R.id.fragment_styles, home);
            transaction.commit();


        } else
            changeState(savedInstanceState.getString("state"));

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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
                savedState = "casual";
                break;

            case (R.id.classy):
                bundle.putString("category", "classy");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                savedState = "classy";
                break;
            case (R.id.autumn):
                bundle.putString("category", "autumn");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                savedState = "autumn";
                break;
            case (R.id.winter):
                bundle.putString("category", "winter");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                savedState = "winter";
                break;
            case (R.id.summer):
                bundle.putString("category", "summer");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                savedState = "summer";
                break;
            case (R.id.spring):
                bundle.putString("category", "spring");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                savedState = "spring";
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
                    Toast.makeText(this, R.string.sign_in_to_open_styles, Toast.LENGTH_LONG).show();
                }
                savedState = "favorite";

                break;
            default:
                return true;

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

    public void changeState(String state) {

        Bundle bundle = new Bundle();
        stylesFragment myFrag = new stylesFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        homeFragment home = new homeFragment();

        switch (state) {
            case "styles":
                bundle.putString("category", "dress");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                savedState = "styles";
                break;
            case "casual":
                bundle.putString("category", "casual");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                savedState = "casual";
                break;
            case "classy":
                bundle.putString("category", "classy");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                savedState = "classy";

                break;
            case "autumn":
                bundle.putString("category", "autumn");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                savedState = "autumn";

                break;
            case "winter":
                bundle.putString("category", "winter");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                savedState = "winter";

                break;
            case "summer":
                bundle.putString("category", "summer");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                savedState = "summer";

                break;
            case "spring":
                bundle.putString("category", "spring");
                bundle.putString("userid", userID);
                myFrag.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, myFrag);
                transaction.commit();
                savedState = "spring";

                break;
            case "home":
                bundle.putString("userid", userID);
                home.setArguments(bundle);
                transaction.replace(R.id.fragment_styles, home);
                transaction.commit();
                savedState = "home";

                break;
            case "favorite":
                if (userID != null) {
                    bundle.putString("userid", userID);
                    FavoriteFragment fav = new FavoriteFragment();
                    fav.setArguments(bundle);
                    transaction.replace(R.id.fragment_styles, fav);
                    transaction.commit();
                }
                savedState = "favorite";

                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("state", savedState);
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