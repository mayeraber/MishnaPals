package com.example.mna.mishnapals;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyMishnayosOnClickListener implements View.OnClickListener{

    protected ArrayList<CaseTakenInfo> cases;
    RecyclerView mrecy;

    public MyMishnayosOnClickListener(RecyclerView madap, ArrayList<CaseTakenInfo> mycases) {
        mrecy = madap;
        cases = mycases;
    }

    @Override
    public void onClick(View v){
        int position = mrecy.getChildAdapterPosition(v);
        if(!cases.get(position).isFinished()) {
            Intent intent = new Intent(v.getContext(), CompletedMasechta.class);
            intent.putExtra("caseId", cases.get(position).getCaseId());
            intent.putExtra("masechta", cases.get(position).getMasechtaTaken());
            v.getContext().startActivity(intent);
            //TODO maybe change to manually update the listitem instead of ending activity and then restarting
        }
    }
}
