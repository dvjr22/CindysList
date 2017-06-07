package valdes.cindyslist;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import valdes.cindyslist.database.DatabaseManager;

public class MyApplication extends Application {


    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("trace", "Application called");
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
