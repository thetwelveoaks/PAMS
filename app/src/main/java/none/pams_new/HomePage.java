package none.pams_new;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * This class displays and provides access to main functionality for users. It serves as a homepage and
 * will be shown immediately after users login.
 * @author Wei Yumou
 * @since 10/03/2015
 */
public class HomePage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
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

    public void goToRecord(View view) {
        Intent intent = new Intent(this, ViewOngoingAppointment.class);
        startActivity(intent);
    }

    public void goToAccount(View view) {
        Intent intent = new Intent(this, Account.class);
        startActivity(intent);
    }

    public void goToNewAppointment(View view) {
        Intent intent = new Intent(this, NewAppointment.class);
        startActivity(intent);
    }

    public void goToChangeAppointment(View view) {
        Intent intent = new Intent(this, ChangeAppointment.class);
        startActivity(intent);
    }

    public void goToDeleteAppointment(View view) {
        Intent intent = new Intent(this, DeleteAppointment.class);
        startActivity(intent);
    }

    public void goToAboutUs(View view) {
        Intent intent = new Intent(this, AboutUs.class);
        startActivity(intent);
    }

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.putExtra("EXIT", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }
}