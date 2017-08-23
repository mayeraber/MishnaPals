package com.example.mna.mishnapals;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by MNA on 8/16/2017.
 */

public class CompletedMasechta extends AppCompatActivity {
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.completed_masechta);

        TextView masechta = (TextView)findViewById(R.id.masechtaCompleted);
        masechta.setText(getIntent().getStringExtra("masechta"));

        //masechta.setText(getIntent().getStringExtra("masechta"));

        findViewById(R.id.completedMasechtaBut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
                Query currUser = ref.orderByChild("userEmail").equalTo(user.getEmail());
                currUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            dataSnapshot = ds.child("cases");}

                        DatabaseReference ref = dataSnapshot.getRef();//.child(getIntent().getStringExtra("caseId")).child("finished").setValue(true);
                        Query query = ref.orderByChild("caseId").equalTo(getIntent().getStringExtra("caseId"));
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    Log.d("aaaab", getIntent().getStringExtra("masechta")+" "+getIntent().getStringExtra("caseId"));
                                    if(ds.child("masechtaTaken").getValue().equals(getIntent().getStringExtra("masechta")))
                                        ds.child("finished").getRef().setValue(true);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getBaseContext(), MyMishnayos.class));
                    }
                },1000);
            }
        });
    }
}
