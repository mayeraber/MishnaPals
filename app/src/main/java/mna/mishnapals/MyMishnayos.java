/*
Displays list of user's taken masechtos and allows user to choose one and open 'CompletedMasechta' activity
to then be able to change status to completed
 */
package mna.mishnapals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;

import mna.mishnapals.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

/**
 * Created by MNA on 8/13/2017.
 */

public class MyMishnayos extends Toolbar_parent {  //extends AppCompatActivity {

    protected RecyclerView rView;
    protected CustomAdapter cAdapter;
    protected RecyclerView.LayoutManager rLayout;
    protected ArrayList<CaseTakenInfo> cases = new ArrayList<>();
    protected Activity context;
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.my_mishnayos2);
        context = this;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        /*
        Populate 'cases' arraylist from user's branch in db with the cases this user participates in
         */

        ref.child("users").child(user.getUid()).child("cases").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cases.clear();
                for(DataSnapshot userCase : dataSnapshot.getChildren()){
                    CaseTakenInfo caseTaken = userCase.getValue(CaseTakenInfo.class);
                    caseTaken.setCaseMasID(userCase.getKey());
                    cases.add(caseTaken);
                }

                if (cases.size() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyMishnayos.this);
                    builder.setMessage("You have no active mishnayos");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
                    builder.show();
                }

                rView = findViewById(R.id.recyclerMasechtos);
                rLayout = new LinearLayoutManager(MyMishnayos.this);
                rView.setLayoutManager(rLayout);
                rView.addItemDecoration(new DividerItemDecoration(rView.getContext(), DividerItemDecoration.VERTICAL));
                Activity activity = (Activity) context;
                cAdapter = new CustomAdapter(cases, activity);
                rView.setAdapter(cAdapter);

                rView.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                ProgressBar pBar = findViewById(R.id.progressBarMyMishnayos);
                                pBar.setVisibility(View.GONE);
                                rView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        }
                );

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeDeleteCallback(cAdapter));
                itemTouchHelper.attachToRecyclerView(rView);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
