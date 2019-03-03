package com.example.viola.vintageviolet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , GoogleApiClient.OnConnectionFailedListener {

    ImageView profile_pic;
    TextView username;
    TextView userEmail;
    GoogleApiClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    MenuItem signing;
    StorageReference storageRef;

    FirebaseStorage storage;
    StorageReference imagesRef;
    StorageReference style1Ref;

    ImageView style1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        signing=navigationView.getMenu().getItem(0);
        style1 =findViewById(R.id.style1);
        FirebaseApp.initializeApp(this);

        storageRef = storage.getInstance().getReference();
        imagesRef = storageRef.child("home");
        style1Ref = imagesRef.child("3.jpg");

        Toast.makeText(this,"image url = "+style1Ref,Toast.LENGTH_LONG).show();

        Glide.with(this).load(style1Ref).into(style1);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            signIn();
            signing.setTitle("Sign out");
        }
        else{
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.sign_in) {
           if(item.getTitle()=="Sign In"){
            signIn();
           item.setTitle("Sign out");
           }
           else{
               signOut();
               item.setTitle("Sign In");

           }
        }

            /*  else if (id == R.id.Summer) {

        }
         else if (id == R.id.Autumn) {

        }
         else if (id == R.id.Spring) {

        }
         else if (id == R.id.nav_share) {

        }
         else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        username.setVisibility(View.VISIBLE);
        userEmail.setVisibility(View.VISIBLE);

    }

    public void signOut(){
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
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                String personPhoto = account.getPhotoUrl().toString();
                String personEmail = account.getEmail();
                String personName = account.getDisplayName();
                if(personPhoto!=null) {
                    Glide.with(this)
                            .load(personPhoto)
                            .into(profile_pic);
                }

                userEmail.setText(personEmail);
                username.setText(personName);
            }

        }
    }
}
