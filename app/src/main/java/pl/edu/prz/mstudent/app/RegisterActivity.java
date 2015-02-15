package pl.edu.prz.mstudent.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import pl.edu.prz.mstudent.R;
import pl.edu.prz.mstudent.model.Course;
import pl.edu.prz.mstudent.utility.Utility;



public class RegisterActivity extends Activity {
    ProgressDialog prgDialog;
    TextView errorMsg;
    EditText nameET;
    EditText surnameET;

    EditText emailET;
    EditText pwdET;
    Spinner courseSP;
    Spinner courseGrupSP;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        TextView tx = (TextView)findViewById(R.id.registerTextHeader);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),
                "fonts/Sketchtica.ttf");
        tx.setTypeface(custom_font);
        errorMsg = (TextView)findViewById(R.id.register_error);
        nameET = (EditText)findViewById(R.id.registerName);
        surnameET = (EditText)findViewById(R.id.registerSurname);
        emailET = (EditText)findViewById(R.id.registerEmail);
        pwdET = (EditText)findViewById(R.id.registerPassword);
        courseSP = (Spinner)findViewById(R.id.courseSpinner);
        courseGrupSP = (Spinner)findViewById(R.id.courseSpinnerGroup);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Proszę czekać...");
        prgDialog.setCancelable(false);

        createSpecializationListView();


        Spinner courseSpinnerGroup = (Spinner) findViewById(R.id.courseSpinnerGroup);
        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this,
                R.array.courseSpinnerGroup, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinnerGroup.setAdapter(adapter);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
    public void registerUser(View view){
        String toastError = "";
        boolean canInstert = true;
        String name = nameET.getText().toString();
        String surname = surnameET.getText().toString();
        String email = emailET.getText().toString();
        String password = pwdET.getText().toString();
        String course = courseSP.getSelectedItem().toString();
        String courseGroup = courseGrupSP.getSelectedItem().toString();

        RequestParams params = new RequestParams();
        if(Utility.isNotNull(name) && Utility.isNotNull(email) && Utility.isNotNull(password)&& Utility.isNotNull(surname)){

            if(Utility.validateEmail(email))
                params.put("username", email);
            else {
                toastError = toastError + "Niepoprawny email. Użyj pełnego studenckiego email. ";
                canInstert = false;
            }
            if(Utility.validateNameAndSurname(name))
                params.put("name", name);
            else{
                toastError = toastError + "Niepoprawne imię. Dozwolone tylko litery, zaczynając od dużej. ";
                canInstert = false;
            }
            if(Utility.validateNameAndSurname(surname))
                params.put("surname", surname);
            else{
                toastError = toastError + "Niepoprawne nazwisko. Dozwolone tylko litery, zaczynając od dużej. ";
                canInstert = false;
            }
            if(Utility.validatePassowrd(password)) {
                password = md5(password);
                params.put("password", password);
            }else{
                toastError = toastError + "Niepoprawne hasło. Dozwolone tylko litery oraz cyfry. Przynajmniej 5 znaków ";
                canInstert = false;
            }

                params.put("coursename",course);
                params.put("coursegroup", courseGroup);


                if(canInstert == true)
                     invokeWS(params);
                 else {
                    errorMsg.setText(toastError);
                    pwdET.setText("");
                }

        }
        else{
            errorMsg.setText("Wszytkie pola muszą być uzupełnione");
        }

    }

    public void createSpecializationListView(){

        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://mstudentservice.jelastic.dogado.eu/common/courses", new AsyncHttpResponseHandler() {
            ArrayList<Course> specialization = new ArrayList<Course>();
            ArrayList<String> courseName = new ArrayList<String>();
            ArrayList<String> specializationGroup = new ArrayList<String>();

            @Override
            public void onSuccess(String response) {
                boolean stan = true;
                prgDialog.hide();
                try {


                    JSONObject jsonobject;
                    JSONArray obj = new JSONArray(response);
                    for (int i = 0; i < obj.length(); i++) {
                        jsonobject = obj.getJSONObject(i);

                        Course spec = new Course();
                        spec.setGroupSize(Integer.parseInt(jsonobject.optString("group")));
                        spec.setName(jsonobject.optString("name"));

                        courseName.add(jsonobject.optString("name"));


                    }

                    Spinner courseSpinner = (Spinner) findViewById(R.id.courseSpinner);
                    courseSpinner
                            .setAdapter(new ArrayAdapter<String>(RegisterActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    courseName));


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void invokeWS(final RequestParams params){
        prgDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("app", params.toString());
        client.post("http://mstudentservice.jelastic.dogado.eu/register/doregister", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {

                prgDialog.hide();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("status")) {
                        setDefaultValues();
                        Toast.makeText(getApplicationContext(), "Zostałeś pomyślnie zarejestrowany!", Toast.LENGTH_LONG).show();
                        nameET.setText("");
                        emailET.setText("");
                        pwdET.setText("");
                        surnameET.setText("");
                        Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);

                    }
                    else {
                        errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void navigatetoLoginActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    public void setDefaultValues(){
        nameET.setText("");
        emailET.setText("");
        pwdET.setText("");
    }

}