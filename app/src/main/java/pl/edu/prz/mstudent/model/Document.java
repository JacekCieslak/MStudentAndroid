package pl.edu.prz.mstudent.model;

import java.util.ArrayList;
import java.util.List;

public class Document {



    public String name;
    public final List<DocumentChild> children = new ArrayList<DocumentChild>();

    public Document(String name) {
        this.name = name;
    }
}
