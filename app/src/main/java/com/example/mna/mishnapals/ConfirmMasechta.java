package com.example.mna.mishnapals;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ConfirmMasechta extends AppCompatActivity {

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_masechta);
        dbRef = FirebaseDatabase.getInstance().getReference();

        final String caseKey = getIntent().getStringExtra("caseKey");
        final String seder = getIntent().getStringExtra("seder");
        final String masechtaNum = getIntent().getStringExtra("masechtaNum");

        final Masechta masechta = (Masechta) getIntent().getSerializableExtra("Masechta");
        TextView confirmMasechta = (TextView)findViewById(R.id.masechtaConfirmDetails);
        confirmMasechta.setText("מסכת "+masechta.hebName);
        TextView confirmPerakim = (TextView)findViewById(R.id.numPerakimConfirm);
        confirmPerakim.setText(masechta.numPerakim+"  פרקים");
        TextView confirmMishnayos = (TextView)findViewById(R.id.numMishnayosConfirm);
        confirmMishnayos.setText(masechta.numMishnayos+"  משניות");
        Button reserveBut = (Button)findViewById(R.id.confirmMasechtaButton);

        final Thread thread = new Thread(){
            @Override
            public void run(){
                try{
                    Thread.sleep(2500);
                    ConfirmMasechta.this.finish();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        reserveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query userEmail = ref.child("users").orderByChild("userEmail").equalTo(user.getEmail());
                Log.d("emailNow", user.getEmail());
                userEmail.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.getChildren())
                            snapshot.getRef().child("cases").push().setValue(new CaseTakenInfo(masechta.engName, caseKey));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                Log.d("hiiiiiiiii", caseKey+""+seder+""+masechtaNum);
                dbRef.child("cases").child(caseKey).child("masechtos").child(""+seder).child(""+masechtaNum).child("status").setValue(true, new DatabaseReference.CompletionListener(){
                    public void onComplete(DatabaseError error, DatabaseReference ref){
                        Toast.makeText(getBaseContext(), "Success!!", Toast.LENGTH_SHORT).show();
                     /*   new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ConfirmMasechta.this.finish();
                            }
                        },2500);
                     */
                        thread.start();
                    }
                });



            }
        });
    }
}
