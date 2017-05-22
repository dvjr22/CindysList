package valdes.cindyslist.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import static valdes.cindyslist.database.DatabaseSchema.*;

/***************************************************************************************************
 * Class to handle query the SQLite database and return results in object form
 *
 */
public class DatabaseCursorWrapper extends CursorWrapper{

    /***********************************************************************************************
     * Constructor
     *
     * Creates an instance of the Cursor
     *
     * @param cursor    An instance of a cursor containing the query to be executed
     */
    public DatabaseCursorWrapper(Cursor cursor){ super(cursor); }


    /***********************************************************************************************
     * Gets all data associated with a product and creates a Product object
     *
     * @return      A Product object from the SQLite database
     */
    public Product getProduct(){

        return new Product(
                getString(getColumnIndex(Products.Attributes.CATEGORY)),
                getString(getColumnIndex(Products.Attributes.PRODUCT)),
                getDouble(getColumnIndex(Products.Attributes.PRICE)),
                getInt(getColumnIndex(Products.Attributes.PIC_ID)),
                getString(getColumnIndex(Products.Attributes.UPC))
        );

    }

    /***********************************************************************************************
     * Gets all data associated with a product and creates a CreatedList object
     *
     * @return      A CreatedList object from the SQLite database
     */
    public CreatedList getList(){

        return new CreatedList(
                getString(getColumnIndex(CreatedLists.Attributes.LIST_NAME)),
                getInt(getColumnIndex(CreatedLists.Attributes.NUM_OF_ITEMS)),
                getDouble(getColumnIndex(CreatedLists.Attributes.TOTAL_COST)),
                getString(getColumnIndex(CreatedLists.Attributes.DATE_CREATED))

        );

    }

    /***********************************************************************************************
     * Gets all data associated with a product in a list and creates a ListProduct object
     *
     * @return      A ListProduct object with values from SQLite database
     */
    public ListProduct getListProduct(){

        return new ListProduct(
                getString(getColumnIndex(Lists.Attributes.LIST_NAME)),
                getString(getColumnIndex(Products.Attributes.CATEGORY)),
                getString(getColumnIndex(Products.Attributes.PRODUCT)),
                getDouble(getColumnIndex(Products.Attributes.PRICE)),
                getInt(getColumnIndex(Products.Attributes.PIC_ID)),
                getString(getColumnIndex(Products.Attributes.UPC)),
                getInt(getColumnIndex(Lists.Attributes.QTY))

        );

    }

    /***********************************************************************************************
     * Gets the category of a product
     *
     * @return      The category of a product
     */
    public String getCategories(){

        return getString(getColumnIndex(Products.Attributes.CATEGORY));

    }

}
