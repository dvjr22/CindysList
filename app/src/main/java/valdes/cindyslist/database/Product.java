package valdes.cindyslist.database;

/***************************************************************************************************
 * Object Class that defines an instance of a product
 * A product is any item that can be purchased at a store
 */
public class Product {

    private String category;
    private String productName;
    private double price;
    private int picId;
    private String upc;


    /***********************************************************************************************
     * Constructor
     *
     * Creates one instance of a product
     *
     * @param category          Category that product falls under
     * @param productName      Name of the product
     * @param price             Cost of the product
     * @param picId            R.id of the pic to be used in view displays
     * @param upc               UPC of the product
     */
    public Product(String category, String productName, double price, int picId, String upc){

        this.category = category;
        this.productName = productName;
        // This info below can be pulled from the server at a later date to increase functunality
        this.price = price;
        this.picId = picId;
        this.upc = upc;

    }

    /***********************************************************************************************
     * Gets the product's category
     *
     * @return      The category of the product
     */
    public String getCategory() { return category; }

    /***********************************************************************************************
     * Sets the category of the product
     *
     * @param category      The type of category to which the product belongs
     */
    public void setCategory(String category) { this.category = category; }

    /***********************************************************************************************
     * Gets the product's name
     *
     * @return      Name of the product
     */
    public String getProductName() { return productName; }

    /***********************************************************************************************
     * Sets the product's name
     *
     * @param productName      The name of the product
     */
    public void setProductName(String productName) { this.productName = productName; }

    /***********************************************************************************************
     * Gets the product's price
     *
     * @return      Price of the product
     */
    public double getPrice() { return price; }

    /***********************************************************************************************
     * Sets the product's price
     *
     * @param price     Price of the product
     */
    public void setPrice(float price) { this.price = price; }

    /***********************************************************************************************
     * Get the R.id of the picture to be used in the views
     *
     * @return      R.id associated with the product's picture
     */
    public int getPicId() { return picId; }

    /***********************************************************************************************
     * Set the id of the pic
     *
     * @param picId     id associated with the product's picture
     */
    // TODO: 5/12/17 Figure out how to assign outside pics to products - DVJ
    public void setPicId(int picId) { this.picId = picId; }

    /***********************************************************************************************
     * Get the product's UPC
     *
     * @return      UPC of the product
     */
    public String getUpc() { return upc; }

    /***********************************************************************************************
     * Set the product's UPC
     *
     * @param upc       UPC of the product
     */
    public void setUpc(String upc) { this.upc = upc; }

}
