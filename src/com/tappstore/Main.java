package com.tappstore;

public class Main
{
    private static final String usename = "nikita";
    private static final String password = "123";

    private static TappStoreClient ts;

    public static void main(String[] args)
    {
        ts = new TappStoreClient(usename, password);
        ts.openConnection();

        test1();

        ts.closeConnection();
    }

    public static void test1()
    { // add/remove developer
        ts.allDevelopers();

        ts.addDeveloperStatusToUser(13);
        ts.allDevelopers();

        ts.removeDeveloperStatusFromUser(13);
        ts.allDevelopers();
    }

    public static void test2()
    { // add/remove user
        //ts.registerUser("Kenny", "McCormick", "02/02/1997", "male", "kenny@spark.com");
    }

    public static void test3()
    { // apps for moderation
        ts.appsForModeration();
    }

}
