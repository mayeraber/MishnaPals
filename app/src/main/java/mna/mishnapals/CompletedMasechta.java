/*
When user taps 'Completed' to enter that a masechta was completed, the database is updated to reflect that;
both in the user's branch and in the case's branch
 */
package mna.mishnapals;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import mna.mishnapals.R;
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

public class CompletedMasechta extends Toolbar_parent {
    public void onCreate(Bundle savedInstance){
        //UtilMishnayosNumbers u = new UtilMishnayosNumbers();
        super.onCreate(savedInstance);
        setContentView(R.layout.completed_masechta);

        TextView masechta = (TextView)findViewById(R.id.masechtaCompleted);
        masechta.setText(getIntent().getStringExtra("masechta"));

        findViewById(R.id.completedMasechtaBut).setOnClickListener(v -> {
            boolean internetConnected = InternetCheckUtility.internetStatus();
            if (!internetConnected) {
                Toast.makeText(CompletedMasechta.this, "Please check your internet connection - It looks like you might be offline", Toast.LENGTH_SHORT).show();
            }
            else{
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final DatabaseReference refRoot = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userCases = refRoot.child("users").child(user.getUid()).child("cases");
            String masechtaCompleted = getIntent().getStringExtra("masechta");
            refRoot.child("cases").child(getIntent().getStringExtra("caseId")).child("masechtos").child(""+UtilMishnayosNumbers.mishnaNums.get(masechtaCompleted).getSederNum()).child(""+UtilMishnayosNumbers.mishnaNums.get(masechtaCompleted).getMasechtaNum()).child("completed").getRef().setValue(true);


            Query query = userCases.orderByChild("caseId").equalTo(getIntent().getStringExtra("caseId"));
            /*
            Go through all masechtos taken by this user until you reach the one being seeked, then set it's
            completion flag to true. Then set the completion flag for the masechta in its case to true as well
            */
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        String masechtaCompleted = getIntent().getStringExtra("masechta");
                        if(ds.child("masechtaTaken").getValue().equals(masechtaCompleted))
                            ds.child("finished").getRef().setValue(true);
                        //Query query1 = refRoot.child("cases").orderByChild()
                        refRoot.child("cases").child(getIntent().getStringExtra("caseId")).child("masechtos").child(""+UtilMishnayosNumbers.mishnaNums.get(masechtaCompleted).getSederNum()).child(""+UtilMishnayosNumbers.mishnaNums.get(masechtaCompleted).getMasechtaNum()).child("completed").getRef().setValue(true);
                    }
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
        }});
    }
}
