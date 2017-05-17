package valdes.cindyslist.database;

/***************************************************************************************************
 * Class to keep track of SQLite db schema
 */
public class DatabaseSchema {

    /***********************************************************************************************
     * Class for the products table
     */
    public static final class Products{

        // Name of the table
        public static final String NAME = "products";

        // Titles of the attributes
        public static final class Attributes{

            public static final String CATEGORY = "category";
            public static final String PRODUCT = "product";
            public static final String PRICE = "price";
            public static final String PIC_ID = "pic_id";
            public static final String UPC = "upc";
            public static final String SELECTIONS = "selections";

        }
    }

    /***********************************************************************************************
     * Class for the created_lists table
     */
    public static final class CreatedLists{

        // Name of the table
        public static final String NAME = "created_lists";

        // Titles of the attributes
        public static final class Attributes{

            public static final String LIST_NAME = "list_name";
            public static final String DATE_CREATED = "date";
            public static final String NUM_OF_ITEMS = "items";
            public static final String TOTAL_COST = "cost";

        }
    }

    /***********************************************************************************************
     * Class for the lists table
     */
    public static final class Lists{

        // Name of the table
        public static final String NAME = "lists";

        // Titles of the attributes
        public static final class Attributes{

            public static final String LIST_NAME = "list_name";
            public static final String PRODUCT = "product";
            public static final String QTY = "qty";

        }
    }

}
