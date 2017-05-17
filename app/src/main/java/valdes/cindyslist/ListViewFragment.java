package valdes.cindyslist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListViewFragment extends Fragment {

    private static final String LIST_TITLE = "list_title";

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

        Toast.makeText(getContext(), getArguments().getString(LIST_TITLE), Toast.LENGTH_SHORT).show();

        return view;
    }

}
