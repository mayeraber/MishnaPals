package com.example.mna.mishnapals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class NewCaseCreated extends AppCompatActivity {

    TextView niftarNameEng;
    TextView dateNiftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_case_created);
        Case caseInfo = (Case) getIntent().getSerializableExtra("Case");
        niftarNameEng = (TextView)findViewById(R.id.niftarNameCreated);
        dateNiftar = (TextView)findViewById(R.id.dateNiftarCreated);

        //char language = Character.getDirectionality(caseInfo.fathersName.charAt(0));
        if(Character.getDirectionality(caseInfo.fathersName.charAt(0)) == Character.DIRECTIONALITY_RIGHT_TO_LEFT) {
            niftarNameEng.setText(caseInfo.firstName + " בן " + caseInfo.fathersName);
        }
        else
            niftarNameEng.setText(caseInfo.firstName+" ben "+caseInfo.fathersName);

        SimpleDateFormat dateNiftarFormatted = new SimpleDateFormat("MMMM d, yyyy");
        dateNiftarFormatted.setCalendar(caseInfo.getEndDate());
        dateNiftar.setText(dateNiftarFormatted.format(caseInfo.getEndDate().getTime()));

       // dateNiftar.setText(caseInfo.endDate.toString());
    }
}
