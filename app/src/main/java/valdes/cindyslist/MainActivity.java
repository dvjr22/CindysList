package valdes.cindyslist;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "trace";

    // Save state variables
    private static final String OUTSTATE_FRAGMENT = "outstate_fragment";

    // Fragments
    private Fragment fragment;

    // Layouts
    private DrawerLayout drawer;

    // Widgets
    private Toolbar toolbar;

    /***********************************************************************************************
     * Creates a new intent to start MainActivity
     *
     * @param context       The context to use
     * @return              The Activity to be started
     */
    public static Intent newIntent(Context context){
        return new Intent(context, MainActivity.class);
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
        setContentView(R.layout.activity_main);

        setUpToolBar();
        setUpDrawer();

        // Restore states
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            // Restore CategoryFragment
            fragment = getSupportFragmentManager().
                    getFragment(savedInstanceState, OUTSTATE_FRAGMENT);
            fragmentManager.beginTransaction().
                    replace(R.id.main_activity_fragment_container, fragment).commit();
        } else {
            // Initial load of Fragments
            loadMain();
        }
    }

    /***********************************************************************************************
     * Loads MainFragment in container
     */
    private void loadMain(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = MainFragment.newInstance();
        fragmentManager.beginTransaction().
                replace(R.id.main_activity_fragment_container, fragment).commit();
    }

    /***********************************************************************************************
     * Loads RecipeFragment in container
     */
    private void loadRecipeFragment(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = RecipesFragment.newInstance();
        fragmentManager.beginTransaction().
                replace(R.id.main_activity_fragment_container, fragment).commit();
    }

    /***********************************************************************************************
     * Setup the Toolbar
     */
    private void setUpToolBar(){

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

    /***********************************************************************************************
     * Setup Navigation Drawer/View
     */
    private void setUpDrawer(){

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawer,
                        toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /***********************************************************************************************
     * Android method
     * Called when user has pressed the back key
     */
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /***********************************************************************************************
     * Android method
     * Called when an item in the drawer is clicked
     *
     * @param item      The selected menu item
     * @return          True to display the selected item
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.nav_home:
                loadMain();
                break;

            case R.id.nav_recipe:
                loadRecipeFragment();
                break;

            case R.id.nav_products:
                break;
            case R.id.navigation_sub_item_1:
                break;
            case R.id.navigation_sub_item_2:
                break;
            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        getSupportFragmentManager().putFragment(outState, OUTSTATE_FRAGMENT, fragment);
    }
}

