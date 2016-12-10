package none.pams_new;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class provides the login functionality for users to log in to their accounts.
 * @author Wei Yumou
 * @since 26/02/2015
 */
public class Login extends ActionBarActivity {

    public static int user_id;
    public static String user_name;
    public static String real_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        password.setText("");
        username.setText("");


    }

    @Override
    protected void onResume() {
        super.onResume();
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        password.setText("");
        username.setText("");
        username.requestFocus();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void login(View view) {
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        try {
            JSONObject usr_pwd = new JSONObject();
            usr_pwd.put("username", username.getText());
            usr_pwd.put("password", password.getText());
            HttpAccess httpAccess = new HttpAccess("user", "login");
            httpAccess.setContent(usr_pwd);
            JSONArray result = httpAccess.getPostResult();
            if (httpAccess.getError().isEmpty()) {
                JSONObject result_obj = result.getJSONObject(0);
                int error = result_obj.getInt("error");
                if (error == 0) {
                    user_id = result_obj.getInt("user");
                    user_name = result_obj.getString("username");
                    real_name = result_obj.getString("name");
                    Intent intent = new Intent(this, HomePage.class);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Wrong Username or Password", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.e("Login", "Wrong password");
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Error:" + httpAccess.getError(), Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void signUp(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
