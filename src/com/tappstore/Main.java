package com.tappstore;

import java.math.BigDecimal;

public class Main
{
    private static final String usename = "nikita";
    private static final String password = "123";

    private static TappStoreClient ts;

    public static void main(String[] args)
    {
        ts = new TappStoreClient(usename, password);
        ts.openConnection();

        test5();

        ts.closeConnection();
    }

    public static void test1()
    { // add/remove developer
        ts.printAllDevelopers();

        ts.addDeveloperStatusToUser(13);
        ts.printAllDevelopers();

        ts.removeDeveloperStatusFromUser(13);
        ts.printAllDevelopers();
    }

    public static void test2()
    { // add user
        ts.printAllUsers();
        ts.registerUser("Sergey", "Brin", "08/21/1973", "male", "brin@google.com");
        ts.printAllUsers();
    }

    public static void test3()
    { // remove user
        ts.printAllUsers();
        ts.removeUser(100500);
        ts.printAllUsers();
    }

    public static void test4()
    { // add app
        ts.printAllApplications();

        BigDecimal price = BigDecimal.valueOf(0.0);
        BigDecimal size = BigDecimal.valueOf(32.4);
        ts.addApplication("Yo", "Just say Yo!", 3, price, 1, size, 4);

        ts.printAllApplications();
    }

    public static void test5()
    { // apps for moderation
        ts.printAppsForModeration();
    }

}
