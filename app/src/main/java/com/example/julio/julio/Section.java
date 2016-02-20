package com.example.julio.julio;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pedro on 2/20/16.
 */
public class Section implements Serializable{
    ArrayList<String> content;
    String title;
    String description;
    int id;
    int color;

    public Section(ArrayList<String> content,String title,String description,int id,int color){
        this.content = content;
        this.title = title;
        this.description = description;
        this.id = id;
        this.color = color;
    }
}
