import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class ShinoBankSystemGUI {
    private JFrame frame;
    private JPanel cardPanel;
    private JPanel loginPanel, registerPanel, accountInfoPanel;
    static ShinoBanks shinobank = new ShinoBanks();

    public ShinoBankSystemGUI() {
        frame = new JFrame("Shino Banks");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 400);
        frame.setLocationRelativeTo(null);

        cardPanel = new JPanel(new CardLayout());

        createLoginPanel();
        createRegisterPanel();
        createAccountInfoPanel();

        cardPanel.add(loginPanel, "Login");
        cardPanel.add(registerPanel, "Register");
        cardPanel.add(accountInfoPanel, "AccountInfo");

        frame.add(cardPanel);
        ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Login");

        frame.setVisible(true);
    }

    // Handle login
    private void handleLogin(String username, int pin) {
        boolean login = shinobank.checkAccount(username, pin);
        if (login) {
            updateAccountInfo();
            ((CardLayout) cardPanel.getLayout()).show(cardPanel, "AccountInfo");
            JOptionPane.showMessageDialog(frame, "Successfully logged in!", "Login Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Incorrect username or PIN.", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Handle registration
    private void handleRegistration(String username, String fname, String lname, int pin, int accountNumber) {
        int status = shinobank.registerAccount(username, fname, lname, pin, accountNumber);
        if (status != 0) {
            JOptionPane.showMessageDialog(frame, "Account successfully created.");
            ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Login");
        } else {
            JOptionPane.showMessageDialog(frame, "Registration failed. Try again.");
        }
    }

    private static int genAccountNumber() {
        double randomNum = Math.random();
        int value = (int) (randomNum * (10_000_000 - 1_000_000 + 1)) + 1_000_000;
        return value;
    }

    // Login Panel
    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(250, 250, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Shino Banks Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 150, 136));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(titleLabel, gbc);

        // Username Field
        JTextField usernameField = new JTextField(15);
        styleInputField(usernameField, "Username");
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        loginPanel.add(usernameField, gbc);

        // PIN Code Field
        JTextField pinField = new JTextField(15);
        styleInputField(pinField, "Pin Code");
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginPanel.add(pinField, gbc);

        // Login Button
        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);

        // Register Link
        JButton registerLink = new JButton("Don't have an account? Register");
        styleLinkButton(registerLink);
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(registerLink, gbc);

        // hyperlink of register
        registerLink.addActionListener(e -> ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Register"));

        // login button function
        loginButton.addActionListener(e -> {
            try {
                String username = usernameField.getText();
                int pin = Integer.parseInt(pinField.getText());
                handleLogin(username, pin);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid username and PIN.");
            }
        });
    }

    // Register Panel
    private void createRegisterPanel() {
        registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBackground(new Color(250, 250, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Shino Banks Registration");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 150, 136));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerPanel.add(titleLabel, gbc);

        // Username Field
        JTextField usernameField = new JTextField(15);
        styleInputField(usernameField, "Username");
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        registerPanel.add(usernameField, gbc);

        // First Name Field
        JTextField firstNameField = new JTextField(15);
        styleInputField(firstNameField, "First Name");
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        registerPanel.add(firstNameField, gbc);

        // Last Name Field
        JTextField lastNameField = new JTextField(15);
        styleInputField(lastNameField, "Last Name");
        gbc.gridy = 3;
        registerPanel.add(lastNameField, gbc);

        // PIN Field
        JTextField pinField = new JTextField(15);
        styleInputField(pinField, "PIN");
        gbc.gridy = 4;
        registerPanel.add(pinField, gbc);

        JButton registerButton = new JButton("Register");
        styleButton(registerButton);
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        registerPanel.add(registerButton, gbc);

        JButton backButton = new JButton("Already have an account? Login");
        styleLinkButton(backButton);
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerPanel.add(backButton, gbc);

        backButton.addActionListener(e -> ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Login"));

        registerButton.addActionListener(e -> {
            try {
                String username = usernameField.getText();
                int pin = Integer.parseInt(pinField.getText());
                int accountNum = genAccountNumber();
                handleRegistration(username, firstNameField.getText(), lastNameField.getText(), pin, accountNum);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid information.");
            }
        });
    }

    // Account Info Panel
    private void createAccountInfoPanel() {
        accountInfoPanel = new JPanel(new GridBagLayout());
        accountInfoPanel.setBackground(new Color(250, 250, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Account Info");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 150, 136));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        accountInfoPanel.add(titleLabel, gbc);

        // Account Info (First Name, Last Name, Balance)
        JLabel accountDetailsLabel = new JLabel("Loading account details...");
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        accountInfoPanel.add(accountDetailsLabel, gbc);

        // Deposit Button
        JButton depositButton = new JButton("Deposit");
        styleButton(depositButton);
        gbc.gridy = 2;
        accountInfoPanel.add(depositButton, gbc);

        // Withdraw Button
        JButton withdrawButton = new JButton("Withdraw");
        styleButton(withdrawButton);
        gbc.gridy = 3;
        accountInfoPanel.add(withdrawButton, gbc);

        // Change PIN Button
        JButton changePinButton = new JButton("Change PIN");
        styleButton(changePinButton);
        gbc.gridy = 4;
        accountInfoPanel.add(changePinButton, gbc);

        // Logout Button
        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton);
        gbc.gridy = 5;
        accountInfoPanel.add(logoutButton, gbc);

        depositButton.addActionListener(e -> {
            String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
            try {
                int amount = Integer.parseInt(amountStr);
                if (amount > 0) {
                    shinobank.Deposit(amount);
                    updateAccountInfo();
                    JOptionPane.showMessageDialog(frame, "Successfully deposited " + amount + " to your account.",
                            "Deposit Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a positive amount.", "Invalid Amount",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount entered. Please enter a valid number.",
                        "Invalid Amount", JOptionPane.ERROR_MESSAGE);
            }
        });

        withdrawButton.addActionListener(e -> {
            String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
            try {
                int amount = Integer.parseInt(amountStr);

                if (amount > 0) {
                    boolean success = shinobank.Withdraw(amount);

                    if (success) {
                        updateAccountInfo();
                        JOptionPane.showMessageDialog(frame,
                                "Successfully withdrew " + NumberFormat.getInstance().format(amount)
                                        + " from your account.",
                                "Withdrawal Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Insufficient funds or error occurred during withdrawal.",
                                "Withdrawal Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a positive amount.", "Invalid Amount",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount entered. Please enter a valid number.",
                        "Invalid Amount", JOptionPane.ERROR_MESSAGE);
            }
        });

        changePinButton.addActionListener(e -> {
            String newPinStr = JOptionPane.showInputDialog(frame, "Enter new PIN:");
            try {
                int newPin = Integer.parseInt(newPinStr);
                shinobank.updatePin(newPin);
                JOptionPane.showMessageDialog(frame, "PIN updated successfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid PIN entered.");
            }
        });

        logoutButton.addActionListener(e -> {
            shinobank.accountNum = 0;
            ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Login");
            JOptionPane.showMessageDialog(frame, "Successfully logged out.", "Logout Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    // Update account info on Account Info panel
    private void updateAccountInfo() {
        if (shinobank.accountNum != 0) {
            String accountDetails = shinobank.getAccountInfo();
            JLabel accountDetailsLabel = (JLabel) accountInfoPanel.getComponent(1);
            accountDetailsLabel.setText("<html>" + accountDetails.replace("\n", "<br>") + "</html>");
        } else {
            JLabel accountDetailsLabel = (JLabel) accountInfoPanel.getComponent(1);
            accountDetailsLabel.setText("Account not found.");
        }
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBackground(new Color(0, 150, 136));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void styleInputField(JTextField field, String placeholder) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void styleLinkButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(new Color(0, 150, 136));
        button.setBackground(new Color(250, 250, 250));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
    }

    public static void main(String[] args) {
        new ShinoBankSystemGUI();
    }
}
