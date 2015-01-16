package pl.edu.prz.mstudent.model;

import java.util.ArrayList;
import java.util.List;

public class Group {

    public String string;
    public final List<HarmonogramChild > children = new ArrayList<HarmonogramChild>();

    public Group(String string) {
        this.string = string;
    }

}
