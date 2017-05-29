package valdes.cindyslist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static valdes.cindyslist.database.DatabaseSchema.*;

/***************************************************************************************************
 * Class that manages the SQLite database
 * DatabaseManager is a singleton, there can only be one instance within the application
 */
public class DatabaseManager {

    private static final String TAG = "trace";

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
     * Gets the results of a query with a cursor over the result
     *
     * @param distinct          Filter out duplicates
     * @param tableName         Name of the table to be queried
     * @param columns           The columns to be selected
     *                          Can be null
     * @param whereClause       Where clause
     *                          Can be null
     * @param whereArgs         The arguments for the where clause
     *                          Can be null
     * @param groupBy           How results should be grouped
     *                          Can be null
     * @return                  An instance of the cursor over the results of the query
     */
    private DatabaseCursorWrapper queryDatabase(boolean distinct, String tableName, String[] columns,
                                                String whereClause, String[] whereArgs,
                                                String groupBy){

        // query(boolean distinct, String table, String[] columns, String selection,
        // String[] selectionArgs, String groupBy, String having, String orderBy)
        // Cursor is closed in the calling method
        Cursor cursor = database.query(
                distinct,
                tableName,
                columns,
                whereClause,
                whereArgs,
                groupBy,
                null, // having
                null, // order by
                null // limit
        );
        return new DatabaseCursorWrapper(cursor);
    }

    /***********************************************************************************************
     * Gets the results of a query with a cursor over the result
     *
     * @param query         The SQLite query
     * @param whereArgs     Arguements for where clause
     * @return              An instance of the cursor over the results of the query
     */
    private DatabaseCursorWrapper rawQuery(String query, String[] whereArgs){

        // rawQuery(String query, String[] selectionArgs)
        // Cursor is closed in the calling method
        Cursor cursor = database.rawQuery(
                query,
                whereArgs
        );
        return new DatabaseCursorWrapper(cursor);
    }

    /***********************************************************************************************
     * Sets the values of a Product to ContentValues to be inserted into the SQLite database
     *
     * @param product       Product to be inserted into the SQLite database
     * @return              The values to be inserted in a ContentValues object
     */
    private static ContentValues setProductValues(Product product){

        ContentValues values = new ContentValues();

        // Place Product values into ContentValues object
        values.put(Products.Attributes.CATEGORY, product.getCategory());
        values.put(Products.Attributes.PRODUCT, product.getProductName());
        values.put(Products.Attributes.PRICE, product.getPrice());
        values.put(Products.Attributes.PIC_ID, product.getPicId());
        values.put(Products.Attributes.UPC, product.getUpc());

        return values;
    }

    /***********************************************************************************************
     * Sets the values of a CreatedList to ContentValues to be inserted into the SQLite database
     *
     * @param createdList       CreatedList to be inserted into the SQLite database
     * @return                  The values to be inserted in a ContentValues object
     */
    private static ContentValues setListValues(CreatedList createdList){

        ContentValues values = new ContentValues();

        // Place CreatedList values into ContentValues object
        values.put(CreatedLists.Attributes.LIST_NAME, createdList.getTitle());
        values.put(CreatedLists.Attributes.DATE_CREATED, createdList.getDate());
        values.put(CreatedLists.Attributes.NUM_OF_ITEMS, createdList.getItems());
        values.put(CreatedLists.Attributes.TOTAL_COST, createdList.getCost());

        return values;
    }

    /***********************************************************************************************
     * Sets the values of the products that have been placed in a CreatedList to be inserted into
     * SQLite database
     *
     * @param listName      The title of the list
     * @param product       The product that has been selected for the list
     * @param qty           The quantity of this item in the list
     * @return              The values to be inserted in a ContentValues object
     */
    private static ContentValues setProductsInListValues(String listName, String product, int qty){

        ContentValues values = new ContentValues();

        // Place values into ContentValues object
        values.put(Lists.Attributes.LIST_NAME, listName);
        values.put(Lists.Attributes.PRODUCT, product);
        values.put(Lists.Attributes.QTY, qty);

        return values;
    }

    /***********************************************************************************************
     * Get all the created lists from the SQLite database
     *
     * @return      A list of all the CreatedLists
     */
    public List<CreatedList> getCreatedLists(){

        List<CreatedList> createdLists = new ArrayList<>();
        // Cursor to go over results of the query
        // select * from created_lists;
        DatabaseCursorWrapper cursor = queryDatabase(false, CreatedLists.NAME, null, null, null, null);

        try{
            // Move to the first returned result
            cursor.moveToFirst();
            // Continue until all results have been read
            while(!cursor.isAfterLast()){
                // Add results to createdLists
                createdLists.add(cursor.getList());
                // Move to the next result
                cursor.moveToNext();
            }
        } finally {
            // Close cursor once all results have been retrieved
            cursor.close();
        }
        return createdLists;
    }

    /***********************************************************************************************
     * Get all the items from a specific list
     *
     * @param listName      The name of the list
     * @return              A list of ListProducts that belong to the list
     */
    public List<ListProduct> getListProducts(String listName){

        List<ListProduct> listProducts = new ArrayList<>();

        String tables = Lists.NAME + " a inner join " + Products.NAME + " b on a." +
                Lists.Attributes.PRODUCT + " = b." + Products.Attributes.PRODUCT;

        // Cursor to go over results of the query
        // select distinct list_name, category, a.product, price, pic_id, upc, qty
        // from list_name a inner join products b on a.product = b.product
        // where list_name = listName
        DatabaseCursorWrapper cursor = queryDatabase(true,
                tables,
                //Lists.NAME + ", " + Products.NAME,
                new String[]  {
                        Lists.Attributes.LIST_NAME,
                        Products.Attributes.CATEGORY,
                        "a." + Products.Attributes.PRODUCT,
                        Products.Attributes.PRICE,
                        Products.Attributes.PIC_ID,
                        Products.Attributes.UPC,
                        Lists.Attributes.QTY
                },
                Lists.Attributes.LIST_NAME + " = ?",
                new String[] { listName },
                null);

        try {
            // Move to the first returned result
            cursor.moveToFirst();
            // Continue until all results have been read
            while (!cursor.isAfterLast()){
                // Add results to listProducts
                listProducts.add(cursor.getListProduct());
                // Move to next result
                cursor.moveToNext();
            }
        } finally {
            // Close cursor
            cursor.close();
        }
        return listProducts;
    }

    /***********************************************************************************************
     * Get all the categories
     *
     * @return      A List of the categories
     */
    public ArrayList<String> getCategories(){

        ArrayList<String> categories = new ArrayList<>();

        // Cursor to go over results of the query
        // select distinct category from products;
        DatabaseCursorWrapper cursor = queryDatabase(true,
                Products.NAME,
                new String[] { Products.Attributes.CATEGORY },
                null,
                null,
                null);

        try{
            // Move to the first returned result
            cursor.moveToFirst();
            // Continue until all results have been read
            while(!cursor.isAfterLast()){
                // Add results to categories
                categories.add(cursor.getCategories());
                // Move to next result
                cursor.moveToNext();
            }
        } finally {
            // Close cursor
            cursor.close();
        }
        return categories;
    }

    /***********************************************************************************************
     * Get all the products that belong to the category
     *
     * @param category      The category of products
     * @return              A list of the products within the category
     */
    public List<Product> getProducts(String category){

        List<Product> products = new ArrayList<>();

        // Cursor to go over results of the query
        // select * from products where category = category;
        DatabaseCursorWrapper cursor = queryDatabase(false,
                Products.NAME,
                null,
                Products.Attributes.CATEGORY + " = ?",
                new String[] { category },
                null);

        try{
            // Move to the first returned result
            cursor.moveToFirst();
            // Continue until all results have been read
            while(!cursor.isAfterLast()){
                // Add results to categories
                products.add(cursor.getProduct());
                // Move to next result
                cursor.moveToNext();
            }
        } finally {
            // Close cursor
            cursor.close();
        }
        return products;
    }

    /***********************************************************************************************
     * Get all the products in a category that have not been added to a list
     *
     * @param category      The category of products
     * @param listName      The list where products are being added
     * @return              A List of Products that have not been added to the list
     */
    public List<Product> getProducts(String category, String listName){

        List<Product> products = new ArrayList<>();

        // select * from products where category = 'category' and product not in
        // (select product from lists where list = 'listName');
        String query =
                "select * from " + Products.NAME + " where " + Products.Attributes.CATEGORY +
                        " = '" + category + "' and " + Products.Attributes.PRODUCT +
                        " not in (select " + Lists.Attributes.PRODUCT + " from " + Lists.NAME +
                        " where " + Lists.Attributes.LIST_NAME + " = '" + listName + "');";

        // Cursor to go over results
        DatabaseCursorWrapper cursor = rawQuery(query, null);

        try{
            // Move to the first returned result
            cursor.moveToFirst();
            // Continue until all results have been read
            while(!cursor.isAfterLast()){
                // Add results to products
                products.add(cursor.getProduct());
                // Move to next result
                cursor.moveToNext();
            }
        } finally {
            // Close cursor
            cursor.close();
        }
        return products;
    }

    /***********************************************************************************************
     * Insert a CreatedList into SQLite database
     *
     * @param createdList       The list to be placed in the database
     */
    public void insertList(CreatedList createdList){

        database.insert(CreatedLists.NAME ,null, setListValues(createdList));
    }

    /***********************************************************************************************
     * Insert a Product into SQLite database
     *
     * @param product       The product to be placed in the database
     */
    public void insertProduct(Product product){

        database.insert(Products.NAME, null, setProductValues(product));
    }

    /***********************************************************************************************
     * Insert a ListProduct into SQLite database
     *
     * @param listName      The list the product belongs to
     * @param product       The name of the product that is in the list
     * @param qty           The number of the product in the list
     */
    public void insertCreatedListItem(String listName, String product, int qty){

        database.insert(Lists.NAME, null, setProductsInListValues(listName, product, qty));
    }

    /***********************************************************************************************
     * Updates the total number of items in the selected list
     *
     * @param listName      The list to have number of items updated
     */
    public int updateListItemTotal(String listName){

        int count = 0;
        // Cursor to go over results of the query
        // select count(*) from list_name where list_name = listName
        DatabaseCursorWrapper cursor = queryDatabase(
                false,
                //tables,
                Lists.NAME,
                new String[] { COUNT },
                //new String[]  { "count(*)" },
                Lists.Attributes.LIST_NAME + " = ?",
                new String[] { listName },
                null);

        // Retrieve results
        cursor.moveToFirst();
        count = cursor.getInt(0);
        cursor.close();

        // Set ContentValue to update item_count
        ContentValues values = new ContentValues();
        values.put(CreatedLists.Attributes.NUM_OF_ITEMS, count);

        // Update database
        database.update(
                CreatedLists.NAME,
                values,
                CreatedLists.Attributes.LIST_NAME + " = ?",
                new String[] { listName });

        return count;
    }

    /***********************************************************************************************
     * Updates the total sum of a list
     *
     * @param listName      The list to have the sum updated
     */
    public double updateListSum(String listName){

        String tables = Lists.NAME + " a inner join " + Products.NAME + " b on a." +
                Lists.Attributes.PRODUCT + " = b." + Products.Attributes.PRODUCT;

        // select sum(price)
        // from list_name a inner join products b on a.product = b.product
        // where list_name = listName
        DatabaseCursorWrapper cursor = queryDatabase(false,
                tables,
                new String[]  { "sum(" + Products.Attributes.PRICE + ")" },
                Lists.Attributes.LIST_NAME + " = ?",
                new String[] { listName },
                null);

        // Move to the first returned result
        cursor.moveToFirst();
        double sum = cursor.getDouble(0);
        cursor.close();

        // Set ContentValue to update cost
        ContentValues values = new ContentValues();
        values.put(CreatedLists.Attributes.TOTAL_COST, sum);

        // Update database
        database.update(
                CreatedLists.NAME,
                values,
                CreatedLists.Attributes.LIST_NAME + " = ?",
                new String[] { listName });

        return sum;
    }


    /***********************************************************************************************
     * Delete the list from the SQLite database
     *
     * @param listName      The CreatedList to be deleted
     */
    public void deleteList(String listName){

        // delete(String table, String whereClause, String[] whereArgs)
        // delete from created_lists where list_name = listName;
        database.delete(CreatedLists.NAME,
                CreatedLists.Attributes.LIST_NAME + " = ?",
                new String[] { listName });

        // delete from lists where list_name = listName;
        database.delete(Lists.NAME,
                Lists.Attributes.LIST_NAME + " = ?",
                new String[] { listName });
    }

}

/*

// One way to update the list count
public void addOneToList(String listName){

        CreatedList createdList = new CreatedList(listName);

        // Cursor to go over results of the query
        // select * from created_lists where list_name = listName;
        DatabaseCursorWrapper cursor = queryDatabase(
                false,
                CreatedLists.NAME,
                null,
                CreatedLists.Attributes.LIST_NAME + " = ?",
                new String[] { listName },
                null);

        // Move to the first returned result, there should only be one
        cursor.moveToFirst();
        createdList = cursor.getList();
        // Close cursor
        cursor.close();

        createdList.setItems(createdList.getItems() + 1);

        database.update(
                CreatedLists.NAME,
                setListValues(createdList),
                CreatedLists.Attributes.LIST_NAME + " = ?",
                new String[] { listName });
    }

 */