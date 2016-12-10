package none.pams_new;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import none.pams_new.Entity.Appointment;
import none.pams_new.Entity.Clinic;
import none.pams_new.Entity.Doctor;
import none.pams_new.Entity.Timeslot;

/**
 * This class work in compliance with activity_update_appointment to establish the user interface to
 * interact with our users in order to update an existing appointment. Users are allowed to change
 * date and time slot only. After that, an available doctor will be allocated to the user and the
 * user can submit the update appointment request. However, if no doctor is available at the user
 * specified time, the user is prompted to choose another time.
 *
 * @author Chen Ningshuang
 * @since 25/03/15
 */
public class UpdateAppointment extends ActionBarActivity implements View.OnClickListener {
    Button btnCalender, btnSubmit, btnFindDoctor;
    EditText txtDate, txtFindDoctor;
    Spinner spinnerTimeslot, spinnerClinic;
    TextView txtTreatment;
    private Appointment appointment;
    private String dateToPost;
    private int mYear, mMonth, mDay;
    private ArrayList<Clinic> clinics = new ArrayList<Clinic>();
    private JSONObject jsonObject = new JSONObject();
    private JSONObject matchDoctor = new JSONObject();
    private Timeslot timeslot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_appointment);

        initialize();
    }

    private void initialize() {
        appointment = (Appointment) getIntent().getSerializableExtra("appointment");
        if (appointment == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Error: System failure!", Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(getApplicationContext(), ChangeAppointment.class);
            startActivity(intent);
        } else {


            btnCalender = (Button) findViewById(R.id.btnCalendar1);
            txtDate = (EditText) findViewById(R.id.txtCalendar1);
            txtFindDoctor = (EditText) findViewById(R.id.txtFindDoctor1);
            txtTreatment = (TextView) findViewById(R.id.txtTreatment);
            txtTreatment.setText(appointment.getTreatmentType().getName());
            txtTreatment.setTextColor(Color.GRAY);
            txtTreatment.setTextSize(15);
            btnCalender.setOnClickListener(this);
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            txtDate.setText(formatter.format(appointment.getDate()));
            try {
                jsonObject.put("patient", Login.user_id);
                jsonObject.put("id", appointment.getId());
                jsonObject.put("timeslot", appointment.getTimeslot().getId());
                jsonObject.put("date", formatter.format(appointment.getDate()));
                jsonObject.put("treatmentType", appointment.getTreatmentType().getId());
                matchDoctor.put("date", formatter.format(appointment.getDate()));
                matchDoctor.put("timeslot", appointment.getTimeslot().getId());
                matchDoctor.put("clinic", appointment.getDoctor().getClinic().getId());
                matchDoctor.put("treatmentType", appointment.getTreatmentType().getId());
                matchDoctor.put("id", appointment.getId());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            btnSubmit = (Button) findViewById(R.id.btnSubmit1);

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (jsonObject.has("id") && jsonObject.has("patient") && jsonObject.has("doctor") && jsonObject.has("timeslot") && jsonObject.has("date") && jsonObject.has("treatmentType")) {

                        HttpAccess httpAccess = new HttpAccess("appointment", "update");
                        httpAccess.setContent(jsonObject);
                        System.out.println(jsonObject);


                        JSONArray array = httpAccess.getPostResult();
                        if (httpAccess.getError().isEmpty()) {
                            try {
                                JSONObject result = array.getJSONObject(0);
                                Object test = result.get("error");
                                if (test instanceof Integer) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Appointment update successfully!", Toast.LENGTH_LONG);
                                    toast.show();
                                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                    startActivity(intent);
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Appointment update failed. Error: " + httpAccess.getError(), Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Error: " + httpAccess.getError(), Toast.LENGTH_LONG);
                            toast.show();
                        }

                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Please fill in all necessary information.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });

            btnFindDoctor = (Button) findViewById(R.id.btnFindDoctor1);
            btnFindDoctor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (matchDoctor.has("treatmentType") && matchDoctor.has("timeslot") && matchDoctor.has("date") && matchDoctor.has("clinic")) {
                        HttpAccess httpAccess = new HttpAccess("appointment", "match_doctor");
                        httpAccess.setContent(matchDoctor);
                        try {
                            JSONArray testArr = httpAccess.getPostResult();
                            if (httpAccess.getError().isEmpty()) {
                                JSONObject test = testArr.getJSONObject(0);
                                if (!test.has("error")) {
                                    Doctor doctor = new Doctor(test);

                                    txtFindDoctor.setText(doctor.getName());

                                    Toast toast = Toast.makeText(getApplicationContext(), "Congratulations! Doctor " + doctor.getName() + " is assigned to you.", Toast.LENGTH_SHORT);
                                    toast.show();
                                    jsonObject.put("doctor", doctor.getId());
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "No doctor available at that time. Please try another time.", Toast.LENGTH_SHORT);
                                    toast.show();
                                    if (txtFindDoctor != null)
                                        txtFindDoctor.setText("");
                                    if (jsonObject.has("doctor")) jsonObject.remove("doctor");
                                }
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Error: " + httpAccess.getError(), Toast.LENGTH_SHORT);
                                toast.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Please fill in necessary information", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });

            spinnerTimeslot = (Spinner) findViewById(R.id.spinnerTimeslot1);
            ArrayList<String> timeslotNames = new ArrayList<String>();
            ArrayList<Timeslot> timeslots = new ArrayList<Timeslot>();
            for (int i = 1; i <= 14; i++) {
                Timeslot obj = new Timeslot(i);
                timeslots.add(obj);
                timeslotNames.add(obj.getRange());
            }


            ArrayAdapter<String> timeslotAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, timeslotNames) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView txt = (TextView) super.getView(position, convertView, parent);
                    txt.setTextColor(Color.BLACK);
                    return txt;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    TextView txt = (TextView) super.getView(position, convertView, parent);
                    txt.setTextColor(Color.BLACK);
                    return txt;
                }
            };
            spinnerTimeslot.setAdapter(timeslotAdapter);
            spinnerTimeslot.setSelection(timeslotAdapter.getPosition(appointment.getTimeslot().getRange()), true);

            spinnerTimeslot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    timeslot = new Timeslot(position + 1);
                    try {
                        jsonObject.put("timeslot", timeslot.getId());
                        matchDoctor.put("timeslot", timeslot.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (txtFindDoctor != null)
                        txtFindDoctor.setText("");
                    if (jsonObject.has("doctor")) jsonObject.remove("doctor");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            ArrayList<String> clinicNames = new ArrayList<>();
            try {
                HttpAccess httpAccess = new HttpAccess("appointment", "clinic");
                JSONArray jsonArray = httpAccess.getGetResult();
                if (httpAccess.getError().isEmpty())
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Clinic obj = new Clinic(jsonObject1);
                        clinics.add(obj);
                        clinicNames.add(obj.getName());
                    }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error: " + httpAccess.getError(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            spinnerClinic = (Spinner) findViewById(R.id.spinnerClinic1);
            ArrayAdapter<String> clinicAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, clinicNames) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView txt = (TextView) super.getView(position, convertView, parent);
                    txt.setTextColor(Color.BLACK);
                    return txt;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    TextView txt = (TextView) super.getView(position, convertView, parent);
                    txt.setTextColor(Color.BLACK);
                    return txt;
                }
            };
            spinnerClinic.setAdapter(clinicAdapter);
            spinnerClinic.setSelection(clinicAdapter.getPosition(appointment.getDoctor().getClinic().getName()));
            spinnerClinic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Clinic clinic = clinics.get(position);
                    if (txtFindDoctor != null)
                        txtFindDoctor.setText("");
                    if (jsonObject.has("doctor")) jsonObject.remove("doctor");
                    try {
                        matchDoctor.put("clinic", clinic.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            txtFindDoctor.setText(appointment.getDoctor().getName());
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

    @Override
    public void onClick(View v) {
        if (v == btnCalender) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            if ((year - 2000) * 400 + monthOfYear * 40 + dayOfMonth <= (mYear - 2000) * 400 + mMonth * 40 + mDay
                                    || (year - 2000) * 400 + monthOfYear * 40 + dayOfMonth > (mYear - 1999) * 400 + mMonth * 40 + mDay) {
                                Toast.makeText(getApplicationContext(), "Cannot choose this date. Please try again!", Toast.LENGTH_LONG).show();
                            } else {
                                txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                dateToPost = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                                try {
                                    jsonObject.put("date", dateToPost);
                                    matchDoctor.put("date", dateToPost);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
            if (txtFindDoctor != null)
                txtFindDoctor.setText("");
            if (jsonObject.has("doctor")) jsonObject.remove("doctor");
        }
    }
}

