/*
Home screen class with signin, and home-screen options of new case,
all avail mishnayos, and user's mishnayos
 */
package mna.mishnapals;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mna.mishnapals.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeScreen extends AppCompatActivity {

    Button searchCaseButton;
    EditText caseSearch;

    FirebaseUser user;
    //specify the options menu, for signing out
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.mishna_pals_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    startActivity(new Intent(getBaseContext(), FirebaseUI_Auth.class));
                    HomeScreen.this.finish();
                });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null)
        {
            // startActivity(new Intent(getBaseContext(), EmailPassword.class));
            startActivity(new Intent(getBaseContext(), FirebaseUI_Auth.class));
            HomeScreen.this.finish();
        }
        //Log.d("a", "user is not null in home");
        //Log.d("username", user.getEmail());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //next 2 lines added for androidx toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_menu_home);
        myToolbar.setNavigationOnClickListener(
                v -> {
                    Intent intent = new Intent(getBaseContext(), HomeScreen.class);
                    startActivity(intent);
                    finish();
                }
        );


        //setContentView(R.layout.home_screen_horizontal);
        View layout = findViewById(R.id.backgroundLayoutHome);
        Drawable background = layout.getBackground();
        background.setAlpha(50);
        Button newCase = (Button) findViewById(R.id.newCase);
        newCase.setOnClickListener(
                v -> newCaseClicked(v)
        );

        Button allAvail = (Button) findViewById(R.id.viewAllAvailButton);
        allAvail.setOnClickListener(
                v -> viewAllClicked(v)
        );

        final Button myMishnayos = (Button)findViewById(R.id.myMishnayosButton);
        myMishnayos.setOnClickListener(v -> myMishnayosClicked(v));

        searchCaseButton = (Button) findViewById(R.id.searchCaseButton);
        caseSearch = (EditText) findViewById(R.id.caseIdSearch);

        searchCaseButton.setOnClickListener(v -> {
            boolean internetConnected = InternetCheckUtility.internetStatus();
            if (!internetConnected) {
                Toast.makeText(HomeScreen.this, "Please check your internet connection - It looks like you might be offline", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getBaseContext(), SearchResult.class);
                intent.putExtra("caseId", caseSearch.getText().toString());
                startActivity(intent);
            }
        });
    }
    public void newCaseClicked(View view)
    {
        Intent intent = new Intent(getBaseContext(), NewCase.class);
        startActivity(intent);
    }

    public void viewAllClicked(View view)
    {
        boolean internetConnected = InternetCheckUtility.internetStatus();
        if (!internetConnected) {
            Toast.makeText(HomeScreen.this, "Please check your internet connection - It looks like you might be offline", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(getBaseContext(), PublicCases.class));}
    }

    public void myMishnayosClicked(View view)
    {
        boolean internetConnected = InternetCheckUtility.internetStatus();
        if (!internetConnected) {
            Toast.makeText(HomeScreen.this, "Please check your internet connection - It looks like you might be offline", Toast.LENGTH_SHORT).show();
        } else {
        startActivity(new Intent(getBaseContext(), MyMishnayos.class));}
    }
}
