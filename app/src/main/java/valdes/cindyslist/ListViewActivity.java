package valdes.cindyslist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListViewActivity extends AppCompatActivity {

    private static final String LIST_TITLE = "list_title";


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

        loadListViewFragment(getIntent().getStringExtra(LIST_TITLE));

    }

    private void loadListViewFragment(String title){

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = ListViewFragment.newInstance(title);
        fragmentManager.beginTransaction().
                replace(R.id.list_view_activity_fragment_container, fragment).commit();

    }
}
