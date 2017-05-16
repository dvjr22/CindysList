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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import valdes.cindyslist.Utilities.SwipeUtility;
import valdes.cindyslist.database.CreatedList;
import valdes.cindyslist.database.DatabaseManager;

public class MainFragment extends Fragment {

    private static final String TAG = "trace";

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;


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

        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview_lists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setSwipeForRecyclerView();

        updateUI();

        return view;
    }

    /***********************************************************************************************
     *
     */
    private void updateUI(){

        DatabaseManager databaseManager = DatabaseManager.get(getActivity());
        List<CreatedList> createdLists = databaseManager.getCreatedLists();
        if(listAdapter == null){
            listAdapter = new ListAdapter(createdLists);
            recyclerView.setAdapter(listAdapter);
        } else {
            listAdapter.notifyDataSetChanged();
        }

    }

    private void setSwipeForRecyclerView(){

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
     *
     */
    private class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //
        private TextView listName, dateCreated, items, total, undo;
        private LinearLayout listLayout, swipeLayout;
        private CreatedList createdList;

        /*******************************************************************************************
         *
         * @param view
         */
        public ListHolder(View view){

            super(view);
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
         *
         * @param createdList
         */
        public void BindList(CreatedList createdList){

            this.createdList = createdList;

            listName.setText(createdList.getTitle());
            dateCreated.setText(createdList.getDate());
            items.setText(String.format(Locale.US," %d", createdList.getItems()));
            total.setText(String.format(Locale.US, "%1$,.2f", createdList.getCost()));

            //items.setText(Integer.toString(createdList.getItems()));
            //total.setText(Double.toString(createdList.getCost()));

        }

        /*******************************************************************************************
         *
         * @param view
         */
        @Override
        public void onClick(View view){

            Toast.makeText(getActivity(), "You clicked the list: " , Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(), "You clicked the list: " + createdList.getTitle(), Toast.LENGTH_SHORT).show();

        }

    }

    private class ListAdapter extends RecyclerView.Adapter<ListHolder>{

        private List<CreatedList> createdLists;
        private List<String> itemsPendingRemoval;

        // 4 sec
        private static final int TIMEOUT = 4000;
        private Handler handler = new Handler();
        // Map pending runnables. Allows cancelation if necessary
        HashMap<String, Runnable> pendingRunnables = new HashMap<>();

        /*******************************************************************************************
         *
         * @param createdLists
         */
        private ListAdapter(List<CreatedList> createdLists){

            this.createdLists = createdLists;
            itemsPendingRemoval = new ArrayList<>();
        }

        /*******************************************************************************************
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public ListHolder onCreateViewHolder(ViewGroup parent, int viewType){

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            // Set up swipe transition.
            // view_swipe_transition includes the layouts view_created_lists and view_swipe_delete
            View view = layoutInflater.inflate(R.layout.view_swipe_transition, parent, false);
            return new ListHolder(view);

        }

        /*******************************************************************************************
         *
         * @param listHolder
         * @param position
         */
        @Override
        public void onBindViewHolder(ListHolder listHolder, int position){

            final CreatedList createdList = createdLists.get(position);

            if(itemsPendingRemoval.contains(createdList.getTitle())){

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
                listHolder.BindList(createdList);
            }

        }

        /*******************************************************************************************
         *
         * @return
         */
        @Override
        public int getItemCount(){
            return createdLists.size();
        }


        private void undoDelete(CreatedList createdList){

            Log.i(TAG, "undoDelete");

            Runnable pendingRemovalRunnable = pendingRunnables.get(createdList.getTitle());
            pendingRunnables.remove(createdList.getTitle());
            if(pendingRemovalRunnable != null)
                handler.removeCallbacks(pendingRemovalRunnable);
            itemsPendingRemoval.remove(createdList.getTitle());
            notifyItemChanged(createdLists.indexOf(createdList));

        }

        private void pendingRemoval(final int position){

            CreatedList createdList = createdLists.get(position);

            if(!itemsPendingRemoval.contains(createdList.getTitle())){
                itemsPendingRemoval.add(createdList.getTitle());
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

        private void deleteList(int position){
            
            CreatedList createdList = createdLists.get(position);
            
            if(itemsPendingRemoval.contains(createdList.getTitle())){

                itemsPendingRemoval.remove(createdList.getTitle());
            }

            if(createdLists.contains(createdList)){

                createdLists.remove(createdList);
                notifyItemRemoved(position);
                // TODO: 5/16/17 remove from SQLite db here
            }

        }

        private boolean isPendingRemoval(int position){

            CreatedList createdList = createdLists.get(position);
            return itemsPendingRemoval.contains(createdList.getTitle());

        }



    }


}
