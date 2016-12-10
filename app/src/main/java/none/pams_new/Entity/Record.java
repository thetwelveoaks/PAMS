package none.pams_new.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * This class models the medical record of one appointment that is added by the doctors from the
 * web-platform. It can be viewed by the users from the Android platform.
 *
 * @author Chen Ningshuang
 * @since 22/03/15.
 */
public class Record implements Serializable {

    private int age;
    private double weight;
    private double height;
    private String medication;

    Record(JSONObject jsonObject) {
        try {

            age = jsonObject.getInt("age");
            weight = jsonObject.getDouble("weight");
            height = jsonObject.getDouble("height");
            medication = jsonObject.getString("medication");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public int getAge() {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public String getMedication() {
        return medication;
    }
}
