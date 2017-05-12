package valdes.cindyslist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static valdes.cindyslist.database.DatabaseSchema.*;

/***************************************************************************************************
 * Class that creates and updates SQLite database
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "cindys_list.db";


    /***********************************************************************************************
     * Constructor
     *
     * Creates a new database if one doesn't exist
     * If no database exists, onCreate is called to create the database
     * If not the first time, check the version number and call onUpgrade for any required updates
     */
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    /***********************************************************************************************
     * Creates a SQLite database if one doesn't exist
     *
     * @param db        The database to be created
     */
    @Override
    public void onCreate(SQLiteDatabase db){

        // Create products table
        // execSQL(String sql)
        db.execSQL("create table " + Products.NAME + "(" +
                " _id integer primary key autoincrement, " +
                Products.Attributes.CATEGORY + ", " +
                Products.Attributes.PRODUCT + ", " +
                Products.Attributes.PRICE + ", " +
                Products.Attributes.PIC_ID + ", " +
                Products.Attributes.UPC + ", " +
                Products.Attributes.SELECTIONS + ")"
        );

        // Create created_lists table
        // execSQL(String sql)
        db.execSQL("create table " + CreatedLists.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CreatedLists.Attributes.LIST_NAME + ", " +
                CreatedLists.Attributes.DATE_CREATED + ", " +
                CreatedLists.Attributes.NUM_OF_ITEMS + ", " +
                CreatedLists.Attributes.TOTAL_COST + ")"
        );

        // Create lists table
        // execSQL(String sql)
        db.execSQL("create table " + Lists.NAME + "(" +
                " _id integer primary key autoincrement, " +
                Lists.Attributes.LIST_NAME + ", " +
                Lists.Attributes.PRODUCT + ", " +
                Lists.Attributes.PRICE + ", " +
                Lists.Attributes.QTY + ")"
        );
    }

    /***********************************************************************************************
     * Checks versions of SQLite database and performs updates as required
     *
     * @param db            The SQLite database to receive updates
     * @param oldVersion    Previous version of the database
     * @param newVersion    Newest version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    /***********************************************************************************************
     * Checks versions of SQLite databvase and performs downgrades as required
     *
     * @param db            The SQLite database to receive downgrades
     * @param oldVersion    Previous version of the database
     * @param newVersion    Newest version of the database
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    private void insertProducts(){

    }




}
