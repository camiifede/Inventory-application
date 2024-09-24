import java.io.Serializable;

/*Creating a class to add products to the inventory. We're implementing serializable to access those items
 when saved/loaded. */
public class Items implements Serializable {

    // The attributes of the Items class.
    int productID;
    String productName;
    String productCategory;
    String productDate;
    int productQuantity;

    //Inserting parameters to create different instances and assigning them to the attributes.
    public Items(int ID, String name, String category, String date, int quantity)
    {
        productID = ID;
        productName = name;
        productCategory = category;
        productDate = date;
        productQuantity = quantity;
    }

    //Accessing Item's ID.
    public int getID()
    {
        return productID;
    }

    //Accessing Item's name.
    public String getName()
    {
        return productName;
    }

    //Accessing Item's category.
    public String getCategory()
    {
        return productCategory;
    }

    //Accessing Item's date.
    public String getDate()
    {
        return productDate;
    }

    //Accessing Item's quantity.
    public int getQuantity()
    {
        return productQuantity;
    }

    //Modifying the quantity of an item through user input.
    public void itemSold(int y)
    {
        productQuantity -= y;

    }
    //Modifying the quantity of an item through user input.
    public void addQuantity(int y) {
        productQuantity += y;
    }
}
