/*
If user chooses from home screen to view all public cases, this is where he is brought to and shown all public cases
 */
//TODO Right now nothing happens if user taps on case....Must add functionality that it should show list of that cases's mishnayos
package com.example.mna.mishnapals;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MNA on 8/15/2017.
 */

public class PublicCases extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.avail_public_cases);
        ListView listView = (ListView) findViewById(R.id.publicCasesList);
        ArrayList<String> caseNames = new ArrayList<>();
        caseNames = getIntent().getStringArrayListExtra("caseNames");
        Bundle sentCases = getIntent().getBundleExtra("cases");
        final ArrayList<Case> publicCases = (ArrayList<Case>) sentCases.getSerializable("Arraylist");
        final ArrayList<String> publicCaseIds = getIntent().getStringArrayListExtra("caseIds");

        final String[] cases = new String[caseNames.size()];
        for (int i = 0; i < caseNames.size(); i++) {
            cases[i] = caseNames.get(i);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.case_info, cases);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Case caseTakenInfo = publicCases.get(position);
                Intent intent = new Intent(getBaseContext(), MasechtosList.class);
                Log.d("lololol", caseTakenInfo.getCaseId());
                Log.d("opopopo", publicCaseIds.get(0));
                startActivity(new Intent(getBaseContext(), MasechtosList.class).putExtra("caseId", caseTakenInfo.getCaseId()).putExtra("caseKey", publicCaseIds.get(position)));
            }
        });
    }
}
