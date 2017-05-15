package valdes.cindyslist.database;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/***************************************************************************************************
 * Object Class that creates an instance of a CreatedList
 * A CreatedList is not a list, it is a label for a list of products that have been created
 * CreatedLists are used to populate the CreatedListRecyclerView
 */
public class CreatedList {

    private String title;
    private String date;
    private int items;
    private double cost;

    /***********************************************************************************************
     * Constructor
     *
     * Creates an instance of a list
     * Used in the initial creation of a list by a user
     *
     * @param title     Title of the list
     * @param items     Number of items in the list
     * @param cost      Total cost of the list
     */
    public CreatedList(String title, int items, double cost){

        this.title = title;
        this.items = items;
        this.cost = cost;

        // Create a timestamp at list creation
        Calendar calendar = Calendar.getInstance();
        Timestamp timeStamp = new Timestamp(calendar.getTime().getTime());
        // Format timestamp to String for TextView display
        date = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US).format(timeStamp);


    }

    /***********************************************************************************************
     * Constructor
     *
     * Creates an instance of a list
     * Used in the reconstruction of a List object from the SQLite database
     *
     * @param title     Title of the list
     * @param items     Number of items in the list
     * @param cost      Total cost of the list
     * @param date      Date the list was created/updated
     */
    public CreatedList(String title, int items, double cost, String date){

        this.title = title;
        this.items = items;
        this.cost = cost;
        this.date = date;

    }

    /***********************************************************************************************
     * Get the list's title
     *
     * @return      The title of the list
     */
    public String getTitle() { return title; }

    /***********************************************************************************************
     * Set the list's title
     *
     * @param title     The title of the list
     */
    public void setTitle(String title) { this.title = title; }

    /***********************************************************************************************
     * Get the date the list was created
     *
     * @return      Date the list was created
     */
    public String getDate() { return date; }

    /***********************************************************************************************
     * Set the date the list was created/updated
     * Used when the user creates/updates the list
     */
    public void setDate() {

        Calendar calendar = Calendar.getInstance();
        Timestamp timeStamp = new Timestamp(calendar.getTime().getTime());
        this.date = timeStamp.toString();

    }

    /***********************************************************************************************
     * Set the date the list was created/updated
     * Used when reconstructing List object by SQLite database
     *
     * @param date      The date the list was created/updated
     */
    public void setDate(String date) { this.date = date; }

    /***********************************************************************************************
     * Get the number of items in the list
     *
     * @return      Number of items in the list
     */
    public int getItems() { return items; }

    /***********************************************************************************************
     * Set the number of items in the list
     *
     * @param items     The number of items in the list
     */
    public void setItems(int items) { this.items = items; }

    /***********************************************************************************************
     * Get the total cost of the items in the list
     *
     * @return      Total cost of the list
     */
    public double getCost() { return cost; }

    /***********************************************************************************************
     * Set the cost of the list
     *
     * @param cost      Total cost of the list
     */
    public void setCost(double cost) { this.cost = cost;}

}
