/*
User can search for a specific case by name. However, it will only display ne result.
 */
//TODO Either enforce that 2 cases cant be called by same name, because a search can only display one, or perhaps allow it but enable the search results to show multiple results
package com.example.mna.mishnapals;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by MNA on 8/13/2017.
 */

public class SearchResult extends Toolbar_parent{//AppCompatActivity{
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.search_results);
        final TextView searchResultName = (TextView)findViewById(R.id.searchResultName);
        TextView searchResultDate = (TextView)findViewById(R.id.searchResultDate);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query caseId = ref.child("cases").orderByChild("caseId").equalTo(getIntent().getStringExtra("caseId"));
        //TODO Add email address to contact admin
                caseId.addListenerForSingleValueEvent(new ValueEventListener() {                        @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()){
                        searchResultName.setText("No match found. \n If you forgot the user ID, email the name of the deceased and the end date of the mishnayos to us and we will try to assist you.");
                    }
                    else {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final DataSnapshot snap = snapshot;
                            String date = snapshot.child("date").child("0").getValue() + "/" + snapshot.child("date").child("1").getValue() + "/" + snapshot.child("date").child("2").getValue();
                            SimpleDateFormat formatWithMonthName = new SimpleDateFormat("MMM dd, yyyy");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");;//new SimpleDateFormat("MM/dd/yyyy");

                            try {
                                searchResultName.setText((String) snapshot.child("firstName").getValue() + " ben " + snapshot.child("fathersName").getValue() + "\n"  + formatWithMonthName.format(simpleDateFormat.parse(date)));//date);
                                //Note: In ablove line the newline character wasnt working until I removed the 'inputType:textPersonName' from the XML
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            searchResultName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SearchResult.this.finish();
                                    Intent intent = new Intent(getBaseContext(), MasechtosList.class);
                                    intent.putExtra("caseKey", snap.getRef().getKey());
                                    startActivity(intent);
                                }
                            });


                        }
                    }
                }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


    }

