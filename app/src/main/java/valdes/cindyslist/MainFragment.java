package valdes.cindyslist;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import valdes.cindyslist.database.CreatedList;
import valdes.cindyslist.database.DatabaseManager;

public class MainFragment extends Fragment {

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

        //setSwipeForRecyclerView();

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
        public void bindList(CreatedList createdList){

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

            Toast.makeText(getActivity(), "You clicked the list: " + createdList.getTitle(), Toast.LENGTH_SHORT).show();

        }

    }

    private class ListAdapter extends RecyclerView.Adapter<ListHolder>{

        private List<CreatedList> createdLists;

        /*******************************************************************************************
         *
         * @param createdLists
         */
        private ListAdapter(List<CreatedList> createdLists){
            this.createdLists = createdLists;
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
            View view = layoutInflater.inflate(R.layout.view_created_lists, parent, false);
            return new ListHolder(view);

        }

        /*******************************************************************************************
         *
         * @param listHolder
         * @param position
         */
        @Override
        public void onBindViewHolder(ListHolder listHolder, int position){

            CreatedList createdList = createdLists.get(position);
            listHolder.bindList(createdList);

        }

        /*******************************************************************************************
         *
         * @return
         */
        @Override
        public int getItemCount(){
            return createdLists.size();
        }

    }


}
