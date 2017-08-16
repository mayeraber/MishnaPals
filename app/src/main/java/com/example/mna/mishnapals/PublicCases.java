package com.example.mna.mishnapals;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MNA on 8/15/2017.
 */

public class PublicCases extends AppCompatActivity{

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.avail_public_cases);
        ListView listView = (ListView)findViewById(R.id.publicCasesList);
        ArrayList<String>caseNames = new ArrayList<>();
        caseNames = getIntent().getStringArrayListExtra("cases");

        String[] cases = new String[caseNames.size()];
        for (int i=0; i<caseNames.size(); i++){
            cases[i] = caseNames.get(i);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.case_info, cases);
        listView.setAdapter(arrayAdapter);
    }
}
