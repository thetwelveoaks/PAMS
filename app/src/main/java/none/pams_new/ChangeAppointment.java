package none.pams_new;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import none.pams_new.Customizes.CustomButtonOnClickListener;
import none.pams_new.Entity.Appointment;

/**
 * This class establishes the user interface where the list of ongoing appointments are displayed,
 * and user can press the image button next  to each appointment to enter the update view of that
 * appointment.
 *
 * @author Chen Ningshuang
 * @since 25/03/15
 */
public class ChangeAppointment extends ActionBarActivity {

    private TableLayout tableLayout;
    private ArrayList<Appointment> appointments;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_appointment);


        initTable();
    }

    private void initTable() {
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        appointments = new ArrayList<Appointment>();
        try {
            HttpAccess httpAccess = new HttpAccess("appointment", "read_ongoing");
            JSONObject patientId = new JSONObject();
            patientId.put("patient", Login.user_id);
            httpAccess.setContent(patientId);
            JSONArray jsonArray = httpAccess.getPostResult();
            if (httpAccess.getError().isEmpty()) {
                if (jsonArray.length() == 0) {
                    TextView txt = new TextView(getApplicationContext());
                    txt.setText("You don't have any ongoing appointments.");
                    txt.setTextColor(Color.BLACK);
                    txt.setTextSize(15);
                    txt.setPadding(10, 20, 10, 20);
                    TableRow tableRow = new TableRow(getApplicationContext());
                    tableRow.addView(txt);
                    tableLayout.addView(tableRow);
                    return;
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    appointments.add(new Appointment(jsonObject));
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Error: " + httpAccess.getError(), Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        if (appointments != null) {

            for (Appointment appointment : appointments) {
                TextView description1 = new TextView(getApplicationContext());
                description1.setText(appointment.toString());
                description1.setTextColor(Color.BLACK);
                description1.setTextSize(15);
                TableRow tableRow1 = new TableRow(getApplicationContext());
                TableRow.LayoutParams layout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1);
                tableRow1.addView(description1, layout);


                final ImageButton btnChange = new ImageButton(getApplicationContext());
                btnChange.setBackgroundResource(R.drawable.small_change);
                intent = new Intent();
                CustomButtonOnClickListener btnListener = new CustomButtonOnClickListener(appointment, intent);
                btnChange.setOnClickListener(btnListener);

                tableRow1.addView(btnChange);

                tableRow1.setPadding(30, 10, 30, 0);

                tableRow1.setGravity(Gravity.CENTER_VERTICAL);
                tableLayout.addView(tableRow1);

            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Server error!", Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            startActivity(intent);
        }


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
