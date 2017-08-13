package com.example.mna.mishnapals;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeScreen extends AppCompatActivity {

    Button searchCaseButton;
    EditText caseSearch;
    //FirebaseDatabase fbdb;
    //DatabaseReference ref;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();

//if(user==null)
        startActivity(new Intent(getBaseContext(), EmailPassword.class));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        View layout = findViewById(R.id.backgroundLayoutHome);
        Drawable background = layout.getBackground();
        background.setAlpha(50);
        Button newCase = (Button) findViewById(R.id.newCase);
        newCase.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        buttonClicked(v);
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


        searchCaseButton = (Button) findViewById(R.id.searchCaseButton);
        caseSearch = (EditText) findViewById(R.id.caseIdSearch);
        searchCaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void buttonClicked(View view)
    {
        Intent intent = new Intent(getBaseContext(), NewCase.class);
        startActivity(intent);
    }

    public void viewAllClicked(View view)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("CURRENTUSERHome", user.getUid());
        Intent intent = new Intent(getBaseContext(), MasechtosList.class);
        startActivity(intent);
    }
}
