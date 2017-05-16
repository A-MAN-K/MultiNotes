package com.example.adsg1.multinotes;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by adsg1 on 2/22/2017.
 */

public class MyNotesHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    public TextView name;
    public TextView description;
    public TextView lastDateUpdate;

    public MyNotesHolder(View view) {
        super(view);
        cardView = (CardView)view.findViewById(R.id.cardView);
        name = (TextView) view.findViewById(R.id.nameCard);
        description = (TextView) view.findViewById(R.id.descriptionCard);
        lastDateUpdate = (TextView) view.findViewById(R.id.lastDateSave);
    }

}
