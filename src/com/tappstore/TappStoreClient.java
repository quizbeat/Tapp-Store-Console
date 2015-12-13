package com.tappstore;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TappStoreClient
{
    private String host = "jdbc:postgresql://localhost:5432/";
    private String databaseName = "tapp_store";
    private String username;
    private String password;

    private Connection conn;

    protected TappStoreClient() { }

    public TappStoreClient(String username, String password)
    {
        this.databaseName = databaseName;
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

    public void register(String firstName, String lastName, String birthDate, String gender, String email)
    {
        try {
            //int id = nextKeyForTable("person");
            // prepare date
            SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy");
            //java.sql.Date date = (java.sql.Date)format.parse(birthDate);
            java.sql.Date date = new java.sql.Date(format.parse(birthDate).getTime());

            PreparedStatement registerStatement = this.conn.prepareStatement(
                      "INSERT INTO person "
                    + "(first_name, last_name, birth_date, gender, email) "
                    + "VALUES (?, ?, ?, ?, ?)");

            //registerStatement.setInt(1, id);
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

    private int nextKeyForTable(String tableName)
    {
        int nextKey = 0;
        try {
            String table = tableName + "_" + tableName + "_id_seq";
            PreparedStatement statement = this.conn.prepareStatement("SELECT nextval(?)");
            statement.setString(1, table);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            nextKey = resultSet.getInt(1);
            System.out.println(String.format("next key %d", nextKey));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return nextKey;
    }

}
