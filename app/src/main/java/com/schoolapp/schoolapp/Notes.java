package com.schoolapp.schoolapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class Notes extends AppCompatActivity {

    ListView listView;
    FloatingActionButton newitem;
    FloatingActionButton backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        listView = findViewById(R.id.notelist);
        newitem = findViewById(R.id.newitem);
        backbtn = findViewById(R.id.backbtn);

        newitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
