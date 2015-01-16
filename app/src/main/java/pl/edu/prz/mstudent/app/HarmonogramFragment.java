package pl.edu.prz.mstudent.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.prz.mstudent.R;
import pl.edu.prz.mstudent.model.Group;
import pl.edu.prz.mstudent.model.HarmonogramChild;


public class HarmonogramFragment extends Fragment {
    View rootview;
    SparseArray<Group> groups = new SparseArray<Group>();
    public CharSequence mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Spinner daysSpinner = (Spinner) getView().findViewById(R.id.days);
//        String[] items = new String[] {
//                "Poniedziałek", "Wtorek", "środa", "Czwartek", "Piątek"};
//
//        ArrayAdapter< String > adapter =
//                new ArrayAdapter < String > (getActivity(), android.R.layout.simple_spinner_item, items);
//        daysSpinner.setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.harmonogram, container, false);
        Log.i("Fragment:", "harmonogramme");
        Spinner daysSpinner = (Spinner) rootview.findViewById(R.id.days);
        String[] items = new String[] {
                "Poniedziałek", "Wtorek", "środa", "Czwartek", "Piątek", "Sobota", "Niedziela"};
        createDayHarmonogram();
        ArrayAdapter< String > adapter =
                new ArrayAdapter < String > (getActivity(), android.R.layout.simple_spinner_item, items);
        daysSpinner.setAdapter(adapter);

        //        ListView list = (ListView) rootview.findViewById(R.id.listView);
//        list.setAdapter(adapter);
//        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
//                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
//                "Linux", "OS/2" };
//
//        ListView listView = (ListView) rootview.findViewById(R.id.listView);
//
//        ArrayAdapter<String> adapterListView = new ArrayAdapter<String>(getActivity().getApplicationContext(),
//                R.layout.harmonogram_list_view, R.id.secondLine, values);
//        listView.setAdapter(adapterListView);


        return rootview;
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

                        harmonogramChild = new HarmonogramChild(
                                jsonobject.optString("hour"),
                                jsonobject.optString("week"),
                                jsonobject.optString("place"),
                                jsonobject.optString("audytorium"),
                                jsonobject.optString("info")
                        );
                        Group group = new Group(jsonobject.optString("classes"));
                        group.children.add(harmonogramChild);
                        groups.append(i, group);

                        ExpandableListView listView = (ExpandableListView) rootview.findViewById(R.id.harmonogramExpandableListView);
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