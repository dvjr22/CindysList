package valdes.cindyslist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class CreateNewListActivity extends AppCompatActivity implements
        CategoriesFragment.CategoriesFragmentListener,
        ProductsFragment.ProductsFragmentListener{

    // Intent variables
    private static final String LIST_NAME = "list_name";
    private static final String DATE = "date";

    // Save state variables
    private static final String OUTSTATE_CATEGORY = "category_fragment";

    // Fragments
    private Fragment categoryFragment;
    private Fragment productFragment;

    // widgets
    private Toolbar toolbar;

    /***********************************************************************************************
     * Creates a new intent to start CreateNewListActivity
     *
     * @param context       The context to use
     * @param listName      The name of the list if there is one
     *                      This is used if a list is being updated
     * @return              The Activity to be started
     */
    public static Intent newIntent(Context context, String listName){

        Intent intent = new Intent(context, CreateNewListActivity.class);
        intent.putExtra(LIST_NAME, listName);
        return intent;
    }

    /***********************************************************************************************
     * Android method
     * Called when Activity is starting
     * Inflate Activity UI, programmatically interact with widgets, restore saved states
     *
     * @param savedInstanceState        Bundle containing the data most recently supplied
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_list);

        // Setup Toolbar
        setUpToolBar();
        // loadCompleteListFragment();

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
            loadCategoryFragment(getIntent().getStringExtra(LIST_NAME));
        }
    }

    /***********************************************************************************************
     * Set up the Toolbar
     */
    private void setUpToolBar(){

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.create_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /***********************************************************************************************
     * Load CategoryFragment in container
     *
     * @param listName      The name of the list
     */
    private void loadCategoryFragment(String listName){

        FragmentManager fragmentManager = getSupportFragmentManager();
        categoryFragment = CategoriesFragment.newInstance(listName);
        fragmentManager.beginTransaction().
                add(R.id.create_list_activity_container_categories, categoryFragment).commit();
    }

    /***********************************************************************************************
     * Loads ProductFragment with items in a category that have not been selected in the list
     *
     * @param category      The category of products to display
     * @param listName      The name of the current list being created
     * @param date          The date the list was created
     */
    public void loadProductsFragment(String category, String listName, String date){

        FragmentManager fragmentManager = getSupportFragmentManager();
        productFragment = ProductsFragment.newInstance(category ,listName, date);
        fragmentManager.beginTransaction().
                replace(R.id.create_list_activity_container_products, productFragment).commit();
    }

    /***********************************************************************************************
     * Loads CompleteListFragment with name, date created, number of items, and the cost of the list
     *
     * @param listName      The name of the list
     * @param date          The date the list was created
     * @param items         The number of items in the list
     * @param cost          The total cost of the list
     */
    public void loadCompleteListFragment(String listName, String date, int items, double cost){

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = CompleteListFragment.newInstance(listName, date, items, cost);
        fragmentManager.beginTransaction().
                replace(R.id.create_list_activity_container_complete, fragment).commit();
    }

    /***********************************************************************************************
     * Android method
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
