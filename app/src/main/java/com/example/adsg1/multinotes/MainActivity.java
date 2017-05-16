package com.example.adsg1.multinotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
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
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";

     // Main content is here

    private ArrayList<Note> reverseList = new ArrayList<Note>();
    private ArrayList<Note> notesfetcher = new ArrayList<Note>() ;

    private RecyclerView recyclerView; // Layout's recyclerview

    private NotesAdapter notesAdapter; // Data to recyclerview adapter

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    Note note1;

    Note noteFromRows;
    private static final int NEW = 1;
    private static final int EDIT = 2;





    TextView name;
    EditText description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


            try {
                notesfetcher = loadFile();
            } catch (IOException e) {

                e.printStackTrace();
            }

        if( notesfetcher.size() != 0) {


            new MyAsyncTask(this).execute("");

        }



        adapter = new NotesAdapter(notesfetcher, this);
        recyclerView.setAdapter(adapter);




    }


@Override
public void onResume()
{
    super.onStart();

    int firstPosition =0 ;
    Intent intent2 = getIntent();

/*

    if(intent2.getSerializableExtra("firstPosition") != null){
        firstPosition = (int)intent2.getSerializableExtra("firstPosition");
        if(firstPosition != 1)
        {
            Collections.reverse(notesfetcher);
        }
    }
*/

  if(notesfetcher.size() != 0) {
    new MyAsyncTask(this).execute("");
}

}

    @Override
    public void onPause()
    {
        super.onPause();

    }



    public class MyAsyncTask extends AsyncTask<String, Void, Boolean> {



        private MainActivity mainActivity;
        private int count;


        public MyAsyncTask(MainActivity ma)
        {
            mainActivity = ma;
        }



        @Override
        protected Boolean doInBackground(String... params)
        {

            StringBuilder sb = new StringBuilder();
            InputStream is = null;
            try
            {
                is = mainActivity.getApplicationContext().openFileInput("MultiNotes.json");
            }
            catch (FileNotFoundException e)
            {
               is = null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String l;
            try
            {
                while((l = reader.readLine())!=null)
                {
                    sb.append(l).append('\n');
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            notesfetcher  =  parseJSON(sb.toString());

            if(notesfetcher == null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }

        private ArrayList<Note> parseJSON(String s) {
            ArrayList<Note> notes1List = new ArrayList<>();
            try {
                JSONArray jr = new JSONArray(s);
                count = jr.length();

                for (int i = 0; i < jr.length(); i++) {
                    JSONObject jnotes = (JSONObject) jr.get(i);
                    String title = jnotes.getString("Title");
                    String description = jnotes.getString("NoteInfo");
               //     String lastSave = jnotes.getString("LastSave");
                    String lastDateUpdate = jnotes.getString("LastDateUpdate");

                    notes1List.add(new Note(title,description, lastDateUpdate ));
                }
                return notes1List;
            } catch (JSONException e) {

                Toast.makeText(mainActivity, "No....", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return notes1List;
            }

        }
        @Override
        protected void onPostExecute(Boolean result)
        {
            super.onPostExecute(result);

        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuAdd:
                Intent intent = new Intent(MainActivity.this, ActivityMultiNotesRow.class);
                startActivityForResult(intent, NEW);
                return true;

            case R.id.menuInfo:
                Intent intent3= new Intent(MainActivity.this, About.class);
                startActivityForResult(intent3, NEW);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v)
    {
        Log.d(TAG, "Running Good !!!");
        int pos = recyclerView.getChildLayoutPosition(v);
        note1 = notesfetcher.get(pos);
        Intent intent = new Intent(MainActivity.this, ActivityMultiNotesRow.class);
        intent.putExtra("changedObject", note1);
        intent.putExtra("positionToBeChanged",pos);
        startActivityForResult(intent, EDIT);
    }



    @Override
    public boolean onLongClick(View v)
    {
        note1 = new Note();
        Log.d(TAG, "Working!!!!!");
        final int pos = recyclerView.getChildLayoutPosition(v);
        note1 = notesfetcher.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Intent data = getIntent();
        builder.setTitle("Delete Note!");
        builder.setMessage("Delete Note"+"  "+ note1.getName()+"?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                notesfetcher.remove(note1);
                adapter = new NotesAdapter(notesfetcher, MainActivity.this );
                recyclerView.setAdapter(adapter);
                saveFile();

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
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
            } catch(FileNotFoundException e){
     //       noteList = null;
            return noteList;


        } catch (IOException e) {

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
        String LastSave="";
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

    public void saveFile() {
        //     ArrayList<Note> arrayList = null    ;
        Toast.makeText(this,"Saving File..",Toast.LENGTH_SHORT).show();
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("MultiNotes.json", Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writeMessageArrayList(writer,notesfetcher);
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


}

