/*
If user chooses from home screen to view all public cases, this is where he is brought to and shown all public cases
 */
package mna.mishnapals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import mna.mishnapals.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by MNA on 8/15/2017.
 */

public class PublicCases extends Toolbar_parent {

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.avail_public_cases2);
        ListView listView = (ListView) findViewById(R.id.publicCasesList);

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        Query publicCase = dbref.child("cases").orderByChild("privateCase").equalTo(false);

        publicCase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Case> publicCases = new ArrayList<>();
                ArrayList<String> caseNames = new ArrayList<>();
                ArrayList<String> publicCaseIds = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    publicCaseIds.add(ds.getKey());
                    Case nextCase = ds.getValue(Case.class);
                    nextCase.setFirebaseID(ds.getKey());
                    publicCases.add(nextCase);
                    caseNames.add(ds.getValue(Case.class).getFirstName());
                }

                ArrayList<String> cases = new ArrayList<>();

                Collections.sort(publicCases, (o1, o2) -> o1.shloshimDateCal().compareTo(o2.shloshimDateCal()));
                //if up the min SDK to 24, can switch to:
                //Collections.sort(publicCases, Comparator.comparing(Case::getDateFormat));

                final ArrayList<Case> currentPublicCases = new ArrayList<Case>();
                final ArrayList<String> currentPublicCaseIds = new ArrayList<>();
                for (int i = 0; i < caseNames.size(); i++) {
                    Case caseInfo = publicCases.get(i);
                    List<Integer> date = caseInfo.getDate();
                    Calendar endDate = caseInfo.shloshimDateCal();
                    if (!Calendar.getInstance().after(endDate)) {
                        String finishDate = (date.get(1) + "/" + date.get(2) + "/" + date.get(0));
                        cases.add(publicCases.get(i).getFirstName() + "\nFinish by: " + finishDate);
                        currentPublicCases.add(caseInfo);
                        currentPublicCaseIds.add(caseInfo.caseId);
                    }
                }
                //String[] cases = Arrays.stream(cases).toArray()
                if (cases.size() > 0){
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(PublicCases.this, R.layout.case_info, cases);
                    listView.setAdapter(arrayAdapter);
                    ProgressBar pBar = findViewById(R.id.progressBarPublic);
                    pBar.setVisibility(View.GONE);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Case caseTakenInfo = currentPublicCases.get(position);
                            boolean internetConnected = InternetCheckUtility.internetStatus();
                            if (!internetConnected) {
                                Toast.makeText(PublicCases.this, "Please check your internet connection - It looks like you might be offline", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                startActivity(new Intent(getBaseContext(), MasechtosList.class).putExtra("caseId", caseTakenInfo.getCaseId()).putExtra("caseKey", currentPublicCases.get(position).getFirebaseID()));}
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
