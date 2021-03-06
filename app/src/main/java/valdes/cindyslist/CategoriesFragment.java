package valdes.cindyslist;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.List;

import valdes.cindyslist.database.CreatedList;
import valdes.cindyslist.database.DatabaseManager;

public class CategoriesFragment extends Fragment {

    private static final String TAG = "trace";

    // Dialog variables
    private static final String REQUEST_TITLE = "request_title";
    private static final int REQUEST_CODE = 0;
    private static final String INTENT_TITLE = "intent_title";

    // Saved state variables
    private static final String OUTSTATE_LIST_NAME = "saved_instance_state_title";
    private static final String OUTSTATE_DATE = "saved_instance_date";

    // Bundle variables
    private static final String LIST_NAME = "list_name";

    private String listName;
    private String date;

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;

    private DatabaseManager databaseManager;

    private CategoriesFragmentListener listener;

    public interface CategoriesFragmentListener{
        void loadProductsFragment(String category, String listName, String date);
        void loadCompleteListFragment(String listName, String date, int items, double cost);
    }

    /***********************************************************************************************
     * Required empty constructor
     */
    public CategoriesFragment() {}

    /***********************************************************************************************
     * Create a new instance of CategoriesFragment
     *
     * @param listName      The name of the list
     *                      This is used when the user wants to update a list
     * @return              A new instance of CategoriesFragment
     */
    public static CategoriesFragment newInstance(String listName){

        CategoriesFragment fragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putString(LIST_NAME, listName);
        fragment.setArguments(args);
        return fragment;
    }

    /***********************************************************************************************
     * Android method
     * Called to have the fragment instantiate its user interface view
     *
     * @param inflater              The LayoutInflater object that can be used to inflate any views
     * @param container             The parent view that the fragment's UI should be attached to
     * @param savedInstanceState    Bundle containing the data most recently supplied
     * @return                      The View of the Fragment UI, or null
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        // Setup RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview_category);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        // Check whether updating or creating a list
        // Check if a list is being updated
        if (getArguments().getString(LIST_NAME) != null){
            listName = getArguments().getString(LIST_NAME);
            CreatedList createdList = databaseManager.getOneCreatedList(listName);
            listener.loadCompleteListFragment(listName,
                    createdList.getDate(), createdList.getItems(), createdList.getCost());
        }
        // Check for save state
        if (savedInstanceState != null) {
            listName = savedInstanceState.getString(OUTSTATE_LIST_NAME);
            date = savedInstanceState.getString(OUTSTATE_DATE);
        }
        // Get a list name for a new list
        if (listName == null) getListTitle();

        return view;
    }

    /***********************************************************************************************
     * Get the name of the list from the user
     */
    private void getListTitle(){

        FragmentManager fragmentManager = getFragmentManager();
        DialogFragment dialogFragment =
                UniversalDialogFragment.newInstance(R.layout.fragment_categories, null);
        dialogFragment.setTargetFragment(CategoriesFragment.this, REQUEST_CODE);
        dialogFragment.show(fragmentManager, REQUEST_TITLE);
    }

    /***********************************************************************************************
     * Initial setup of RecyclerView
     */
    private void updateUI(){

        // Get the database
        databaseManager = DatabaseManager.get(getContext());
        List<String> categories = databaseManager.getCategories();

        // Check if the adapter has been setup and checks for changes
        if(listAdapter == null){
            listAdapter = new ListAdapter(categories);
            recyclerView.setAdapter(listAdapter);
        } else {
            listAdapter.notifyDataSetChanged();
        }
    }

    /***********************************************************************************************
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString(OUTSTATE_LIST_NAME, listName);
        outState.putString(OUTSTATE_DATE, date);
    }

    /***********************************************************************************************
     * Android method
     * Called when a fragment is first attached to its context
     *
     * @param context       The context to use
     */
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        listener = (CategoriesFragmentListener) context;
    }

    /***********************************************************************************************
     * Android method
     * Gets the title of the list from UniversalDialogFragment
     *
     * @param requestCode       The code of the original request
     * @param resultCode        The code of the result
     * @param data              The data that is being sent between dialog and fragment
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE){
            listName = data.getStringExtra(INTENT_TITLE);
            CreatedList createdList = new CreatedList(listName);
            date = createdList.getDate();
            // Insert list name into database
            databaseManager.insertList(createdList);
            //load situation fragment
            listener.loadCompleteListFragment(listName, date,
                    createdList.getItems(), createdList.getCost());
        }
    }

    /***********************************************************************************************
     * Class that sets up the container for each instance of a view to be displayed in the
     * Recycler view.
     */
    private class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //
        private TextView categoryTextView;
        private String category;

        /*******************************************************************************************
         * Constructor
         *
         * Creates one instance of the view that will be displayed within the RecyclerView
         *
         * @param view      The view that will be used within the RecyclerView
         */
        private ListHolder(View view){

            super(view);
            // attach listener for onClick events
            view.setOnClickListener(this);
            // view_category.xml
            categoryTextView = (TextView) view.findViewById(R.id.textview_category);
        }

        /*******************************************************************************************
         * Sets the attributes of each category to the appropriate view
         *
         * @param category      The ListProduct that will be displayed in the RecyclerView
         */
        private void bindList(String category){

            this.category = category;
            categoryTextView.setText(category);
        }

        /*******************************************************************************************
         * Android method
         * Handles all onClick events for the view
         *
         * @param view      The view being clicked
         */
        @Override
        public void onClick(View view){

            listener.loadProductsFragment(category, listName, date);
        }

    }

    /***********************************************************************************************
     * Class that binds all the views within the RecyclerView to be displayed
     */
    private class ListAdapter extends RecyclerView.Adapter<ListHolder>{

        // List of Strings to be displayed in RecyclerView
        private List<String> categories;


        /*******************************************************************************************
         * Constructor
         *
         * @param categories      The list of objects to be displayed in the RecyclerView
         */
        private ListAdapter(List<String> categories){

            this.categories = categories;
        }

        /*******************************************************************************************
         * Android method
         * Creates the ViewHolder for the RecyclerView
         *
         * @param parent        ViewGroup to which view will be added
         * @param viewType      The type of view
         * @return              ListHolder with the given view
         */
        @Override
        public ListHolder onCreateViewHolder(ViewGroup parent, int viewType){

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            // Setup the view that will be in the RecyclerView
            View view = layoutInflater.inflate(R.layout.view_category, parent, false);
            return new ListHolder(view);
        }

        /*******************************************************************************************
         * Android method
         * Displays data in the specified position
         *
         * @param listHolder        ViewHolder with the data to be displayed
         * @param position          The position in the adapter
         */
        @Override
        public void onBindViewHolder(ListHolder listHolder, int position){

            String category  = categories.get(position);
            listHolder.bindList(category);
        }


        /*******************************************************************************************
         * Android method
         * Get the size of the list of objects to be displayed
         *
         * @return      The size of the list
         */
        @Override
        public int getItemCount(){
            return categories.size();
        }

    }

}
