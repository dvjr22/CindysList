package valdes.cindyslist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import valdes.cindyslist.Utilities.Magic;

/***************************************************************************************************
 * Dialog that handles all requirements based on calling parent Fragment/Activity
 */
public class UniversalDialogFragment extends DialogFragment {

    private static final String TAG = "trace";

    // String used to get the id of the Activity/Fragment that is calling the dialog
    private static final String PARENT_ID = "parent_id";
    private static final String CATEGORY_LIST = "category_list";

    // sendResult variables
    private static final String INTENT_TITLE = "intent_title";
    private static final String INTENT_CATEGORY = "intent_category";
    private static final String INTENT_PRODUCT = "intent_product";

    // widgets and layouts
    private LinearLayout addTitleLayout, addProductLayout;
    private EditText listTitle, productName, categoryName;
    private Spinner categorySpinner;

    // variables
    private int parentId;
    private List<String> categories;
    private String addedCategory;

    // Listener setup in case I need to set it up
    private UniversalDialogFragmentListener listener;

    public interface UniversalDialogFragmentListener{}

    /***********************************************************************************************
     * Creates a new instance of a dialog fragment
     *
     * @param parent_id     the R_id of the layout being used by the parent Activity/Fragment
     * @return              New instance of Dialog Fragment
     */
    public static UniversalDialogFragment newInstance(int parent_id, ArrayList<String> categories){

        UniversalDialogFragment fragment = new UniversalDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PARENT_ID, parent_id);
        bundle.putStringArrayList(CATEGORY_LIST, categories);
        fragment.setArguments(bundle);
        return fragment;
    }

    // Required empty constructor
    public UniversalDialogFragment(){}


    // Android onCreate method
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_universal, null);

        // Get the id of the parent Activity/Fragment
        parentId = getArguments().getInt(PARENT_ID);

        // Get the id of the text to be displayed based on parent_id
        int stringId = DataStructures.getString(parentId);

        // Add title layout
        addTitleLayout = (LinearLayout) view.findViewById(R.id.univ_diag_add_title);
        listTitle = (EditText) view.findViewById(R.id.univ_diag_edittext_add_title);

        // Add product layout
        addProductLayout = (LinearLayout) view.findViewById(R.id.univ_diag_add_product);
        productName = (EditText) view.findViewById(R.id.univ_diag_edittext_add_product);
        categoryName = (EditText) view.findViewById(R.id.univ_diag_edittext_add_category);
        categorySpinner = (Spinner) view.findViewById(R.id.univ_diag_spinner_category);

        // Set visibility of layouts
        setDisplay();

        // Return Dialog
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(stringId)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        sendResult(Activity.RESULT_OK, parentId, listTitle.getText().toString(),
                                addedCategory, productName.getText().toString());
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // TODO: 5/23/2017 need method to handle negative clicks
                        startActivity(MainActivity.newIntent(getContext()));
                    }
                }).create();
    }

    /***********************************************************************************************
     *  Here in case listener is set up
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /***********************************************************************************************
     * Sends results to parent Activity/Fragment
     *
     * Current setup is for a Fragment and sending results to onActivityResult method
     *
     * @param resultCode        Result code used to identify user response
     */
    private void sendResult(int resultCode, int parentId, String title, String category,
                            String product){

        if(getTargetFragment() == null){
            return;
        }

        // Set data according to parent Fragment
        Intent intent = new Intent();

        switch (parentId){

            case R.id.menu_add_item:
                intent.putExtra(INTENT_CATEGORY, category);
                intent.putExtra(INTENT_PRODUCT, product);
                break;

            case R.layout.fragment_categories:
                intent.putExtra(INTENT_TITLE, title);
                break;
        }
        // Return to parent Fragment with results
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }


    /***********************************************************************************************
     * Sets the visibility and listeners of Views based on the parent that called Fragment
     */
    private void setDisplay(){

        switch (parentId){

            case R.id.menu_add_item:
                addTitleLayout.setVisibility(View.GONE);

                // EditText Listener
                categoryName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void afterTextChanged(Editable editable) {
                        addedCategory = categoryName.getText().toString();
                    }
                });
                // Invisible on start up
                categoryName.setVisibility(View.GONE);

                // Setup Spinner
                categories = getArguments().getStringArrayList(CATEGORY_LIST);
                categories.add(getResources().getString(R.string.other));

                ArrayAdapter <String> adapter = new ArrayAdapter<>
                        (getContext(),android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(adapter);

                // Spinner Listener
                categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        // Check selection, handle "Other" by making EditText visible
                        // If standard category selected, set EditText to GONE
                        if (categories.size() - 1 == i){
                            categoryName.setVisibility(View.VISIBLE);
                            categoryName.setText(getResources().
                                    getText(R.string.univ_diag_new_category));
                            addedCategory = categoryName.getText().toString();
                        } else {
                            categoryName.setVisibility(View.GONE);
                            addedCategory = adapterView.getSelectedItem().toString();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;

            case R.layout.fragment_categories:
                addProductLayout.setVisibility(View.GONE);
                listTitle.setText(getResources().
                        getString(R.string.univ_diag_generic_title, Magic.getDate()));
                break;
        }

    }

    /***********************************************************************************************
     * Creates a Map of parent ids and string ids
     * Each parent id corresponds to a parent Activity/Fragment
     * Each string id corresponds to the string needed to be displayed based on the parent
     * Strings are stored in the res/string folder
     */
    @SuppressWarnings("unchecked")
    private static class DataStructures{

        // Map withe key type Integer and Object type Integer
        private static Map <Integer, Integer> dialogStrings;

        static {
            dialogStrings = new HashMap();
            dialogStrings.put(R.id.menu_add_item, R.string.test);
            dialogStrings.put(R.layout.fragment_categories, R.string.univ_diag_add);
        }

        /*******************************************************************************************
         * Get the HashMap
         *
         * @return          HashMap of parent and string ids
         */
        private static Map getDialogStrings(){
            return dialogStrings;
        }

        /*******************************************************************************************
         * Gets the id of the String associated with the id
         *
         * @param id        parent Activity/Fragment id
         * @return          String id of the String to be displayed
         */
        private static int getString(int id){
            return dialogStrings.get(id);
        }

    }

}



