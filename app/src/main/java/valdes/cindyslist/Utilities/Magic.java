package valdes.cindyslist.Utilities;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/***************************************************************************************************
 *
 */
public class Magic {

    /***********************************************************************************************
     *
     * @return
     */
    public static String getDate(){

        Calendar calendar = Calendar.getInstance();
        Timestamp timeStamp = new Timestamp(calendar.getTime().getTime());
        // Format timestamp to String for TextView display
        return new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US).format(timeStamp);
    }

}
