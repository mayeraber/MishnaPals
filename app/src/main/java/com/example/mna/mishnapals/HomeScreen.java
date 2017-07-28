package com.example.mna.mishnapals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Button newCase = (Button)findViewById(R.id.newCase);
        newCase.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        buttonClicked(v);
                    }
                }
        );

        Button allAvail = (Button)findViewById(R.id.viewAllAvailButton);
        allAvail.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewAllClicked(v);
                    }
                }
        );

    }

    public void buttonClicked(View view)
    {
        Intent intent = new Intent(getBaseContext(), NewCase.class);
        startActivity(intent);
    }

    public void viewAllClicked(View view)
    {
        Intent intent = new Intent(getBaseContext(), MasechtosList.class);
        startActivity(intent);
    }
}
