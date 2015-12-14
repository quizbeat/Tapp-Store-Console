package com.tappstore;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.text.DateFormat;

public class TappStoreClient
{
    private static final String host = "jdbc:postgresql://localhost:5432/";
    private static final String databaseName = "tapp_store";
    private String username;
    private String password;

    private Connection conn;

    protected TappStoreClient() { }

    public TappStoreClient(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

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
            System.out.println("Database connected");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void closeConnection()
    {
        try {
            this.conn.close();
            System.out.println("Database disconnected");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

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

            System.out.println(String.format("User %s registered", firstName));
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ParseException exception) {
            exception.printStackTrace();
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
            System.out.println(String.format("User #%d is developer now", id));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void addModeratorStatusToUser(int id)
    {
        try {
            PreparedStatement statement = this.conn.prepareStatement(
                    "INSERT INTO moderator VALUES (?)"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println(String.format("User #%d is moderator now", id));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void allDevelopers()
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

            System.out.println(String.format("%-15s %-20s %-20s", "Person id", "First name", "Last name"));

            while (resultSet.next()) {
                int person_id = resultSet.getInt("person_id");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                System.out.println(String.format("%-15d %-20s %-20s", person_id, first_name, last_name));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
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
            System.out.println(String.format("User #%d is not developer anymore", id));
        } catch (SQLException exception) {
            exception.printStackTrace();
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
            System.out.println(String.format("User #%d is not moderator anymore", id));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void appsForModeration()
    {
        try {
            Statement statement = this.conn.createStatement();
            String query = "SELECT * FROM apps_for_moderation()";
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("Request\t\tDate\t\t\tApp id\t\tApp name");
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            while (resultSet.next()) {
                int request_id = resultSet.getInt("request_id");
                java.util.Date request_date = resultSet.getTimestamp("request_date");
                int app_id = resultSet.getInt("app_id");
                String  app_name = resultSet.getString("app_name");
                System.out.println(String.format("%d\t\t\t%s\t\t%d\t\t\t%s", request_id, df.format(request_date), app_id, app_name));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
