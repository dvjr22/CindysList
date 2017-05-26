/*
This is an experiment

package valdes.cindyslist;

import android.app.Application;
import android.util.Log;

import valdes.cindyslist.database.DatabaseManager;

public class God extends Application {

    private static DatabaseManager databaseManager;

    @Override
    public void onCreate(){

        setUpDatabaseManager();
        Log.i("trace", "Application called");

        super.onCreate();
    }

    private void setUpDatabaseManager(){
        databaseManager = DatabaseManager.get(getApplicationContext());
    }

    public static DatabaseManager getDatabaseManager(){
        return databaseManager;
    }

    public static String tag(){
        return "trace";
    }

}
*/