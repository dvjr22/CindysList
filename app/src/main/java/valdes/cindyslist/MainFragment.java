package valdes.cindyslist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview_lists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

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
        private TextView listName, dateCreated, items, total;

        /*******************************************************************************************
         *
         * @param view
         */
        public ListHolder(View view){

            super(view);
            view.setOnClickListener(this);

            listName = (TextView) view.findViewById(R.id.textview_list_name);
            dateCreated = (TextView) view.findViewById(R.id.textview_date_created);
            items = (TextView) view.findViewById(R.id.textview_items);
            total = (TextView) view.findViewById(R.id.textview_total);

        }

        /*******************************************************************************************
         *
         * @param createdList
         */
        public void bindList(CreatedList createdList){

            listName.setText(createdList.getTitle());
            dateCreated.setText(createdList.getDate());
            items.setText(Integer.toString(createdList.getItems()));
            total.setText(Double.toString(createdList.getCost()));

        }

        /*******************************************************************************************
         *
         * @param view
         */
        @Override
        public void onClick(View view){

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
