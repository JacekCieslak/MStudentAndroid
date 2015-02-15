package pl.edu.prz.mstudent.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import pl.edu.prz.mstudent.R;
import pl.edu.prz.mstudent.model.Document;
import pl.edu.prz.mstudent.model.DocumentChild;


public class DocumentsFragment extends Fragment {
    View rootview;
    SparseArray<Document> groups = new SparseArray<Document>();
    UserSessionManager session;
    private ProgressDialog prgDialog;
    private String userName;
    private String course;
    String url = "http://mstudentservice.jelastic.dogado.eu/file/attachment?course=java efa-di-2&file=test.pdf&coursetype=laboratorium";
    // Media player object

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.documents, container, false);

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Proszę Czekać...");
        prgDialog.setCancelable(false);

        session = new UserSessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(UserSessionManager.KEY_EMAIL);

        getCourseName(userName+"@stud.prz.edu.pl");

        return rootview;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/mstudent/");
        boolean deleted = file.delete();
    }



    public String getCourseName(String userName){
        RequestParams params = new RequestParams();
        params.put("username", userName);
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://mstudentservice.jelastic.dogado.eu/user/document/getcourse",params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                boolean stan = true;
                prgDialog.hide();
                try {


                    String group = null;
                    JSONObject jsonobject;
                    JSONArray obj = new JSONArray(response);
                    groups.clear();

                    if (obj.length() != 0) {
                        for (int i = 0; i < obj.length(); i++) {
                            jsonobject = obj.getJSONObject(i);
                                 course =   jsonobject.optString("name");
                                  group =    jsonobject.optString("group");
                        }
                        TextView courseTextView = (TextView) rootview.findViewById(R.id.documentsTextView);
                        courseTextView.setText(course+" Grupa L"+group);
                        createMaterialsExpandableListView(course);
                    }
                }catch(JSONException e){
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

        return course;
    }

    public void createMaterialsExpandableListView(final String course){
        RequestParams params = new RequestParams();
        params.put("course", course);
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://mstudentservice.jelastic.dogado.eu/user/document/getdocuments",params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                boolean stanFileLab = false;
                boolean stanFileGrade = false;
                prgDialog.hide();
                try {

                    DocumentChild documentChildLab;
                    DocumentChild documentChildCourse;
                    JSONObject jsonobject;
                    groups.clear();

                    JSONArray obj = new JSONArray(response);
                    int size = obj.length();
                    if (obj.length() != 0) {
                        Document documentLab = new Document("Laboratorium");
                        Document documentCourse = new Document("Wyklady");
                        for (int i = 0; i < obj.length(); i++) {
                            jsonobject = obj.getJSONObject(i);
                            if(jsonobject.optString("type").equals("Laboratorium")) {
                                documentChildLab = new DocumentChild(jsonobject.optString("name"), course);
                                documentLab.children.add(documentChildLab);
                                stanFileLab = true;
                            }else {
                                documentChildCourse = new DocumentChild(jsonobject.optString("name"),course);
                                documentCourse.children.add(documentChildCourse);
                                stanFileGrade = true;
                            }

                        }
                        groups.append(0, documentLab);
                        groups.append(1, documentCourse);

                        ExpandableListView listView = (ExpandableListView) rootview.findViewById(R.id.documentExpandableListView);
                        DocumentsExpandableListAdapter adapter = new DocumentsExpandableListAdapter(getActivity(), groups);

                        listView.setAdapter(adapter);
                    }
                    if(!stanFileGrade || !stanFileLab) {

                        if (!stanFileLab) {
                            Document documentLab = new Document("Laboratorium");
                            documentChildLab = new DocumentChild("Brak materiałów w katalogu Laboratorium.", "");
                            documentLab.children.add(documentChildLab);
                            groups.append(0, documentLab);
                        }
                        if (!stanFileGrade) {
                            Document documentCourse = new Document("Wyklady");
                            documentChildCourse = new DocumentChild("Brak materiałów w katalogu Wykłady.", "");
                            documentCourse.children.add(documentChildCourse);
                            groups.append(1, documentCourse);
                        }
                        ExpandableListView listView = (ExpandableListView) rootview.findViewById(R.id.documentExpandableListView);
                        DocumentsExpandableListAdapter adapter = new DocumentsExpandableListAdapter(getActivity(), groups);
                        listView.setAdapter(adapter);
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