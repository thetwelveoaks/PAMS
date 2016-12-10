package none.pams_new.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * This class models the clinic distributed in different locations.
 *
 * @author Chen Ningshuang
 * @since 21/03/15
 */
public class Clinic implements Serializable {
    private int id;
    private String name;
    private String website;

    public Clinic(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            name = jsonObject.getString("name");
            website = jsonObject.getString("website");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return name;
    }
}

