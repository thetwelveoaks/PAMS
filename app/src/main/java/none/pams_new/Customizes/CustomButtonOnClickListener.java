package none.pams_new.Customizes;

import android.content.Intent;
import android.view.View;

import none.pams_new.ChangeAppointment;
import none.pams_new.Entity.Appointment;
import none.pams_new.NewAppointment;
import none.pams_new.UpdateAppointment;
import none.pams_new.ViewRecord;

/**
 * This is a customized on-click-listener for a button. This on-click-listener passes the selected
 * appointment information from the previous activity to the next activity. If the appointment
 * selected contains a record, then the UpdateAppointment activity will start. Instead, if the
 * appointment contains a record, then the ViewRecord activity will start.
 *
 * @author Chen Ningshuang
 * @since 24/3/15
 */
public class CustomButtonOnClickListener implements View.OnClickListener {
    private Appointment appointment;
    private Intent intent;

    public CustomButtonOnClickListener(Appointment appointment, Intent intent) {
        this.appointment = appointment;
        this.intent = intent;
    }

    @Override
    public void onClick(View v) {
        if (appointment.getRecord() == null) {
            intent.setClass(v.getContext(), UpdateAppointment.class);
            intent.putExtra("appointment", appointment);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);
        } else {
            intent.setClass(v.getContext(), ViewRecord.class);
            intent.putExtra("appointment", appointment);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);
        }
    }
}

