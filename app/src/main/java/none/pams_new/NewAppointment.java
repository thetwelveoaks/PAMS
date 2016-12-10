package none.pams_new;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import none.pams_new.Customizes.CustomClinicSpinnerAdapter;
import none.pams_new.Customizes.CustomTimeslotSpinnerAdapter;
import none.pams_new.Customizes.CustomTreatmentSpinnerAdapter;
import none.pams_new.Entity.Appointment;
import none.pams_new.Entity.Clinic;
import none.pams_new.Entity.Doctor;
import none.pams_new.Entity.Timeslot;
import none.pams_new.Entity.Treatment;

/**
 * This class work in compliance with activity_new_appointment to establish the user interface to
 * interact with our users in order to create a new appointment. Users are allowed to choose date,
 * time slot, clinic and type of treatment. After that, an available doctor will be allocated to the
 * user and the user can submit the new appointment request. If no doctor is available at the user
 * specified time, the user is prompted to choose another time.
 * If the user has chosen a treatment that requires prerequisite and it has not been satisfied, he
 * is not allowed to proceed.
 *
 * @author Chen Ningshuang
 * @since 07/03/15
 */

public class NewAppointment extends ActionBarActivity implements View.OnClickListener {

    Button btnCalender, btnSubmit, btnFindDoctor;
    EditText txtDate, txtFindDoctor;
    Spinner spinnerTimeslot, spinnerTreatment, spinnerClinic;
    private String dateToPost;
    private int mYear, mMonth, mDay;
    private ArrayList<Treatment> treatments = new ArrayList<Treatment>();
    private ArrayList<Clinic> clinics = new ArrayList<Clinic>();
    private JSONObject jsonObject = new JSONObject();
    private JSONObject matchDoctor = new JSONObject();
    private Timeslot timeslot;

    protected void onCreate(Bundle savedInstanceState) {
        try {
            jsonObject.put("patient", Login.user_id);
            System.out.println(Login.user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        btnCalender = (Button) findViewById(R.id.btnCalendar);
        txtDate = (EditText) findViewById(R.id.txtCalendar);
        txtFindDoctor = (EditText) findViewById(R.id.txtFindDoctor);

        btnCalender.setOnClickListener(this);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jsonObject.has("patient") && jsonObject.has("doctor") && jsonObject.has("timeslot") && jsonObject.has("date") && jsonObject.has("treatmentType")) {
                    try {
                        System.out.println("JSON patient: " + jsonObject.getString("patient"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HttpAccess httpAccess = new HttpAccess("appointment", "create");
                    httpAccess.setContent(jsonObject);

                    try {
                        JSONObject result = httpAccess.getPostResult().getJSONObject(0);
                        if (httpAccess.getError().isEmpty()) {
                            if (result.get("error") instanceof Integer) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Appointment made successfully!", Toast.LENGTH_LONG);
                                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                startActivity(intent);
                                toast.show();
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Appointment creation failure: " + result.getString("error"), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Error: " + httpAccess.getError(), Toast.LENGTH_LONG);
                            toast.show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please fill in all necessary information.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        btnFindDoctor = (Button) findViewById(R.id.btnFindDoctor);
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

        ArrayList<Timeslot> timeslots = new ArrayList<Timeslot>();
        for (int i = 1; i <= 14; i++)
            timeslots.add(new Timeslot(i));
        spinnerTimeslot = (Spinner) findViewById(R.id.spinnerTimeslot);
        CustomTimeslotSpinnerAdapter timeslotSpinnerAdapter = new CustomTimeslotSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, timeslots);
        spinnerTimeslot.setAdapter(timeslotSpinnerAdapter);
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

        try {
            HttpAccess httpAccess = new HttpAccess("appointment", "treatment");
            JSONArray jsonArray = httpAccess.getGetResult();
            if (httpAccess.getError().isEmpty())
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = null;
                    jsonObject1 = jsonArray.getJSONObject(i);
                    treatments.add(new Treatment(jsonObject1));
                }
            else {
                Toast toast = Toast.makeText(getApplicationContext(), "Error: " + httpAccess.getError(), Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        spinnerTreatment = (Spinner) findViewById(R.id.spinnerTreatment);
        final CustomTreatmentSpinnerAdapter adapter = new CustomTreatmentSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, treatments);
        spinnerTreatment.setAdapter(adapter);

        spinnerTreatment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Treatment treatment = adapter.getItem(position);
                try {
                    jsonObject.put("treatmentType", treatment.getId());
                    matchDoctor.put("treatmentType", treatment.getId());
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

        try {
            HttpAccess httpAccess = new HttpAccess("appointment", "clinic");
            JSONArray jsonArray = httpAccess.getGetResult();
            if (httpAccess.getError().isEmpty())
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    clinics.add(new Clinic(jsonObject1));
                }
            else {
                Toast toast = Toast.makeText(getApplicationContext(), "Error: " + httpAccess.getError(), Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        spinnerClinic = (Spinner) findViewById(R.id.spinnerClinic);
        final CustomClinicSpinnerAdapter clinicAdapter = new CustomClinicSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, clinics);
        spinnerClinic.setAdapter(clinicAdapter);

        spinnerClinic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Clinic clinic = clinicAdapter.getItem(position);
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


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_appointment, menu);
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
