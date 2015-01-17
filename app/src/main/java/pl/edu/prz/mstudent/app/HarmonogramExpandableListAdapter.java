package pl.edu.prz.mstudent.app;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import pl.edu.prz.mstudent.R;
import pl.edu.prz.mstudent.model.Harmonogram;
import pl.edu.prz.mstudent.model.HarmonogramChild;

public class HarmonogramExpandableListAdapter extends BaseExpandableListAdapter {

    private final SparseArray<Harmonogram> groups;
    public LayoutInflater inflater;
    public Activity activity;

    public HarmonogramExpandableListAdapter(Activity act, SparseArray<Harmonogram> groups) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final HarmonogramChild harmonogramChild = (HarmonogramChild) getChild(groupPosition, childPosition);
        TextView place = null;
        TextView audytorium = null;
        TextView info = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.harmonogram_details, null);
        }
        audytorium = (TextView) convertView.findViewById(R.id.audytoriumText);
        audytorium.setText(harmonogramChild.audytorium);

        info = (TextView) convertView.findViewById(R.id.infoText);
        if(!harmonogramChild.info.equals(""))
            info.setText(harmonogramChild.info);
        else
            info.setText("Brak dodatkowych informacji.");



        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, harmonogramChild.info,
                        Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.harmonogram_group, null);
        }
        Harmonogram harmonogram = (Harmonogram) getGroup(groupPosition);


        TextView title = null;
        TextView hour = null;
        TextView place = null;
        title = (TextView) convertView.findViewById(R.id.harmonogramTitle);
        title.setText(harmonogram.course);

        hour = (TextView) convertView.findViewById(R.id.sharmonogramHour);
        hour.setText(harmonogram.hour);

        place = (TextView) convertView.findViewById(R.id.harmonogramPlace);
        place.setText(harmonogram.place);

//        ImageView image = (ImageView) convertView.findViewById(R.id.harmonogramIcon);
//        image.setImageResource(R.drawable.wyklad);
        //e((CheckedTextView) convertView).setChecked(isExpanded);
//        TextView text = null;
////        CheckedTextView text = (CheckedTextView)   convertView.findViewById(R.id.textView1);
////        text.setText(group.string);
//        text  = (TextView) convertView.findViewById(R.id.firstLine);
//        text.setText("asda");
////        TextView hour = (TextView) convertView.findViewById(R.id.hourView);
////        hour.setText(group.place);
////        TextView place = (TextView) convertView.findViewById(R.id.secondLine);
////        place.setText(group.place);
//
        ImageView image = (ImageView) convertView.findViewById(R.id.harmonogramIcon);
        if(harmonogram.course .equals("Konsultacje"))
            image.setImageResource(R.drawable.konsultacje);
        else if(harmonogram.course.equals("Wykład"))
            image.setImageResource(R.drawable.wyklad);
        else if(harmonogram.course.equals("Laboratorium"))
            image.setImageResource(R.drawable.laboratorium);
        else
            image.setVisibility(View.GONE);
         return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
