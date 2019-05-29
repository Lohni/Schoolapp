package com.schoolapp.schoolapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

public class Calendar extends AppCompatActivity {

    //Button bn;
    MaterialCalendarView calendarview;
   // boolean status = false;


   @Override
    protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_calendar);
     // bn = (Button) findViewById(R.id.button);

       calendarview = findViewById(R.id.calendarView);


            calendarview.setTileSize(150);
            showcurrentdate();
            calendarview.setSelectedDate(CalendarDay.today());


            calendarview.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);



               calendarview.setOnDateChangedListener(new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay date, boolean b) {
                        Toast.makeText(Calendar.this,""+ date,Toast.LENGTH_SHORT).show();

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        FragmentOne f1 = new FragmentOne();
                        fragmentTransaction.add(R.id.fragment_container,f1);
                        fragmentTransaction.commit();

                       // bn.setOnClickListener(new View.OnClickListener() {
                           // @Override
                           // public void onClick(View v) {
                                //FragmentManager fragmentManager = getSupportFragmentManager();
                        //                               // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                                //if(!status){
                                   // FragmentOne f1 = new FragmentOne();
                                   // fragmentTransaction.add(R.id.fragment_container,f1);
                                    //fragmentTransaction.commit();
                                    //bn.setText("LOAD SECOND FRAGMENT");
                                   // status = true;
                                //}else{

                                   // FragmentTwo f2 = new FragmentTwo();
                                   // fragmentTransaction.add(R.id.fragment_container,f2);
                                   // fragmentTransaction.commit();
                                    //}




                          //  }
                        //});

                    }
              });





   }

    private void showcurrentdate(){
        calendarview.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        calendarview.setDateSelected(CalendarDay.today(), true);
    }



}



