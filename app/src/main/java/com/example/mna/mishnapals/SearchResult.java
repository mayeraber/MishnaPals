package com.example.mna.mishnapals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by MNA on 8/13/2017.
 */

public class SearchResult extends AppCompatActivity{
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.search_results);
        final TextView searchResultName = (TextView)findViewById(R.id.searchResultName);
        TextView searchResultDate = (TextView)findViewById(R.id.searchResultDate);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query caseId = ref.child("cases").orderByChild("caseId").equalTo(getIntent().getStringExtra("caseId"));

                caseId.addListenerForSingleValueEvent(new ValueEventListener() {                        @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        final DataSnapshot snap = snapshot;
                        String date = snapshot.child("date").child("1").getValue()+"/"+snapshot.child("date").child("2").getValue()+"/"+snapshot.child("date").child("0").getValue();
                        searchResultName.setText((String)snapshot.child("firstName").getValue()+" ben "+snapshot.child("fathersName").getValue()+" "+date);
                        searchResultName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getBaseContext(), MasechtosList.class);
                                intent.putExtra("caseKey", snap.getRef().getKey());
                                startActivity(intent);
                            }
                        });


                    }}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


    }

