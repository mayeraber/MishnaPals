/*
List of masechtos to show all masechtos of the case with their status' and button for reservation which starts 'ConfirmMasechta' activity
Designs the expandable drop-down list, including bubble count of number of avail masechtos for each seder in the group-header, and masechta
info on each child with status and option to register if masechta is available
This activity is called from 'NewCaseCreated' and from 'SearchResult'
 */
package mna.mishnapals;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import mna.mishnapals.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MasechtosList extends Toolbar_parent {

    Masechta[] sederZeraim = new Masechta[11];
    Masechta[] sederMoed = new Masechta[12];
    Masechta[] sederNashim = new Masechta[7];
    Masechta[] sederNezikin = new Masechta[10];
    Masechta[] sederKodshim = new Masechta[11];
    Masechta[] sederTaharos = new Masechta[12];
    static ArrayList<Masechta[]> allSedarim = new ArrayList<Masechta[]>();
    String[] zeraimEng, zeraimHeb, moedEng , moedHeb, nashimEng, nashimHeb, nezikinEng, nezikinHeb, kodshimEng, kodshimHeb, taharosEng,taharosHeb;
    String[] sedarimEng = new String[]{"Zeraim", "Moed", "Nashim", "Nezikin", "Kodshim", "Taharos"};
    String[] sedarimHeb = new String[]{"זרעים", "מועד", "נשים", "נזיקין", "קדשים", "טהרות"};
    LinkedHashMap<String, Masechta[]> masechtos = new LinkedHashMap<>();
    private DatabaseReference mDatabase, casesEndpoint, usersEndpoint;
    DatabaseReference caseRef;
    String caseKey;
    String caseId;
    TextView takenLabel;
    Button reserveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masechtos_list2);
        takenLabel = (TextView)findViewById(R.id.takenLabel);
        reserveButton = (Button)findViewById(R.id.reserveMasechtaButton);
        createMasechtos();  //method to populate the 'allSedarim' ArrayList and 'masechtos' hashmap

        caseKey = getIntent().getStringExtra("caseKey");

        ExpandableListView expandableListView = (ExpandableListView)findViewById(R.id.masechtosListView);
        LinkedHashMap<String, Masechta[]> details = masechtos;
        List<String> titles = new ArrayList<>(masechtos.keySet());

        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(this, titles, details);
        expandableListView.setAdapter(expandableListAdapter);

    }

    /*
    Create a 'Masechta' object for every masechta, add each to its respective seder, then add all sedarim to the 'allSedarim' ArrayList and to 'masechtos' hashmap
     */
    public void createMasechtos()
    {
        zeraimEng = new String[]{"Berachos", "Peah", "Demai", "Kelaim", "Shviis", "Terumos", "Maaseros", "Maaser Sheini", "Chalah", "Orlah", "Bikurim"};
        zeraimHeb = new String[]{"ברכות", "פאה", "דמאי ", "כלאים", "שביעית", "תרומות", "מעשרות", "מעשר שני", "חלה", "ערלה", "ביכורים"};

        moedEng = new String[]{"Shabbos", "Eiruvin", "Pesachim", "Shkalim", "Yoma", "Succah", "Beizah", "Rosh Hashana", "Taanis", "Megillah", "Moed Katan", "Chagigah"};
        moedHeb = new String[]{"שבת", "עירובין", "פסחים", "שקלים", "יומא", "סוכה", "ביצה", "ראש השנה", "תענית", "מגילה", "מועד קטן", "חגיגה"};

        nashimEng = new String[]{"Yevamos", "Kesubos", "Nedarim", "Nazir", "Sotah", "Gittin", "Kiddushin"};
        nashimHeb = new String[]{"יבמות", "כתובות", "נדרים", "נזיר", "סוטה", "גיטין", "קידושין"};

        nezikinEng = new String[]{"Bava Kama", "Bava Metzia", "Bava Basra", "Sanherdin", "Makkos", "Shevuos", "Edios", "Avodah Zarah", "Avos", "Horios"};
        nezikinHeb = new String[]{"בבא קמא", "בבא מציעא", "בבא בתרא", "סנהדרין", "מכות", "שבועות", "עדיות", "עבודה זרה", "אבות", "הוריות"};

        kodshimEng = new String[]{"Zevachim", "Menachos", "Chullin", "Bechoros", "Eiruchin", "Temurah", "Kerisos", "Meilah", "Tamid", "Middos", "Kinim"};
        kodshimHeb = new String[]{"זבחים", "מנחות", "חולין", "בכורות", "ערכין", "תמורה", "כריתות", "מעילה", "תמיד", "מדות", "קנים"};

        taharosEng = new String[]{"Keilim", "Ohalos", "Negaim", "Parah", "Taharos", "Mikvaos", "Niddah", "Machshirin", "Zavim", "Tevul Yom", "Yadayim", "Uktzin"};
        taharosHeb= new String[]{"כלים", "אהלות", "נגעים", "פרה", "טהרות", "מקואות", "נדה", "מכשירין", "זבים", "טבול יום", "ידים", "עוקצין"};

        //number of perakim in each masechta
        int[] numPerakimZeraim = new int[]{9, 8, 7, 9, 10, 11, 5, 5, 4, 3, 4};
        int[] numPerakimMoed = new int[]{24, 10, 10, 8, 8, 5, 5, 4, 4, 4, 3, 3};
        int[] numPerakimNashim = new int[]{16, 13, 11, 9, 9, 9, 4};
        int[] numPerakimNezikin = new int[]{10, 10, 10, 11, 3, 8, 8,5, 6, 3};
        int[] numPerakimKodshim = new int[]{14, 13, 12, 9, 9, 7, 6, 6, 7, 5,3};
        int[] numPerakimTaharos = new int[]{30, 18, 14, 12, 10, 10, 10, 6, 5, 4, 4, 3};

        //number of mishnayos in each masechta
        int[] numMishnayosZeraim = new int[]{57, 69, 53, 77, 89, 101, 40, 57, 38, 35, 39};
        int[] numMishnayosMoed = new int[]{139, 96, 89, 52, 61, 53, 42, 35, 34, 33, 24, 23};
        int[] numMishnayosNashim = new int[]{128, 111, 90, 60, 67, 75, 47};
        int[] numMishnayosNezikin = new int[]{79, 101, 86, 71, 34, 62, 74, 50, 108, 20};
        int[] numMishnayosKodshim = new int[]{101, 93, 74, 73, 50, 35, 43, 38, 34, 34, 15};
        int[] numMishnayosTaharos = new int[]{254, 134, 115, 96, 92, 71, 79, 54, 32, 26, 22, 28};

        for(int i=0; i<zeraimEng.length; i++)
        {
            sederZeraim[i] = new Masechta(zeraimEng[i], zeraimHeb[i], numPerakimZeraim[i], numMishnayosZeraim[i]);
        }
        for(int i=0; i<moedEng.length; i++)
        {
            sederMoed[i] = new Masechta(moedEng[i], moedHeb[i], numPerakimMoed[i], numMishnayosMoed[i]);
        }
        for(int i=0; i<nashimEng.length; i++)
        {
            sederNashim[i] = new Masechta(nashimEng[i], nashimHeb[i], numPerakimNashim[i], numMishnayosNashim[i]);
        }
        for(int i=0; i<nezikinEng.length; i++)
        {
            sederNezikin[i] = new Masechta(nezikinEng[i], nezikinHeb[i], numPerakimNezikin[i], numMishnayosNezikin[i]);
        }
        for(int i=0; i<kodshimEng.length; i++)
        {
            sederKodshim[i] = new Masechta(kodshimEng[i], kodshimHeb[i], numPerakimKodshim[i], numMishnayosKodshim[i]);
        }
        for(int i=0; i<taharosEng.length; i++)
        {
            sederTaharos[i] = new Masechta(taharosEng[i], taharosHeb[i], numPerakimTaharos[i], numMishnayosTaharos[i]);
        }

        allSedarim.add(sederZeraim);
        allSedarim.add(sederMoed);
        allSedarim.add(sederNashim);
        allSedarim.add(sederNezikin);
        allSedarim.add(sederKodshim);
        allSedarim.add(sederTaharos);

        masechtos.put(sedarimHeb[0], sederZeraim);
        masechtos.put(sedarimHeb[1], sederMoed);
        masechtos.put(sedarimHeb[2], sederNashim);
        masechtos.put(sedarimHeb[3], sederNezikin);
        masechtos.put(sedarimHeb[4], sederKodshim);
        masechtos.put(sedarimHeb[5], sederTaharos);
    }

    /*
    Designs the expandable list; the group-headers in one method, and the children in another
     */
    private class ExpandableListAdapter extends BaseExpandableListAdapter
    {
        Context context;
        List<String> titles;
        HashMap<String, Masechta[]> details;
        TextView takenLabel;
        Button reserveButton;
        public ExpandableListAdapter(Context context, List<String> titles, HashMap<String, Masechta[]> details)
        {
            this.context = context;
            this.titles = titles;
            this.details = details;
            takenLabel = (TextView)findViewById(R.id.takenLabel);
            reserveButton = (Button)findViewById(R.id.reserveMasechtaButton);
        }

        @Override
        public int getGroupCount() {
            return titles.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return (details.get(titles.get(groupPosition))).length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return titles.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return details.get(titles.get(groupPosition))[childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        /*
        Designs the group-headers, including a bubble count of number of available masechtos in the seder
         */
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView==null)
            {
                LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.seder_header, null);
            }
            TextView sederName = (TextView)convertView.findViewById(R.id.sederTitle);
            final TextView masechtosOpen = (TextView)convertView.findViewById(R.id.masechtosRemaining);
            sederName.setTypeface(null, Typeface.BOLD);
            sederName.setText((String)getGroup(groupPosition));
            int numOpen = details.get(titles.get(groupPosition)).length;
            final String groupPos=""+groupPosition;
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            ref.child("cases").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int openMasechtos=0;
                    int counter=0;
                    for(int i=0; i< dataSnapshot.child(caseKey).child("masechtos").child(groupPos).getChildrenCount(); i++)
                    {
                            MasechtaStatus ms =  dataSnapshot.child(caseKey).child("masechtos").child(groupPos).child(""+i).getValue(MasechtaStatus.class);
                             if(!ms.status)
                                openMasechtos += 1;

                    }

                    if(openMasechtos==0) {
                        masechtosOpen.setText("All Taken");
                        masechtosOpen.setBackgroundColor(0xFF00FF00);
                        masechtosOpen.setWidth(150);
                        masechtosOpen.setTextSize(15);
                    }
                    else {
                        masechtosOpen.setText(""+openMasechtos);//("" + details.get(titles.get(groupPosition)).length);

                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.bubble);
                        Bitmap.createScaledBitmap(bm, 10, 10, true);

                        //masechtosOpen.setBackgroundResource(R.drawable.bubble);
                        masechtosOpen.setBackgroundResource(R.drawable.bubble_circle);
                    }

                }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            return convertView;
        }

        /*
        Designs the children - with masechta info, availability status and reservation button
         */
        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
          // if(convertView==null)
          // {
               LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               convertView = inflater.inflate(R.layout.masechta_info, null);
         //  }
            final String groupPos = ""+groupPosition;
            final String childPos = ""+childPosition;

            TextView masechtaName = (TextView)convertView.findViewById(R.id.masechtaName);
            TextView perakim = (TextView)convertView.findViewById(R.id.numPerakim);
            TextView mishnayos = (TextView)convertView.findViewById(R.id.numMishnayos);
            final TextView taken = (TextView)convertView.findViewById(R.id.takenLabel);
            final Button takeMasechta = (Button)convertView.findViewById(R.id.reserveMasechtaButton);
            final TextView completedLabel = (TextView)convertView.findViewById(R.id.completedTag);
            masechtaName.setText((details.get(titles.get(groupPosition))[childPosition].hebName));
            perakim.setText((details.get(titles.get(groupPosition))[childPosition].numPerakim)+ " פרקים ");
            mishnayos.setText(""+  (details.get(titles.get(groupPosition))[childPosition].numMishnayos)+ " משניות ");
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

            ref.child("cases").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    MasechtaStatus ms = dataSnapshot.child(caseKey).child("masechtos").child(groupPos).child(childPos).getValue(MasechtaStatus.class);

                    if(ms.status)
                    {
                        taken.setVisibility(View.VISIBLE);
                        takeMasechta.setVisibility(View.GONE);
                        //if((boolean)dataSnapshot.child("cases").child(caseKey).child("masechtos").child(groupPos).child(childPos).child("completed").getValue()){
                        if(ms.completed){
                            completedLabel.setVisibility(View.VISIBLE);
                        }
                    }
                    else{
                        takeMasechta.setVisibility((View.VISIBLE));
                        taken.setVisibility(View.GONE);
                        //findViewById(R.id.reserveMasechtaButton).setVisibility(View.VISIBLE);
                        //findViewById(R.id.takenLabel).setVisibil ity(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            takeMasechta.setOnClickListener(
                    new View.OnClickListener(){
                        public void onClick(View view)
                        {
                            reserveButtonClicked(view, groupPosition, childPosition);
                        }
                    }
            );

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        /*
        When a masechta is chosen, collect all needed info and start 'ConfirmMasechta' activity and pass in the info
         */
        public void reserveButtonClicked(View view, int groupPosition, int childPosition)
        {
            final int groupPos = groupPosition;
            final int childPos = childPosition;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
            String key = mDatabase.getKey();

            Intent intent = new Intent(getBaseContext(), ConfirmMasechta.class);
            intent.putExtra("Masechta", details.get(titles.get(groupPosition))[childPosition]);
            intent.putExtra("caseKey", caseKey);
            intent.putExtra("seder", ""+groupPos);
            intent.putExtra("masechtaNum", ""+childPos);
            startActivity(intent);
        }
    }
}
