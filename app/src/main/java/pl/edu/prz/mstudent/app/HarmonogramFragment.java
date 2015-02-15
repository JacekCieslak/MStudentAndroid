package pl.edu.prz.mstudent.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.edu.prz.mstudent.R;
import pl.edu.prz.mstudent.model.Harmonogram;
import pl.edu.prz.mstudent.model.HarmonogramChild;


public class HarmonogramFragment extends Fragment {
    View rootview;
    SparseArray<Harmonogram> groups = new SparseArray<Harmonogram>();
    ArrayList<String> dayList = new ArrayList<String>();
    public CharSequence mTitle;
    private  ProgressDialog prgDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.harmonogram, container, false);

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Proszę Czekać...");
        prgDialog.setCancelable(false);

        Spinner daysSpinner = (Spinner) rootview.findViewById(R.id.days);
        dayList.add("Poniedziałek");
        dayList.add("Wtorek");
        dayList.add("Środa");
        dayList.add("Czwartek");
        dayList.add("Piątek");
        dayList.add("Sobota");
        dayList.add("Niedziela");


        ArrayAdapter< String > adapter =
                new ArrayAdapter < String > (getActivity(), android.R.layout.select_dialog_item, dayList);
        daysSpinner.setAdapter(adapter);


        daysSpinner.setOnItemSelectedListener(new   AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                createDayHarmonogram(dayList.get(i));

            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub


            }

        });
        return rootview;
    }


    public void createDayHarmonogram(String day){

        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://mstudentservice.jelastic.dogado.eu/user/schedule/getschedule?day="+day, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                boolean stan = true;
                prgDialog.hide();
                try {

                    HarmonogramChild harmonogramChild;
                    JSONObject jsonobject;
                    groups.clear();

                    JSONArray obj = new JSONArray(response);
                    if (obj.length() != 0) {
                        for (int i = 0; i < obj.length(); i++) {
                            jsonobject = obj.getJSONObject(i);

                            harmonogramChild = new HarmonogramChild(
                                    jsonobject.optString("audytorium"),
                                    jsonobject.optString("information")
                            );
                            int plusOneHour = Integer.parseInt(jsonobject.optString("hour"))+1;
                            Harmonogram harmonogram = new Harmonogram(jsonobject.optString("classes"), jsonobject.optString("place"), "Godz. "+ jsonobject.optString("hour")+" - "+Integer.toString(plusOneHour),jsonobject.optString("week"));
                            harmonogram.children.add(harmonogramChild);
                            groups.append(i, harmonogram);

                            ExpandableListView listView = (ExpandableListView) rootview.findViewById(R.id.harmonogramExpandableListView);
                            HarmonogramExpandableListAdapter adapter = new HarmonogramExpandableListAdapter(getActivity(), groups);

                            listView.setAdapter(adapter);

                        }
                    }else {
                        Harmonogram harmonogram = new Harmonogram("Brak zajęć tego dnia.","","","");
                        groups.append(0, harmonogram);
                        ExpandableListView listView = (ExpandableListView) rootview.findViewById(R.id.harmonogramExpandableListView);
                        HarmonogramExpandableListAdapter adapter = new HarmonogramExpandableListAdapter(getActivity(), groups);

                        listView.setAdapter(adapter);
                    }
                    }catch(JSONException e){
                        Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                        prgDialog.hide();
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