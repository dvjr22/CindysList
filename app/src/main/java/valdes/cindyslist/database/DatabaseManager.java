package valdes.cindyslist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static valdes.cindyslist.database.DatabaseSchema.*;

/***************************************************************************************************
 * Class that manages the SQLite database
 * DatabaseManager is a singleton, there can only be one instance within the application
 */
public class DatabaseManager {

    private static DatabaseManager databaseManager;
    private SQLiteDatabase database;

    /***********************************************************************************************
     * Gets the current instance of the DatabaseManager if one exists
     * If one doesn't exist, DatabaseManager is called to create one
     *
     * @param context       The context of the application
     * @return              An instance of the databaseManager
     */
    public static DatabaseManager get(Context context){

        if(databaseManager == null){
            databaseManager = new DatabaseManager(context);
        }
        return databaseManager;

    }

    /***********************************************************************************************
     * Creates a connection to the SQLite database
     *
     * @param context       The context of the application
     */
    private DatabaseManager(Context context){

        context = context.getApplicationContext();
        database = new DatabaseHelper(context).getWritableDatabase();

    }

    /***********************************************************************************************
     * Sets up a Cursor to query the SQLite database
     *
     * @param tableName         Name of the table to be queried
     * @param columns           The columns to be selected
     *                          Can be null
     * @param whereClause       Where clause
     *                          Can be null
     * @param whereArgs         The arguments for the where clause
     *                          Can be null
     * @param groupBy           How results should be grouped
     *                          Can be null
     * @return                  An instance of the cursor with the query
     */
    private DatabaseCursorWrapper queryDatabase(String tableName, String[] columns,
                                                String whereClause, String[] whereArgs,
                                                String groupBy){

        // query(String table, String[] columns, String selection, String[] selectionArgs,
        //      String groupBy, String having, String orderBy)
        // Cursor is closed in the calling method
        Cursor cursor = database.query(
                tableName,
                columns,
                whereClause,
                whereArgs,
                groupBy,
                null, // having
                null // order by
        );

        return new DatabaseCursorWrapper(cursor);

    }

    /***********************************************************************************************
     * Sets the values of a Product to ContentValues to be inserted into the SQLite database
     *
     * @param product       Product to be inserted into the SQLite database
     * @return              The values to be inserted in a ContentValues object
     */
    // TODO: 5/12/17 return to private after testing - DVJ
    public static ContentValues setProductValues(Product product){

        ContentValues values = new ContentValues();

        values.put(Products.Attributes.CATEGORY, product.getCategory());
        values.put(Products.Attributes.PRODUCT, product.getProductName());
        values.put(Products.Attributes.PRICE, product.getPrice());
        values.put(Products.Attributes.PIC_ID, product.getPicId());
        values.put(Products.Attributes.UPC, product.getUpc());

        return values;

    }



}
