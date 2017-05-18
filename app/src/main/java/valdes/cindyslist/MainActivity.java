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

public class MainActivity extends AppCompatActivity {

    private static final String DIALOG_CONFIRM = "dialog_confirm";

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

    /***********************************************************************************************
     * Android method
     *
     * Adds menu icons to Toolbar if one is present
     *
     * @param menu      The options menu to display items
     * @return          True for the menu to be displayed, false if not shown
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    /***********************************************************************************************
     * Android method
     *
     * Called when items on the Toolbar are clicked
     *
     * @param item      The item clicked on the Toolbar
     * @return          True if the item was clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast toast = Toast.makeText(this, "Works", Toast.LENGTH_SHORT);

        switch (item.getItemId()){

            case R.id.menu_add_item:

                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogFragment dialogFragment = UniversalDialogFragment.newInstance(R.id.menu_add_item);
                dialogFragment.show(fragmentManager, DIALOG_CONFIRM);

            case R.id.action_settings:
                toast.show();
                return true;

            case R.id.action_report:
                toast.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    /***********************************************************************************************
     *
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        switch (requestCode){

            default:
                return;
        }
    }

}

