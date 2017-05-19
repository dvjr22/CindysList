package valdes.cindyslist;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.List;

import valdes.cindyslist.database.DatabaseManager;
import valdes.cindyslist.database.ListProduct;

public class ListViewActivity extends AppCompatActivity {

    private static final String LIST_TITLE = "list_title";

    private Toolbar toolbar;

    /***********************************************************************************************
     *
     * @param context
     * @param listName
     * @return
     */
    public static Intent newIntent(Context context, String listName){

        Intent intent = new Intent(context, ListViewActivity.class);
        intent.putExtra(LIST_TITLE, listName);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        setUpToolBar(getIntent().getStringExtra(LIST_TITLE));
        loadListViewFragment(getIntent().getStringExtra(LIST_TITLE));

    }

    /***********************************************************************************************
     *
     * @param title
     */
    private void loadListViewFragment(String title){

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = ListViewFragment.newInstance(title);
        fragmentManager.beginTransaction().
                add(R.id.list_view_activity_fragment_container, fragment).commit();

    }

    /***********************************************************************************************
     *
     */
    private void setUpToolBar(String title){

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}
