package com.example.mna.mishnapals;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import java.sql.Time;
import java.util.Date;

import static java.util.Calendar.YEAR;

public class NewCase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_case);
        final Calendar calendar = Calendar.getInstance();
        final Date date = new Date();
        final EditText dateNiftar = (EditText) findViewById(R.id.dateNiftar);
        dateNiftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewCase.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                        dateNiftar.setText(arg1 + "/" + arg2 + "/" + arg3);
                    }
                }, calendar.get(YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

            }
        });


        final CheckBox makePrivate = (CheckBox) findViewById(R.id.makePrivateCheckBox);
        makePrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (makePrivate.isChecked()) {
                    caseIdEntry.setVisibility(View.VISIBLE);//setEnabled(true);
                    caseIdConfirm.setVisibility(View.VISIBLE);//setEnabled(true);
                    caseIDLabel.setVisibility(View.VISIBLE);
                    confirmIDLabel.setVisibility(View.VISIBLE);
                } else {
                    caseIdEntry.setVisibility(View.GONE);
                    caseIdConfirm.setVisibility(View.GONE);
                    caseIDLabel.setVisibility(View.GONE);
                    confirmIDLabel.setVisibility(View.GONE);
                }
            }

            EditText caseIdEntry = (EditText) findViewById(R.id.caseIdEntry);
            EditText caseIdConfirm = (EditText) findViewById(R.id.caseIDConfirm);
            TextView caseIDLabel = (TextView) findViewById(R.id.caseIDLabel);
            TextView confirmIDLabel = (TextView) findViewById(R.id.confirmCaseIDLabel);
        });

    }
}