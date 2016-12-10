package none.pams_new;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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

import none.pams_new.Entity.Appointment;

/***
 * This class provides interfaces for users to cancel an existing appointment
 * Author: Li Chuqiao
 * Since 30/03/2015
 */
public class DeleteAppointment extends ActionBarActivity {
    TableLayout table;
    private JSONObject jsonObject = new JSONObject();
    private ArrayList<Appointment> appointments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_appointment);
        table = (TableLayout) findViewById(R.id.appointment_view_delete);
        RelativeLayout background = (RelativeLayout) findViewById(R.id.background_delete_view);
        background.setBackgroundColor(Color.WHITE);
        background.getBackground().setAlpha(80);
        HttpAccess httpAccess = new HttpAccess("appointment", "read_ongoing");
        try {
            jsonObject.put("patient", Login.user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpAccess.setContent(jsonObject);
        try {
            JSONArray jsonArray = httpAccess.getPostResult();
            if (httpAccess.getError().isEmpty()) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Appointment appointment = new Appointment(jsonObject1);
                    appointments.add(appointment);
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Error:" + httpAccess.getError(), Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!appointments.isEmpty()) {
            for (final Appointment appointment : appointments) {
                final Context mContext = this;
                TextView description = new TextView(getApplicationContext());
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = appointment.getDate();
                description.setText(appointment.toString());
                description.setTextColor(Color.BLACK);
                TableRow row = new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1);


                /*description.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder detail = new AlertDialog.Builder(mContext);
                        detail.setTitle("Appointment Details");
                        detail.setMessage(appointment.toString());
                        detail.show();
                    }
                });*/
                ImageButton deleteBtn = new ImageButton(this);
                deleteBtn.setBackgroundResource(R.drawable.remove);
                row.addView(description, lp);
                row.addView(deleteBtn);
                row.setPadding(10, 30, 30, 10);
                row.setGravity(Gravity.CENTER_VERTICAL);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

                        alert.setMessage("Are you sure you want to cancel the appointment?");

                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                HttpAccess httpAccess1 = new HttpAccess("appointment", "delete");
                                try {
                                    jsonObject.put("patient", Login.user_id);
                                    jsonObject.put("id", appointment.getId());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                httpAccess1.setContent(jsonObject);
                                try {
                                    JSONObject result = httpAccess1.getPostResult().getJSONObject(0);
                                    if (result.has("error") && result.getInt("error") == 0) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Appointment is canceled successfully!", Toast.LENGTH_LONG);
                                        toast.show();
                                        finish();
                                        startActivity(getIntent());
                                    } else {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Appointment cancellation failed. " + result.getString("error"), Toast.LENGTH_LONG);
                                        toast.show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        alert.show();
                    }
                });

                table.addView(row);
            }
        } else {
            TextView text = new TextView(this);
            text.setText("You don't have any ongoing appointment.");
            text.setTextColor(Color.BLACK);
            text.setTextSize(15);
            text.setPadding(10, 30, 30, 10);
            table.addView(text);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete_appointment, menu);
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
