/*
Displays list of user's taken masechtos and allows user to choose one and open 'CompletedMasechta' activity
to then be able to change status to completed
TODO Maybe add functionality to remove list item with swipe to side
 */
package com.example.mna.mishnapals;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by MNA on 8/13/2017.
 */

public class MyMishnayos extends Toolbar_parent {  //extends AppCompatActivity {

    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.my_mishnayos);

        final ListView listMishnayos = findViewById(R.id.listMishnayos);

        final ArrayList<CaseTakenInfo> cases = new ArrayList<>();
        final ListAdapter listAdapter = new ListAdapter(getBaseContext() , R.layout.my_masechta, cases);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        /*
        Populate 'cases' arraylist from user's branch in db with the cases this user participates in
         */
        Query currUser = ref.child("users").orderByChild("userEmail").equalTo(user.getEmail());//.getRef().child("cases");
        Log.d("curr", user.getEmail());
        currUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cases.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    dataSnapshot = ds.child("cases");}

                for(DataSnapshot userCase : dataSnapshot.getChildren()){
                    CaseTakenInfo caseTaken = userCase.getValue(CaseTakenInfo.class);
                    Log.d("curr6", caseTaken.getMasechtaTaken()+" "+caseTaken.isFinished());
                    cases.add(caseTaken);
                }
                listMishnayos.setAdapter(listAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*
        If user selects an unfinished masechta, open the 'CompletedMasechta' activity to enable user to change status to completed
         */
        listMishnayos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!cases.get(position).isFinished()) {
                    Intent intent = new Intent(getBaseContext(), CompletedMasechta.class);
                    //CaseTakenInfo caseTakenInfo = cases.get(position);
                    intent.putExtra("caseId", cases.get(position).getCaseId());
                    intent.putExtra("masechta", cases.get(position).getMasechtaTaken());
                    startActivity(intent);
                    //TODO maybe change to manually update the listitem instead of ending activity and then restarting
                    MyMishnayos.this.finish();
                }
            }
        });
    }

    /*
    Create the list of masechtos
     */
    private class ListAdapter extends ArrayAdapter<CaseTakenInfo>
    {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("cases");

        public ListAdapter(Context context, int textViewResourceId){
            super(context, textViewResourceId);
        }

        public ListAdapter(Context context, int textViewResourceId, List<CaseTakenInfo> casesTaken){
            super(context, textViewResourceId, casesTaken);
        }

        /*
        Fills in the fields of each masechta in the list with masechta name, date to complete, etc
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            View v = convertView;
            if (v == null) {
                LayoutInflater li;
                li = LayoutInflater.from(getContext());
                v = li.inflate(R.layout.my_masechta, null);
                holder = new ViewHolder();
                holder.masechta = v.findViewById(R.id.masechtaName);
                v.setTag(holder);
            }
            else {
                holder = (ViewHolder)v.getTag();
            }

            final CaseTakenInfo caseTaken = getItem(position);
            Log.d("aaaaa", "" + caseTaken.getMasechtaTaken());

            if (caseTaken != null) {
                holder.masechta.setText("Masechta: "+caseTaken.getMasechtaTaken());
                final TextView completionStatus = (TextView) v.findViewById(R.id.completionStatus);
                final TextView nameNiftar = (TextView) v.findViewById(R.id.nameNiftar);
                DatabaseReference dref = dbref.child(caseTaken.getCaseId());
                dref = dref.child("firstName");
                dref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        nameNiftar.setText("Name of Niftar: "+((String)dataSnapshot.getValue()));
                        completionStatus.setText("Status: "+(caseTaken.isFinished()?"Completed":"Not Completed"));
                        Log.d("curr6", "resetStat "+caseTaken.getMasechtaTaken());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                /*
                Retreive date niftar from db and fill it in
                 */
                final TextView dateNiftar = (TextView) v.findViewById(R.id.dateNiftar);
                final TextView timeRemaining = (TextView) v.findViewById(R.id.timeRemaining);
                final ColorStateList textViewDefaultColor = dateNiftar.getTextColors();
                dref = dbref.child(caseTaken.getCaseId()).child("date");
                dref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Integer[] YearMonthDay = new Integer[4];
                        String date = "";
                        int counter=0;
                        for (DataSnapshot sp : dataSnapshot.getChildren()) {
                            counter++;
                            YearMonthDay[counter] = sp.getValue(Integer.class);
                            Log.d("aaaa", "" + sp.getValue(Integer.class));
                            date += sp.getValue(Integer.class)+(counter<3 ? "/":"");
                        }
                        Log.d("abababa", ""+YearMonthDay);
                        //Log.d("aaaa", ""+dataSnapshot.getValue(List<>.class));
                        // dateNiftar.setText(dataSnapshot.child("0").getValue(Integer.class));
                        dateNiftar.setText("End Date: "+date);

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            LocalDate localDate = LocalDate.of(YearMonthDay[1], YearMonthDay[2], YearMonthDay[3]);
                            LocalDate today = LocalDate.now();
                            int date_passed = localDate.compareTo((today));
                            Period p = Period.between(localDate, today);
                            if (date_passed < 0) {
                                if (p.getYears() > 1) {
                                    timeRemaining.setText("Time Remaining: Ended " + p.getYears() + " years, " + p.getMonths() + " months, " + p.getDays() + " days ago");
                                }
                                else if (p.getMonths() > 1){
                                    timeRemaining.setText("Time Remaining: Ended " + p.getMonths() + " months, " + p.getDays() + " days ago");
                                }
                                else {
                                    timeRemaining.setText("Time Remaining: Ended " + p.getDays() + " days ago");
                                }
                            }
                            else {
                                timeRemaining.setText("Time Remaining: " + p.getMonths()+" months, "+p.getDays() + " days");
                            }
                        }else{
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                            try {
                                Date formattedDate = simpleDateFormat.parse(date);
                                Date currDate = Calendar.getInstance().getTime();
                                long diff = formattedDate.getTime() - currDate.getTime();
                                int days = (int)Math.ceil((double)diff/(double)(24*60*60*1000));
                                if(days>0){
                                    timeRemaining.setTextColor(textViewDefaultColor);
                                    timeRemaining.setText("Time Remaining: "+ days + " days");}
                                else if(days == 0){
                                    timeRemaining.setTextColor(Color.RED);
                                    timeRemaining.setText("Time Remaining: ENDS TODAY");}
                                else if(days < 0){
                                    timeRemaining.setTextColor(Color.LTGRAY);
                                    timeRemaining.setText("Time Remaining: Ended "+ Math.abs(days) + " days ago");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                                timeRemaining.setText("Error Calculating Time");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            return v;
        }

    }
    static class ViewHolder{
        TextView masechta;
    }
}
