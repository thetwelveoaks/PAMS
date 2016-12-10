package none.pams_new;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class provides Signup functionality for new-comers to register their account
 * It provides basic error-checking for users' information inputted.
 * @author Wei Yumou
 * @since 20/02/2015
 */
public class SignUp extends ActionBarActivity {

    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private JSONArray contact_mth;
    private long sel_contact_mth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        spinner = (Spinner) findViewById(R.id.contact_sp);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getContactMethod();
        if (contact_mth != null) {
            try {
                int index;
                for (index = 0; index < contact_mth.length(); ++index) {
                    JSONObject jsonObject = contact_mth.getJSONObject(index);
                    String contact_name = jsonObject.getString("name");
                    spinnerAdapter.add(contact_name);
                }
                spinner.setAdapter(spinnerAdapter);
                spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
            }
        } else {

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getContactMethod() {
        HttpAccess httpAccess = new HttpAccess("user", "contact_method");
        contact_mth = httpAccess.getGetResult();
        Log.e("result", contact_mth.toString());

    }

    public void sendRegistration(View view) {
        EditText username = (EditText) findViewById(R.id.username_et);
        EditText password = (EditText) findViewById(R.id.password_et);
        EditText retype = (EditText) findViewById(R.id.re_enter_pwd);
        EditText realName = (EditText) findViewById(R.id.realname_et);
        EditText contact = (EditText) findViewById(R.id.contact_et);
        if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty() ||
                retype.getText().toString().isEmpty() || realName.getText().toString().isEmpty() ||
                contact.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            if (!password.getText().toString().equals(retype.getText().toString())) {
                Toast toast = Toast.makeText(getApplicationContext(), "Different passwords, please check", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                try {
                    JSONObject regPackage = new JSONObject();
                    regPackage.put("username", username.getText());
                    regPackage.put("name", realName.getText());
                    regPackage.put("password", password.getText());
                    regPackage.put("contact", contact.getText());
                    regPackage.put("contact_method", sel_contact_mth);
                    HttpAccess httpAccess = new HttpAccess("user", "register");
                    httpAccess.setContent(regPackage);
                    JSONArray result = httpAccess.getPostResult();
                    if (httpAccess.getError().isEmpty()) {
                        JSONObject resultObj = result.getJSONObject(0);
                        if (resultObj.getInt("error") == 0) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent = new Intent(this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Error:" + resultObj.getString("error_msg"), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        Log.e("reg result", result.toString());
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Error:" + httpAccess.getError(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (JSONException e) {
                    Log.e("error", e.getMessage());
                }
            }
        }
    }

    private class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            sel_contact_mth = id + 1;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

/*
username: string
name: string
password: string
contact: string
contact_method: id
 */

}