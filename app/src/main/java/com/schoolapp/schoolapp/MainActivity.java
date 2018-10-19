package com.schoolapp.schoolapp;

import android.Manifest;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;


public class MainActivity extends AppCompatActivity {

        SwipeButton musicbtn;
        SwipeButton achievbtn;
        SwipeButton subjectbtn;
        SwipeButton calendarbtn;
        SwipeButton notebtn;
        SwipeButton homewrkbtn;
        FloatingActionButton settings;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.mainmenu);

            musicbtn = findViewById(R.id.musicbtn);
            achievbtn = findViewById(R.id.achievbtn);
            subjectbtn = findViewById(R.id.faecherbtn);
            calendarbtn = findViewById(R.id.calendarbtn);
            notebtn = findViewById(R.id.notebtn);
            homewrkbtn = findViewById(R.id.homewrkbtn);
            settings = findViewById(R.id.settings);

            musicbtn.setOnActiveListener(new OnActiveListener() {
                @Override
                public void onActive() {
                    Toast.makeText(MainActivity.this, "Music",Toast.LENGTH_SHORT).show();
                }
            });
            achievbtn.setOnActiveListener(new OnActiveListener() {
                @Override
                public void onActive() {
                    Toast.makeText(MainActivity.this, "Achievements",Toast.LENGTH_SHORT).show();
                }
            });
            subjectbtn.setOnActiveListener(new OnActiveListener() {
                @Override
                public void onActive() {
                    Toast.makeText(MainActivity.this, "Subject",Toast.LENGTH_SHORT).show();
                }
            });
            calendarbtn.setOnActiveListener(new OnActiveListener() {
                @Override
                public void onActive() {
                    Toast.makeText(MainActivity.this, "Calendar",Toast.LENGTH_SHORT).show();
                }
            });
            notebtn.setOnActiveListener(new OnActiveListener() {
                @Override
                public void onActive() {
                    Intent intent = new Intent(MainActivity.this, Notes.class);
                    startActivity(intent);
                }
            });
            homewrkbtn.setOnActiveListener(new OnActiveListener() {
                @Override
                public void onActive() {
                    Toast.makeText(MainActivity.this, "Homework",Toast.LENGTH_SHORT).show();
                }
            });

            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
