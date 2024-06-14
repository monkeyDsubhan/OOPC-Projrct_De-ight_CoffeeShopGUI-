import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class CoffeeShopGUI extends JFrame {

    private JComboBox<String> coffeeTypeComboBox;
    private ButtonGroup sizeButtonGroup;
    private JCheckBox milkCheckBox, sugarCheckBox, whippedCreamCheckBox;
    private Map<String, Double> itemPrices;
    private JTextArea summaryTextArea;
    private JButton placeOrderButton;
    private JButton showSummaryButton;
    private JButton registerButton;
    private boolean isAdminLoggedIn = false;

    public CoffeeShopGUI() {
        setTitle("Coffee Delights");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(255, 248, 231));
        initializeItemPrices();
        coffeeTypeComboBox = new JComboBox<>(new String[] { "Espresso", "Latte", "Cappuccino", "Americano" });
        sizeButtonGroup = new ButtonGroup();
        milkCheckBox = new JCheckBox("Milk");
        sugarCheckBox = new JCheckBox("Sugar");
        whippedCreamCheckBox = new JCheckBox("Whipped Cream");
        JRadioButton smallRadioButton = new JRadioButton("Small");
        JRadioButton mediumRadioButton = new JRadioButton("Medium");
        JRadioButton largeRadioButton = new JRadioButton("Large");
        sizeButtonGroup.add(smallRadioButton);
        sizeButtonGroup.add(mediumRadioButton);
        sizeButtonGroup.add(largeRadioButton);
        placeOrderButton = new JButton("Place My Order");
        placeOrderButton.addActionListener(e -> placeOrder());
        placeOrderButton.setBackground(new Color(255, 69, 0));
        placeOrderButton.setForeground(Color.WHITE);
        placeOrderButton.setFont(new Font("Arial", Font.BOLD, 16));
        showSummaryButton = new JButton("Show Summary");
        showSummaryButton.addActionListener(e -> showSummary());
        showSummaryButton.setBackground(new Color(0, 128, 0));
        showSummaryButton.setForeground(Color.WHITE);
        showSummaryButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> showRegisterDialog());
        registerButton.setBackground(new Color(70, 130, 180));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);
        for (Map.Entry<String, Double> entry : itemPrices.entrySet()) {
            JMenuItem menuItem = new JMenuItem(String.format("%s - $%.2f", entry.getKey(), entry.getValue()));
            menu.add(menuItem);
        }
        setLayout(new BorderLayout());
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        summaryTextArea = new JTextArea("Order Summary:\n");
        summaryTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(summaryTextArea);
        scrollPane.setPreferredSize(new Dimension(getWidth(), getHeight() / 2));
        summaryPanel.add(scrollPane, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.CENTER);
        JPanel optionsPanel = new JPanel(new GridLayout(6, 2, 10, 5));
        optionsPanel.setBackground(new Color(255, 248, 231));
        add(optionsPanel, BorderLayout.NORTH);
        addSection(optionsPanel, "Select Coffee Type", coffeeTypeComboBox);
        addSection(optionsPanel, "Select Portion Size", smallRadioButton, mediumRadioButton, largeRadioButton);
        addSection(optionsPanel, "Select Extras", milkCheckBox, sugarCheckBox, whippedCreamCheckBox);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(showSummaryButton);
        bottomPanel.add(placeOrderButton);
        bottomPanel.add(registerButton);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void addSection(JPanel panel, String title, JComponent... components) {
        JPanel sectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sectionPanel.setBackground(new Color(255, 248, 231));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        sectionPanel.add(titleLabel);
        for (JComponent component : components) {
            sectionPanel.add(component);
        }
        panel.add(sectionPanel);
    }

    private void initializeItemPrices() {
        itemPrices = new HashMap<>();
        itemPrices.put("Espresso", 2.0);
        itemPrices.put("Latte", 3.5);
        itemPrices.put("Cappuccino", 4.0);
        itemPrices.put("Americano", 2.5);
        itemPrices.put("Small", 0.0);
        itemPrices.put("Medium", 1.0);
        itemPrices.put("Large", 2.0);
        itemPrices.put("Milk", 0.5);
        itemPrices.put("Sugar", 0.2);
        itemPrices.put("Whipped Cream", 0.8);
    }

    private void placeOrder() {
        if (isAdminLoggedIn) {
            // Implement admin-related functionality
        } else {
            // Implement user-related functionality
            StringBuilder orderSummary = new StringBuilder("Order Summary:\n");
            double totalPrice = 0.0;
            String selectedCoffeeType = (String) coffeeTypeComboBox.getSelectedItem();
            orderSummary.append(
                    String.format("Coffee Type: %s - $%.2f\n", selectedCoffeeType, itemPrices.get(selectedCoffeeType)));
            totalPrice += itemPrices.get(selectedCoffeeType);
            for (Enumeration<AbstractButton> buttons = sizeButtonGroup.getElements(); buttons.hasMoreElements();) {
                AbstractButton button = buttons.nextElement();
                if (button.isSelected()) {
                    orderSummary.append(
                            String.format("Size: %s - $%.2f\n", button.getText(), itemPrices.get(button.getText())));
                    totalPrice += itemPrices.get(button.getText());
                }
            }
            if (milkCheckBox.isSelected()) {
                orderSummary.append(String.format("Extras: Milk - $%.2f\n", itemPrices.get("Milk")));
                totalPrice += itemPrices.get("Milk");
            }
            if (sugarCheckBox.isSelected()) {
                orderSummary.append(String.format("Extras: Sugar - $%.2f\n", itemPrices.get("Sugar")));
                totalPrice += itemPrices.get("Sugar");
            }
            if (whippedCreamCheckBox.isSelected()) {
                orderSummary.append(String.format("Extras: Whipped Cream - $%.2f\n", itemPrices.get("Whipped Cream")));
                totalPrice += itemPrices.get("Whipped Cream");
            }
            orderSummary.append(String.format("Total Price: $%.2f\n", totalPrice));
            summaryTextArea.setText(orderSummary.toString());
            insertOrderIntoDatabase(orderSummary.toString(), totalPrice);

            // Ask for payment
            showPaymentDialog(totalPrice);
        }
    }

    private void showPaymentDialog(double totalPrice) {
        String[] paymentOptions = { "Credit Card", "Debit Card", "Cash" };
        int paymentChoice = JOptionPane.showOptionDialog(this, "Select payment option:", "Payment",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, paymentOptions, paymentOptions[0]);

        if (paymentChoice != JOptionPane.CLOSED_OPTION) {
            String paymentMethod = paymentOptions[paymentChoice];
            JOptionPane.showMessageDialog(this, String
                    .format("Payment successful! Total Amount: $%.2f\nPayment Method: %s", totalPrice, paymentMethod));
        }
    }

    private void insertOrderIntoDatabase(String orderDetails, double totalPrice) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO orders (order_details, total_price) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, orderDetails);
                preparedStatement.setDouble(2, totalPrice);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception (show a message, log, etc.)
        }
    }

    private void showSummary() {
        placeOrder();
    }

    private void showRegisterDialog() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField,
                "Confirm Password:", confirmPasswordField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Register", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (password.equals(confirmPassword)) {
                if (registerUser(username, password)) {
                    JOptionPane.showMessageDialog(this, "Registration successful! Welcome, " + username + "!");
                } else {
                    JOptionPane.showMessageDialog(this, "Registration failed. Please try again.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Passwords do not match. Please try again.");
            }
        }
    }

    private boolean registerUser(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                return preparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception (show a message, log, etc.)
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CoffeeShopGUI coffeeShopGUI = new CoffeeShopGUI();
            coffeeShopGUI.showLoginDialog();
        });
    }

    private void showLoginDialog() {
        String[] options = { "Existing User", "Create User", "Continue as Guest" };
        int choice = JOptionPane.showOptionDialog(this, "Login or create a new user?", "Login",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            // Existing user login
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            Object[] message = {
                    "Username:", usernameField,
                    "Password:", passwordField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Login", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (validateUser(username, password)) {
                    isAdminLoggedIn = true;
                    JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + username + "!");
                } else {
                    isAdminLoggedIn = false;
                    JOptionPane.showMessageDialog(this, "Invalid credentials. Continuing as a guest.");
                }
            }
        } else if (choice == 1) {
            // Create new user
            showRegisterDialog();
        } else {
            // Continue as a guest
            isAdminLoggedIn = false;
        }
    }

    private boolean validateUser(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception (show a message, log, etc.)
            return false;
        }
    }
}

class DatabaseConnection {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "system";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC Driver not found", e);
        }
    }
}