package com.schoolapp.schoolapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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


        final String oldname = getIntent().getStringExtra("Name");
        final boolean isnew = getIntent().getBooleanExtra("IsNew", true);
        if(!isnew){
            notetext.setText(readNote(oldname));
            notename.setText(oldname);
        }

        savenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = notename.getText().toString();
                final String text = notetext.getText().toString();
                saveNoteText(oldname, name, text, isnew);
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

    private void deleteNote(final String name) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(NoteView.this);
        builder.setMessage("Delete?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                File f = new File(getFilesDir(),name);
                boolean delete = f.delete();
                if (delete){Toast.makeText(NoteView.this, "Successfully Deleted", Toast.LENGTH_SHORT).show(); finish();}
                else Toast.makeText(NoteView.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void saveNoteText(String oldname, String name, String text, Boolean isnew) {

        if (name.isEmpty()) Toast.makeText(this, "Field Name is empty", Toast.LENGTH_SHORT).show();
        else if (isnew) {
            try (FileOutputStream fos = openFileOutput(name, MODE_PRIVATE);
                 OutputStreamWriter osw = new OutputStreamWriter(fos)) {
                osw.write(text);
                Toast.makeText(NoteView.this, "Data successfully saved!", Toast.LENGTH_SHORT).show();
            } catch (IOException t) {
                Log.d(TAG, "saveNoteText: ", t);
            }
        } else {
            File f = new File(getFilesDir(),oldname);
            boolean delete = f.delete();
            if (delete) {
                try (FileOutputStream fos = openFileOutput(name, MODE_PRIVATE);
                     OutputStreamWriter osw = new OutputStreamWriter(fos)) {
                    osw.write(text);
                    Toast.makeText(NoteView.this, "Data successfully saved!", Toast.LENGTH_SHORT).show();
                } catch (IOException t) {
                    Log.d(TAG, "saveNoteText: ", t);
                }
            }
        }
    }

    private String readNote(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileInputStream fis = openFileInput(name);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String s;
            //Zeilenweise Lesen
            while ((s = br.readLine()) != null) {
                stringBuilder.append(s);
                stringBuilder.append("\n");
            }
        } catch (IOException t) {
            Log.d(TAG, "readNote:  ", t);
        }
        return stringBuilder.toString();
    }


}
