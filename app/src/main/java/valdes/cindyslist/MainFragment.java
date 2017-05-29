package valdes.cindyslist;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import valdes.cindyslist.utilities.SwipeUtility;
import valdes.cindyslist.database.CreatedList;
import valdes.cindyslist.database.DatabaseManager;
import valdes.cindyslist.database.Product;

public class MainFragment extends Fragment {

    private static final String TAG = "trace";

    // Dialog variables
    private static final String REQUEST_NEW_PRODUCT = "request_new_product";
    private static final int REQUEST_CODE = 0;
    private static final String INTENT_CATEGORY = "intent_category";
    private static final String INTENT_PRODUCT = "intent_product";

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private FloatingActionButton createNewList;

    private DatabaseManager databaseManager;


    /***********************************************************************************************
     * Required empty constructor
     */
    public MainFragment() {}

    /***********************************************************************************************
     * Creates a new instance of MainFragment
     *
     * @return      New instance of MainFragment
     */
    public static MainFragment newInstance(){

        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        // Set menu
        setHasOptionsMenu(true);

        // Setup Button
        createNewList = (FloatingActionButton) view.findViewById(R.id.button_create_list);
        createNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(CreateNewListActivity.newIntent(getContext(), null));

            }
        });

        // Setup RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview_lists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        setSwipe();

        return view;
    }

    /***********************************************************************************************
     * Initial setup of RecyclerView
     */
    private void updateUI(){

        // Get the database
        databaseManager = DatabaseManager.get(getContext());
        List<CreatedList> createdLists = databaseManager.getCreatedLists();
        // Check if the adapter has been setup and checks for changes
        if(listAdapter == null){
            listAdapter = new ListAdapter(createdLists);
            recyclerView.setAdapter(listAdapter);
        } else {
            listAdapter.notifyDataSetChanged();
        }
    }

    /***********************************************************************************************
     * Set swipe to RecyclerView
     */
    private void setSwipe(){

        SwipeUtility swipe = new SwipeUtility(0, ItemTouchHelper.LEFT, getActivity()) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int swipedPosition = viewHolder.getAdapterPosition();
                ListAdapter adapter = (ListAdapter) recyclerView.getAdapter();
                adapter.pendingRemoval(swipedPosition);
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder){

                int position = viewHolder.getAdapterPosition();
                ListAdapter adapter = (ListAdapter) recyclerView.getAdapter();
                if(adapter.isPendingRemoval(position)){
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipe);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        swipe.setLeftSwipeLable(getString(R.string.delete));
        swipe.setLeftcolorCode(ContextCompat.getColor(getActivity(), R.color.colorRed));
    }


    /***********************************************************************************************
     * Android method
     * Adds menu icons to Toolbar if one is present
     *
     * @param menu          The options menu to display items
     * @param inflater      Places menu times into menu
     * @return              True for the menu to be displayed, false if not shown
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /***********************************************************************************************
     * Android method
     * Called when items on the Toolbar are clicked
     *
     * @param item      The item clicked on the Toolbar
     * @return          True if the item was clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast toast = Toast.makeText(getContext(), "Fragment", Toast.LENGTH_SHORT);
        switch (item.getItemId()){

            case R.id.menu_add_item:
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment dialogFragment = UniversalDialogFragment.
                        newInstance(R.id.menu_add_item, databaseManager.getCategories());
                dialogFragment.setTargetFragment(MainFragment.this, REQUEST_CODE);
                dialogFragment.show(fragmentManager, REQUEST_NEW_PRODUCT);
                return true;

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
            databaseManager.insertProduct(new Product(data.getStringExtra(INTENT_CATEGORY),
                    data.getStringExtra(INTENT_PRODUCT)));
        }
    }

    /***********************************************************************************************
     * Class that sets up the container for each instance of a view to be displayed in the
     * Recycler view.
     */
    private class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Widgets
        private TextView listName, dateCreated, items, total, undo;
        private LinearLayout listLayout, swipeLayout;
        private CreatedList createdList;

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

            // view_created_list.xml
            listLayout = (LinearLayout) view.findViewById(R.id.view_created_lists);
            listName = (TextView) view.findViewById(R.id.textview_list_name);
            dateCreated = (TextView) view.findViewById(R.id.textview_date_created);
            items = (TextView) view.findViewById(R.id.textview_items);
            total = (TextView) view.findViewById(R.id.textview_total);

            // view_swipe_delete.xml
            swipeLayout = (LinearLayout) view.findViewById(R.id.view_swipe_delete);
            undo = (TextView) view.findViewById(R.id.undo);
        }

        /*******************************************************************************************
         * Sets the attributes of each CreatedList to the appropriate views
         *
         * @param createdList       The CreatedList that will be displayed in the RecyclerView
         */
        private void bindList(CreatedList createdList){

            this.createdList = createdList;
            listName.setText(getResources().getString(R.string.list_name, createdList.getTitle()));
            dateCreated.setText(getResources().
                    getString(R.string.date_created, createdList.getDate()));
            items.setText(getResources().getString(R.string.total_num_items,
                    String.format(Locale.US," %d", createdList.getItems())));
            total.setText(getResources().getString(R.string.total_cost,
                    String.format(Locale.US, "%1$,.2f", createdList.getCost())));
        }

        /*******************************************************************************************
         * Android method
         * Handles all onClick events for the view
         *
         * @param view      The view being clicked
         */
        @Override
        public void onClick(View view){

            startActivity(ListViewActivity.newIntent(getContext(), createdList.getTitle()));
        }

    }

    /***********************************************************************************************
     * Class that binds all the views within the RecyclerView to be displayed
     */
    private class ListAdapter extends RecyclerView.Adapter<ListHolder>{

        // List of objects to be displayed in RecyclerView
        private List<CreatedList> createdLists;
        // List of CreatedList titles to track removal
        private List<String> pendingRemoval;

        // 3 sec time until delete
        private static final int TIMEOUT = 3000;
        // Handler class to handle time delay
        private Handler handler = new Handler();
        // Map pending runnables. Allows cancelation if necessary
        HashMap<String, Runnable> pendingRunnables = new HashMap<>();

        /*******************************************************************************************
         * Constructor
         *
         * @param createdLists      The list of objects to be displayed in the RecyclerView
         */
        private ListAdapter(List<CreatedList> createdLists){

            this.createdLists = createdLists;
            pendingRemoval = new ArrayList<>();
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
            // Set up swipe transition.
            // view_swipe_transition_lists includes the layouts view_created_lists and view_swipe_delete
            View view = layoutInflater.inflate(R.layout.view_swipe_transition_lists, parent, false);
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

            final CreatedList createdList = createdLists.get(position);

            if(pendingRemoval.contains(createdList.getTitle())){

                listHolder.listLayout.setVisibility(View.GONE);
                listHolder.swipeLayout.setVisibility(View.VISIBLE);
                listHolder.undo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undoDelete(createdList);
                    }
                });
            } else {

                listHolder.listLayout.setVisibility(View.VISIBLE);
                listHolder.swipeLayout.setVisibility(View.GONE);
                listHolder.bindList(createdList);
            }
        }

        /*******************************************************************************************
         * Android method
         * Get the size of the list of objects to be displayed
         *
         * @return      The size of the list
         */
        @Override
        public int getItemCount(){
            return createdLists.size();
        }


        /*******************************************************************************************
         * Cancels the delete action
         *
         * @param createdList       The createdList that the delete action should be cancelled
         */
        private void undoDelete(CreatedList createdList){

            Runnable pendingRemovalRunnable = pendingRunnables.get(createdList.getTitle());
            pendingRunnables.remove(createdList.getTitle());
            if(pendingRemovalRunnable != null)
                handler.removeCallbacks(pendingRemovalRunnable);
            pendingRemoval.remove(createdList.getTitle());
            notifyItemChanged(createdLists.indexOf(createdList));
        }

        /*******************************************************************************************
         * Queues a CreatedList to be deleted within the timeout
         *
         * @param position      The position of the CreatedList within the adapter
         */
        private void pendingRemoval(final int position){

            CreatedList createdList = createdLists.get(position);

            if(!pendingRemoval.contains(createdList.getTitle())){
                pendingRemoval.add(createdList.getTitle());
                notifyItemChanged(position);
                Runnable pendingRemovalRunnable = new Runnable() {
                    @Override
                    public void run() {

                        deleteList(position);

                    }
                };
                handler.postDelayed(pendingRemovalRunnable, TIMEOUT);
                pendingRunnables.put(createdList.getTitle(), pendingRemovalRunnable);
            }
        }

        /*******************************************************************************************
         * Delete the CreatedList from existence
         *
         * @param position      The position of the CreatedList within the adapter
         */
        private void deleteList(int position){
            
            CreatedList createdList = createdLists.get(position);
            
            if(pendingRemoval.contains(createdList.getTitle())){
                pendingRemoval.remove(createdList.getTitle());
            }

            if(createdLists.contains(createdList)){
                createdLists.remove(createdList);
                notifyItemRemoved(position);
                databaseManager.deleteList(createdList.getTitle());
            }
        }

        /*******************************************************************************************
         * Check if a CreatedList is pending to be deleted
         *
         * @param position      The position of the CreatedList
         * @return              Status of CreatedList
         */
        private boolean isPendingRemoval(int position){

            CreatedList createdList = createdLists.get(position);
            return pendingRemoval.contains(createdList.getTitle());
        }

    }

}
