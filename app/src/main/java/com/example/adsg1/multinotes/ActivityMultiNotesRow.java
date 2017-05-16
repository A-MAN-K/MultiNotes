package com.example.adsg1.multinotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by adsg1 on 2/22/2017.
 */

public class ActivityMultiNotesRow extends AppCompatActivity {

    private static final String TAG = "ActivityMultiNotesRow";
    private TextView name;
    private EditText description;
    Note note = new Note();
    String nameString;
    int flag =0;
    public ArrayList<Note> arrayList = new ArrayList<Note>();
    Note noteChanged = new Note();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_notes_row);

        try {
            arrayList = loadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Intent intent = getIntent();

        noteChanged = (Note)intent.getSerializableExtra("changedObject");

        if(noteChanged != null) {

            name = (TextView) findViewById(R.id.name);
            description = (EditText) findViewById(R.id.description);
            name.setText(noteChanged.getName().toString());
            description.setText(noteChanged.getDescription().toString());

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return true;
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuSave:
                name = (TextView) findViewById(R.id.name);
                nameString = name.getText().toString();
                note.setName(nameString); // setting the name of the notePad
                description = (EditText) findViewById(R.id.description);
                description.setMovementMethod(new ScrollingMovementMethod());
                description.setTextIsSelectable(true);
                note.setDescription(description.getText().toString());
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("EEE MMM d, hh:mm:ss a");
                String strDate = mdformat.format(calendar.getTime());
                note.setLastDateUpdate(String.valueOf(strDate));


                if( nameString.equals("") || nameString.equals("")) {
                    Toast.makeText(this, "Notes without title can't be saved", Toast.LENGTH_LONG).show();
                }
                else
                {

                    Toast.makeText(this, "Note Saved", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ActivityMultiNotesRow.this, MainActivity.class).putExtra("noteFromRows",note);
                    startActivity(intent);
                    return true;

                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPause()
    {
        super.onPause();
        if(flag == 1 || flag == 0) {
            note.setName(name.getText().toString());
            note.setDescription(description.getText().toString());
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("EEE MMM d, hh:mm:ss a");
            String strDate = mdformat.format(calendar.getTime());
            note.setLastDateUpdate(String.valueOf(strDate));


            int positionToBeChanged = 0;
            Intent intent = getIntent();
            if (intent.getSerializableExtra("positionToBeChanged") != null) {
                positionToBeChanged = (int) intent.getSerializableExtra("positionToBeChanged");
             //   arrayList.set(positionToBeChanged, note);
                arrayList.remove(positionToBeChanged);
                arrayList.add(note);

                Note temp = new Note();
                temp = arrayList.get(arrayList.size()-1);

                for(int i = arrayList.size()-1; i > 0; i--)
                {
                    arrayList.set(i,arrayList.get(i-1));
                }
                arrayList.set(0, temp);


            } else {
                arrayList.add(note);
               /* Intent intent2 = new Intent(ActivityMultiNotesRow.this, MainActivity.class);
                int firstPosition = 1;
                intent.putExtra("firstPosition", firstPosition);
*/              Collections.reverse(arrayList);

            }

                saveFile();

        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to save the note?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void onClick(DialogInterface dialog, int id) {

                        int positionToBeChanged = 0;
                        Intent intent = getIntent();
                        if (intent.getSerializableExtra("positionToBeChanged") != null) {
                            positionToBeChanged = (int) intent.getSerializableExtra("positionToBeChanged");
                            name = (TextView)findViewById(R.id.name);
                            description = (EditText)findViewById(R.id.description);
                            note.setName(name.getText().toString());
                            note.setDescription(description.getText().toString());
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("EEE MMM d, hh:mm:ss a");
                            String strDate = mdformat.format(calendar.getTime());
                            flag = 1; //setting flag in case of Back Yes Pressed
                            note.setLastDateUpdate(String.valueOf(strDate));
                       //     arrayList.set(positionToBeChanged, note);
                       //     saveFile();
                            if( note.getName().equals("") || note.getName() == null ) {
                                Toast.makeText(ActivityMultiNotesRow.this, "Notes without title can't be saved", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Intent intent1 = new Intent(ActivityMultiNotesRow.this, MainActivity.class).putExtra("noteFromRows", note);
                                startActivity(intent1);
                            }

                        }
                        else
                        {
                            name = (TextView)findViewById(R.id.name);
                            description = (EditText)findViewById(R.id.description);
                            note.setName(name.getText().toString());
                            note.setDescription(description.getText().toString());
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("EEE MMM d, hh:mm:ss a");
                            String strDate = mdformat.format(calendar.getTime());
                            note.setLastDateUpdate(String.valueOf(strDate));
                            flag = 1;
                        //    arrayList.add(note);
                          //  saveFile();
                            if( note.getName().equals("") || note.getName() == null ) {
                                Toast.makeText(ActivityMultiNotesRow.this, "Notes without title can't be saved", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Intent intent1 = new Intent(ActivityMultiNotesRow.this, MainActivity.class).putExtra("noteFromRows", note);
                                startActivity(intent1);
                            }

                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        flag = 2;
                        Intent intent1 = new Intent(ActivityMultiNotesRow.this, MainActivity.class).putExtra("noteFromRows",note);
                        startActivity(intent1);

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void saveFile() {
   //     ArrayList<Note> arrayList = null    ;
        Toast.makeText(this,"Saving File..",Toast.LENGTH_SHORT).show();
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("MultiNotes.json", Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writeMessageArrayList(writer,arrayList);
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeMessageArrayList(JsonWriter writer,ArrayList<Note> arrayList) throws IOException {
        writer.beginArray();
            for (Note noteVal : arrayList) {
                writeValues(writer, noteVal);
            }
        writer.endArray();
    }

    public void writeValues(JsonWriter writer,Note noteVal) throws IOException {
        writer.beginObject();
        writer.name("Title").value(noteVal.getName());
        writer.name("LastDateUpdate").value(noteVal.getLastDateUpdate());
        writer.name("NoteInfo").value(noteVal.getDescription());
        writer.endObject();
    }

    //Override Method

    @Override
    protected void onDestroy()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();

        super.onDestroy();
        if(dialog!= null)
        {
            dialog.dismiss();
            dialog = null;
        }
    }




    protected void onResume()
    {
        super.onStart();
        //load file
        try {
            arrayList = loadFile();  //Load the JSON containing the Notepad data if any exist
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(note.getDescription()!=null)    //null means no files has been loaded
        {
            name.setText(note.getName());
            description.setText(note.getDescription());
            Log.d(TAG, "Run Perfect:");
        }
    }





    private ArrayList<Note> loadFile() throws IOException {

        Log.d(TAG, "loadFile: Loading JSON File");

        ArrayList<Note> noteList = new ArrayList<Note>();
        // Note note = new Note();

        try {
            InputStream is = getApplicationContext().openFileInput("MultiNotes.json");
            JsonReader reader = new JsonReader(new InputStreamReader(is, getString(R.string.encoding)));
            noteList = readNoteList(reader);
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return noteList;
    }


    public ArrayList<Note> readNoteList(JsonReader reader) throws IOException {
        ArrayList<Note> noteList=new ArrayList<>();
        reader.beginArray();
        while(reader.hasNext()){
            noteList.add(readNotes(reader));
        }
        reader.endArray();
        return noteList;
    }

    public Note readNotes(JsonReader reader) throws IOException {
        String Title="";
      //  String LastSave="";
        String NoteInfo="";
        String LastDateUpdate="";

        reader.beginObject();
        while(reader.hasNext()){
            String name=reader.nextName();
            if(name.equals("Title")){
                Title=reader.nextString();
            }
            else if(name.equals("LastDateUpdate")){
                LastDateUpdate=reader.nextString();
            }
            else if(name.equals("NoteInfo")){
                NoteInfo=reader.nextString();
            }
            else{
                reader.skipValue();
            }

        }
        reader.endObject();
        return new Note(Title, NoteInfo, LastDateUpdate);
    }



}
