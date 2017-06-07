package valdes.cindyslist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/***************************************************************************************************
 * Used on application initial launch to display icon while app is loaded
 */
public class SplashActivity extends AppCompatActivity {

    /***********************************************************************************************
     * Android method
     *
     * Called when Activity is starting
     * Inflate Activity UI, programmatically interact with widgets, restore saved states
     *
     * @param savedInstanceState        Bundle containing the data most recently supplied
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Load MainActivity when ready
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        // Finish to destroy SplashActivity
        finish();
    }

}
