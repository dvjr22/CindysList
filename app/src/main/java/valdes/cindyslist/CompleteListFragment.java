package valdes.cindyslist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CompleteListFragment extends Fragment {


    public CompleteListFragment() {
        // Required empty public constructor
    }

    public static CompleteListFragment newInstance(){
        return new CompleteListFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complete_list, container, false);

        return view;
    }

}
