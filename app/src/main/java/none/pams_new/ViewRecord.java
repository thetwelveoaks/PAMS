package none.pams_new;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.SimpleDateFormat;

import none.pams_new.Entity.Appointment;

/**
 * This class establishes the user interface that displays the medical record information of one
 * specific appointment passed in by the ViewHistoricalAppointment activity. The basic appointment
 * information as well as the height, weight and medication of the user.
 *
 * @author Chen Ningshuang
 * @since 25/03/15
 */

public class ViewRecord extends ActionBarActivity {

    private TextView patient;
    private TextView doctor;
    private TextView time;
    private TextView treatment;
    private TextView height;
    private TextView weight;
    private TextView medication;
    private Appointment appointment;
    private TextView age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);
        initialize();
        displayInfo();
    }

    private void initialize() {
        patient = (TextView) findViewById(R.id.patient_name);
        doctor = (TextView) findViewById(R.id.doctor_name);
        time = (TextView) findViewById(R.id.time);
        treatment = (TextView) findViewById(R.id.treatment_type);
        height = (TextView) findViewById(R.id.height);
        weight = (TextView) findViewById(R.id.weight);
        medication = (TextView) findViewById(R.id.medication);
        age = (TextView) findViewById(R.id.age);
        appointment = (Appointment)getIntent().getSerializableExtra("appointment");
        if (appointment == null) {
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            Toast toast = Toast.makeText(getApplicationContext(), "Appointment passing failure.", Toast.LENGTH_LONG);
            toast.show();
            startActivity(intent);
        }

    }

    private void displayInfo() {
        patient.setText("Patient Name: " + Login.real_name);
        doctor.setText("Doctor Name: " + appointment.getDoctor().getName());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        time.setText("Appointment Time: " + format.format(appointment.getDate()) + " " + appointment.getTimeslot().getRange());
        treatment.setText("Treatment Type: " + appointment.getTreatmentType().getName());
        height.setText("Height: " + appointment.getRecord().getHeight() + " CM");
        weight.setText("Weight: " + appointment.getRecord().getWeight() + "KG");
        medication.setText("Medication: " + appointment.getRecord().getMedication());
        age.setText("Age: " + appointment.getRecord().getAge());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_record, menu);
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