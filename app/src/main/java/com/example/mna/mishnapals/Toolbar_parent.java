package com.example.mna.mishnapals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public abstract class Toolbar_parent extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        /*setContentView(getLayoutResource());
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_menu_home);
        myToolbar.setNavigationOnClickListener(
                v -> {
                    Intent intent = new Intent(getBaseContext(), MyMishnayos.class);
                    startActivity(intent);
                }
        );*/
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        super.setContentView(view);
        configToolbar(view);
    }

    private void configToolbar(View view) {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_menu_home);
        myToolbar.setNavigationOnClickListener(
                v -> {
                    Intent intent = new Intent(getBaseContext(), HomeScreen.class);
                    startActivity(intent);
                }
        );
    }
}
