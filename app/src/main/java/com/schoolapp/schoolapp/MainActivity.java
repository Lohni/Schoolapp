package com.schoolapp.schoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;


public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.mainmenu);

            SwipeButton musicbtn = findViewById(R.id.musicbtn);
            SwipeButton achievbtn = findViewById(R.id.achievbtn);
            SwipeButton subjectbtn = findViewById(R.id.faecherbtn);
            SwipeButton calendarbtn = findViewById(R.id.calendarbtn);
            SwipeButton notebtn = findViewById(R.id.notebtn);
            SwipeButton homewrkbtn = findViewById(R.id.homewrkbtn);
            FloatingActionButton settings = findViewById(R.id.settings);

            musicbtn.setOnActiveListener(new OnActiveListener() {
                @Override
                public void onActive() {
                    Toast.makeText(MainActivity.this, "Music",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Musicplayer.class);
                    startActivity(intent);
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
                    Intent intent = new Intent(MainActivity.this, Calendar.class);
                    startActivity(intent);
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
