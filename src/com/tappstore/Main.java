package com.tappstore;

public class Main
{
    public static void main(String[] args)
    {
        String usename = "nikita";
        String password = "123";
        TappStoreClient ts = new TappStoreClient(usename, password);
        ts.openConnection();

        //ts.registerUser("Kenny", "McCormick", "02/02/1997", "male", "kenny@spark.com");
        //ts.appsForModeration();
        ts.allDevelopers();

        //ts.removeDeveloperStatusFromUser(3);

        ts.closeConnection();
    }
}
