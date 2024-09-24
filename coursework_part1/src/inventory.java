import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.Color;
import java.io.*;
import java.util.List;

public class inventory {

    //Defining attributes that will be assigned to the objects that the user will create.
    public static int newID;
    public static String newName, newCategory, newDate;
    public static int newQuantity;
    //Defining attributes that will be assigned to the activities that the user will initiate.
    public static int activityID, activityQuantity;
    public static String activityName, activityDate, activityFunction;

    public static DefaultTableModel tableLayout;
    public static JTable table;


    //Creating a new array list that will later be populated with elements given by the user.
    public static ArrayList<Items> myList = new ArrayList<>();
    //Creating a new array list that will register activities on the products.
    public static ArrayList<Activities> activities = new ArrayList<>();
    //Creating a new panel.
    public static JPanel buttonPanel = new JPanel(null);
    public static JTextArea text;

    private static final String DATA_FILE_PATH = "path/to/Inventory_data.ser";


    public static void main(String[] args) {
        JFrame frame = new JFrame("Inventory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(150, 15, 800, 800);

        buttonPanel.setBounds(0, 0, 200, 800);
        buttonPanel.setBackground(new Color(0xFFFFFF));

        // Create a custom title panel
        frame.add(createTitlePanel());

        // Create a JTextArea
        text = new JTextArea();
        text.setBackground(new Color(16, 15, 15, 255));
        text.setForeground(Color.white);
        text.setEditable(false);
        //Adding the possibility to scroll through the text area.
        JScrollPane scrollPaneText = new JScrollPane(text);
        scrollPaneText.setBounds(206, 28, 582, 412);
        frame.add(scrollPaneText);

        /*A call to createButton function is made. We are here creating all defining all the buttons that are needed in
        the GUI.*/
        createButton("Add Product", 50);
        createButton("Remove Product", 120);
        createButton("Add Quantity", 190);
        createButton("Display Inventory", 260);
        createButton("Display Activities", 330);
        createButton("Display Category", 400);
        createButton("Delete Product", 470);
        createButton("Vendor Report", 540);
        createButton("Save Data", 610);
        createButton("Load Data", 680);

        //Creating a new table for the GUI with its columns name.
        tableLayout = new DefaultTableModel(new String[]{"Activity ID", "Activity Quantity",
                "Activity Name", "Activity Category", "Activity Date"}, 0);
        table = new JTable(tableLayout);
        table.setBackground(new Color(0xD2D2D2));
        //Adding the possibility to scroll through the table.
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(206, 440, 580, 320);
        //Adding: scroll pane, button panel to the frame and setting it to visible.
        frame.add(scrollPane);
        frame.add(buttonPanel);
        frame.setVisible(true);

    }

    //Creating a function to add a title panel to the frame.
    private static JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setBounds(206, 0, 580, 30);
        panel.setBackground(new Color(0x100F0F));

        Font titleFont = new Font("Times New Roman", Font.BOLD, 18);

        JLabel titleLabel = new JLabel("Inventory");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(0xF4FFFFFF, true));

        panel.add(titleLabel);

        return panel;
    }

    /*Creating a method to add buttons to the frame. Each button will display a different function when it is
    activated through a lambda function. */
    private static void createButton(String name, int y) {
        Font font = new Font("Times new Roman", Font.BOLD, 14);
        JButton x = new JButton(name);
        //Customising the buttons.
        x.setFont(font);
        x.setForeground(Color.black);
        x.setFocusPainted(false);
        x.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        x.setBackground(new Color(0xE7964F));
        x.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                x.setBackground(new Color(0xA2A4A2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                x.setBackground(new Color(0xE7964F));
            }
        });
        x.setBounds(20, y, 170, 30);
        //Action listeners are used to output a method whenever a button is pressed.
        x.addActionListener(e -> {
            switch (name) {
                case "Add Product" -> AddToStock();
                case "Remove Product" -> RemoveFromStock();
                case "Add Quantity" -> addQuantity();
                case "Display Inventory" -> displayInventory();
                case "Display Activities" -> displayActivities();
                case "Display Category" -> displayCategory();
                case "Delete Product" -> deleteProduct();
                case "Vendor Report" -> vendorReport();
                case "Save Data" -> saveData();
                case "Load Data" -> loadData();
            }
        });
        //Adding the button to the panel.
        buttonPanel.add(x);
    }

    /*Creating a function to add new products to the inventory. If the same product has been entered twice
    an error message will be displayed. The activity will also be recorded. */
    public static void AddToStock() {
        //User inputs
        JTextField productID = new JTextField();
        JTextField productName = new JTextField();
        JTextField productCategory = new JTextField();
        JTextField productDate = new JTextField();
        JTextField productQuantity = new JTextField();
        //Creating a new Item after the user has inputted its variables.
        Object[] display = {
                "Product ID: ", productID,
                "Product name: ", productName,
                "Product Category: ", productCategory,
                "Product Date: ", productDate,
                "Product Quantity: ", productQuantity
        };

        int info = JOptionPane.showConfirmDialog(null, display, "Please enter product's " +
                "information.", JOptionPane.OK_CANCEL_OPTION);

        //Receiving user inputs and checking for errors through try and catch functions.
        if (info == JOptionPane.OK_OPTION) {
            try{
                newID = Integer.parseInt(productID.getText());
                newQuantity = Integer.parseInt(productQuantity.getText());
                if(newID < 0 || newQuantity < 0){
                    JOptionPane.showMessageDialog(null, "The numbers entered must be greater " +
                            "than 0.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Invalid input, please make sure " +
                        "that only integers have been entered.", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try{
                newName = productName.getText();
                newCategory = productCategory.getText();
                newDate = productDate.getText();
            }
            catch(IllegalArgumentException e){
                JOptionPane.showMessageDialog(null, "Invalid input, make sure to " +
                        "only enter a string.", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean isDuplicate = false;

            /*The for loop will check if there are doubles within the inventory and if so an error
            message will be displayed. If a duplicate is not found a new item will be created instead. */
            for (Items i : myList) {
                if (i.getID() == newID) {
                    isDuplicate = true;
                    break;
                }
            }
            // ERROR message panel when a product is entered twice.
            if (isDuplicate) {
                JOptionPane.showMessageDialog(null, "This item has already been entered!",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                /* After accessing the "Items" class, we create a new object that will store the inputs earlier
                entered by the user. These will be the new object's attributes. */
                Items item = new Items(newID, newName, newCategory, newDate, newQuantity);

                activityID = item.getID();
                activityQuantity = item.getQuantity();
                activityName = item.getName();
                activityDate = item.getDate();
                activityFunction = "Add Product";

                Activities activity = new Activities(activityID, activityQuantity, activityName, activityDate,
                        activityFunction);
                activities.add(activity);

                Object[] newProduct = {newID, newQuantity, newName, newCategory, newDate};
                tableLayout.addRow(newProduct);
                table.repaint();

                myList.add(item);

                JOptionPane.showMessageDialog(null, "The product has been added to the " +
                        "inventory.", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /*This function enables the user to search for an item within the inventory and update its quantity.
    If the item is found the program will ask the user to enter the quantity of units sold.
    From here a call to the Items class' methods is made (itemSold(int y)) and the object's quantity attribute
    will be updated.
    */
    public static void RemoveFromStock() {
        JTextField productID = new JTextField();
        JTextField productQuantity = new JTextField();
        Object[] display = {
                "Product ID: ", productID,
                "Quantity to Remove:", productQuantity
        };
        int info = JOptionPane.showConfirmDialog(null, display, "Please enter the product's " +
                "information.", JOptionPane.OK_CANCEL_OPTION);

        boolean isDuplicate = false;

        if (info == JOptionPane.OK_OPTION) {
            try{
                newID = Integer.parseInt(productID.getText());
                newQuantity = Integer.parseInt(productQuantity.getText());
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Invalid input, please make sure " +
                        "that only integers have been entered.", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        for (Items i : myList) {
            if (i.getID() == newID) {
                isDuplicate = true;
                i.itemSold(newQuantity);
                activityID = i.getID();
                activityQuantity = newQuantity;
                activityName = i.getName();
                activityDate = i.getDate();
                activityFunction = "Remove Quantity";
                Activities newActivity = new Activities(activityID, activityQuantity, activityName,
                        activityDate, activityFunction);
                activities.add(newActivity);

                int index = myList.size() - 1;
                boolean found = false;

                for (int j = 0; j < tableLayout.getRowCount(); j++) {
                    if ((int) tableLayout.getValueAt(j, 0) == newID) {
                        index = j;
                        found = true;
                        break;
                    }
                }
                //Updating the table whenever an item's quantity has been changed.
                if (found) {
                    tableLayout.setValueAt(i.getQuantity(), index, 1);
                }

                table.repaint();

                JOptionPane.showMessageDialog(null, "The total product quantity is now: " +
                        i.getQuantity(), "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                //When updated, if the quantity is less or equal to 0 the item will be removed from the inventory.
                if (i.getQuantity() <= 0) {
                    myList.remove(i);
                    tableLayout.removeRow(index);
                    break;
                }
            }
        }
        if (!isDuplicate) {
            JOptionPane.showMessageDialog(null, "Product not found!",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Creating a function that will display the current inventory.
    public static void displayInventory() {
        text.append("""

                  Activity ID\tActivity Quantity\t   Activity Name\t   Activity Category   Activity Date
                """);
        if (myList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The inventory is empty!", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
        for (Items i : myList) {
            text.append("  " + i.getID() + "\t" + i.getQuantity() + "\t   " +
                    i.getName() + "\t   " + i.getCategory() + "\t       " + i.getDate() + "\n");
        }
    }

    //Creating a function that will display the last four activities for each product.
    public static void displayActivities() {
        ArrayList<Activities> lastFour = new ArrayList<>();
        JTextField productID = new JTextField();
        Object[] display = {
                "Product ID: ", productID
        };
        //Variable used to check existing product ID.
        boolean isDuplicate = false;

        int info = JOptionPane.showConfirmDialog(null, display, "Please enter product's " +
                "ID.", JOptionPane.OK_CANCEL_OPTION);
        //Receiving user inputs and checking for errors through try and catch functions.
        if (info == JOptionPane.OK_OPTION) {
            try {
                newID = Integer.parseInt(productID.getText());
                text.append("\n  Activity ID" +
                        "\tActivity Quantity" + "\t  Activity Name" + "\t  Activity Date" + "\t  Activity Function");
                if(newID < 0){
                    JOptionPane.showMessageDialog(null, "The numbers entered must be greater " +
                            "than 0.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Invalid input, please make sure " +
                        "that only integers have been entered.", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }



            //Creating an ArrayList that will store the activities for matching ID.
            for (Activities i : activities) {
                if (i.getID() == newID) {
                    isDuplicate = true;
                    lastFour.add(i);
                }
            }

            /*
            Setting a value for the last four elements of the newly created Arraylist.
            Converting the arrayList into a list.
            It's now possible to use the subList method to create a subSet which will only include the last four
            elements.
            Iterating each of these elements through a for loop to display the last four activities of the product.
            */
            int index = Math.max(lastFour.size()-4, 0);
            List<Activities> listLastFour = lastFour;
            listLastFour = listLastFour.subList(index, lastFour.size());
            for (Activities x : listLastFour) {
                text.append("\n  " + x.getID() + "\t" + x.getQuantity() + "\t  " +
                        x.getName() + "\t  " + x.getDate() + "\t  " + x.getActivityFunction());
            }
        }
        //Error message if no duplicates are found.
        if (!isDuplicate) {
            JOptionPane.showMessageDialog(null, "Product not found!",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Creating a function to add quantities to an existing product.
    public static void addQuantity() {
        JTextField productID = new JTextField();
        JTextField productQuantity = new JTextField();
        Object[] display = {
                "Product ID: ", productID,
                "Product Quantity: ", productQuantity
        };
        int info = JOptionPane.showConfirmDialog(null, display, "Please enter product's " +
                "information.", JOptionPane.OK_CANCEL_OPTION);

        boolean isDuplicate = false;

        if (info == JOptionPane.OK_OPTION) {
            newID = Integer.parseInt(productID.getText());
            newQuantity = Integer.parseInt(productQuantity.getText());

            int index = 0;
            for (Items i : myList) {
                if (i.getID() == newID) {
                    isDuplicate = true;
                    i.addQuantity(newQuantity);
                    activityID = i.getID();
                    activityQuantity = newQuantity;
                    activityName = i.getName();
                    activityDate = i.getDate();
                    activityFunction = "Add Quantity";
                    Activities newActivity = new Activities(activityID, activityQuantity, activityName,
                            activityDate, activityFunction);
                    activities.add(newActivity);
                    tableLayout.setValueAt(i.getQuantity(), index, 1);

                    JOptionPane.showMessageDialog(null, "The total product quantity is now: " +
                            i.getQuantity(), "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                index++;
            }
            table.repaint();

            if (!isDuplicate) {
                JOptionPane.showMessageDialog(null, "Product not found!",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    /*Creating a function to delete a product in the inventory by selecting the ID.
    A try/catch block is executed to check for input errors.
    A for loop is executed to iterate through each item in the list to check that the product exists. If it does it will
    be removed from the list.
     */
    public static void deleteProduct() {
        JTextField productID = new JTextField();
        Object[] display = {
                "Product ID: ", productID
        };
        int info = JOptionPane.showConfirmDialog(null, display, "Please enter product's " +
                "ID.", JOptionPane.OK_CANCEL_OPTION);

        boolean isDuplicate = false;

        int index;
        if (info == JOptionPane.OK_OPTION) {
            try {
                newID = Integer.parseInt(productID.getText());
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Invalid input, please make sure " +
                        "that only integers have been entered.", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (int j = 0; j < myList.size(); j++) {
                Items i = myList.get(j);
                if (i.getID() == newID) {
                    isDuplicate = true;
                    index = j;
                    tableLayout.removeRow(index);
                    myList.remove(i);
                    activityID = i.getID();
                    activityQuantity = 0;
                    activityName = i.getName();
                    activityDate = i.getDate();
                    activityFunction = "Deleted";
                    Activities newActivity = new Activities(activityID, activityQuantity, activityName,
                            activityDate, activityFunction);
                    activities.add(newActivity);
                    JOptionPane.showMessageDialog(null, "Product deleted successfully.",
                            "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
            if (!isDuplicate) {
                JOptionPane.showMessageDialog(null, "Product not found!", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /* Creating a function that will display only the products that belong to the same category. In addition, they will
    be sorted by quantity by making a call to the sortByQuantity() method.
    A for loop is initiated to iterate through each item in the inventory and, if they matched to the desired category,
    they will be displayed in the text area. If not an error will be displayed to the user.
     */
    public static void displayCategory() {
        JTextField productCategory = new JTextField();
        Object[] display = {
                "Product Category: ", productCategory
        };
        boolean isDuplicate = false;
        int info = JOptionPane.showConfirmDialog(null, display, "Please enter the product's category.",
                JOptionPane.OK_CANCEL_OPTION);

        if (info == JOptionPane.OK_OPTION) {
            newCategory = productCategory.getText();
            text.append("""

                     Activity ID\tActivity Quantity\t   Activity Name\t   Activity Date
                    """);
            //Sorting product category by quantity.
            sortByQuantity(myList);

            for (Items i : myList) {
                if (i.getCategory().equals(newCategory)){
                    isDuplicate = true;
                    text.append(" " + i.getID() + "	" + i.getQuantity() + "	   " +
                            i.getName() + "	   " + i.getDate() + "	   " + "\n");
                }
            }
        }
        if (!isDuplicate) {
            JOptionPane.showMessageDialog(null, "Category not found!",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }


    }

    /* Creating a method to display all the activities of a product.
    We first prompt the user to input the product ID. A try/catch block is initiated to check for inputs errors.
    A for loop will then iterate through each activity, checking their IDs and, if they match to
    the user input ID, a list of all the activities of that product will be displayed. If the ID entered is not found
    an Error is raised.
     */
    public static void vendorReport() {
        JTextField productID = new JTextField();
        Object[] display = {
                "Product ID: ", productID
        };
        int info = JOptionPane.showConfirmDialog(null, display, "Please enter product's " +
                "ID.", JOptionPane.OK_CANCEL_OPTION);
        boolean isDuplicate = false;

        if (info == JOptionPane.OK_OPTION) {
            try {
                newID = Integer.parseInt(productID.getText());
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Invalid input, please make sure " +
                        "that only integers have been entered.", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            text.append("\n");
            text.append("""

                      Activity ID\tActivity Quantity\t   Activity Name\t   Activity Date\t   Activity Function
                    """);
            for (Activities i : activities) {
                if (i.getID() == newID) {
                    isDuplicate = true;
                    text.append("  " + i.getID() + "\t" + i.getQuantity() + "\t   " +
                            i.getName() + "\t   " + i.getDate() + "\t   " + i.getActivityFunction() + "\n");
                }
            }
            if (!isDuplicate) {
                JOptionPane.showMessageDialog(null, "Product not found!", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //Creating a function to save the data that we currently hold in the inventory in a new file.
    public static void saveData() {
        try {
            File directory = new File(DATA_FILE_PATH).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE_PATH))) {
                oos.writeObject(myList);
                oos.writeObject(activities);
                JOptionPane.showMessageDialog(null, "The inventory has been saved successfully!",
                        "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Couldn't save the inventory. Try again!",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Creating a function to load the data that we previously saved on a file.
    public static void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE_PATH))){
            tableLayout.setRowCount(0);
            myList = (ArrayList<Items>) ois.readObject();
            activities = (ArrayList<Activities>) ois.readObject();
            JOptionPane.showMessageDialog(null, "File loaded successfully!",
                    "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            for(Items i : myList){
                Object[] newProduct = {i.getID(), i.getQuantity(), i.getName(),
                        i.getCategory(), i.getDate()};
                tableLayout.addRow(newProduct);
                table.repaint();
            }
        }
        catch(FileNotFoundException e){
            JOptionPane.showMessageDialog(null, "File not found! Please try again.",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading the file.", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //Creating a method to sort a list of items y their quantity number.
    public static void sortByQuantity(ArrayList<Items> list) {
        int n = list.size();
        boolean swapped;

        do {
            swapped = false;
            for (int i = 1; i < n; i++) {
                if (list.get(i - 1).getQuantity() > list.get(i).getQuantity()) {
                    // Items get swapped if in the wrong order
                    Items temp = list.get(i - 1);
                    list.set(i - 1, list.get(i));
                    list.set(i, temp);
                    swapped = true;
                }
            }
        } while (swapped);
    }

}
