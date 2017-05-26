package valdes.cindyslist.database;


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

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "database";

    private static final int VERSION = 1;
    private static String DB_PATH = "/data/data/valdes.cindyslist/databases/";
    private static String DB_NAME = "cindys_list.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;


    /**
     * Constructor
     *
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */

    public DBHelper(Context context) {

        super(context, DB_NAME, null, VERSION);
        this.myContext = context;

        boolean dbexist = checkDataBase();
        if (!dbexist) {
            try {
                createdatabase();
            } catch (Exception e) {
                Log.i(TAG, ("DBHelper catch else") );
            }
        }

        /*
        boolean dbexist = checkDataBase();
        if (dbexist) {
            try {
                openDatabBase();
            } catch (Exception e) {
                Log.i(TAG, ("DBHelper catch open") );
            }


        } else {
            try {
                createdatabase();
            } catch (Exception e) {
                Log.i(TAG, ("DBHelper catch else") );
            }


        }
*/
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createdatabase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                e.printStackTrace();

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        boolean checkDB = false;

        try{
            String myPath = DB_PATH + DB_NAME;
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
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDatabBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public  synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();
        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }


}
