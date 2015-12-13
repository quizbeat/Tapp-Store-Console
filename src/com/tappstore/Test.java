package com.tappstore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Test
{
    public static void main(String[] args)
    {
        String host = "jdbc:postgresql://localhost:5432/";
        String dbname = "tapp_store";
        String username = "nikita";
        String password = "123";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }

        try {
            Connection conn = DriverManager.getConnection(
                    host + dbname, username, password);
            System.out.println("Database connected");
            conn.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
