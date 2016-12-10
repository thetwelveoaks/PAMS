package none.pams_new.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * This class models doctors that can be assigned to users.
 *
 * @author Chen Ningshuang
 * @since 21/03/15
 */
public class Doctor extends User implements Serializable {
    private Clinic clinic;

    public Doctor(JSONObject jsonObject) {
        super(jsonObject);
        try {
            clinic = new Clinic(jsonObject.getJSONObject("clinic"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Clinic getClinic() {
        return clinic;
    }

}

