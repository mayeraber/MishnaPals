//TODO Work more on the sign in options
/*
Home screen class with signin, and home-screen options of new case,
all avail mishnayos, and user's mishnayos
 */
package com.example.mna.mishnapals;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.mna.mishnapals.SignInOptions.googleApiClient;

public class HomeScreen extends AppCompatActivity {

    Button searchCaseButton;
    EditText caseSearch;
    private FirebaseAuth fAuth;

    //FirebaseDatabase fbdb;
    //DatabaseReference ref;
    FirebaseUser user;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        fAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient= new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApiIfAvailable(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.mishna_pals_menu, menu);
        return true;
    }

    /* Used before started using firebaseUI
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        fAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient);
        startActivity(new Intent(getBaseContext(), SignInOptions.class));
        HomeScreen.this.finish();
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getBaseContext(), FirebaseUI_Auth.class));
                        HomeScreen.this.finish();
                    }
                });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();

    if(user==null)
    {
           // startActivity(new Intent(getBaseContext(), EmailPassword.class));

        startActivity(new Intent(getBaseContext(), FirebaseUI_Auth.class));
            HomeScreen.this.finish();
    }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        //setContentView(R.layout.home_screen_horizontal);
        View layout = findViewById(R.id.backgroundLayoutHome);
        Drawable background = layout.getBackground();
        background.setAlpha(50);
        Button newCase = (Button) findViewById(R.id.newCase);
        newCase.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        newCaseClicked(v);
                    }
                }
        );

        Button allAvail = (Button) findViewById(R.id.viewAllAvailButton);
        allAvail.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewAllClicked(v);
                    }
                }
        );

        final Button myMishnayos = (Button)findViewById(R.id.myMishnayosButton);
        myMishnayos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMishnayosClicked(v);
            }
        });

        searchCaseButton = (Button) findViewById(R.id.searchCaseButton);
        caseSearch = (EditText) findViewById(R.id.caseIdSearch);

        searchCaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SearchResult.class);
                intent.putExtra("caseId", caseSearch.getText().toString());
                startActivity(intent);
            }
        });
    }
        public void newCaseClicked(View view)
        {
            Intent intent = new Intent(getBaseContext(), NewCase.class);
            startActivity(intent);
        }

        /*
        Check db for the public cases, put them into an arraylist and package into Intent to
        pass to "PublicCases' class
         */
        public void viewAllClicked(View view)
        {
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
            Query publicCase = dbref.child("cases").orderByChild("privateCase").equalTo(false);
            publicCase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final ArrayList<Case> publicCases = new ArrayList<Case>();
                    final ArrayList<String> publicCaseNames = new ArrayList<String>();
                    final ArrayList<String> publicCaseKeys = new ArrayList<String>();

                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        publicCaseKeys.add(ds.getKey());
                        publicCases.add(ds.getValue(Case.class));
                        publicCaseNames.add(ds.getValue(Case.class).getFirstName());
                        Log.d("aaaaa", ""+ds.getValue(Case.class).firstName);
                    }
                    Bundle extras = new Bundle();
                    extras.putSerializable("Arraylist", publicCases);
                    Intent intent = new Intent(getBaseContext(), PublicCases.class);
                    intent.putExtra("caseNames", publicCaseNames).putExtra("cases", extras).putExtra("caseIds", publicCaseKeys);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Log.d("CURRENTUSERHome", user.getUid());
            Intent intent = new Intent(getBaseContext(), MasechtosList.class);
            startActivity(intent);*/
        }

        public void myMishnayosClicked(View view)
        {
            startActivity(new Intent(getBaseContext(), MyMishnayos.class));
        }
    }
