package none.pams_new;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import none.pams_new.Customizes.CustomButtonOnClickListener;
import none.pams_new.Entity.Appointment;

/**
 * This class establishes the user interface that displays the list of historical appointments made
 * by the user. By pressing the buttons aligned right to each appointment item, users can proceed
 * view their medical record for that specific appointment updated by the doctors using the web
 * platform, and the ViewRecord activity will start. In addition, users can also press the toggle
 * button located at the top of the screen to switch to the ongoing appointments view. In that case,
 * the ViewOngoingAppointment activity will start.
 *
 * @author Chen Ningshuang
 * @since 25/03/15
 */

public class ViewHistoricalAppointment extends ActionBarActivity {

    TableLayout tableLayout;
    Button btnOngoing;
    ArrayList<Appointment> appointments;
    JSONObject request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_historical_appointment);
        initialize();
        if (getAppointment())
            displayAppointment();
    }

    private void initialize() {
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        btnOngoing = (Button) findViewById(R.id.btn_ongoing);
        appointments = new ArrayList<Appointment>();
        request = new JSONObject();
        try {
            request.put("patient", Login.user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btnOngoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewOngoingAppointment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    private boolean getAppointment() {
        HttpAccess httpAccess = new HttpAccess("appointment", "read_history");
        httpAccess.setContent(request);
        try {
            JSONArray array = httpAccess.getPostResult();
            if (httpAccess.getError().isEmpty()) {
                for (int i = 0; i < array.length(); i++) {
                    appointments.add(new Appointment(array.getJSONObject(i)));
                }
                if (array.length() == 0) {
                    TextView txt = new TextView(getApplicationContext());
                    txt.setText("You don't have any historical appointments.");
                    txt.setTextColor(Color.BLACK);
                    txt.setTextSize(15);
                    txt.setPadding(10,20,10,20);
                    TableRow tableRow = new TableRow(getApplicationContext());
                    tableRow.addView(txt);
                    tableLayout.addView(tableRow);
                    return false;
                }
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
    private void displayAppointment() {
        for (Appointment appointment : appointments) {
            TextView txt = new TextView(getApplicationContext());
            String info = appointment.toString();
            txt.setText(info);
            txt.setTextSize(15);
            txt.setTextColor(Color.BLACK);
            TableRow tableRow = new TableRow(getApplicationContext());
            TableRow.LayoutParams layout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1);
            tableRow.addView(txt, layout);

            ImageButton imgbtn = new ImageButton(getApplicationContext());
            Intent intent = new Intent();
            imgbtn.setOnClickListener(new CustomButtonOnClickListener(appointment, intent));
            imgbtn.setBackgroundResource(R.drawable.viewrecord);
            tableRow.setGravity(Gravity.CENTER_VERTICAL);
            tableRow.addView(imgbtn);
            tableRow.setPadding(10,20,10,20);
            tableLayout.addView(tableRow);

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_historical_appointment, menu);
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
}
