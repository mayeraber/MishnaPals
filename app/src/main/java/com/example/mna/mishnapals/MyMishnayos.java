 package com.example.mna.mishnapals;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MNA on 8/13/2017.
 */

public class MyMishnayos extends AppCompatActivity {

    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.my_mishnayos);

        final ListView listMishnayos = (ListView)findViewById(R.id.listMishnayos);

        final ArrayList<CaseTakenInfo> cases = new ArrayList<>();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query currUser = ref.child("users").orderByChild("userEmail").equalTo(user.getEmail());//.getRef().child("cases");
        //Log.d("curr", currUser.getKey());
        currUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                     dataSnapshot = ds.child("cases");}

                for(DataSnapshot userCase : dataSnapshot.getChildren()){
                   CaseTakenInfo caseTaken = userCase.getValue(CaseTakenInfo.class);
                    Log.d("curr6", caseTaken.getMasechtaTaken());
                    cases.add(caseTaken);
                }
                ListAdapter listAdapter = new ListAdapter(getBaseContext() , R.layout.my_masechta, cases);
                listMishnayos.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listMishnayos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    private class ListAdapter extends ArrayAdapter<CaseTakenInfo>
    {


        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("cases");

        public ListAdapter(Context context, int textViewResourceId){
            super(context, textViewResourceId);
        }

        public ListAdapter(Context context, int textViewResourceId, List<CaseTakenInfo> casesTaken){
            super(context, textViewResourceId, casesTaken);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            View v = convertView;
            if (v == null) {
                LayoutInflater li;
                li = LayoutInflater.from(getContext());
                v = li.inflate(R.layout.my_masechta, null);
                holder = new ViewHolder();
                holder.masechta = (TextView) v.findViewById(R.id.masechtaName);
                v.setTag(holder);
            }
            else {
                holder = (ViewHolder)v.getTag();
            }

                CaseTakenInfo caseTaken = getItem(position);
                Log.d("aaaaa", "" + caseTaken.getMasechtaTaken());

                if (caseTaken != null) {
                    holder.masechta.setText("Masechta: "+caseTaken.getMasechtaTaken());
                    final TextView nameNiftar = (TextView) v.findViewById(R.id.nameNiftar);
                    DatabaseReference dref = dbref.child(caseTaken.getCaseId()).child("firstName");
                    dref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            nameNiftar.setText("Name: "+((String)dataSnapshot.getValue()));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    final TextView dateNiftar = (TextView) v.findViewById(R.id.dateNiftar);
                    dref = dbref.child(caseTaken.getCaseId()).child("date");
                    dref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String date = "";
                            int counter=0;
                            for (DataSnapshot sp : dataSnapshot.getChildren()) {
                                counter++;
                                Log.d("aaaa", "" + sp.getValue(Integer.class));
                                date += sp.getValue(Integer.class)+(counter<3 ? "/":"");
                            }
                            //Log.d("aaaa", ""+dataSnapshot.getValue(List<>.class));
                            // dateNiftar.setText(dataSnapshot.child("0").getValue(Integer.class));
                            dateNiftar.setText("End Date: "+date);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                return v;
            }

    }
    static class ViewHolder{
        TextView masechta;
    }


}
