/*
Display a confirmation screen with the case and masechta the user selected. When user confirms the selection
update the db, set alarm, and display toast confirmation message
 */
package com.example.mna.mishnapals;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.GregorianCalendar;

import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
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
import java.util.Calendar;

public class ConfirmMasechta extends Toolbar_parent {

    DatabaseReference dbRef;
    String caseTakenKey;
    Masechta masechta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_masechta);
        dbRef = FirebaseDatabase.getInstance().getReference();

        final String caseKey = getIntent().getStringExtra("caseKey");
        caseTakenKey = caseKey;
        final String seder = getIntent().getStringExtra("seder");
        final String masechtaNum = getIntent().getStringExtra("masechtaNum");

        masechta = (Masechta) getIntent().getSerializableExtra("Masechta");
        TextView confirmMasechta = (TextView)findViewById(R.id.masechtaConfirmDetails);
        confirmMasechta.setText("מסכת "+masechta.hebName);
        TextView confirmPerakim = (TextView)findViewById(R.id.numPerakimConfirm);
        confirmPerakim.setText(masechta.numPerakim+"  פרקים");
        TextView confirmMishnayos = (TextView)findViewById(R.id.numMishnayosConfirm);
        confirmMishnayos.setText(masechta.numMishnayos+"  משניות");
        Button reserveBut = (Button)findViewById(R.id.confirmMasechtaButton);
        /*final Thread thread = new Thread(){
            @Override
            public void run(){
                try{
                    //Thread.sleep(2500);
                    //ConfirmMasechta.this.finish();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        };*/
        /*
        When user confirms selection, add a new 'CaseTakenInfo' to the user's branch in the db, and then set
        the masechta status to true (i.e. taken) in the case's branch in the db. Also set alarm to remind user
        about the masechta taken. Display progress circle as db is updated.
        Lastly, display a toast message that the reservation was successful
         */
        reserveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.timerCircle).setVisibility(View.VISIBLE);
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
                //setAlarm(); Stopped using for now. See function below

                Log.d("hiiiiiiiii", caseKey+""+seder+""+masechtaNum);
                dbRef.child("cases").child(caseKey).child("masechtos").child(""+seder).child(""+masechtaNum).child("status").setValue(true, new DatabaseReference.CompletionListener(){
                    public void onComplete(DatabaseError error, DatabaseReference ref1){

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d("hiiiiiiiii", "reached calendar event");
                                String endYear = dataSnapshot.child("cases").child(caseTakenKey).child("date").child("0").getValue().toString();
                                String endMonth = dataSnapshot.child("cases").child(caseTakenKey).child("date").child("1").getValue().toString();
                                String endDay = dataSnapshot.child("cases").child(caseTakenKey).child("date").child("2").getValue().toString();

                                //add event to users calendar
                                Calendar beginTime = Calendar.getInstance();
                                //calendar months start at '0', so subtract one from month
                                beginTime.set(Integer.parseInt(endYear), (Integer.parseInt(endMonth)-1), (Integer.parseInt(endDay)), 12, 00 ,0);
                                Calendar endTime = Calendar.getInstance();
                                endTime.set(Integer.parseInt(endYear), (Integer.parseInt(endMonth)-1), (Integer.parseInt(endDay)), 14, 00 ,0);
                                Intent intent = new Intent(Intent.ACTION_INSERT)
                                        .setData(Events.CONTENT_URI)
                                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                                        .putExtra(Events.TITLE, "MishnaPals Reminder")
                                        .putExtra(Events.DESCRIPTION, "Finish mishnayos " + masechta.hebName +" tonight");
                                        //.putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
                                findViewById(R.id.timerCircle).setVisibility(View.INVISIBLE);
                                Toast.makeText(getBaseContext(), "Success!!", Toast.LENGTH_SHORT).show();
                                ConfirmMasechta.this.finish();
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        /*
                        findViewById(R.id.timerCircle).setVisibility(View.INVISIBLE);

                        Toast.makeText(getBaseContext(), "Success!!", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ConfirmMasechta.this.finish();
                            }
                        },2500);
                        */
                        //thread.start();
                    }
                });
            }
        });
    }

    //TODO work more on the alarm
    //UPDATE 2/2024: Dropped the alarm feature for the foreseeable future. Switched to Calendar reminder. See AlarmSetter.java comments
    public void setAlarm()
    {
        final Calendar endDate = Calendar.getInstance();
        //get date to parse https://stackoverflow.com/questions/12473550/how-to-convert-string-date-to-long-millseconds and then send to notification
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String endMonth = dataSnapshot.child("cases").child(caseTakenKey).child("date").child("0").getValue().toString();
                String endDay = dataSnapshot.child("cases").child(caseTakenKey).child("date").child("1").getValue().toString();
                String endYear = dataSnapshot.child("cases").child(caseTakenKey).child("date").child("2").getValue().toString();

                //calendar months start at '0', so subtract one from month
                endDate.set(Integer.parseInt(endYear), (Integer.parseInt(endMonth)-1), (Integer.parseInt(endDay)), 8, 35 ,3);
                Intent alarmIntent = new Intent(ConfirmMasechta.this, AlarmSetter.class);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, endDate.getTimeInMillis(), PendingIntent.getBroadcast(ConfirmMasechta.this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Long time = new GregorianCalendar().getTimeInMillis()+30000;

        //Toast.makeText(this, "Alarm scheduled", Toast.LENGTH_LONG).show();
    }
}