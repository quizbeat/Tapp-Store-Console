package com.tappstore;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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

    public void removeDeveloperStatusFromUser(int id)
    {
        try {
            PreparedStatement statement = this.conn.prepareStatement(
                    "DELETE FROM developer WHERE developer_id = ?"
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
                    "DELETE FROM moderator WHERE moderator_id = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println(String.format("User #%d is not moderator anymore", id));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
