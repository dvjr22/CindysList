package valdes.cindyslist.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import static valdes.cindyslist.database.DatabaseSchema.*;

/***************************************************************************************************
 * Class to delegate calls to Cursor
 * Used to retrieve data and reconstruct objects
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
     * @return      A product object from the SQLite database
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

}
