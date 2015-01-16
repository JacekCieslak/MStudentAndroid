package pl.edu.prz.mstudent.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jaca on 2015-01-11.
 */
public class GradeFragment extends Fragment {
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
       // rootview = inflater.inflate(R.layout.grade, container, false);
        Log.i("Fragment:", "grade");
        return rootview;
    }
}


