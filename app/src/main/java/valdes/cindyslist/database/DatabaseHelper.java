package valdes.cindyslist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import valdes.cindyslist.R;

import static valdes.cindyslist.database.DatabaseSchema.*;



/***************************************************************************************************
 * Class that creates and updates SQLite database
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String TAG = "trace";

    private static final int VERSION = 1;
    private static String DATABASE_PATH = "/data/data/valdes.cindyslist/databases/";
    private static String DATABASE_NAME = "cindys_list.db";
    private SQLiteDatabase database;
    private final Context context;


    /***********************************************************************************************
     * Constructor
     *
     * Creates a new database if one doesn't exist
     * If no database exists, onCreate is called to create the database
     * If not the first time, check the version number and call onUpgrade for any required updates
     */
    public DatabaseHelper(Context context){

        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;

        if (!checkDatabase()) {
            try {
                createDatabase();
            } catch (Exception e) {

            }
        }
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDatabase() throws IOException{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {
                copyDatabase();
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("Error copying database");
            }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDatabase(){

        boolean checkDB = false;

        try{
            String myPath = DATABASE_PATH + DATABASE_NAME;
            //checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();

        }catch(SQLiteException e){
            //database does't exist yet.
        }
        return checkDB;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDatabase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DATABASE_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    @Override
    public  synchronized void close(){

        if(database != null)
            database.close();
        super.close();
    }

    /***********************************************************************************************
     * Creates a SQLite database if one doesn't exist
     *
     * @param db        The database to be created
     */
    @Override
    public void onCreate(SQLiteDatabase db){
/*
        // Create products table
        // execSQL(String sql)
        db.execSQL(
                "create table " + Products.NAME + "(" +
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
        db.execSQL(
                "create table " + CreatedLists.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CreatedLists.Attributes.LIST_NAME + ", " +
                CreatedLists.Attributes.DATE_CREATED + ", " +
                CreatedLists.Attributes.NUM_OF_ITEMS + ", " +
                CreatedLists.Attributes.TOTAL_COST + ")"
        );
        // Create lists table
        // execSQL(String sql)
        db.execSQL(
                "create table " + Lists.NAME + "(" +
                " _id integer primary key autoincrement, " +
                Lists.Attributes.LIST_NAME + ", " +
                Lists.Attributes.PRODUCT + ", " +
                Lists.Attributes.QTY + ")"
        );
*/
        // For testing purposes
        // insertProducts(db);
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

    /***********************************************************************************************
     * Inserts testing data into SQLite db
     *
     * @param db        SQLite database that data will receive data
     */
    /*
    private void insertProducts(SQLiteDatabase db){

        ContentValues apple = DatabaseManager.
                setProductValues(new Product("Fruits", "Apples", .99, R.drawable.apples, "123456"));
        ContentValues bannana = DatabaseManager.
                setProductValues(new Product("Fruits", "Bananas", 1.00, R.drawable.banana, "123457"));

        ContentValues carrots = DatabaseManager.
                setProductValues(new Product("Vegetables", "Carrots", 1.99, R.drawable.carrots, "123458"));
        ContentValues garlic = DatabaseManager.
                setProductValues(new Product("Vegetables", "Garlic", 2.99, R.drawable.garlic, "123459"));

        ContentValues ham = DatabaseManager.
                setProductValues(new Product("Deli", "Ham", 6.99, R.drawable.ham, "1234561"));
        ContentValues cheese = DatabaseManager.
                setProductValues(new Product("Deli", "Cheese", 4.99, R.drawable.cheese, "1234562"));

        db.insert(Products.NAME, null, apple);
        db.insert(Products.NAME, null, bannana);
        db.insert(Products.NAME, null, carrots);
        db.insert(Products.NAME, null, garlic);
        db.insert(Products.NAME, null, ham);
        db.insert(Products.NAME, null, cheese);

        CreatedList list1 = new CreatedList("Test List One", 10, 23.52);
        CreatedList list2 = new CreatedList("Test List Two", 15, 50.63);
        CreatedList list3 = new CreatedList("Test List Three", 20, 98.36);

        ContentValues listOne = DatabaseManager.
                setListValues(list1);
        ContentValues listTwo = DatabaseManager.
                setListValues(list2);
        ContentValues listThree = DatabaseManager.
                setListValues(list3);

        db.insert(CreatedLists.NAME, null, listOne);
        db.insert(CreatedLists.NAME, null, listTwo);
        db.insert(CreatedLists.NAME, null, listThree);

        ContentValues product2 = DatabaseManager.setProductsInListValues(list1.getTitle(), "Bananas", 4);
        ContentValues product3 = DatabaseManager.setProductsInListValues(list1.getTitle(), "Ham", 1);
        ContentValues product4 = DatabaseManager.setProductsInListValues(list1.getTitle(), "Carrots", 1);
        ContentValues product1 = DatabaseManager.setProductsInListValues(list1.getTitle(), "Apples", 2);

        db.insert(Lists.NAME, null, product1);
        db.insert(Lists.NAME, null, product2);
        db.insert(Lists.NAME, null, product3);
        db.insert(Lists.NAME, null, product4);

        ContentValues product5 = DatabaseManager.setProductsInListValues(list2.getTitle(), "Apples", 4);
        ContentValues product6 = DatabaseManager.setProductsInListValues(list2.getTitle(), "Cheese", 1);
        ContentValues product7 = DatabaseManager.setProductsInListValues(list2.getTitle(), "Garlic", 3);
        ContentValues product8 = DatabaseManager.setProductsInListValues(list2.getTitle(), "Ham", 1);

        db.insert(Lists.NAME, null, product5);
        db.insert(Lists.NAME, null, product6);
        db.insert(Lists.NAME, null, product7);
        db.insert(Lists.NAME, null, product8);

        ContentValues product9 = DatabaseManager.setProductsInListValues(list3.getTitle(), "Apples", 6);
        ContentValues product10 = DatabaseManager.setProductsInListValues(list3.getTitle(), "Garlic", 2);
        ContentValues product11 = DatabaseManager.setProductsInListValues(list3.getTitle(), "Carrots", 3);
        ContentValues product12 = DatabaseManager.setProductsInListValues(list3.getTitle(), "Cheese", 1);
        ContentValues product13 = DatabaseManager.setProductsInListValues(list3.getTitle(), "Bananas", 4);
        ContentValues product14 = DatabaseManager.setProductsInListValues(list3.getTitle(), "Ham", 1);

        db.insert(Lists.NAME, null, product9);
        db.insert(Lists.NAME, null, product10);
        db.insert(Lists.NAME, null, product11);
        db.insert(Lists.NAME, null, product12);
        db.insert(Lists.NAME, null, product13);
        db.insert(Lists.NAME, null, product14);

    }
*/
}
