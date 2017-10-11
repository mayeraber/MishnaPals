package com.example.mna.mishnapals;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailPasswordSignin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button createAccount;
    EditText emailEntry, passEntry;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password_signin);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currUser = mAuth.getCurrentUser();
        createAccount = (Button)findViewById(R.id.createAccountEmailPass);
        emailEntry = (EditText)findViewById(R.id.emailPassEmail);
        passEntry = (EditText)findViewById(R.id.emailPassPas);
        email = emailEntry.getText().toString();
        password = passEntry.getText().toString();
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               newAccount(email, password);
            }
        });
    }

    public void newAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getBaseContext(), HomeScreen.class));
                            EmailPasswordSignin.this.finish();
                        }
                        else{

                        }
                    }
                });
    }
}
