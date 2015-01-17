package pl.edu.prz.mstudent.app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.prz.mstudent.R;
import pl.edu.prz.mstudent.model.Grade;


public class GradeListApdapter extends ArrayAdapter<Grade> {


    public GradeListApdapter(Context context, ArrayList<Grade> groups) {
        super(context, 0, groups);
    }


    @Override
    public View getView ( int position, View convertView, ViewGroup parent ) {
        Grade grade = (Grade) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grade_list_view, parent, false);
        }

        TextView title = null;
        title = (TextView) convertView.findViewById(R.id.gradeTitle);
        title.setText(grade.title);



        ImageView image = (ImageView) convertView.findViewById(R.id.gradeIcon);
//        if(grade.grade  )
//            image.setImageResource(R.drawable.konsultacje);
//        else if(harmonogram.course.equals("Wykład"))
//            image.setImageResource(R.drawable.wyklad);
//        else if(harmonogram.course.equals("Laboratorium"))
//            image.setImageResource(R.drawable.laboratorium);
//        else
        image.setImageResource(R.drawable.trojka);
        return convertView;

    }

}
