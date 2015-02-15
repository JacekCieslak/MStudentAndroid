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
        if(grade.grade == 2) {
            image.setImageResource(R.drawable.ocena2);
        }
        else if(grade.grade == 3)
            image.setImageResource(R.drawable.ocena3);
        else if(grade.grade == 3.5)
            image.setImageResource(R.drawable.ocena3_2);
        else if(grade.grade == 4)
            image.setImageResource(R.drawable.ocena4);
        else if(grade.grade == 4.5)
            image.setImageResource(R.drawable.ocena4_2);
        else if(grade.grade == 5)
            image.setImageResource(R.drawable.ocena5);
        else
            image.setVisibility(View.GONE);
        return convertView;

    }

}
