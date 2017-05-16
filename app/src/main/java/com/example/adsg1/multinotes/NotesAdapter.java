package com.example.adsg1.multinotes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static com.example.adsg1.multinotes.R.id.parent;

/**
 * Created by adsg1 on 2/22/2017.
 */

public class NotesAdapter extends RecyclerView.Adapter<MyNotesHolder> {


    private static final String TAG = "NotesAdapter";
    private ArrayList<Note> noteList;
    private MainActivity mainAct;
    private LayoutInflater layoutInflater;


    public NotesAdapter(ArrayList<Note> noteList, MainActivity mainActivity) {

        this.noteList = noteList;
        mainAct  = mainActivity;

    }




    @Override
    public MyNotesHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        view.setOnClickListener(mainAct);
        view.setOnLongClickListener(mainAct);
        return new MyNotesHolder(view);
    }

    @Override
    public void onBindViewHolder(MyNotesHolder holder, int position) {
        Note note = noteList.get(position);
        holder.name.setText(note.getName());
        holder.description.setText(note.getDescription());
        holder.lastDateUpdate.setText(note.getLastDateUpdate());
        if(note.getDescription().length() > 79)
        {
            String str = "...";
            String parsedStr = note.getDescription().substring(0,79)+str;
            holder.description.setText(parsedStr);
        }
        else {
            holder.description.setText(note.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }






}
