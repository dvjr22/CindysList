package valdes.cindyslist;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    /***********************************************************************************************
     *
     * @param context
     * @return
     */
    public static Intent newIntent(Context context){
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolBar();
        loadMain();
    }

    /***********************************************************************************************
     *
     */
    private void loadMain(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = MainFragment.newInstance();
        fragmentManager.beginTransaction().
                replace(R.id.main_activity_fragment_container, fragment).commit();
    }

    /***********************************************************************************************
     *
     */
    private void setUpToolBar(){

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

}

