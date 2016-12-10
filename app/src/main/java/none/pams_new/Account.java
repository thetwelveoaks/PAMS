package none.pams_new;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/***
 * This class provides interfaces for users to view their account information.
 * The notable feature that this class offers is that it will change the greetings to users
 * according to the current time.
 * @author Li Chuqiao, Chen Ningshuang
 * @since 21/03/2015
 */

public class Account extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Singapore"));
        Calendar noonCal = Calendar.getInstance();
        noonCal.set(Calendar.HOUR_OF_DAY, 12);
        noonCal.set(Calendar.MINUTE, 0);
        noonCal.set(Calendar.SECOND, 0);
        noonCal.set(Calendar.MILLISECOND, 0);
        Date noon = noonCal.getTime();

        Calendar eveCal = Calendar.getInstance();
        eveCal.set(Calendar.HOUR_OF_DAY, 17);
        eveCal.set(Calendar.MINUTE, 0);
        eveCal.set(Calendar.SECOND, 0);
        eveCal.set(Calendar.MILLISECOND, 0);
        Date eve = eveCal.getTime();

        Date current = Calendar.getInstance().getTime();

        TextView realName = (TextView) findViewById(R.id.realName);
        System.out.println(current.toString());
        if (current.before(noon))
            realName.setText("Good morning, " + Login.real_name);
        else if (current.before(eve) && current.after(noon))
            realName.setText("Good afternoon, " + Login.real_name);
        else if (current.after(eve))
            realName.setText("Good evening, " + Login.real_name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToLogIn(View view) {
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
