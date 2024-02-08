import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Base class for orders (appointment or product order)
abstract class Order {
    abstract void displayOrderSummary(); // Abstract method to display order summary
}

// Appointment class representing a scheduled appointment
class Appointment extends Order {
    private Date date;
    private String time;

    // Constructor to initialize appointment date and time
    public Appointment(Date date, String time) {
        this.date = date;
        this.time = time;
    }

    // Method to display appointment details
    @Override
    void displayOrderSummary() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        System.out.println("Appointment Details:");
        System.out.println("Date: " + sdf.format(date));
        System.out.println("Time: " + time);
    }

    // Getter method for appointment date
    public Date getDate() {
        return date;
    }

    // Getter method for appointment time
    public String getTime() {
        return time;
    }
}

// Product class representing a service or item for purchase
class Product extends Order {
    private String name;
    private double price;
    private int quantity;

    // Constructor to initialize product details
    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Method to display product order summary
    @Override
    void displayOrderSummary() {
        // Format and print product details
        System.out.printf("%-35s $%7.2f %5d%n", name, price, quantity);
    }

    // Method to calculate total price of the product
    double getTotalPrice() {
        return price * quantity;
    }

    // Getter method for product name
    public String getName() {
        return name;
    }

    // Getter method for product quantity
    public int getQuantity() {
        return quantity;
    }

    // Method to update product quantity
    public void setQuantity(int quantity) {
        this.quantity += quantity;
    }
}

// OrderManager class to manage orders and generate order summary
class OrderManager {
    private Map<String, Product> productMap;
    private Appointment appointment;

    // Constructor to initialize product map and appointment
    public OrderManager() {
        productMap = new HashMap<>();
        appointment = null;
    }

    // Method to add a product to the order
    void addProduct(Product product) {
        // If product already exists, update quantity; otherwise, add new product
        if (productMap.containsKey(product.getName())) {
            productMap.get(product.getName()).setQuantity(product.getQuantity());
        } else {
            productMap.put(product.getName(), product);
        }
    }

    // Method to set appointment for the order
    void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    // Method to display order summary
    void displayOrderSummary() {
        if (productMap.isEmpty() && appointment == null) {
            System.out.println("No products or appointment set.");
            return;
        }

        // Print order summary header
        System.out.println("Order Summary:");
        System.out.printf("%-35s %-10s %5s%n", "Product", "Price", "Quantity");
        System.out.println("------------------------------------------------------------------");

        // Variables to track total products and total price
        int totalProducts = 0;
        double totalPrice = 0.0;

        // Iterate over products and display their order summary
        for (Product product : productMap.values()) {
            product.displayOrderSummary();
            totalProducts += product.getQuantity();
            totalPrice += product.getTotalPrice();
        }

        // Display appointment details if set
        if (appointment != null) {
            appointment.displayOrderSummary();
        }

        // Print total products and total price
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-42s %5d%n", "Total Products:", totalProducts);
        System.out.printf("%-42s $%.2f%n", "Total Price:", totalPrice);
    }

    // Method to write order summary to a text file
    void writeOrderSummaryToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            if (productMap.isEmpty() && appointment == null) {
                writer.println("No products or appointment set.");
                return;
            }

            // Write order summary header
            writer.println("Order Summary:");
            writer.printf("%-35s %-10s %5s%n", "Product", "Price", "Quantity");
            writer.println("------------------------------------------------------------------");

            // Variables to track total products and total price
            int totalProducts = 0;
            double totalPrice = 0.0;

            // Iterate over products and write their order summary
            for (Product product : productMap.values()) {
                writer.printf("%-35s $%7.2f %5d%n", product.getName(), product.getTotalPrice(), product.getQuantity());
                totalProducts += product.getQuantity();
                totalPrice += product.getTotalPrice();
            }

            // Write appointment details if set
            if (appointment != null) {
                writer.println("Appointment Details:");
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                writer.println("Date: " + sdf.format(appointment.getDate()));
                writer.println("Time: " + appointment.getTime());
            }

            // Write total products and total price
            writer.println("------------------------------------------------------------------");
            writer.printf("%-42s %5d%n", "Total Products:", totalProducts);
            writer.printf("%-42s $%.2f%n", "Total Price:", totalPrice);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // Getter method for product map
    public Map<String, Product> getProductMap() {
        return productMap;
    }

    // Getter method for appointment
    public Appointment getAppointment() {
        return appointment;
    }
}

// Main class to run the program
public class OrderManagementSystem {
    // Constants for product names and prices
    private static final String[] PRODUCT_NAMES = {"Car Wash", "Oil Change", "Brake Service", "Tire Rotation", "Alignment", "Battery Replacement"};
    private static final double[] PRODUCT_PRICES = {25.0, 50.0, 100.0, 20.0, 80.0, 150.0};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OrderManager orderManager = new OrderManager();

        // Display welcome message
        System.out.println("Welcome to AutoZone!");

        // Main menu loop
        while (true) {
            // Display menu options
            System.out.println("\nMenu:");
            System.out.println("1. Add Product");
            System.out.println("2. Set Appointment");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    // Add Product
                    boolean addAnotherProduct = true;
                    while (addAnotherProduct) {
                        // Display available products
                        System.out.println("Available Products:");
                        System.out.printf("%-5s %-35s %-10s%n", "No.", "Product", "Price");
                        System.out.println("------------------------------------------------------------------");
                        for (int i = 0; i < PRODUCT_NAMES.length; i++) {
                            System.out.printf("%-5d %-35s $%.2f%n", i + 1, PRODUCT_NAMES[i], PRODUCT_PRICES[i]);
                        }

                        // Prompt user to choose product and quantity
                        int productNumber;
                        while (true) {
                            System.out.print("Enter product number: ");
                            try {
                                productNumber = Integer.parseInt(scanner.nextLine());
                                if (productNumber < 1 || productNumber > PRODUCT_NAMES.length) {
                                    System.out.println("Invalid product number. Please try again.");
                                } else {
                                    break; // Exit loop if product number is valid
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a number.");
                            }
                        }
                        System.out.print("Enter quantity: ");
                        int quantity;
                        try {
                            quantity = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            continue;
                        }

                        // Create product object and add to order manager
                        Product product = new Product(PRODUCT_NAMES[productNumber - 1], PRODUCT_PRICES[productNumber - 1], quantity);
                        orderManager.addProduct(product);
                        System.out.println("Product added successfully!");

                        // Prompt user to add another product
                        while (true) {
                            System.out.print("Add another product? (yes/no): ");
                            String response = scanner.nextLine().toLowerCase();
                            if (response.equals("yes")) {
                                break; // Exit loop and add another product
                            } else if (response.equals("no")) {
                                addAnotherProduct = false;
                                break; // Exit loop and return to main menu
                            } else {
                                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                            }
                        }
                    }
                    break;

                case 2:
                    // Set Appointment
                    boolean confirmAppointment = false;
                    while (!confirmAppointment) {
                        Date date = null;
                        System.out.println("Enter appointment date in MM/dd/yyyy format (e.g., 12/31/2022): ");
                        String dateStr = scanner.nextLine();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        dateFormat.setLenient(false);
                        try {
                            date = dateFormat.parse(dateStr);
                        } catch (ParseException e) {
                            System.out.println("Invalid date format. Please enter date in MM/dd/yyyy format.");
                            continue;
                        }
                        String time = null;
                        boolean retryTime = true;
                        while (retryTime) {
                            System.out.println("Enter appointment time in HH:mm AM/PM format (e.g., 10:00 AM): ");
                            time = scanner.nextLine();
                            // Validate appointment time format
                            if (!isValidTimeFormat(time)) {
                                System.out.println("Invalid time format. Please enter time in HH:mm AM/PM format (e.g., 10:00 AM).");
                            } else {
                                retryTime = false;
                            }
                        }
                        System.out.println("Appointment Date: " + dateFormat.format(date));
                        System.out.println("Appointment Time: " + time);
                        while (true) {
                            System.out.print("Confirm appointment? (yes/no): ");
                            String response = scanner.nextLine().toLowerCase();
                            if (response.equals("yes")) {
                                confirmAppointment = true;
                                break; // Exit loop and confirm appointment
                            } else if (response.equals("no")) {
                                break; // Exit loop and retry appointment date/time
                            } else {
                                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                            }
                        }
                        if (confirmAppointment) {
                            Appointment appointment = new Appointment(date, time);
                            orderManager.setAppointment(appointment);
                            System.out.println("Appointment set successfully!");
                        }
                    }
                    break;

                case 3:
                    // Exit
                    if (orderManager.getProductMap().isEmpty() && orderManager.getAppointment() == null) {
                        System.out.println("No products or appointment set. Please add products or set an appointment before exiting.");
                    } else if (orderManager.getProductMap().isEmpty()) {
                        System.out.println("No products set. Please add products before exiting.");
                    } else if (orderManager.getAppointment() == null) {
                        System.out.println("No appointment set. Please set an appointment before exiting.");
                    } else {
                        // Write order summary to file
                        orderManager.writeOrderSummaryToFile("AutoZone_summary.txt");
                        System.out.println("Order summary written to AutoZone_summary.txt.");
                        scanner.close();
                        System.exit(0);
                    }
                    break;

                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
    }

    // Method to validate time format (HH:mm AM/PM)
    private static boolean isValidTimeFormat(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        try {
            sdf.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
