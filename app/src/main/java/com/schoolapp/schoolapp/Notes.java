package com.schoolapp.schoolapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;

public class Notes extends AppCompatActivity {

    ListView listView;
    FloatingActionButton newitem;
    FloatingActionButton backbtn;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        getListcontent();
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

    private void getListcontent() {
        File f;

    }
}
