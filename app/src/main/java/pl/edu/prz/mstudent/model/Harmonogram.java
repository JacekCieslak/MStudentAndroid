package pl.edu.prz.mstudent.model;

import java.util.ArrayList;
import java.util.List;

public class Harmonogram {



    public String course;
    public String place;
    public String hour;
    public String week;

    public final List<HarmonogramChild > children = new ArrayList<HarmonogramChild>();


    public Harmonogram(String course, String place, String hour, String week) {
        this.course = course;
        this.place = place;
        this.hour = hour;
        this.week = week;
    }


}
