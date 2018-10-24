package com.schoolapp.schoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class Notes extends AppCompatActivity {

    ListAdapter listAdapter;
    ArrayList<String> listdata;
    ListView noteView;
    Button newitem;
    Button backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        getListcontent();
        newitem = findViewById(R.id.additem);
        backbtn = findViewById(R.id.backbtn);

        newitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Notes.this, NoteView.class);
                i.putExtra("Name", "");
                startActivity(i);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        noteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentItem = listAdapter.getItem(position).toString();
                Intent i = new Intent(Notes.this, NoteView.class);
                i.putExtra("Name", currentItem);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListcontent();
    }

    private void getListcontent() {

        String[] f = fileList();
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, f);
        noteView = findViewById(R.id.notelist);
        noteView.setAdapter(listAdapter);
    }


}
