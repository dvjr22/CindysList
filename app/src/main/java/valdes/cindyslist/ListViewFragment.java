package valdes.cindyslist;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import valdes.cindyslist.Utilities.SwipeUtility;
import valdes.cindyslist.database.DatabaseManager;
import valdes.cindyslist.database.ListProduct;

public class ListViewFragment extends Fragment {

    private static final String TAG = "trace";

    private static final String LIST_TITLE = "list_title";

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;

    private DatabaseManager databaseManager;

    /***********************************************************************************************
     * Required empty constructor
     */
    public ListViewFragment() {}

    /***********************************************************************************************
     *
     * @param listName
     * @return
     */
    public static ListViewFragment newInstance(String listName){

        Bundle args = new Bundle();
        args.putString(LIST_TITLE, listName);
        ListViewFragment fragment = new ListViewFragment();
        fragment.setArguments(args);
        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        // Setup RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI(getArguments().getString(LIST_TITLE));
        setSwipe();

        return view;
    }

    /***********************************************************************************************
     * Initial setup of RecyclerView
     */
    private void updateUI(String title){

        // Get the database
        databaseManager = DatabaseManager.get(getActivity());
        List<ListProduct> listProducts = databaseManager.getListProducts(title);

        for (int i = 0; i < listProducts.size(); i++){
            Log.i(TAG, listProducts.get(i).getProduct());
        }

        // Check if the adapter has been setup and checks for changes
        if(listAdapter == null){
            listAdapter = new ListAdapter(listProducts);
            recyclerView.setAdapter(listAdapter);

            Log.i(TAG, "null adapter set up");

        } else {
            listAdapter.notifyDataSetChanged();

            Log.i(TAG, "data set changed");
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

        Log.i(TAG, "set swipe");

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipe);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        swipe.setLeftSwipeLable(getString(R.string.delete));
        swipe.setLeftcolorCode(ContextCompat.getColor(getActivity(), R.color.colorRed));

    }

    /***********************************************************************************************
     * Class that sets up the container for each instance of a view to be displayed in the
     * Recycler view.
     */
    private class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //
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

            // view_swipe_delete.xml
            swipeLayout = (LinearLayout) view.findViewById(R.id.view_swipe_delete);
            undo = (TextView) view.findViewById(R.id.undo);

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
         *
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

            Log.i(TAG, "ListAdapter: " + listProducts.size());

        }

        /*******************************************************************************************
         * Android method
         *
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
         *
         * Displays data in the specified position
         *
         * @param listHolder        ViewHolder with the data to be displayed
         * @param position          The position in the adapter
         */
        @Override
        public void onBindViewHolder(ListHolder listHolder, int position){

            final ListProduct listProduct = listProducts.get(position);

            Log.i(TAG, "onBindViewHolder: " + listProduct.getProduct());

            if(pendingRemoval.contains(listProduct.getProduct())){

                listHolder.itemLayout.setVisibility(View.GONE);
                listHolder.swipeLayout.setVisibility(View.VISIBLE);
                listHolder.undo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undoDelete(listProduct);
                    }
                });
            } else {

                listHolder.itemLayout.setVisibility(View.VISIBLE);
                listHolder.swipeLayout.setVisibility(View.GONE);
                listHolder.bindList(listProduct);
            }

        }

        /*******************************************************************************************
         * Android method
         *
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
