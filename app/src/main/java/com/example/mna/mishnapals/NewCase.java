package com.example.mna.mishnapals;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import java.sql.Time;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.LogManager;

import static java.util.Calendar.YEAR;

public class NewCase extends AppCompatActivity {

    EditText caseIdEntry;
    EditText caseIdConfirm;
    TextView caseIDLabel;
    TextView confirmIDLabel;
    EditText niftarName;
    EditText fatherName;
    EditText dateNiftar;
    int day, month, year;
    Calendar dateNiftarCal = new GregorianCalendar();
    DatePickerDialog datePickerDialog;
    AlertDialog.Builder info;
    private DatabaseReference mDatabase, casesEndpoint, usersEndpoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_case);
        View background = findViewById(R.id.backgroundLayout);
        Drawable back = background.getBackground();
        back.setAlpha(50);
        final Calendar calendar = Calendar.getInstance();
        final Date date = new Date();
        dateNiftar = (EditText) findViewById(R.id.dateNiftar);
        niftarName = (EditText)findViewById(R.id.nameOfNiftar);
        fatherName = (EditText)findViewById(R.id.fatherName);

        caseIdEntry = (EditText) findViewById(R.id.caseIdEntry);
        caseIdConfirm = (EditText) findViewById(R.id.caseIDConfirm);
        caseIDLabel = (TextView) findViewById(R.id.caseIDLabel);
        confirmIDLabel = (TextView) findViewById(R.id.confirmCaseIDLabel);

        dateNiftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               datePickerDialog = new DatePickerDialog(NewCase.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                        dateNiftar.setText(arg2 + "/" + arg3 + "/" + arg1);

                        dateNiftarCal.set(arg1, arg2, arg3);
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


        });

        info = new AlertDialog.Builder(this);
        info.setMessage("Check this box if you want to make the case private. If you do, you will have to create an id number that people can use to access the case");

        info.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog dialog = info.create();

        Button privateInfo = (Button)findViewById(R.id.privateInfoButton);

        privateInfo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {

                        final Button okButton = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                        LinearLayout.LayoutParams butonLayout = (LinearLayout.LayoutParams) okButton.getLayoutParams();
                        butonLayout.width= ViewGroup.LayoutParams.MATCH_PARENT;
                        okButton.setLayoutParams(butonLayout);
                    }
                });
                dialog.show();

            }
        });

        Button createCaseButton = (Button)findViewById(R.id.createCaseButton);
        createCaseButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {
                createCaseClicked(view);
            }
        });

        //firebase code
        mDatabase = FirebaseDatabase.getInstance().getReference();
        casesEndpoint = mDatabase.child("cases");
        usersEndpoint = mDatabase.child("users");
       /* casesEndpoint.setValue("Case #1");
        usersEndpoint.setValue("User A");*/
    }

    public void createCaseClicked(View view)
    {
        if(!isEmpty(niftarName) && !isEmpty(fatherName)&& !isEmpty(dateNiftar))
        {
            Case newCase = new Case(niftarName.getText().toString(), fatherName.getText().toString());
            newCase.setEndDate(dateNiftarCal);
            Intent intent = new Intent(getBaseContext(), NewCaseCreated.class);
            intent.putExtra("Case", newCase);
            startActivity(intent);
            //store in firebase db
            String key = usersEndpoint.push().getKey();
            newCase.setCaseId(key);
            casesEndpoint.child(key).setValue(newCase);
        }
        else{
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isEmpty(EditText editText)
    {
        if(editText.getText().toString().trim().length()==0)
            return true;
        return false;
    }
}