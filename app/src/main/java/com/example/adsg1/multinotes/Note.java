package com.example.adsg1.multinotes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by adsg1 on 2/22/2017.
 */

public class Note implements Serializable{

    private String name;
    private String description;
    private String lastDateUpdate;
    private String lastSave;

    public Note() {
    }

    public Note(String name, String description, String lastDateUpdate) {


        this.name = name;
        this.description = description;
        this.lastDateUpdate = lastDateUpdate;
    }

    public String getLastSave() {

        return lastSave;
    }

    public void setLastSave(String lastSave) {
        this.lastSave = lastSave;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLastDateUpdate()
    {
        return lastDateUpdate;
    }

    public void setLastDateUpdate(String lastDateUpdate)
    {
        this.lastDateUpdate = lastDateUpdate;
    }

    @Override
    public String toString() {
        return "Note{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", lastDateUpdate='" + lastDateUpdate + '\'' +
                '}';
    }





}
