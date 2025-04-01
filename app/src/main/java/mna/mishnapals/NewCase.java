/*
NewCase activity linked to from the home screen when user selects to create a new case.
User inputs all necessary info, case is created, and then user is shown the details of
the new case in the 'NewCaseCreated' activity
 */
package mna.mishnapals;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import mna.mishnapals.R;
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
import java.util.Calendar;
import java.util.Locale;
import static java.util.Calendar.YEAR;

public class NewCase extends AppCompatActivity {

    EditText caseIdEntry;
    EditText caseIdConfirm;
    TextView caseIDLabel;
    TextView confirmIDLabel;
    TextView idTaken;
    TextView idMatch;
    EditText niftarName;
    EditText fatherName;
    EditText dateNiftar;
    EditText shloshimDateBox;
    TextView shloshimDateLabel;
    int day, month, year;
    String[] dateSplit;
    //Calendar dateNiftarCal = new GregorianCalendar();
    DatePickerDialog datePickerDialog;
    AlertDialog.Builder info;
    private DatabaseReference mDatabase, casesEndpoint, usersEndpoint;
    boolean isPrivate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_case);
        View background = findViewById(R.id.backgroundLayout);
        Drawable back = background.getBackground();
        back.setAlpha(50);

        final Calendar calendar = Calendar.getInstance();
        dateNiftar = (EditText) findViewById(R.id.dateNiftar);
        shloshimDateBox = (EditText) findViewById(R.id.dateShloshim);
        shloshimDateLabel = (TextView) findViewById(R.id.dateShloshimLabel);
        niftarName = (EditText) findViewById(R.id.nameOfNiftar);
        fatherName = (EditText) findViewById(R.id.fatherName);

        caseIdEntry = (EditText) findViewById(R.id.caseIdEntry);
        caseIdConfirm = (EditText) findViewById(R.id.caseIDConfirm);
        caseIDLabel = (TextView) findViewById(R.id.caseIDLabel);
        confirmIDLabel = (TextView) findViewById(R.id.confirmCaseIDLabel);
        idTaken = (TextView) findViewById(R.id.idTaken);
        idMatch = (TextView) findViewById(R.id.idMatch);

        /*
        Set up date-picker calendar popup from which to select the date niftar, then add 30 to get the shloshim date
        Display each in its respective textview
         */
        dateNiftar.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(NewCase.this, (arg0, arg1, arg2, arg3) -> {
                year = arg1;
                month = arg2+1;
                day = arg3 ;
                String dateNif = month + "/" + day + "/" + year;
                Calendar c1 = Calendar.getInstance();
                String shloshimDate = dateNif;
                String shloshimDateForSplit;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                SimpleDateFormat formatWithMonthName = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                try {
                    c1.setTime(simpleDateFormat.parse(dateNif));
                    c1.add(Calendar.DAY_OF_MONTH, 30);
                    shloshimDateForSplit = simpleDateFormat.format(c1.getTime());
                    shloshimDate = formatWithMonthName.format(c1.getTime());
                    dateSplit = shloshimDateForSplit.split("/");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    dateNiftar.setText(formatWithMonthName.format(simpleDateFormat.parse(dateNif)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                shloshimDateLabel.setVisibility(View.VISIBLE);
                shloshimDateBox.setVisibility(View.VISIBLE);
                shloshimDateBox.setText(shloshimDate);

            }, calendar.get(YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });


        /*
        If user chooses to make case private, allow user to name the case for reference
         */
        final CheckBox makePrivate = (CheckBox) findViewById(R.id.makePrivateCheckBox);
        makePrivate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (makePrivate.isChecked()) {
                isPrivate = true;
                caseIdEntry.setVisibility(View.VISIBLE);//setEnabled(true);
                caseIdConfirm.setVisibility(View.VISIBLE);//setEnabled(true);
                caseIDLabel.setVisibility(View.VISIBLE);
                confirmIDLabel.setVisibility(View.VISIBLE);

                if (getResources().getConfiguration().orientation == 2) {
                    final ScrollView sv = (ScrollView) findViewById(R.id.scrollNewCase);
                    sv.post(() -> sv.scrollTo(0, sv.getBottom()));
                }

            } else {
                isPrivate = false;
                caseIdEntry.setVisibility(View.GONE);
                caseIdConfirm.setVisibility(View.GONE);
                caseIDLabel.setVisibility(View.GONE);
                confirmIDLabel.setVisibility(View.GONE);
            }
        });

        info = new AlertDialog.Builder(this);
        info.setMessage("Check this box if you want to make the case private. If you do, you will have to create an id number that people can use to access the case");

        info.setNeutralButton("OK", (dialog, which) -> dialog.cancel());
        final AlertDialog dialog = info.create();

        Button privateInfo = (Button) findViewById(R.id.privateInfoButton);

        privateInfo.setOnClickListener(view -> {

            dialog.setOnShowListener(dialog1 -> {

                final Button okButton = ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_NEUTRAL);
                LinearLayout.LayoutParams butonLayout = (LinearLayout.LayoutParams) okButton.getLayoutParams();
                butonLayout.width = ViewGroup.LayoutParams.MATCH_PARENT;
                okButton.setLayoutParams(butonLayout);
            });
            dialog.show();

        });

        caseIdConfirm.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query caseId = ref.child("cases").orderByChild("caseId").equalTo(caseIdEntry.getText().toString().toLowerCase());

                caseId.addListenerForSingleValueEvent(new ValueEventListener() {                        @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        idTaken.setCompoundDrawablesWithIntrinsicBounds(R.drawable.indicator_input_error, 0,0,0);
                        //idTaken.setText("ID taken already");
                        idTaken.setText("ID taken already");
                        idTaken.setVisibility(View.VISIBLE);
                        caseIdEntry.requestFocus();
                    }
                    else{
                        if (caseIdEntry.getText().toString().length() < 5) {
                            idTaken.setVisibility(View.VISIBLE);
                            idTaken.setText("ID must be atleast 5 characters");
                            caseIdEntry.requestFocus();
                        }
                        else {
                            idTaken.setVisibility(View.GONE);
                        }
                    }

                }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        Button createCaseButton = (Button) findViewById(R.id.createCaseButton);
        createCaseButton.setOnClickListener(view -> createCaseClicked(view));

        //firebase code
        mDatabase = FirebaseDatabase.getInstance().getReference();
        casesEndpoint = mDatabase.child("cases");
        usersEndpoint = mDatabase.child("users");
    }

    /*
    When 'Create Case' button is clicked, create the case, push to db, and open activity to show the details of the new case
     */
    public void createCaseClicked(View view) {
        boolean idNotMatch = false;
        if (isPrivate) {
            if (!caseIdEntry.getText().toString().equalsIgnoreCase(caseIdConfirm.getText().toString())) {
                idMatch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.indicator_input_error, 0,0,0);
                idMatch.setVisibility(View.VISIBLE);
                caseIdConfirm.requestFocus();
                idNotMatch = true;
                idMatch.postDelayed(() -> idMatch.setVisibility(View.GONE), 4000);
            } else {
                idMatch.setVisibility(View.GONE);
                idNotMatch = false;
            }
        }

        boolean internetConnected = InternetCheckUtility.internetStatus();
        if (internetConnected && !isEmpty(niftarName) && !isEmpty(fatherName) && !isEmpty(dateNiftar) && !idNotMatch) {

            Case newCase = new Case(niftarName.getText().toString(), fatherName.getText().toString());
            newCase.setEndShloshim(Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[2]));

            //store in firebase db
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String key = usersEndpoint.push().getKey();
            //newCase.setUserNameOpened(user.getEmail());
            newCase.setUserNameOpened(user.getUid()); //to protect email privacy in db, instead of needing to restrict access with firebase rule
            newCase.createMasechtos();

            if (((CheckBox) findViewById(R.id.makePrivateCheckBox)).isChecked() && caseIdEntry.getText().toString() != null) {
                newCase.setCaseIdPrivate(caseIdEntry.getText().toString().toLowerCase());
                newCase.setPrivateCase(true);
            } else {
                newCase.setCaseId(key);
            }

            casesEndpoint.child(key).setValue(newCase);
            Intent intent = new Intent(getBaseContext(), NewCaseCreated.class);
            intent.putExtra("Case", newCase);
            intent.putExtra("caseKey", key);
            startActivity(intent);
            this.finish();
        } else if (!internetConnected) {
            Toast.makeText(this, "Please check your internet connection - It looks like you might be offline", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

}

