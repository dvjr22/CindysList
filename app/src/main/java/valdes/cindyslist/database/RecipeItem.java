package valdes.cindyslist.database;

/***************************************************************************************************
 * Object Class that defines an instance of a product belonging to a recipe
 */
public class RecipeItem {

    String recipeName;
    String product;
    double measurement;
    String unit;

    /***********************************************************************************************
     * Constructor
     * Creates an instance of an item belonging to a recipe
     *
     * @param recipeName        The title of the recipe
     * @param product           The item in the recipe
     * @param measurement       The amount of the item needed in the recipe
     * @param unit              The type of measurement
     */
    public RecipeItem(String recipeName, String product, double measurement, String unit){

        this.recipeName = recipeName;
        this.product = product;
        this.measurement = measurement;
        this.unit = unit;
    }

    /***********************************************************************************************
     * Get the name of the recipe the item belongs to
     *
     * @return      The recipe name
     */
    public String getRecipeName() {
        return recipeName;
    }

    /***********************************************************************************************
     * Set the recipe name
     *
     * @param recipeName        The name of the recipe
     */
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    /***********************************************************************************************
     * Get the name of the product
     *
     * @return      The name of the product
     */
    public String getProduct() {
        return product;
    }

    /***********************************************************************************************
     * Set the product name
     *
     * @param product       The name of the product
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /***********************************************************************************************
     * Get the amount of a product in a recipe
     *
     * @return      The amount needed in the recipe
     */
    public double getMeasurement() {
        return measurement;
    }

    /***********************************************************************************************
     * Set the measurement
     *
     * @param measurement       The amount in the recipe
     */
    public void setMeasurement(double measurement) {
        this.measurement = measurement;
    }

    /***********************************************************************************************
     * Get the measurement unit of the product
     *
     * @return      The type of measurement
     */
    public String getUnit() {
        return unit;
    }

    /***********************************************************************************************
     * Set the unit of measure
     *
     * @param unit      The unit of measurement
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

}
