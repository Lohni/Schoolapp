package com.schoolapp.schoolapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

    public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.mainmenu);
            thisIsATest();
        }

        private void thisIsATest() {

            System.out.println("Dei Muata sei Gsicht");
        }



    }
