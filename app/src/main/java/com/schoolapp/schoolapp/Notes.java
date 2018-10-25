package com.schoolapp.schoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Notes extends AppCompatActivity {

    ListAdapter listAdapter;
    ListView noteView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        getListcontent();
        Button newitem = findViewById(R.id.additem);
        Button backbtn = findViewById(R.id.backbtn);

        newitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Notes.this, NoteView.class);
                i.putExtra("Name", "");
                i.putExtra("IsNew", true);
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
                i.putExtra("IsNew", false);
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
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, f);
        noteView = findViewById(R.id.notelist);
        noteView.setAdapter(listAdapter);
    }


}
