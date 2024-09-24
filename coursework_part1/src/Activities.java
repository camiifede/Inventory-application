import java.io.Serializable;

/*Creating a class to add activities to the inventory. We're implementing serializable to access those items
 when saved/loaded. */
public class Activities implements Serializable {

    //The attributes of the Activities class.

    int activityID, activityQuantity;
    String activityName, activityDate, activityFunction;
    //Inserting parameters to create different instances and assigning them to the attributes.
    public Activities(int newID, int newQuantity, String newName, String newDate, String newFunction){
        activityID = newID;
        activityQuantity = newQuantity;
        activityName = newName;
        activityDate = newDate;
        activityFunction = newFunction;
    }

    //Accessing Activity's ID.
    public int getID() {
        return activityID;
    }

    //Accessing Activity's quantity.
    public int getQuantity() {
        return activityQuantity;
    }

    //Accessing Activity's name.
    public String getName() {
        return activityName;
    }

    //Accessing Activity's date.
    public String getDate() {
        return activityDate;
    }

    //Accessing Activity's function.
    public String getActivityFunction() { return activityFunction; }
}

