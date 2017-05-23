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


import valdes.cindyslist.Utilities.SwipeUtility;
import valdes.cindyslist.database.DatabaseManager;
import valdes.cindyslist.database.Product;

public class ProductsFragment extends Fragment {

    private static final String TAG = "trace";

    private static final String CATEGORY = "category";
    private static final String LIST_NAME = "list_name";

    private String listName;

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;

    private DatabaseManager databaseManager;

    /***********************************************************************************************
     * Required empty constructor
     */
    public ProductsFragment() {}

    /***********************************************************************************************
     *
     *
     * @param category
     * @return
     */
    public static ProductsFragment newInstance(String category, String listName){

        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);
        args.putString(LIST_NAME, listName);
        fragment.setArguments(args);
        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        listName = getArguments().getString(LIST_NAME);

        // Setup RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI(getArguments().getString(CATEGORY));
        setSwipe();

        return view;

    }

    /***********************************************************************************************
     * Initial setup of RecyclerView
     */
    private void updateUI(String category){

        // Get the database
        databaseManager = DatabaseManager.get(getActivity());
        List<Product> products = databaseManager.getProducts(category, listName);

        // Check if the adapter has been setup and checks for changes
        if(listAdapter == null){
            listAdapter = new ListAdapter(products);
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
     * Class that sets up the container for each instance of a view to be displayed in the
     * Recycler view.
     */
    private class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Views to be bound
        private TextView productTextView;
        private ImageView iProduct;

        private LinearLayout itemLayout, swipeLayout;

        private Product product;

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

            // view_item_create.xml
            itemLayout = (LinearLayout) view.findViewById(R.id.view_item_create);
            productTextView = (TextView) view.findViewById(R.id.textview_product_create);
            iProduct = (ImageView) view.findViewById(R.id.imageview_product_create);

            // view_swipe_added.xml
            swipeLayout = (LinearLayout) view.findViewById(R.id.view_swipe_add);

        }

        /*******************************************************************************************
         * Sets the attributes of each ListProduct to the appropriate views
         *
         * @param product       The product that will be displayed in the RecyclerView
         */
        private void bindList(Product product){

            this.product = product;

            productTextView.setText(product.getProductName());
            iProduct.setImageResource(product.getPicId());

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
        private List<Product> products;
        // List of CreatedList titles to track removal
        private List<String> pendingRemoval;

        // .5 sec time for adding to database
        private static final int TIMEOUT = 100;
        // Handler class to handle time delay
        private Handler handler = new Handler();
        // Map pending runnables. Allows cancelation if necessary
        HashMap<String, Runnable> pendingRunnables = new HashMap<>();

        /*******************************************************************************************
         * Constructor
         *
         * @param products      The list of objects to be displayed in the RecyclerView
         */
        private ListAdapter(List<Product> products){

            this.products = products;
            pendingRemoval = new ArrayList<>();

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
            // view_swipe_transition_lists includes the layouts view_item_create and view_swipe_added
            View view = layoutInflater.inflate(R.layout.view_swipe_transition_add, parent, false);
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

            Product product = products.get(position);

            if(pendingRemoval.contains(product.getProductName())){

                listHolder.itemLayout.setVisibility(View.GONE);
                listHolder.swipeLayout.setVisibility(View.VISIBLE);
                // onClick can be setup here
            } else {
                listHolder.itemLayout.setVisibility(View.VISIBLE);
                listHolder.swipeLayout.setVisibility(View.GONE);
                listHolder.bindList(product);
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
            return products.size();
        }


        /*******************************************************************************************
         * Queues a CreatedList to be deleted within the timeout
         *
         * @param position      The position of the CreatedList within the adapter
         */
        private void pendingRemoval(final int position){

            Product product = products.get(position);

            if(!pendingRemoval.contains(product.getProductName())){
                pendingRemoval.add(product.getProductName());
                notifyItemChanged(position);
                Runnable pendingRemovalRunnable = new Runnable() {
                    @Override
                    public void run() {

                        addItemToList(position);

                    }
                };
                handler.postDelayed(pendingRemovalRunnable, TIMEOUT);
                pendingRunnables.put(product.getProductName(), pendingRemovalRunnable);
            }

        }

        /*******************************************************************************************
         * Add the Item to the list
         *
         * @param position      The position of the item within the RecyclerView
         */
        private void addItemToList(int position){

            Product product = products.get(position);

            if(pendingRemoval.contains(product.getProductName())){
                pendingRemoval.remove(product.getProductName());
            }

            if(products.contains(product)){
                products.remove(product);
                notifyItemRemoved(position);
                // TODO: 5/22/2017 take care of qty total  
                databaseManager.insertCreatedListItem(listName, product.getProductName(), 1);
            }

        }

        /*******************************************************************************************
         * Check if a CreatedList is pending to be deleted
         *
         * @param position      The position of the CreatedList
         * @return              Status of CreatedList
         */
        private boolean isPendingRemoval(int position){

            Product product = products.get(position);
            return pendingRemoval.contains(product.getProductName());

        }

    }

}
