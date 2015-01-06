package pl.edu.prz.mstudent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import pl.edu.prz.mstudent.model.Specialization;


/**
 *
 * Register Activity Class
 */

public class RegisterActivity extends Activity {
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Name Edit View Object
    EditText nameET;
    // Email Edit View Object
    EditText surnameET;

    EditText emailET;
    // Passwprd Edit View Object
    EditText pwdET;
    Spinner specializationSP;
    Spinner SpecializationGrupSP;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Find Error Msg Text View control by ID
        errorMsg = (TextView)findViewById(R.id.register_error);
        // Find Name Edit View control by ID
        nameET = (EditText)findViewById(R.id.registerName);
        surnameET = (EditText)findViewById(R.id.registerSurname);
        // Find Email Edit View control by ID
        emailET = (EditText)findViewById(R.id.registerEmail);
        // Find Password Edit View control by ID
        pwdET = (EditText)findViewById(R.id.registerPassword);
        // Instantiate Progress Dialog object
        specializationSP = (Spinner)findViewById(R.id.specializationSpinner);
        SpecializationGrupSP = (Spinner)findViewById(R.id.specializtionSpinnerGroup);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        createSpecializationListView();


        Spinner specializtionSpinnerGroup = (Spinner) findViewById(R.id.specializtionSpinnerGroup);
        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this,
                R.array.specializtionSpinnerGroup, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specializtionSpinnerGroup.setAdapter(adapter);
    }

    /**
     * Method gets triggered when Register button is clicked
     *
     * @param view
     */
    public void registerUser(View view){
        String name = nameET.getText().toString();
        String surname = surnameET.getText().toString();
        String email = emailET.getText().toString();
        String password = pwdET.getText().toString();
        String specialization = specializationSP.getSelectedItem().toString();
        String specializationGroup = SpecializationGrupSP.getSelectedItem().toString();

        RequestParams params = new RequestParams();
        // When Name Edit View, Email Edit View and Password Edit View have values other than Null
        if(Utility.isNotNull(name) && Utility.isNotNull(email) && Utility.isNotNull(password)){
            // When Email entered is Valid
            if(Utility.validate(email)){
                // Put Http parameter name with value of Name Edit View control
                params.put("name", name);
                params.put("surname", surname);
                // Put Http parameter username with value of Email Edit View control
                params.put("username", email);
                params.put("specializationName",specialization);
                params.put("specializationGroup", specializationGroup);
                // Put Http parameter password with value of Password Edit View control
                params.put("password", password);
                params.put("status","false");
                // Invoke RESTful Web Service with Http parameters
                Toast.makeText(getApplicationContext(), params.toString(), Toast.LENGTH_LONG).show();
                invokeWS(params);
            }
            // When Email is invalid
            else{
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            }
        }
        // When any of the Edit View control left blank
        else{
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }

    }

    public void createSpecializationListView(){

        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://mstudent.jelastic.dogado.eu/register/getspecialization", null, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            ArrayList<Specialization> specialization = new ArrayList<Specialization>();
            ArrayList<String> specializationName = new ArrayList<String>();
            ArrayList<String> specializationGroup = new ArrayList<String>();
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                boolean stan = true;
                prgDialog.hide();
                try {


                    JSONObject jsonobject;
                    JSONArray obj = new JSONArray(response);
                    for (int i = 0; i < obj.length(); i++) {
                        jsonobject = obj.getJSONObject(i);

                        Specialization spec = new Specialization();
                        spec.setGroupSize(Integer.parseInt(jsonobject.optString("group")));
                        spec.setName(jsonobject.optString("name"));

                         specializationName.add(jsonobject.optString("name"));



                    //    specializationGroup.add(jsonobject.optString("group"));
         //               specialization.add(spec);
                    }

                    Spinner specializationSpinner = (Spinner) findViewById(R.id.specializationSpinner);
                    specializationSpinner
                            .setAdapter(new ArrayAdapter<String>(RegisterActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    specializationName));

//                    Spinner specializationSpinnerGroup = (Spinner) findViewById(R.id.specializtionSpinnerGroup);
//                    specializationSpinnerGroup
//                            .setAdapter(new ArrayAdapter<String>(RegisterActivity.this,
//                                    android.R.layout.simple_spinner_dropdown_item,
//                                    specializationGroup));


//                    Toast.makeText(getApplicationContext(), specializationName.toString(), Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void invokeWS(RequestParams params){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://mstudent.jelastic.dogado.eu/register/doregister",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if(obj.getBoolean("status")){
                        // Set Default Values for Edit View controls
                        setDefaultValues();
                        // Display successfully registered message using Toast
                        Toast.makeText(getApplicationContext(), "You are successfully registered!", Toast.LENGTH_LONG).show();
                    }
                    // Else display error message
                    else{
                        errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Method which navigates from Register Activity to Login Activity
     */
    public void navigatetoLoginActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    /**
     * Set degault values for Edit View controls
     */
    public void setDefaultValues(){
        nameET.setText("");
        emailET.setText("");
        pwdET.setText("");
    }

}