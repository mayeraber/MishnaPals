/*
After creating a new case, the user is shown the details, and given a link to bring him to the list of mishnayos to choose from in the 'MasechtosList' class
 */
package mna.mishnapals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mna.mishnapals.R;

import java.util.List;

public class NewCaseCreated extends Toolbar_parent {

    TextView niftarNameEng;
    TextView dateNiftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_case_created);
        final Case caseInfo = (Case) getIntent().getSerializableExtra("Case");
        niftarNameEng = (TextView)findViewById(R.id.niftarNameCreated);
        dateNiftar = (TextView)findViewById(R.id.dateNiftarCreated);

        //char language = Character.getDirectionality(caseInfo.fathersName.charAt(0));
        if(Character.getDirectionality(caseInfo.fathersName.charAt(0)) == Character.DIRECTIONALITY_RIGHT_TO_LEFT) {
            niftarNameEng.setText(caseInfo.firstName + " בן " + caseInfo.fathersName);
        }
        else
            niftarNameEng.setText(caseInfo.firstName+" ben "+caseInfo.fathersName);

        List<Integer> date = caseInfo.getDate();
        dateNiftar.setText(date.get(1)+"/"+date.get(2)+"/"+date.get(0));
        Button takeMishnayos = (Button)findViewById(R.id.takeMishnayosAfterReserve);
        takeMishnayos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), MasechtosList.class).putExtra("caseId", caseInfo.getCaseId()).putExtra("caseKey", getIntent().getStringExtra("caseKey")));
            }
        });
    }
}
