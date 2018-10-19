package com.schoolapp.schoolapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;


public class MainActivity extends AppCompatActivity {

        SwipeButton musicbtn;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.mainmenu);

            musicbtn = findViewById(R.id.musicbtn);
            musicbtn.setOnStateChangeListener(new OnStateChangeListener() {
                @Override
                public void onStateChange(boolean active) {
                    Toast.makeText(MainActivity.this, "Test"+ active, Toast.LENGTH_SHORT).show();
                }
            });


        }


    }
