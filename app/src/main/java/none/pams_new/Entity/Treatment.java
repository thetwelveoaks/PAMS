package none.pams_new.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * This class models the treatments offered by our hospital that can be chosen by the users.
 *
 * @author Chen Ningshuang
 * @since 22/03/15.
 */
public class Treatment implements Serializable {
    private int id;
    private String name;

    public Treatment(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            name = jsonObject.getString("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }


}

