package com.example.julio.julio;

import java.io.Serializable;

/**
 * Created by pedro on 2/20/16.
 */
public class Element implements Serializable{
    public enum ElementType{
        Image,
        Text,
        Latex
    }

    ElementType type;
    String content;

    public Element(ElementType type, String content){
        this.type = type;
        this.content = content;
    }
}
