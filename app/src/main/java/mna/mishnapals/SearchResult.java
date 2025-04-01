/*
User can search for a specific case by name. However, it will only display ne result.
 */
package mna.mishnapals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import mna.mishnapals.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by MNA on 8/13/2017.
 */

public class SearchResult extends Toolbar_parent{//AppCompatActivity{
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.search_results);
        final TextView searchResultName = (TextView)findViewById(R.id.searchResultName);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query caseId = ref.child("cases").orderByChild("caseId").equalTo(getIntent().getStringExtra("caseId").toLowerCase());
        //TODO Add email address to contact admin
                caseId.addListenerForSingleValueEvent(new ValueEventListener() {                        @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()){
                        searchResultName.setText("No match found");//. \n If you forgot the user ID, email the name of the deceased and the end date of the mishnayos to us and we will try to assist you.");
                    }
                    else {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final DataSnapshot snap = snapshot;
                            String date = snapshot.child("date").child("0").getValue() + "/" + snapshot.child("date").child("1").getValue() + "/" + snapshot.child("date").child("2").getValue();
                            SimpleDateFormat formatWithMonthName = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

                            try {
                                searchResultName.setText((String) snapshot.child("firstName").getValue() + " ben/bas " + snapshot.child("fathersName").getValue() + "\n"  + formatWithMonthName.format(simpleDateFormat.parse(date)));//date);
                                //Note: In above line the newline character wasn't working until I removed the 'inputType:textPersonName' from the XML
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

