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

    private Toolbar toolbar;

    public static Intent newIntent(Context context){
        return new Intent(context, CreateNewListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_list);

        setUpToolBar();

        loadCategoryFragment();
        loadCompleteListFragment();

    }

    /***********************************************************************************************
     *
     */
    private void setUpToolBar(){

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle("Create A List");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void loadCategoryFragment(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = CategoriesFragment.newInstance();
        fragmentManager.beginTransaction().
                add(R.id.create_list_activity_container_categories, fragment).commit();

    }

    public void loadProductsFragment(String category){

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = ProductsFragment.newInstance(category);
        fragmentManager.beginTransaction().
                replace(R.id.create_list_activity_container_products, fragment).commit();

    }

    private void loadCompleteListFragment(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = CompleteListFragment.newInstance();
        fragmentManager.beginTransaction().
                replace(R.id.create_list_activity_container_complete, fragment).commit();

    }

}
