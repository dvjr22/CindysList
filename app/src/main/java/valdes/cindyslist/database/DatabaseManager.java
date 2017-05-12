package valdes.cindyslist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

}
