package none.pams_new.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * This class models the user's account that stores their information.
 *
 * @author Chen Ningshuang
 * @since 1/03/15.
 */
public class User implements Serializable {
    private String id;
    private String username;
    private String name;
    private boolean is_Admin;
    private boolean is_Doctor;


    User(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("id");
            username = jsonObject.getString("username");
            name = jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

}

