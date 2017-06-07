package valdes.cindyslist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

public class CompleteListFragment extends Fragment {

    // Bundle variables
    private static final String LIST_NAME = "list_name";
    private static final String DATE = "date";
    private static final String ITEMS = "items";
    private static final String TOTAL = "total";

    // Widgets
    private TextView listName, date, items, total;

    public CompleteListFragment() {}

    /***********************************************************************************************
     * Creates a new instance of CompleteListFragment
     *
     * @param listName      The name of the list
     * @param date          The date the list was created
     * @param items         The number of items in the list
     * @param total         The total cost of the list
     * @return              A new instance of CompleteListFragment
     */
    public static CompleteListFragment newInstance(String listName, String date, int items, double total){

        CompleteListFragment fragment = new CompleteListFragment();
        Bundle args = new Bundle();
        args.putString(LIST_NAME, listName);
        args.putString(DATE, date);
        args.putInt(ITEMS, items);
        args.putDouble(TOTAL, total);
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

        View view = inflater.inflate(R.layout.fragment_complete_list, container, false);

        listName = (TextView) view.findViewById(R.id.textview_list_name);
        date = (TextView) view.findViewById(R.id.textview_date_created);
        items = (TextView) view.findViewById(R.id.textview_items);
        total = (TextView) view.findViewById(R.id.textview_total);

        listName.setText(getResources().getString(R.string.list_name,
                getArguments().getString(LIST_NAME)));
        date.setText(getResources().getString(R.string.date_created,
                getArguments().getString(DATE)));
        items.setText(getResources().getString(R.string.total_num_items,
                String.format(Locale.US," %d", getArguments().getInt(ITEMS))));
        total.setText(getResources().getString(R.string.total_cost,
                String.format(Locale.US, "%1$,.2f", getArguments().getDouble(TOTAL))));

        return view;
    }

}
