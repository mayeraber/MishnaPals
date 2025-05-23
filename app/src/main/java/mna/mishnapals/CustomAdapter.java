package mna.mishnapals;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import mna.mishnapals.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    protected ArrayList<CaseTakenInfo> cases;
    private View.OnClickListener mOnClickLis;
    RecyclerView mReyclerView;
    protected Activity context;
    UtilMishnayosNumbers u;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView masechta;
        TextView nameNiftar;
        TextView nameFather;
        TextView dateNiftar;
        TextView timeRemaining;
        TextView completionStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View
            masechta = (TextView) itemView.findViewById(R.id.masechtaName);
            nameNiftar = (TextView) itemView.findViewById(R.id.nameNiftar);
            nameFather = (TextView) itemView.findViewById(R.id.nameFather);
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

        public TextView getNameFather() {
            return nameFather;
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
    public CustomAdapter(ArrayList<CaseTakenInfo> casesList, Activity context) {
        cases = casesList;
        u = new UtilMishnayosNumbers();
        this.context = context;
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
        mOnClickLis = new MyMishnayosOnClickListener(mReyclerView, cases, this.context);
        view.setOnClickListener(mOnClickLis);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final CaseTakenInfo caseTaken = cases.get(position);
        /*
        Populate 'cases' arraylist from user's branch in db with the cases this user participates in
         */
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("cases");
        DatabaseReference dref = dbref.child(caseTaken.getCaseId());
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Case nextCase = dataSnapshot.getValue(Case.class);
                nextCase.setFirebaseID(dataSnapshot.getKey());
                viewHolder.getNameNiftar().setText("Name of Niftar: " + (nextCase.getFirstName()));
                viewHolder.getNameFather().setText("Name of Father: " + (nextCase.getFathersName()));
                viewHolder.getCompletionStatus().setText("Status: " + (caseTaken.isFinished() ? "Completed" : "Not Completed"));
                if (!caseTaken.isFinished()) {
                    viewHolder.getCompletionStatus().setTextColor(Color.RED);
                }
                viewHolder.getMasechta().setText("Masechta: " + caseTaken.getMasechtaTaken());
                Integer[] YearMonthDay = nextCase.getDate().toArray(new Integer[0]);
                Integer[] MonthDayYear = nextCase.getDateYearLast().toArray(new Integer[0]);
                String date = "";
                for (int i=0; i<3; i++) {
                    date += MonthDayYear[i] + (i < 2 ? "/" : "");
                }
                // dateNiftar.setText(dataSnapshot.child("0").getValue(Integer.class));
                viewHolder.getDateNiftar().setText("End Date: " + date);
                final ColorStateList textViewDefaultColor = viewHolder.getDateNiftar().getTextColors();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDate localDate = LocalDate.of(YearMonthDay[0], YearMonthDay[1], YearMonthDay[2]);
                    LocalDate today = LocalDate.now();
                    int date_passed = localDate.compareTo((today));
                    if (date_passed < 0) {
                        Period p = Period.between(localDate, today);
                        if (p.getYears() > 1) {
                            viewHolder.getTimeRemaining().setText(context.getString(R.string.time_remaining_ended) + p.getYears() + " years, " + p.getMonths() + " months, " + p.getDays() + " days ago");
                        } else if (p.getMonths() > 1) {
                            viewHolder.getTimeRemaining().setText(context.getString(R.string.time_remaining_ended) + p.getMonths() + " months, " + p.getDays() + " days ago");
                        } else {
                            viewHolder.getTimeRemaining().setText(context.getString(R.string.time_remaining_ended) + p.getDays() + " days ago");
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
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
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

    public void deleteItemFromFirebase(int position, boolean completed) {
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, null != cases ? cases.size() : 0);

        String caseTakenID = cases.get(position).getCaseId();
        String caseMasID = cases.get(position).getCaseMasID();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("cases").child(caseMasID).getRef().removeValue();


        if (!completed){
            CaseTakenInfo caseTakenInfo = cases.get(position);
            String masechtaTaken = caseTakenInfo.getMasechtaTaken();
            //Combo masechtaInfo = caseTakenInfo.getSeder();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
            dbRef.child("cases").child(caseTakenID).child("masechtos").child(""+UtilMishnayosNumbers.mishnaNums.get(masechtaTaken).getSederNum()).child(""+UtilMishnayosNumbers.mishnaNums.get(masechtaTaken).getMasechtaNum()).child("status").setValue(false);
        }
    }
}