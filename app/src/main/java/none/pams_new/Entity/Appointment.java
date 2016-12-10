package none.pams_new.Entity;

import android.annotation.TargetApi;
import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class models the appoinment that can be created, updated, deleted and viewed by users.
 *
 * @author Chen Ningshuang
 * @since 21/03/15
 */
public class Appointment implements Serializable {
    private Doctor doctor;
    private Date date;
    private Clinic clinic;
    private Record record;
    private Treatment treatmentType;
    private Doctor referrer;
    private Timeslot timeslot;
    private int id;
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public Appointment(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            doctor = new Doctor(jsonObject.getJSONObject("doctor"));
            date = formatter.parse(jsonObject.getString("date"));
            if (jsonObject.has("record"))
                record = new Record(jsonObject.getJSONObject("record"));
            treatmentType = new Treatment(jsonObject.getJSONObject("treatmentType"));
            if (jsonObject.has("referrer"))
                referrer = new Doctor(jsonObject.getJSONObject("referrer"));
            timeslot = new Timeslot(jsonObject.getInt("timeSlot"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Date getDate() {
        return date;
    }

    public Record getRecord() {
        return record;
    }

    public Treatment getTreatmentType() {
        return treatmentType;
    }

    public Doctor getReferrer() {
        return referrer;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public int getId() {
        return id;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String toString() {
        if (referrer == null)
            return "Date: " + formatter.format(date) + " " + timeslot.getRange() + System.lineSeparator() +
                    "Treatment: " + treatmentType.toString() + System.lineSeparator() +
                    "Doctor: " + doctor.getName() + System.lineSeparator()
                    + "Clinic: " + doctor.getClinic().getName();
        else
            return "Date: " + formatter.format(date) + " " + timeslot.getRange() + System.lineSeparator() +
                    "Treatment: " + treatmentType.toString() + System.lineSeparator() +
                    "Doctor: " + doctor.getName() + System.lineSeparator()
                    + "Clinic: " + doctor.getClinic().getName() + System.lineSeparator() +
                    "Referred by: Doctor " + referrer.getName();
    }
}

