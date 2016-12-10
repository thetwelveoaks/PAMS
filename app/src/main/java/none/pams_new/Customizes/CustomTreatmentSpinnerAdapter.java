package none.pams_new.Customizes;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import none.pams_new.Entity.Treatment;

/**
 * This is a customized spinner adapter that handles the choice among treatments. The difference
 * between this spinner adapter and the built-in one is the passing of the internal value of a
 * choice. The built-in spinner adapter only returns the position of the selected item. However,
 * this customized spinner adapter can display the type of the treatment and return the id of the
 * selected treatment.
 * Created by chenningshuang on 22/03/15.
 */
public class CustomTreatmentSpinnerAdapter extends ArrayAdapter<Treatment> {

    private Context context;
    private ArrayList<Treatment> treatments;

    public CustomTreatmentSpinnerAdapter(Context context, int textViewResourceId, ArrayList<Treatment> treatments) {
        super(context, textViewResourceId, treatments);
        this.context = context;
        this.treatments = treatments;
    }

    public int getCount() {
        return treatments.size();
    }

    public Treatment getItem(int position) {
        return treatments.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setPadding(20, 30, 30, 20);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(treatments.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setPadding(20, 30, 30, 20);
        label.setText(treatments.get(position).getName());

        return label;
    }
}

