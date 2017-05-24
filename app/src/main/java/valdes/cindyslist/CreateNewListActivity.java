package valdes.cindyslist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class CreateNewListActivity extends AppCompatActivity implements
        CategoriesFragment.CategoriesFragmentListener{

    // Save state variables
    private static final String OUTSTATE_CATEGORY = "category_fragment";

    // widgets
    private Toolbar toolbar;
    private Fragment categoryFragment;
    private Fragment productFragment;

    /***********************************************************************************************
     * Creates a new intent to start CreateNewListActivity
     *
     * @param context       The context to use
     * @return              The Activity to be started
     */
    public static Intent newIntent(Context context){
        return new Intent(context, CreateNewListActivity.class);
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
        setContentView(R.layout.activity_create_new_list);

        setUpToolBar();
        loadCompleteListFragment();

        // Restore states
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            // Restore CategoryFragment
            categoryFragment = getSupportFragmentManager().
                    getFragment(savedInstanceState, OUTSTATE_CATEGORY);
            fragmentManager.beginTransaction().
                    replace(R.id.create_list_activity_container_categories, categoryFragment).commit();
        } else {
            // Initial load of Fragments
            loadCategoryFragment();
        }
    }

    /***********************************************************************************************
     * Set up the Toolbar
     */
    private void setUpToolBar(){

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle("Create A List");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /***********************************************************************************************
     * Load CategoryFragment in container
     */
    private void loadCategoryFragment(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        categoryFragment = CategoriesFragment.newInstance();
        fragmentManager.beginTransaction().
                add(R.id.create_list_activity_container_categories, categoryFragment).commit();
    }

    /***********************************************************************************************
     * Loads ProductFragment with items in a category that have not been selected in the list
     *
     * @param category      The category of products to display
     * @param listName      The name of the current list being created
     */
    public void loadProductsFragment(String category, String listName){

        FragmentManager fragmentManager = getSupportFragmentManager();
        productFragment = ProductsFragment.newInstance(category ,listName);
        fragmentManager.beginTransaction().
                replace(R.id.create_list_activity_container_products, productFragment).commit();
    }

    /***********************************************************************************************
     *
     */
    private void loadCompleteListFragment(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = CompleteListFragment.newInstance();
        fragmentManager.beginTransaction().
                replace(R.id.create_list_activity_container_complete, fragment).commit();
    }

    /***********************************************************************************************
     * Android method
     *
     * Saves the state of Activity in order to be restored
     *
     * @param outState      Bundle to save state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        // Save Fragment instance
        getSupportFragmentManager().putFragment(outState, OUTSTATE_CATEGORY, categoryFragment);
    }

}
