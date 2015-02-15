package pl.edu.prz.mstudent.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.prz.mstudent.R;
import pl.edu.prz.mstudent.model.Harmonogram;
import pl.edu.prz.mstudent.model.HarmonogramChild;


public class MessageFragment extends Fragment {
    View rootview;
    SparseArray<Harmonogram> groups = new SparseArray<Harmonogram>();
    ProgressDialog prgDialog;
    UserSessionManager session;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.mesage, container, false);
        session = new UserSessionManager(getActivity().getApplicationContext());

//        ProgressDialog prgDialog = new ProgressDialog(getActivity().getApplicationContext());
//        prgDialog.setMessage("Proszę czekać...");
//        prgDialog.setCancelable(false);



        
        return rootview;
    }

    public void createData() {
     //  HarmonogramChild harmonogramChild = new HarmonogramChild("nie","9","A/B","B100","Info");
        for (int j = 0; j < 5; j++) {
       //     Group group = new Group("Test " + j);
       //         group.children.add(harmonogramChild);
        //    groups.append(j, group);

        }

    }


    public void createDayHarmonogram(){

    //    prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://mstudentservice.jelastic.dogado.eu/adminstrator/schedule/getschedule", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                boolean stan = true;
      //          prgDialog.hide();
                try {

                    HarmonogramChild harmonogramChild;
                    JSONObject jsonobject;
                    JSONArray obj = new JSONArray(response);
                    for (int i = 0; i < obj.length(); i++) {
                        jsonobject = obj.getJSONObject(i);

//                         harmonogramChild = new HarmonogramChild(
//                                jsonobject.optString("audytorium"),
//                                jsonobject.optString("week"),
//                                jsonobject.optString("information")
//                        );
                     //   Group group = new Group(jsonobject.optString("classes"));
                     //   group.children.add(harmonogramChild);
                    //    groups.append(i, group);

                        ExpandableListView listView = (ExpandableListView) rootview.findViewById(R.id.expandableListView);
                        HarmonogramExpandableListAdapter adapter = new HarmonogramExpandableListAdapter(getActivity(), groups);
                        listView.setAdapter(adapter);

                    }

                } catch (JSONException e) {
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
