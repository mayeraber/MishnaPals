package com.example.mna.mishnapals;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    protected ArrayList<CaseTakenInfo> cases;
    private View.OnClickListener mOnClickLis;
    RecyclerView mReyclerView;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView masechta;
        TextView nameNiftar;
        TextView dateNiftar;
        TextView timeRemaining;
        TextView completionStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View
            masechta = (TextView) itemView.findViewById(R.id.masechtaName);
            nameNiftar = (TextView) itemView.findViewById(R.id.nameNiftar);
            dateNiftar = (TextView) itemView.findViewById(R.id.dateNiftar);
            timeRemaining = (TextView) itemView.findViewById(R.id.timeRemaining);
            completionStatus = (TextView) itemView.findViewById(R.id.completionStatus);
        }

        public TextView getMasechta() {
            return masechta;
        }

        public TextView getNameNiftar() {
            return nameNiftar;
        }

        public TextView getDateNiftar() {
            return dateNiftar;
        }

        public TextView getTimeRemaining() {
            return timeRemaining;
        }

        public TextView getCompletionStatus() {
            return completionStatus;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param casesList CaseTakenInfo containing the data to populate views to be used
     *                  by RecyclerView
     */
    public CustomAdapter(ArrayList<CaseTakenInfo> casesList) {
        cases = casesList;
    }

    // Create new views (invoked by the layout manager)

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mReyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.my_masechta, viewGroup, false);
        mOnClickLis = new MyMishnayosOnClickListener(mReyclerView, cases);
        view.setOnClickListener(mOnClickLis);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final CaseTakenInfo caseTaken = cases.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        /*
        Populate 'cases' arraylist from user's branch in db with the cases this user participates in
         */
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("cases");

        DatabaseReference dref = dbref.child(caseTaken.getCaseId());
        dref = dref.child("firstName");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewHolder.getNameNiftar().setText("Name of Niftar: " + ((String) dataSnapshot.getValue()));
                viewHolder.getCompletionStatus().setText("Status: " + (caseTaken.isFinished() ? "Completed" : "Not Completed"));
                viewHolder.getMasechta().setText("Masechta: " + caseTaken.getMasechtaTaken());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //final TextView dateNiftar = (TextView) v.findViewById(R.id.dateNiftar);
        //final TextView timeRemaining = (TextView) v.findViewById(R.id.timeRemaining);
        final ColorStateList textViewDefaultColor = viewHolder.getDateNiftar().getTextColors();
        dref = dbref.child(caseTaken.getCaseId()).child("date");
        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer[] YearMonthDay = new Integer[4];
                String date = "";
                int counter = 0;
                for (DataSnapshot sp : dataSnapshot.getChildren()) {
                    counter++;
                    YearMonthDay[counter] = sp.getValue(Integer.class);
                    date += sp.getValue(Integer.class) + (counter < 3 ? "/" : "");
                }
                // dateNiftar.setText(dataSnapshot.child("0").getValue(Integer.class));
                viewHolder.getDateNiftar().setText("End Date: " + date);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDate localDate = LocalDate.of(YearMonthDay[1], YearMonthDay[2], YearMonthDay[3]);
                    LocalDate today = LocalDate.now();
                    int date_passed = localDate.compareTo((today));
                    if (date_passed < 0) {
                        Period p = Period.between(localDate, today);
                        if (p.getYears() > 1) {
                            viewHolder.getTimeRemaining().setText("Time Remaining: Ended " + p.getYears() + " years, " + p.getMonths() + " months, " + p.getDays() + " days ago");
                        } else if (p.getMonths() > 1) {
                            viewHolder.getTimeRemaining().setText("Time Remaining: Ended " + p.getMonths() + " months, " + p.getDays() + " days ago");
                        } else {
                            viewHolder.getTimeRemaining().setText("Time Remaining: Ended " + p.getDays() + " days ago");
                        }
                    } else {
                        Period p = Period.between(today, localDate);
                        if (p.getMonths() > 0) {
                            viewHolder.getTimeRemaining().setText("Time Remaining: " + p.getMonths() + " months, " + p.getDays() + " days");
                        } else {
                            viewHolder.getTimeRemaining().setText("Time Remaining: " + p.getDays() + " days");
                        }
                    }
                } else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        Date formattedDate = simpleDateFormat.parse(date);
                        Date currDate = Calendar.getInstance().getTime();
                        long diff = formattedDate.getTime() - currDate.getTime();
                        int days = (int) Math.ceil((double) diff / (double) (24 * 60 * 60 * 1000));
                        if (days > 0) {
                            viewHolder.getTimeRemaining().setTextColor(textViewDefaultColor);
                            viewHolder.getTimeRemaining().setText("Time Remaining: " + days + " days");
                        } else if (days == 0) {
                            viewHolder.getTimeRemaining().setTextColor(Color.RED);
                            viewHolder.getTimeRemaining().setText("Time Remaining: ENDS TODAY");
                        } else if (days < 0) {
                            viewHolder.getTimeRemaining().setTextColor(Color.LTGRAY);
                            viewHolder.getTimeRemaining().setText("Time Remaining: Ended " + Math.abs(days) + " days ago");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        viewHolder.getTimeRemaining().setText("Error Calculating Time");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cases.size();
    }

    public void deleteItemFromFirebase(int position) {
        String caseTakenID = cases.get(position).getCaseTakenID();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query currUser = ref.child("users").orderByChild("userEmail").equalTo(user.getEmail());//.getRef().child("cases");
        currUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cases.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    dataSnapshot = ds.child("cases");}

                for(DataSnapshot userCase : dataSnapshot.getChildren()){
                    if (userCase.getKey().equals(caseTakenID)) {
                        userCase.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}