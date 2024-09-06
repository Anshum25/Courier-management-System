import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


class CourierManagementSystem {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean b = true;
        Database db = new Database();
        try {
            db.connect();
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database: " + e.getMessage());
            return;
        }

        while (b) {
            System.out.println();
            System.out.println("Welcome to the Courier Management System");
            System.out.println();
            System.out.println("1. Login as Admin");
            System.out.println("2. Sign Up as Customer");
            System.out.println("3. Login as Customer");
            System.out.println("4. Login as Worker");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        Admin.login(db, scanner);
                        break;
                    case 2:
                        Customer.signUp(db, scanner);
                        break;
                    case 3:
                        Customer.login(db, scanner);
                        break;
                    case 4:
                        Worker.login(db, scanner);
                        break;
                    case 5:
                        System.out.println("Thnk you for Choosing us...");
                        b = false;
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }

       
    }
}


// Admin Class
class Admin {
     static final String adminUsername = "Anshum";
     static final String adminPassword = "1234";

    public static void login(Database db, Scanner scanner) throws Exception {
        System.out.println("Enter Admin Username:");
        String username = scanner.nextLine();
        System.out.println("Enter Admin Password:");
        String password = scanner.nextLine();

        if (username.equals(adminUsername) && password.equals(adminPassword)) {
            System.out.println("Admin Login Successful!");
            adminMenu(db, scanner);
        } else {
            System.out.println("Invalid , please try again.");
        }
    }


     static void adminMenu(Database db, Scanner scanner) throws Exception {
        boolean b = true;

        while (b) {
            System.out.println("Admin Menu");
            System.out.println("1. View All Customers");
            System.out.println("2. View All Couriers");
            System.out.println("3. Add Workers");
            System.out.println("4. Remove Workers");
            System.out.println("5. View All Workers");
            System.out.println("6. Show Rejected Couriers");
            System.out.println("7. Update Courier by ID");
            System.out.println("8. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    db.viewCustomers();
                    break;
                case 2:
                    db.viewCouriers(true); // Admin sees all couriers including status
                    break;
                    case 3:
                    db.addWorker();
                    break;
                    case 4:
                    db.removeWorker();
                    break;
                case 5:
                    db.viewWorkers();
                    break;
                case 6:
                    db.viewRejectedCouriers();
                    break;
                case 7:
                    updateCourier(db, scanner);
                    break;
                    
                case 8:
                    b = false;
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
        
    }
    

     static void updateCourier(Database db, Scanner scanner) throws SQLException {
        System.out.println("Enter Courier ID to update:");
        int courierId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Enter new status (Pending Pickup, In Transit, Delivered, Rejected, Damaged):");
        String status = scanner.nextLine();

        db.updateCourierStatus(courierId, status);
        System.out.println("Courier status updated successfully!");
    }
   
}

// Customer Class
class Customer {
    int id;
    String name;
    String phoneNumber;
    String address;
    String email;
    String password;

    public Customer(int id, String name, String phoneNumber, String address, String email, String password) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.password = password;
    }

    public static void signUp(Database db, Scanner scanner) throws SQLException {
        System.out.println("Enter your name:");
        String name = scanner.nextLine();
        System.out.println("Enter your phone number:");
        String phoneNumber = scanner.nextLine();
        System.out.println("Enter your address:");
        String address = scanner.nextLine();
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        db.addCustomer(new Customer(0, name, phoneNumber, address, email, password));
        System.out.println("Sign Up Successful!");
    }

    public static void login(Database db, Scanner scanner) throws SQLException {
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        Customer customer = db.findCustomerByEmail(email);
        if (customer != null && customer.password.equals(password)) {
            System.out.println("Customer Login Successful!");
            customerMenu(db, scanner, customer);
        } else {
            System.out.println("Invalid credentials, please try again.");
        }
    }
     static void customerMenu(Database db, Scanner scanner, Customer customer) throws SQLException {
        boolean b = true;

        while (b) {
            System.out.println("Customer Menu");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. Create Courier");
            System.out.println("4. View Couriers");
            System.out.println("5. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println(customer);
                    break;
                case 2:
                    updateProfile(scanner, customer);
                    db.updateCustomer(customer);
                    break;
                case 3:
                    createCourier(db, scanner, customer);
                    break;
                case 4:
                    db.viewCustomerCouriers(customer, false); // Customer sees only non-rejected couriers
                    break;
                case 5:
                    b = false;
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
    static void updateProfile(Scanner scanner, Customer customer) {
        System.out.println("Update Profile");
        
        System.out.println("Enter your new phone number (press Enter to skip):");
        String newPhoneNumber = scanner.nextLine();
        if (!newPhoneNumber.isEmpty()) {
            customer.phoneNumber = newPhoneNumber;
        }
    
        System.out.println("Enter your new address (press Enter to skip):");
        String newAddress = scanner.nextLine();
        if (!newAddress.isEmpty()) {
            customer.address = newAddress;
        }
    
        System.out.println("Enter your new email (press Enter to skip):");
        String newEmail = scanner.nextLine();
        if (!newEmail.isEmpty()) {
            customer.email = newEmail;
        }
        
        System.out.println("Profile updated successfully!");
    }
    
     static void createCourier(Database db, Scanner scanner, Customer customer) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter courier details");
        System.out.println("Enter recipient name:");
        String recipientName = scanner.nextLine();
        System.out.println("Enter recipient address:");
        String recipientAddress = scanner.nextLine();
        System.out.println("Enter package description:");
        String packageDescription = scanner.nextLine();
        System.out.println("Enter package weight:");
        double packageWeight = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        double distance = db.getDistance(recipientAddress);
        double price = Pricing.calculatePrice(packageWeight, distance);

        Courier courier = new Courier(0, customer.id, recipientName, recipientAddress, packageDescription, packageWeight, price, "Pending Pickup");
        db.addCourier(courier);
        System.out.println("your total amount :- " + price);
        System.out.println("1.net banking\n2.Online Payment\n3.Credit card");
        System.out.println("enter payment method :- ");
        int c = sc.nextInt();
        switch (c) {
            case 1:
            System.out.println("payment succsssfull");
                break;
            case 2 : 
            System.out.println("payment succsssfull");
            break;
            case 3: 
            System.out.println("payment succsssfull");
            break;
            
            default:
            System.out.println("invalid choice");
                break;
        }
        
        System.out.println("Courier created successfully! Your pending couriers:");
        db.viewCustomerCouriers(customer, true); // Display pending couriers
    }
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

// Worker Class
class Worker {
    int id;
    String name;
    String phoneNumber;
    String email;
    String password;

    public Worker(int id, String name, String phoneNumber, String email, String password) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    public static void login(Database db, Scanner scanner) throws SQLException {
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        Worker worker = db.findWorkerByEmail(email);

        if (worker != null && worker.password.equals(password)) {
            System.out.println("Worker Login Successful!");
            workerMenu(db, scanner, worker);
        } else {
            System.out.println("Invalid credentials, please try again.");
        }
    }

     static void workerMenu(Database db, Scanner scanner, Worker worker) throws SQLException {
        boolean b = true;

        while (b) {
            System.out.println("Worker Menu");
            System.out.println("1. View Profile");
            System.out.println("2. Pick Up Courier");
            System.out.println("3. Deliver Courier");
            System.out.println("4. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println(worker);
                    break;
                case 2:
                    pickUpCourier(db, scanner, worker);
                    break;
                case 3:
                    deliverCourier(db, scanner, worker);
                    break;
                case 4:
                    b = false;
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

     static void pickUpCourier(Database db, Scanner scanner, Worker worker) throws SQLException {
        System.out.println("Enter Courier ID to pick up:");
        int courierId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Courier courier = db.getCourierById(courierId);

        if (courier != null && "Pending Pickup".equals(courier.status)) {
            System.out.println("Enter pickup time (format YYYY-MM-DD HH:MM:SS):");
            String pickupTime = scanner.nextLine();
            db.updateCourierStatus(courierId, "In Transit");
            db.assignWorkerToCourier(courierId, worker.id, pickupTime, null);
            System.out.println("Courier picked up successfully!");
        } else {
            System.out.println("Invalid courier ID or courier is not in pending pickup status.");
        }
    }
    public static void pickUpNextCourier(Database db, Scanner scanner, Worker worker) throws SQLException {
        Courier nextCourier = db.getNextCourier();
        if (nextCourier != null && "Pending Pickup".equals(nextCourier.status)) {
            System.out.println("Enter pickup time (format YYYY-MM-DD HH:MM:SS):");
            String pickupTime = scanner.nextLine();
            db.updateCourierStatus(nextCourier.id, "In Transit");
            db.assignWorkerToCourier(nextCourier.id, worker.id, pickupTime, null);
            System.out.println("Courier picked up successfully! " + nextCourier);
        } else {
            System.out.println("No pending couriers available for pickup.");
        }
    }

     static void deliverCourier(Database db, Scanner scanner, Worker worker) throws SQLException {
        System.out.println("Enter Courier ID to deliver:");
        int courierId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Courier courier = db.getCourierById(courierId);

        if (courier != null && "In Transit".equals(courier.status)) {
            System.out.println("Enter delivery time (format YYYY-MM-DD HH:MM:SS):");
            String deliveryTime = scanner.nextLine();
            db.updateCourierStatus(courierId, "Delivered");
            db.updateCourierDeliveryDetails(courierId, deliveryTime);
            System.out.println("Courier delivered successfully!");
        } else {
            System.out.println("Invalid courier ID or courier is not in transit.");
        }
    }
    

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

// Database Class
class Database {
     Connection con;
     Queue<Courier> courierQueue = new LinkedList<>();

    public void connect() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/courier_db", "root", "");
    }

    public class MyLinkedList<T> {
        // Node class representing each element in the list
        private class Node {
            T data;
            Node next;
    
            Node(T data) {
                this.data = data;
                this.next = null;
            }
        }
    
        private Node head; // Head of the linked list
        private Node tail; // Tail of the linked list
    
        public MyLinkedList() {
            this.head = null;
            this.tail = null;
        }
    
        // Method to add an element to the end of the list
        public void add(T data) {
            Node newNode = new Node(data);
    
            if (head == null) {
                // If the list is empty, set head and tail to the new node
                head = tail = newNode;
            } else {
                // Add the new node to the end of the list
                tail.next = newNode;
                tail = newNode;
            }
        //    System.out.println("Added: " + data);
        }
    
        // Method to remove and return the first element from the list
        public T poll() {
            if (head == null) {
              //  System.out.println("The list is empty, nothing to poll.");
                return null;
            }
    
            T data = head.data;
            head = head.next;
    
            // If the list becomes empty after polling, set tail to null
            if (head == null) {
                tail = null;
            }
    
            // System.out.println("Polled: " + data);
            return data;
        }
    
    }

    public void addCustomer(Customer customer) throws SQLException {
        String query = "INSERT INTO customers (name, phone_number, address, email, password) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, customer.name);
            pstmt.setString(2, customer.phoneNumber);
            pstmt.setString(3, customer.address);
            pstmt.setString(4, customer.email);
            pstmt.setString(5, customer.password);
            
            pstmt.executeUpdate();
        }
    }
    public void addWorker() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter worker's name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter worker's email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter worker's password: ");
        String password = scanner.nextLine();
    
        System.out.println("Enter worker's phone number: ");
        String phoneNumber = scanner.nextLine();
        
        // Update the SQL query to include four placeholders
        String sql = "INSERT INTO workers (name, email, password, phone_number) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, phoneNumber);
            pstmt.executeUpdate();
            
            System.out.println("Worker added successfully.");
        }
    }
    
    public void removeWorker() throws Exception{
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter worker's email to remove: ");
        String email = scanner.nextLine();
        
        // Delete the worker's information from the database
        //con conn = DriverManager.getcon(DB_URL, DB_USER, DB_PASSWORD);
        String sql = "DELETE FROM workers WHERE email = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        //PreparedStatement pstmt = conection.prepareStatement(sql);
            pstmt.setString(1, email);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Worker removed successfully.");
            } else {
                System.out.println("Worker not found.");
            }
        } 
        
    }
    
    

    public void updateCustomer(Customer customer) throws SQLException {
        String query = "UPDATE customers SET phone_number = ?, address = ?, email = ? WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, customer.phoneNumber);
            pstmt.setString(2, customer.address);
            pstmt.setString(3, customer.email);
            pstmt.setInt(4, customer.id);
            pstmt.executeUpdate();
        }
    }

    public void addCourier(Courier courier) throws SQLException {
        String query = "INSERT INTO couriers (customer_id, recipient_name, recipient_address, package_description, package_weight, price, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
           
            pstmt.setInt(1, courier.customerId);
            pstmt.setString(2, courier.recipientName);
            pstmt.setString(3, courier.recipientAddress);
            pstmt.setString(4, courier.packageDescription);
            pstmt.setDouble(5, courier.packageWeight);
            pstmt.setDouble(6, courier.price);
            pstmt.setString(7, courier.status);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                courier.id = rs.getInt(1);  // Get the generated ID
            }

            // Add the courier to the queue
            courierQueue.add(courier);
        }
    }
    public Courier getNextCourier() {
        return courierQueue.poll(); // Retrieves and removes the head of the queue
    }

    public void updateCourierStatus(int courierId, String status) throws SQLException {
        String query = "UPDATE couriers SET status = ? WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, courierId);
            pstmt.executeUpdate();
        }
    }

    public void assignWorkerToCourier(int courierId, int workerId, String pickupTime, String deliveryTime) throws SQLException {
        String query = "UPDATE couriers SET worker_id = ?, pickup_time = ?, delivery_time = ? WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, workerId);
            pstmt.setString(2, pickupTime);
            pstmt.setString(3, deliveryTime);
            pstmt.setInt(4, courierId);
            pstmt.executeUpdate();
        }
    }

    public void updateCourierDeliveryDetails(int courierId, String deliveryTime) throws SQLException {
        String query = "UPDATE couriers SET delivery_time = ? WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, deliveryTime);
            pstmt.setInt(2, courierId);
            pstmt.executeUpdate();
        }
    }

    public Customer findCustomerByEmail(String email) throws SQLException {
        String query = "SELECT * FROM customers WHERE email = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        }
        return null;
    }

    public Worker findWorkerByEmail(String email) throws SQLException {
        String query = "SELECT * FROM workers WHERE email = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Worker(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        }
        return null;
    }

    public Courier getCourierById(int courierId) throws SQLException {
        String query = "SELECT * FROM couriers WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, courierId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Courier(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getString("recipient_name"),
                        rs.getString("recipient_address"),
                        rs.getString("package_description"),
                        rs.getDouble("package_weight"),
                        rs.getDouble("price"),
                        rs.getString("status")
                );
            }
        }
        return null;
    }

    public void viewCustomers() throws SQLException {
        String query = "SELECT * FROM customers";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }
        }
    }

    public void viewWorkers() throws SQLException {
        String query = "SELECT * FROM workers";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println(new Worker(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }
        }
    }

    public void viewCouriers(boolean showStatus) throws SQLException {
        String query = showStatus ? "SELECT * FROM couriers" : "SELECT id, customer_id, recipient_name, recipient_address, package_description, package_weight, price FROM couriers";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                if (showStatus) {
                    System.out.println(new Courier(
                            rs.getInt("id"),
                            rs.getInt("customer_id"),
                            rs.getString("recipient_name"),
                            rs.getString("recipient_address"),
                            rs.getString("package_description"),
                            rs.getDouble("package_weight"),
                            rs.getDouble("price"),
                            rs.getString("status")
                    ));
                } else {
                    System.out.println(new Courier(
                            rs.getInt("id"),
                            rs.getInt("customer_id"),
                            rs.getString("recipient_name"),
                            rs.getString("recipient_address"),
                            rs.getString("package_description"),
                            rs.getDouble("package_weight"),
                            rs.getDouble("price"),
                            null
                    ));
                }
            }
        }
    }

    public void viewCustomerCouriers(Customer customer, boolean showPending) throws SQLException {
        String query = showPending ? "SELECT * FROM couriers WHERE customer_id = ? AND status = 'Pending Pickup'" : "SELECT * FROM couriers WHERE customer_id = ? AND status <> 'Rejected' AND status <> 'Damaged'";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, customer.id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(new Courier(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getString("recipient_name"),
                        rs.getString("recipient_address"),
                        rs.getString("package_description"),
                        rs.getDouble("package_weight"),
                        rs.getDouble("price"),
                        rs.getString("status")
                ));
            }
        }
    }

    public void viewRejectedCouriers() throws SQLException {
        String query = "SELECT * FROM couriers WHERE status = 'Rejected' OR status = 'Damaged'";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println(new Courier(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getString("recipient_name"),
                        rs.getString("recipient_address"),
                        rs.getString("package_description"),
                        rs.getDouble("package_weight"),
                        rs.getDouble("price"),
                        rs.getString("status")
                ));
            }
        }
    }

    public double getDistance(String address) throws SQLException {
        // This is a placeholder for distance calculation logic
        return 10.0; // Dummy distance value
    }
}

// Pricing Class
class Pricing {
     static double basePrice = 50.0;

    public static void setBasePrice(double price) {
        basePrice = price;
    }

    public static double calculatePrice(double weight, double distance) {
        return basePrice + (weight * 10) + (distance * 5);
        
    }
    
}

// Courier Class
class Courier {
    int id;
    int customerId;
    String recipientName;
    String recipientAddress;
    String packageDescription;
    double packageWeight;
    double price;
    String status;

    public Courier(int id, int customerId, String recipientName, String recipientAddress, String packageDescription, double packageWeight, double price, String status) {
        this.id = id;
        this.customerId = customerId;
        this.recipientName = recipientName;
        this.recipientAddress = recipientAddress;
        this.packageDescription = packageDescription;
        this.packageWeight = packageWeight;
        this.price = price;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Courier{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", recipientName='" + recipientName + '\'' +
                ", recipientAddress='" + recipientAddress + '\'' +
                ", packageDescription='" + packageDescription + '\'' +
                ", packageWeight=" + packageWeight +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}




