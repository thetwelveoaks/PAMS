package none.pams_new.Customizes;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import none.pams_new.Entity.Timeslot;

/**
 * This is a customized spinner adapter that handles the choice among time slots. The difference
 * between this spinner adapter and the built-in one is the passing of the internal value of a
 * choice. The built-in spinner adapter only returns the position of the selected item. However,
 * this customized spinner adapter can display the range of time slot and return the id of the
 * selected time slot.
 *
 * @author Chen Ningshuang
 * @since 24/03/15
 */
public class CustomTimeslotSpinnerAdapter extends ArrayAdapter<Timeslot> {
    private Context context;
    private ArrayList<Timeslot> timeslots;

    public CustomTimeslotSpinnerAdapter(Context context, int textViewResourceId, ArrayList<Timeslot> timeslots) {
        super(context, textViewResourceId, timeslots);
        this.context = context;
        this.timeslots = timeslots;
    }

    public int getCount() {
        return timeslots.size();
    }

    public Timeslot getItem(int position) {
        return timeslots.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setPadding(20, 30, 30, 20);

        label.setText(timeslots.get(position).getRange());


        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setPadding(20, 30, 30, 20);
        label.setText(timeslots.get(position).getRange());

        return label;
    }
}

