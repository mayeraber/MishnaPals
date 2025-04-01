/*
switched to firebaseui
 */
package mna.mishnapals;

import android.content.Intent;
import androidx.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mna.mishnapals.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignInOptions extends AppCompatActivity {
    protected static GoogleApiClient googleApiClient;

    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth fAuth;
    private DatabaseReference mDatabase, usersEndpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_options);
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


        SignInButton signInButton = (SignInButton)findViewById(R.id.googleSigninOption);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        findViewById(R.id.emailPasswordSignin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), EmailPasswordSignin.class));
            }
        });
        findViewById(R.id.googleSigninOption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                //startActivity(new Intent(getBaseContext(), GoogleSignIn.class));
            }
        });
        findViewById(R.id.signOutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // fAuth.signOut();
               // Auth.GoogleSignInApi.signOut(googleApiClient);
            }
        });
        new UtilMishnayosNumbers();
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                final GoogleSignInAccount account = result.getSignInAccount();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                fAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
                            Query userExists = ref.orderByChild("userEmail").equalTo(account.getEmail());
                            userExists.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(!dataSnapshot.exists()){
                                        FirebaseUser user = fAuth.getCurrentUser();
                                        User newUser = new User();
                                        newUser.setUserEmail(user.getEmail());
                                        newUser.setUserId(user.getUid());
                                        newUser.setCases(null);
                                        mDatabase = FirebaseDatabase.getInstance().getReference();
                                        usersEndpoint = mDatabase.child("users");
                                        String key = usersEndpoint.push().getKey();
                                        usersEndpoint.child(key).setValue(newUser);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            startActivity(new Intent(getBaseContext(), HomeScreen.class));
                            SignInOptions.this.finish();
                        }
                    }
                });
            }
        }
    }
}
