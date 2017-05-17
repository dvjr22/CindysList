package valdes.cindyslist.database;


public class ListProduct {

    private String listName;
    private String category;
    private String product;
    private double price;
    private int picId;
    private String upc;
    private int qty;

    public ListProduct(String listName, String category, String product, double price, int picId,
                       String upc, int qty){

        this.listName = listName;
        this.category = category;
        this.product = product;
        this.price = price;
        this.picId = picId;
        this.upc = upc;
        this.qty = qty;

    }

    public String getListName() {
        return listName;
    }

    public String getCategory() {
        return category;
    }

    public String getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    public int getPicId() {
        return picId;
    }

    public String getUpc() {
        return upc;
    }

    public int getQty() {
        return qty;
    }

    public String toString(){
        return listName + " " + category + " " + product + " " + Double.toString(price) + " " +
                Integer.toString(picId) + " " + upc + " " + Integer.toString(qty);
    }
}
