package valdes.cindyslist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import valdes.cindyslist.database.Product;
import valdes.cindyslist.utilities.SwipeUtility;
import valdes.cindyslist.database.DatabaseManager;
import valdes.cindyslist.database.ListProduct;

public class ListViewFragment extends Fragment {

    private static final String TAG = "trace";

    // Dialog variables
    private static final String REQUEST_NEW_PRODUCT = "request_new_product";
    private static final int REQUEST_CODE = 0;
    private static final String INTENT_CATEGORY = "intent_category";
    private static final String INTENT_PRODUCT = "intent_product";

    // Bundle variables
    private static final String LIST_TITLE = "list_title";

    // Update variables
    private String listName;

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;

    private DatabaseManager databaseManager;

    /***********************************************************************************************
     * Required empty constructor
     */
    public ListViewFragment() {}

    /***********************************************************************************************
     * Creates a new instance of ListViewFragment
     *
     * @param listName      The name of the list
     * @return              A new instance of ListViewFragment
     */
    public static ListViewFragment newInstance(String listName){

        ListViewFragment fragment = new ListViewFragment();
        Bundle args = new Bundle();
        args.putString(LIST_TITLE, listName);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        // Get list name in the event user needs to update
        listName = getArguments().getString(LIST_TITLE);
        // Set menu
        setHasOptionsMenu(true);

        // Setup RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI(listName);
        setSwipe();

        return view;
    }

    /***********************************************************************************************
     * Initial setup of RecyclerView
     */
    private void updateUI(String title){

        // Get the database
        databaseManager = DatabaseManager.get(getContext());
        List<ListProduct> listProducts = databaseManager.getListProducts(title);

        // Check if the adapter has been setup and checks for changes
        if(listAdapter == null){
            listAdapter = new ListAdapter(listProducts);
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
        swipe.setLeftcolorCode(ContextCompat.getColor(getActivity(), R.color.colorGreen));
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

        inflater.inflate(R.menu.menu_list_view, menu);
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

        switch (item.getItemId()){

            case R.id.menu_add_item:
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment dialogFragment =
                        UniversalDialogFragment.newInstance(R.id.menu_add_item, databaseManager.getCategories());
                dialogFragment.setTargetFragment(ListViewFragment.this, REQUEST_CODE);
                dialogFragment.show(fragmentManager, REQUEST_NEW_PRODUCT);
                return true;

            case R.id.menu_update_list:
                startActivity(CreateNewListActivity.newIntent(getContext(), listName));
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

        // Views to be bound
        private TextView product, price, qty, undo;
        private ImageView iProduct;
        private LinearLayout itemLayout, swipeLayout;
        private ListProduct listProduct;

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

            // view_item.xml
            itemLayout = (LinearLayout) view.findViewById(R.id.view_item);
            product = (TextView) view.findViewById(R.id.textview_product);
            price = (TextView) view.findViewById(R.id.textview_price);
            qty = (TextView) view.findViewById(R.id.textview_qty);
            
            iProduct = (ImageView) view.findViewById(R.id.imageview_product); 

            // view_swipe_remove_list.xml
            swipeLayout = (LinearLayout) view.findViewById(R.id.view_swipe_remove);
        }

        /*******************************************************************************************
         * Sets the attributes of each ListProduct to the appropriate views
         *
         * @param listProduct       The ListProduct that will be displayed in the RecyclerView
         */
        private void bindList(ListProduct listProduct){

            this.listProduct = listProduct;

            product.setText(listProduct.getProduct());
            price.setText(String.format(Locale.US, "%1$,.2f", listProduct.getPrice()));
            qty.setText(String.format(Locale.US, "%d", listProduct.getQty()));
            iProduct.setImageResource(listProduct.getPicId());

        }

        /*******************************************************************************************
         * Android method
         * Handles all onClick events for the view
         *
         * @param view      The view being clicked
         */
        @Override
        public void onClick(View view){


        }

    }

    /***********************************************************************************************
     * Class that binds all the views within the RecyclerView to be displayed
     */
    private class ListAdapter extends RecyclerView.Adapter<ListHolder>{

        // List of objects to be displayed in RecyclerView
        private List<ListProduct> listProducts;
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
         * @param listProducts      The list of objects to be displayed in the RecyclerView
         */
        private ListAdapter(List<ListProduct> listProducts){

            this.listProducts = listProducts;
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
            View view = layoutInflater.inflate(R.layout.view_swipe_transition_items, parent, false);
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

            final ListProduct listProduct = listProducts.get(position);

            if(pendingRemoval.contains(listProduct.getProduct())){

                listHolder.itemLayout.setVisibility(View.GONE);
                listHolder.swipeLayout.setVisibility(View.VISIBLE);
            } else {

                listHolder.itemLayout.setVisibility(View.VISIBLE);
                listHolder.swipeLayout.setVisibility(View.GONE);
                listHolder.bindList(listProduct);
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
            return listProducts.size();
        }


        /*******************************************************************************************
         * Cancels the delete action
         *
         * @param listProduct      The listProduct that the delete action should be cancelled
         */
        private void undoDelete(ListProduct listProduct){

            Runnable pendingRemovalRunnable = pendingRunnables.get(listProduct.getProduct());
            pendingRunnables.remove(listProduct.getProduct());
            if(pendingRemovalRunnable != null)
                handler.removeCallbacks(pendingRemovalRunnable);
            pendingRemoval.remove(listProduct.getProduct());
            notifyItemChanged(listProducts.indexOf(listProduct));

        }

        /*******************************************************************************************
         * Queues a CreatedList to be deleted within the timeout
         *
         * @param position      The position of the CreatedList within the adapter
         */
        private void pendingRemoval(final int position){

            ListProduct listProduct = listProducts.get(position);

            if(!pendingRemoval.contains(listProduct.getProduct())){
                pendingRemoval.add(listProduct.getProduct());
                notifyItemChanged(position);
                Runnable pendingRemovalRunnable = new Runnable() {
                    @Override
                    public void run() {

                        deleteList(position);

                    }
                };
                handler.postDelayed(pendingRemovalRunnable, TIMEOUT);
                pendingRunnables.put(listProduct.getProduct(), pendingRemovalRunnable);
            }
        }

        /*******************************************************************************************
         * Delete the CreatedList from existence
         *
         * @param position      The position of the CreatedList within the adapter
         */
        private void deleteList(int position){

            ListProduct createdList = listProducts.get(position);

            if(pendingRemoval.contains(createdList.getProduct())){
                pendingRemoval.remove(createdList.getProduct());
            }

            if(listProducts.contains(createdList)){
                listProducts.remove(createdList);
                notifyItemRemoved(position);
                databaseManager.deleteList(createdList.getProduct());
            }
        }

        /*******************************************************************************************
         * Check if a CreatedList is pending to be deleted
         *
         * @param position      The position of the CreatedList
         * @return              Status of CreatedList
         */
        private boolean isPendingRemoval(int position){

            ListProduct listProduct = listProducts.get(position);
            return pendingRemoval.contains(listProduct.getProduct());
        }

    }

}
