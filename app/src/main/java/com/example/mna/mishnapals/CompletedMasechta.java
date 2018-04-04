/*
When user taps 'Completed' to enter that a masechta was completed, the database is updated to reflect that;
both in the user's branch and in the case's branch
 */
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
        UtilMishnayosNumbers u = new UtilMishnayosNumbers();
        super.onCreate(savedInstance);
        setContentView(R.layout.completed_masechta);

        TextView masechta = (TextView)findViewById(R.id.masechtaCompleted);
        masechta.setText(getIntent().getStringExtra("masechta"));

        //masechta.setText(getIntent().getStringExtra("masechta"));

        findViewById(R.id.completedMasechtaBut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final DatabaseReference refRoot = FirebaseDatabase.getInstance().getReference();
                Query currUser = refRoot.child("users").orderByChild("userEmail").equalTo(user.getEmail());
                currUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            dataSnapshot = ds.child("cases");}

                        final DatabaseReference ref = dataSnapshot.getRef();//.child(getIntent().getStringExtra("caseId")).child("finished").setValue(true);
                        Query query = ref.orderByChild("caseId").equalTo(getIntent().getStringExtra("caseId"));
                        /*
                        Go through all masechtos taken by this user until you reach the one being seeked, then set it's
                        completion flag to true. Then set the completion flag for the masechta in its case to true as well
                         */
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    Log.d("aaaab", getIntent().getStringExtra("masechta")+" "+getIntent().getStringExtra("caseId"));
                                    String masechtaCompleted = getIntent().getStringExtra("masechta");
                                    if(ds.child("masechtaTaken").getValue().equals(masechtaCompleted))
                                        ds.child("finished").getRef().setValue(true);
                                    Log.d("cholHamoed", ""+(getIntent().getStringExtra("caseId")+" "+masechtaCompleted)+" "+UtilMishnayosNumbers.mishnaNums.get(masechtaCompleted).getSederNum());
                                    //Query query1 = refRoot.child("cases").orderByChild()
                                    refRoot.child("cases").child(getIntent().getStringExtra("caseId")).child("masechtos").child(""+UtilMishnayosNumbers.mishnaNums.get(masechtaCompleted).getSederNum()).child(""+UtilMishnayosNumbers.mishnaNums.get(masechtaCompleted).getMasechtaNum()).child("completed").getRef().setValue(true);
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
                        CompletedMasechta.this.finish();
                    }
                },1000);
            }
        });
    }
}
