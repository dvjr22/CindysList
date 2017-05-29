package valdes.cindyslist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;


public class ListViewActivity extends AppCompatActivity {

    // Intent variables
    private static final String LIST_TITLE = "list_title";
    private static final String DATE = "date";

    // Widgets
    private Toolbar toolbar;

    /***********************************************************************************************
     * Creates a new intent to start ListViewActivity
     *
     * @param context       The context to use
     * @param listName      The name of the list to be displayed
     * @param date          The date the list was created
     * @return              The Activity to be started
     */
    public static Intent newIntent(Context context, String listName, String date){

        Intent intent = new Intent(context, ListViewActivity.class);
        intent.putExtra(LIST_TITLE, listName);
        intent.putExtra(DATE, date);
        return intent;
    }

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
        setContentView(R.layout.activity_list_view);

        setUpToolBar(getIntent().getStringExtra(LIST_TITLE));
        loadListViewFragment(getIntent().getStringExtra(LIST_TITLE),
                getIntent().getStringExtra(DATE));
    }

    /***********************************************************************************************
     * Load ListViewFragment in container
     *
     * @param title     The title of the list to be displayed
     * @param date      The date the list was created
     */
    private void loadListViewFragment(String title, String date){

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = ListViewFragment.newInstance(title, date);
        fragmentManager.beginTransaction().
                add(R.id.list_view_activity_fragment_container, fragment).commit();
    }

    /***********************************************************************************************
     * Setup the Toolbar
     */
    private void setUpToolBar(String title){

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
