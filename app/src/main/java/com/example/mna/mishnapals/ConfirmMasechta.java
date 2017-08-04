package com.example.mna.mishnapals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ConfirmMasechta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_masechta);
        Masechta masechta = (Masechta) getIntent().getSerializableExtra("Masechta");
        TextView confirmMasechta = (TextView)findViewById(R.id.masechtaConfirmDetails);
        confirmMasechta.setText("מסכת "+masechta.hebName);
        TextView confirmPerakim = (TextView)findViewById(R.id.numPerakimConfirm);
        confirmPerakim.setText(masechta.numPerakim+"  פרקים");
        TextView confirmMishnayos = (TextView)findViewById(R.id.numMishnayosConfirm);
        confirmMishnayos.setText(masechta.numMishnayos+"  משניות");
    }
}
