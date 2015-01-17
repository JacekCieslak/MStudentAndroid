package pl.edu.prz.mstudent.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pl.edu.prz.mstudent.R;
import pl.edu.prz.mstudent.model.Grade;


public class GradeFragment extends Fragment {
    View rootview;
    UserSessionManager session;
    ArrayList<Grade> listGrade = new ArrayList<Grade>();
    private ListView listViewGrade;
    public CharSequence mTitle;
    private ProgressDialog prgDialog;
    private String username;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.grade, container, false);
        session = new UserSessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
         username = user.get(UserSessionManager.KEY_EMAIL);
        TextView gradeTextView = (TextView) rootview.findViewById(R.id.gradeTextView);
        gradeTextView.setText("Oceny: "+username);
        createGradeListView();
        return rootview;
    }


    public void createGradeListView(){

//        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://mstudentservice.jelastic.dogado.eu/user/grade/getgrade?username="+username, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                boolean stan = true;
    //            prgDialog.hide();
                try {


                    JSONObject jsonobject;
                   // groups.clear();

                    JSONArray obj = new JSONArray(response);
                    int size = obj.length();
                    if (obj.length() != 0) {
                        for (int i = 0; i < obj.length(); i++) {
                            jsonobject = obj.getJSONObject(i);
                            Grade grade = new Grade(jsonobject.optString("title"), Float.parseFloat(jsonobject.optString("grade")));

                            listGrade.add(i, grade);

                        }
                        ListView listView = (ListView) rootview.findViewById(R.id.gradeListView);
                        GradeListApdapter adapter = new GradeListApdapter(getActivity().getApplicationContext(), listGrade);

                        listView.setAdapter(adapter);
                    }else {
//                        Harmonogram harmonogram = new Harmonogram("Brak zajęć tego dnia.","","");
//                        groups.append(0, harmonogram);
//                        ExpandableListView listView = (ExpandableListView) rootview.findViewById(R.id.harmonogramExpandableListView);
//                        HarmonogramExpandableListAdapter adapter = new HarmonogramExpandableListAdapter(getActivity(), groups);
//
//                        listView.setAdapter(adapter);
                    }
                }catch(JSONException e){
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                //        prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getActivity().getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                else if (statusCode == 500) {
                    Toast.makeText(getActivity().getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}


