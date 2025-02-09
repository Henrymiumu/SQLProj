import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserMode {
    List<String> shoppingCart = new ArrayList<>();
    public static void userMode(Connection connection, String username) throws SQLException {
        System.out.println("Login successfully");
        //Home 1.Surfing 2.Shopping Cart 3.Exit
        //Surfing 1.Show all 2.Filtered
        //Filtered 1.Category 2.Price Range 3.Brand
        while(true){
            System.out.println("Menu: ");
            System.out.println("What would you like to do?");
            System.out.println("\t1. Look for products");
            System.out.println("\t2. Shopping cart");
            System.out.println("\t3. Terminate system");
            Scanner scanner = new Scanner(System.in);
            int choice = 0;
            try{
                choice = scanner.nextInt();
            }catch (Exception e){
                System.out.println("Please input only numbers");
            }
            switch (choice){
                case 1: //look for products
                    lookproducts(connection,username);
                    continue;
                case 2:
                    shoppingcart(connection,username);
                    continue;
                case 3:
                    System.out.println("The system will now terminate");
                    return;
                default:
                    System.out.println("Please input numbers in range");
            }


        }
    }
    private static void lookproducts(Connection connection, String username) throws SQLException {
        System.out.println("Here are some products for you: ");
        Statement statement = connection.createStatement();
        String sql = "select id, name, brand, price from products order by rand() limit 30;";
        SwingUtilities.invokeLater(() -> {
            try {
                ShowTable.showtable(statement, sql);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        while (true) {
            System.out.println("Would you like to add any of them to the shopping cart? (Y/N)");

        String input;
        int quantityinDb = 0;
        String name = "";
        String productID = "";
        Scanner scanner = new Scanner(System.in);
        input = scanner.nextLine();
        switch (input.toLowerCase()) {
            case "y":
                while (true) {
                    System.out.println("Please input the Product ID");
                    productID = scanner.nextLine().trim();
                    if (isPositiveInteger(productID)) {
                        int count = 0;
                        ResultSet resultSet = statement.executeQuery("select  name, count(*), quantity from products where id = " + productID + ";");
                        if (resultSet.next()) {
                            quantityinDb = resultSet.getInt("quantity");
                            count = resultSet.getInt("count(*)");
                            name = resultSet.getString("name");
                        }
                        if (count == 0) {
                            System.out.println("Product not found, Please try again");
                            continue;
                        } else {
                            addtoshoppingcart(username, statement, name, quantityinDb, Integer.parseInt(productID));
                            break;
                        }
                    }
                }
            case "n":
                System.out.println("What you wish to do next?");
                System.out.println("1. Search for more products");
                System.out.println("2. Look at items in shopping cart");
                String choice = "";


                break;
            default:
                System.out.println("Invalid input, please try again.");

        }
        }

    }
    private static void shoppingcart(Connection connection) {
    }

    private static void addtoshoppingcart(String username, Statement statement, String name, int quantity, int ID) throws SQLException {
        int amount = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input amount");
        amount = scanner.nextInt();


        if (amount > quantity) {
            System.out.println("Not enough quantity available. Only " + quantity + " left in stock.");
            System.out.println("What would you like to do?");
            System.out.println("1. Update amount");
            System.out.println("2. Buy remaining (" + quantity + ")");
            System.out.println("3. Cancel operation");
            String choice = "";
            choice = scanner.nextLine();
            while (true) {
                switch (choice) {
                    case "1":
                        addtoshoppingcart(username,statement, name, quantity, ID);
                        break;
                    case "2":
                        confirmAddToShoppingCart(statement,username, name, quantity, ID, quantity);
                        break;
                    case "3":
                        return;
                    default:
                        System.out.println("Invalid input, please try again");
                }
            }

        } else {
            confirmAddToShoppingCart(statement,username, name, amount, ID, quantity);
        }
    }
    private static void confirmAddToShoppingCart(Statement statement, String username, String name, int amount, int ID, int quantity) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String choice;
        System.out.println("Product Information as below: ");
        System.out.println("Product name: "+ name);
        System.out.println("Quantity: "+amount);
        System.out.println("\nIs the information correct?");
        choice = scanner.nextLine();
        while(true) {
            switch (choice.toLowerCase()) {
                case "y":
                    statement.executeUpdate("insert into shoppingcart (username, prodid, amount) values ("+ username + "," + ID + "," + amount + ")");
                    System.out.println("executed addtoShoppingCart");
                    break;
                case "n":
                    System.out.println("What would you like to do?");
                    System.out.println("1. Update amount");
                    System.out.println("2. Cancel operation");
                    String choice2;
                    choice2 = scanner.nextLine().trim();
                    if (choice2.equals("1")){
                        addtoshoppingcart(username, statement, name, quantity, ID);
                    } else if (choice2.equals("2")){
                        return;
                }
                default:
                    System.out.println("Invalid input, please try again");
            }
        }
    }
    public static boolean isPositiveInteger(String input){
        try{
            int number = Integer.parseInt(input);
            return number >=0;
        }catch (NumberFormatException e){
            return false;
        }
    }
}