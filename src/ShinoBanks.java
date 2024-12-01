import java.sql.*;
import java.text.NumberFormat;

public class ShinoBanks {
    final private String databaseName = "Accounts";
    final static String url = "jdbc:mysql://localhost:3306";
    final static String username = "root";
    final static String password = "Mikkichan1.";

    Connection connection = null;
    Statement statement = null;
    ResultSet resultset = null;

    int accountNum;

    public ShinoBanks() {
        String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        String checkDatabaseQuery = "SHOW DATABASES LIKE '" + databaseName + "'";
        String useDatabaseQuery = "USE " + databaseName;

        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            resultset = statement.executeQuery(checkDatabaseQuery);
            if (resultset.next()) {
                statement.executeUpdate(useDatabaseQuery);
                System.out.println("Database and table loaded");
            } else {
                statement.executeUpdate(createDatabaseQuery);
                System.out.println("Database and table created");
                statement.executeUpdate(useDatabaseQuery);
                createTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createTable() {
        String createTableQuery = "CREATE TABLE Accounts (" +
                "AccountNumber INT, " +
                "username VARCHAR(50), " +
                "fName VARCHAR(100) DEFAULT NULL, " +
                "lName VARCHAR(100) DEFAULT NULL, " +
                "Pin INT DEFAULT NULL, " +
                "Balance INT DEFAULT 0, " +
                "PRIMARY KEY (username, AccountNumber))";

        try {
            statement.executeUpdate(createTableQuery);
            System.out.println("Table 'accounts' created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    boolean checkAccount(String username, int pin) {
        String checkAccountQuery = "SELECT * FROM accounts WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkAccountQuery)) {

            preparedStatement.setString(1, username);
            resultset = preparedStatement.executeQuery();

            if (resultset.next()) {
                int acc_pin = resultset.getInt("Pin");
                if (acc_pin == pin) {
                    accountNum = resultset.getInt("AccountNumber");
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    int registerAccount(String username, String fname, String lname, int pin, int accountNumber) {
        String insertInfoQuery = "INSERT INTO Accounts (AccountNumber, username, fName, lName, Pin, Balance) VALUES (?, ?, ?, ?, ?, 0)";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(insertInfoQuery)) {
            preparedStatement.setInt(1, accountNumber);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, fname);
            preparedStatement.setString(4, lname);
            preparedStatement.setInt(5, pin);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) insert into: accounts.");
            return accountNumber;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getAccountInfo() {
        String query = "SELECT fName, lName, Balance FROM Accounts WHERE AccountNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, accountNum);
            resultset = preparedStatement.executeQuery();
            if (resultset.next()) {
                String firstName = resultset.getString("fName");
                String lastName = resultset.getString("lName");
                int balance = resultset.getInt("Balance");
                String formattedBalance = NumberFormat.getInstance().format(balance);

                return "Account Number: " + accountNum + "\n" +
                        "First Name: " + firstName + "\n" +
                        "Last Name: " + lastName + "\n" +
                        "Balance: " + formattedBalance;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Account not found";
    }

    void Deposit(int amount) {
        String updateBalanceQuery = "UPDATE Accounts SET Balance = Balance + ? WHERE AccountNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateBalanceQuery)) {
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, accountNum);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Deposit successful. Updated balance for account: " + accountNum);
            } else {
                System.out.println("No account found with the provided account number.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updatePin(int newPin) {
        String updateBalanceQuery = "UPDATE Accounts SET Pin = ? WHERE AccountNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateBalanceQuery)) {
            preparedStatement.setInt(1, newPin);
            preparedStatement.setInt(2, accountNum);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Successfully update pin");
            } else {
                System.out.println("No account found with the provided account number.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    boolean Withdraw(int amount) {
        String AccInfoQuery = "SELECT Balance FROM Accounts WHERE AccountNumber = ?";
        String updateBalanceQuery = "UPDATE Accounts SET BALANCE = BALANCE - ? WHERE AccountNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(AccInfoQuery);
                PreparedStatement updateStatement = connection.prepareStatement(updateBalanceQuery)) {
            preparedStatement.setInt(1, accountNum);
            resultset = preparedStatement.executeQuery();
            if (resultset.next()) {
                int balance = resultset.getInt("Balance");
                if (amount > balance) {
                    return false;
                } else {
                    updateStatement.setInt(1, amount);
                    updateStatement.setInt(2, accountNum);
                    int rowsAffected = updateStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        return true;
                    } else {
                        return false;
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}