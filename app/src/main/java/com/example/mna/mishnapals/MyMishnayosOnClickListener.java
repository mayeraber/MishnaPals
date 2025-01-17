package com.example.mna.mishnapals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyMishnayosOnClickListener implements View.OnClickListener{

    protected ArrayList<CaseTakenInfo> cases;
    RecyclerView mrecy;
    protected Activity context;

    public MyMishnayosOnClickListener(RecyclerView madap, ArrayList<CaseTakenInfo> mycases, Activity context) {
        mrecy = madap;
        cases = mycases;
        this.context = context;
    }

    @Override
    public void onClick(View v){
        boolean internetConnected = InternetCheckUtility.internetStatus();
        if (!internetConnected) {
            Toast.makeText(context, "Please check your internet connection - It looks like you might be offline", Toast.LENGTH_SHORT).show();
        }
        else{
                int position = mrecy.getChildAdapterPosition(v);
                if (!cases.get(position).isFinished()) {
                    Intent intent = new Intent(v.getContext(), CompletedMasechta.class);
                    intent.putExtra("caseId", cases.get(position).getCaseId());
                    intent.putExtra("masechta", cases.get(position).getMasechtaTaken());
                    v.getContext().startActivity(intent);
                    context.finish();

                    //TODO maybe change to manually update the listitem instead of ending activity and then restarting
                }
            }
    }
}
