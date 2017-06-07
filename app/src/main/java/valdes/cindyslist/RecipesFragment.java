package valdes.cindyslist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecipesFragment extends Fragment {


    /***********************************************************************************************
     * Required empty constructor
     */
    public RecipesFragment() {}

    /***********************************************************************************************
     * Creates a new instance of RecipesFragment
     *
     * @return      New instance of RecipesFragment
     */
    public static RecipesFragment newInstance(){

        return new RecipesFragment();
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

        View view  = inflater.inflate(R.layout.fragment_recipes, container, false);

        return view;
    }

}
