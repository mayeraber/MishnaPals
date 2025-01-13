/*
If user chooses from home screen to view all public cases, this is where he is brought to and shown all public cases
 */
package com.example.mna.mishnapals;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
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
        ArrayList<String> caseNames;
        caseNames = getIntent().getStringArrayListExtra("caseNames");
        Bundle sentCases = getIntent().getBundleExtra("cases");
        final ArrayList<Case> publicCases = (ArrayList<Case>) sentCases.getSerializable("Arraylist");
        final ArrayList<String> publicCaseIds = getIntent().getStringArrayListExtra("caseIds");

        ArrayList<String> cases = new ArrayList<>();

        Collections.sort(publicCases, (o1, o2) -> o1.shloshimDateCal().compareTo(o2.shloshimDateCal()));
        //if up the min SDK to 24, can switch to:
        //Collections.sort(publicCases, Comparator.comparing(Case::getDateFormat));
        Log.d("test", caseNames.size() +" " +cases.size());

        final ArrayList<Case> currentPublicCases = new ArrayList<Case>();
        final ArrayList<String> currentPublicCaseIds = new ArrayList<>();
        for (int i = 0; i < caseNames.size(); i++) {
            Log.d("test", "begggggggggggggggggggggggggg");
            Case caseInfo = publicCases.get(i);
            List<Integer> date = caseInfo.getDate();
            Calendar endDate = caseInfo.shloshimDateCal();
            if (!Calendar.getInstance().after(endDate)) {
                String finishDate = (date.get(1) + "/" + date.get(2) + "/" + date.get(0));
                cases.add(publicCases.get(i).getFirstName() + "\nFinish by: " + finishDate);
                Log.d("date", "" + publicCases.get(i).getFirstName() + "\nFinish by: " + finishDate);
                currentPublicCases.add(caseInfo);
                currentPublicCaseIds.add(caseInfo.caseId);
            }
        }
        //String[] cases = Arrays.stream(cases).toArray()
        if (cases.size() > 0){
            Log.d("size", "aaaaaaaaaaaaaaa"+cases.size());
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.case_info, cases);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Log.d("test", "in clickkkkkkkkkkkkkkkkkkkk");
                    Case caseTakenInfo = currentPublicCases.get(position);
                    Intent intent = new Intent(getBaseContext(), MasechtosList.class);
                    Log.d("lololol", caseTakenInfo.getCaseId());
                    //Log.d("opopopo", currentPublicCaseIds.get(0));
                    startActivity(new Intent(getBaseContext(), MasechtosList.class).putExtra("caseId", caseTakenInfo.getCaseId()).putExtra("caseKey", currentPublicCases.get(position).getFirebaseID()));
                }
            });
        }

    }
}
