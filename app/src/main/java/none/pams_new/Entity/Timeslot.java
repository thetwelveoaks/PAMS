package none.pams_new.Entity;

import java.io.Serializable;

/**
 * This class models the different time slots the users can choose. It's every half hour from 9am to
 * 5pm excluding the lunch break at 12:00-13:00.
 *
 * @author Chen Ningshuang
 * @since 22/03/15.
 */
public class Timeslot implements Serializable {
    private int id;
    private String range;

    public Timeslot(int id) {
        this.id = id;

        if (id == 1) range = "9:00 - 9:30";
        if (id == 2) range = "9:30 - 10:00";
        if (id == 3) range = "10:00 - 10:30";
        if (id == 4) range = "10:30 - 11:00";
        if (id == 5) range = "11:00 - 11:30";
        if (id == 6) range = "11:30 - 12:00";
        if (id == 7) range = "13:00 - 13:30";
        if (id == 8) range = "13:30 - 14:00";
        if (id == 9) range = "14:00 - 14:30";
        if (id == 10) range = "14:30 - 15:00";
        if (id == 11) range = "15:00 - 15:30";
        if (id == 12) range = "15:30 - 16:00";
        if (id == 13) range = "16:00 - 16:30";
        if (id == 14) range = "16:30 - 17:00";

    }

    public int getId() {
        return id;
    }

    public String getRange() {
        return range;
    }

    public String toString() {
        return range;
    }
}
