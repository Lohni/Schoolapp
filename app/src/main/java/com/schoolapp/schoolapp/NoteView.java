package com.schoolapp.schoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class NoteView extends AppCompatActivity {

    private static final String TAG = "NoteView";
    EditText notename, notetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_view);



        Button savenote = findViewById(R.id.savenote);
        Button deletenote = findViewById(R.id.deletenote);
        notename = findViewById(R.id.notename);
        notetext = findViewById(R.id.notetext);
        Button revertbtn = findViewById(R.id.revertbtn);



        if(!getIntent().getStringExtra("Name").isEmpty()){
            final String oldname = getIntent().getStringExtra("Name");
            notetext.setText(readNote(oldname));
            notename.setText(oldname);
        }

        savenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = notename.getText().toString();
                final String text = notetext.getText().toString();
                saveNoteText(name, text);
            }
        });
        deletenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = notename.getText().toString();
                deleteNote(name);
            }
        });
        revertbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void deleteNote(String name) {
        File f = new File(name);
        boolean delete = f.delete();

        if(delete){Toast.makeText(this, "Successfully deleted", Toast.LENGTH_SHORT).show(); finish();}
        else Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }

    private void saveNoteText(String name, String text) {
        if (name.isEmpty()) Toast.makeText(this, "Field Name is empty", Toast.LENGTH_SHORT).show();
        else {
            try (FileOutputStream fos = openFileOutput(name, MODE_PRIVATE);

                 OutputStreamWriter osw = new OutputStreamWriter(fos)) {
                osw.write(name + System.lineSeparator() + text);
                fos.close();
                Toast.makeText(NoteView.this, "Data successfully saved!", Toast.LENGTH_SHORT).show();
            } catch (IOException t) {
                Log.d(TAG, "saveNoteText: ", t);
            }
        }
    }

    private String readNote(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileInputStream fis = openFileInput("" + name);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String s;
            //Zeilenweise Lesen
            while ((s = br.readLine()) != null) {
                if (stringBuilder.length() > 0) stringBuilder.append('\n');
            }
            stringBuilder.append(s);
        } catch (IOException t) {
            Log.d(TAG, "readNote:  ", t);
        }
        return stringBuilder.toString();
    }


}
