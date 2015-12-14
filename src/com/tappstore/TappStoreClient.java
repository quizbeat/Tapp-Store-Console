package com.tappstore;

import java.math.BigDecimal;
import java.sql.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TappStoreClient
{
    private static final String host = "jdbc:postgresql://localhost:5432/";
    private static final String databaseName = "tapp_store";
    private String username;
    private String password;

    private Connection conn;


    // INITIALIZATION

    protected TappStoreClient() { }

    public TappStoreClient(String username, String password)
    {
        this.username = username;
        this.password = password;
    }


    // DATABASE CONNECTION

    public void openConnection()
    {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }

        try {
            this.conn = DriverManager.getConnection(
                this.host + this.databaseName, this.username, this.password);
            System.out.println("> Database connected\n");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void closeConnection()
    {
        try {
            this.conn.close();
            System.out.println("> Database disconnected\n");
        } catch (SQLException exception) {
            printErrorForSQLException(exception);
        }
    }


    // APPLICATIONS

    public void addApplication(String appName, String description, int categoryID,
                               BigDecimal price, int appVersion, BigDecimal appSize, int devID)
    {
        try {
            PreparedStatement statement = this.conn.prepareStatement(
                    "INSERT INTO application " +
                    "(app_name, description, category_id, price, app_version, app_size, developer_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, appName);
            statement.setString(2, description);
            statement.setInt(3, categoryID);
            statement.setBigDecimal(4, price);
            statement.setInt(5, appVersion);
            statement.setBigDecimal(6, appSize);
            statement.setInt(7, devID);

            statement.executeUpdate();

            System.out.println(String.format("> Application %s sent to moderation\n", appName));
        } catch (SQLException exception) {
            printErrorForSQLException(exception);
        }
    }

    public void printAllApplications()
    {
        try {
            Statement statement = this.conn.createStatement();
            String query =
                    "SELECT a.app_id, a.app_name, c.category_name, a.price::numeric, a.status " +
                            "FROM application a " +
                            "INNER JOIN category c " +
                            "ON a.category_id = c.category_id " +
                            "ORDER BY app_id ASC";
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("***************************** " +
                    "APPLICATIONS LIST " +
                    "*****************************\n");
            System.out.println(String.format("%-10s %-20s %-20s %-10s %-20s\n",
                    "App id", "App name", "Category", "Price", "Status"));

            while (resultSet.next()) {
                int app_id = resultSet.getInt("app_id");
                String app_name = resultSet.getString("app_name");
                String category = resultSet.getString("category_name");
                BigDecimal price = resultSet.getBigDecimal("price");
                String status = resultSet.getString("status");
                System.out.println(String.format("%-10d %-20s %-20s %-10s %-20s",
                        app_id, app_name, category, NumberFormat.getCurrencyInstance().format(price), status));
            }
            System.out.print("\n");
        } catch (SQLException exception) {
            printErrorForSQLException(exception);
        }
    }


    // USERS

    public void registerUser(String firstName, String lastName, String birthDate, String gender, String email)
    {
        try {
            // prepare date
            SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy");
            java.sql.Date date = new java.sql.Date(format.parse(birthDate).getTime());

            PreparedStatement registerStatement = this.conn.prepareStatement(
                      "INSERT INTO person "
                    + "(first_name, last_name, birth_date, gender, email) "
                    + "VALUES (?, ?, ?, ?, ?)");

            registerStatement.setString(1, firstName);
            registerStatement.setString(2, lastName);
            registerStatement.setDate(3, date);
            registerStatement.setString(4, gender.toLowerCase());
            registerStatement.setString(5, email);

            registerStatement.executeUpdate();

            System.out.println(String.format("> User %s registered\n", firstName));
        } catch (SQLException exception) {
            printErrorForSQLException(exception);
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
    }

    public void removeUser(int id)
    {
        try {
            PreparedStatement statement = this.conn.prepareStatement(
                    "DELETE FROM person WHERE person_id = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println(String.format("> User #%d removed\n", id));
        } catch (SQLException exception) {
            printErrorForSQLException(exception);
        }
    }

    public void printAllUsers()
    {
        try {
            Statement statement = this.conn.createStatement();
            String query =
                    "SELECT person_id, first_name, last_name, birth_date, gender, email, balance::numeric " +
                            "FROM person " +
                            "ORDER BY person_id ASC ";
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("********************************************************* " +
                               "USERS LIST " +
                               "*********************************************************\n");
            System.out.println(String.format("%-10s %-20s %-20s %-15s %-10s %-30s %-15s\n",
                    "User id", "First name", "Last name", "Birth date", "Gender", "Email", "Balance"));

            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");


            while (resultSet.next()) {
                int person_id = resultSet.getInt("person_id");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                java.util.Date birth_date = resultSet.getTimestamp("birth_date");
                String gender = resultSet.getString("gender");
                String email = resultSet.getString("email");
                BigDecimal balance = resultSet.getBigDecimal("balance");
                System.out.println(String.format("%-10d %-20s %-20s %-15s %-10s %-30s %-15s",
                        person_id, first_name, last_name, df.format(birth_date), gender, email,
                        NumberFormat.getCurrencyInstance().format(balance)));
            }
            System.out.print("\n");
        } catch (SQLException exception) {
            printErrorForSQLException(exception);
        }
    }


    // DEVELOPERS

    public void printAllDevelopers()
    {
        try {
            Statement statement = this.conn.createStatement();
            String query =
                    "SELECT d.person_id, p.first_name, p.last_name " +
                            "FROM developer d " +
                            "INNER JOIN person p " +
                            "ON d.person_id = p.person_id " +
                            "ORDER BY d.person_id ASC ";
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("***************** DEVELOPERS LIST *****************");
            System.out.println(String.format("%-15s %-20s %-20s", "User id", "First name", "Last name"));

            while (resultSet.next()) {
                int person_id = resultSet.getInt("person_id");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                System.out.println(String.format("%-15d %-20s %-20s", person_id, first_name, last_name));
            }
            System.out.print("\n");
        } catch (SQLException exception) {
            printErrorForSQLException(exception);
        }
    }

    public void addDeveloperStatusToUser(int id)
    {
        try {
            PreparedStatement statement = this.conn.prepareStatement(
                    "INSERT INTO developer VALUES (?)"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println(String.format("> User #%d is developer now\n", id));
        } catch (SQLException exception) {
            printErrorForSQLException(exception);
        }
    }

    public void removeDeveloperStatusFromUser(int id)
    {
        try {
            PreparedStatement statement = this.conn.prepareStatement(
                    "DELETE FROM developer WHERE person_id = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println(String.format("> User #%d is not developer anymore\n", id));
        } catch (SQLException exception) {
            printErrorForSQLException(exception);
        }
    }


    // MODERATORS

    public void addModeratorStatusToUser(int id)
    {
        try {
            PreparedStatement statement = this.conn.prepareStatement(
                    "INSERT INTO moderator VALUES (?)"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println(String.format("> User #%d is moderator now\n", id));
        } catch (SQLException exception) {
            printErrorForSQLException(exception);
        }
    }

    public void removeModeratorStatusFromUser(int id)
    {
        try {
            PreparedStatement statement = this.conn.prepareStatement(
                    "DELETE FROM moderator WHERE person_id = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println(String.format("> User #%d is not moderator anymore\n", id));
        } catch (SQLException exception) {
            printErrorForSQLException(exception);
        }
    }

    public void printAppsForModeration()
    {
        try {
            Statement statement = this.conn.createStatement();
            String query = "SELECT * FROM apps_for_moderation()";
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("***************** PUBLICATION REQUESTS *****************\n");
            System.out.println(String.format("%-10s %-20s %-10s %-20s\n", "Request", "Date", "App id", "App name"));
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            while (resultSet.next()) {
                int request_id = resultSet.getInt("request_id");
                java.util.Date request_date = resultSet.getTimestamp("request_date");
                int app_id = resultSet.getInt("app_id");
                String  app_name = resultSet.getString("app_name");
                System.out.println(String.format("%-10d %-20s %-10d %-20s", request_id, df.format(request_date), app_id, app_name));
            }
            System.out.print("\n");
        } catch (SQLException exception) {
            printErrorForSQLException(exception);
        }
    }


    // HELPER METHODS

    private static void printErrorForSQLException(SQLException exception)
    {
        System.err.println("SQLState: " + exception.getSQLState());
        System.err.println("Error code: " + exception.getErrorCode());
        System.err.println("Message: " + exception.getMessage());
    }
}
