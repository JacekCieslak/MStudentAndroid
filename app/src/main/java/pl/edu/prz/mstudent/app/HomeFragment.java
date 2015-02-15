package pl.edu.prz.mstudent.app;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import pl.edu.prz.mstudent.R;


public class HomeFragment extends Fragment {
    View rootview;
    private String username;
    UserSessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.home, container, false);
        session = new UserSessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        username = user.get(UserSessionManager.KEY_EMAIL);
        TextView uName = (TextView) rootview.findViewById(R.id.userName);
        uName.setText("Witamy: "+username + "@stud.prz.edu.pl");

        TextView tx = (TextView) rootview.findViewById(R.id.mstudentLogo);
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Sketchtica.ttf");
        tx.setTypeface(custom_font);
        return rootview;
    }
}
