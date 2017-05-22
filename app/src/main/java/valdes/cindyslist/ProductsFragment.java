package valdes.cindyslist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProductsFragment extends Fragment {

    private static final String CATEGORY = "category";

    /***********************************************************************************************
     * Required empty constructor
     */
    public ProductsFragment() {}

    public static ProductsFragment newInstance(String category){

        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);
        fragment.setArguments(args);
        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

}
