package none.pams_new;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import none.pams_new.Entity.Appointment;

/**
 * This class establishes the user interface that displays the list of ongoing appointments
 * information made by the user. In addition, users can also press the toggle
 * button located at the top of the screen to switch to the historical appointments view. In that
 * case, the ViewHistoricalAppointment activity will start.
 *
 * @author Chen Ningshuang
 * @since 25/03/15
 */

public class ViewOngoingAppointment extends ActionBarActivity {

    private ListView listView;
    private ArrayList<Appointment> appointments;
    private JSONObject request;
    private Button btnHistorical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ongoing_appointment);
        initilize();
        if (getAppointments())
            displayAppointments();

    }

    private void initilize() {
        listView = (ListView) findViewById(R.id.listView);
        btnHistorical = (Button) findViewById(R.id.btn_historical);
        try {
            request = new JSONObject();
            request.put("patient", Login.user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnHistorical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewHistoricalAppointment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    private boolean getAppointments() {
        HttpAccess httpAccess = new HttpAccess("appointment", "read_ongoing");
        httpAccess.setContent(request);
        JSONArray array = httpAccess.getPostResult();

        appointments = new ArrayList<Appointment>();
        try {
            if (httpAccess.getError().isEmpty()) {
                System.out.println(array.length());
                if (array.length() == 0) {
                    String[] txt = {"You don't have any ongoing appointments."};
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, txt) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            TextView txt = (TextView) super.getView(position, convertView, parent);
                            txt.setTextColor(Color.BLACK);
                            txt.setTextSize(15);
                            txt.setPadding(10, 20, 10, 20);
                            return txt;
                        }
                    };
                    listView.setAdapter(adapter);
                    return false;
                } else
                    for (int i = 0; i < array.length(); i++)
                        appointments.add(new Appointment(array.getJSONObject(i)));
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Error: " + httpAccess.getError(), Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (JSONException e) {
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            Toast toast = Toast.makeText(getApplicationContext(), "JSONObject error!", Toast.LENGTH_LONG);
            toast.show();
            startActivity(intent);
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void displayAppointments() {
        ArrayList<String> info = new ArrayList<String>();

        for (Appointment appointment : appointments) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String txt = appointment.toString();

            info.add(txt);
            System.out.println(txt);
        }

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, info) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView txt = (TextView) super.getView(position, convertView, parent);
                txt.setPadding(10,20,10,20);
                txt.setTextSize(15);
                txt.setTextColor(Color.BLACK);
                return txt;
            }
        };
        listView.setAdapter(listAdapter);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
        finish();
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
}
