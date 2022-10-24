package main.java;
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)

import Objects.Item;
import Objects.Beverages;
import Objects.Candy;
import Objects.Chips;
import Objects.Gum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class VendingMachineCLI {

    public static Scanner userInput = new Scanner(System.in);
    public static Map<String, Item> itemList = new HashMap<>(); // Map of vending machine items: (Code, Item)
    public static final int MAX_QTY = 5;
    public static double customerBalance = 0.0;

    public static void main(String[] args) {

        // Finds inventory file
        File inventoryFile = new File("vendingmachine.csv");

        // Reads inventory file and splits each line by pipe
        try (Scanner readFile = new Scanner(inventoryFile)) {
            while (readFile.hasNextLine()) {
                String currentLine = readFile.nextLine();
                String[] currentArray = currentLine.split("\\|");

                // Instantiates all items (fully stock), and adds item to itemList Map
                switch (currentArray[3]) {
                    case "Chip" -> {
                        // Create Chip with name, price, quantity
                        Chips chip = new Chips(currentArray[1], Double.parseDouble(currentArray[2]), 5);
                        itemList.put(currentArray[0], chip);
                    }
                    case "Candy" -> {
                        // Create Candy with name, price, quantity
                        Candy candies = new Candy(currentArray[1], Double.parseDouble(currentArray[2]), 5);
                        itemList.put(currentArray[0], candies);
                    }
                    case "Drink" -> {
                        // Create Beverage with name, price, quantity
                        Beverages drinks = new Beverages(currentArray[1], Double.parseDouble(currentArray[2]), 5);
                        itemList.put(currentArray[0], drinks);
                    }
                    case "Gum" -> {
                        // Create Gum with name, price, quantity
                        Gum gum = new Gum(currentArray[1], Double.parseDouble(currentArray[2]), 5);
                        itemList.put(currentArray[0], gum);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("");
            System.out.println("ERROR:");
            System.out.println("File not found");
        }

        // Opens main menu
        mainMenu();
    }

    public static void mainMenu() {

        while (true) {

            System.out.println("");
            System.out.println("--- Vending Machine ---");
            System.out.println("(1) Display Vending Machine Items");
            System.out.println("(2) Purchase");
            System.out.println("(3) Exit");
            System.out.print("Please select an option: ");

            // Saves user input as mainMenuInput
            String mainMenuInput = userInput.nextLine();

            switch (mainMenuInput) {
                // Option 1: Display Vending Machine Items
                case "1" -> {
                    displayItemList();
                    System.out.print("\nPress R to return to the main menu: ");

                    // Loops error message until correct key is pressed
                    String returnToMain = userInput.nextLine();
                    while (!returnToMain.equalsIgnoreCase("R")) {
                        System.out.println("");
                        System.out.println("ERROR:");
                        System.out.println("Invalid response. Please try again.");
                        System.out.print("Press R to return to the main menu: ");
                        returnToMain = userInput.nextLine();
                    }
                }

                // Option 2: Purchase Menu
                case "2" -> {
                    purchaseMenu();
                    return;
                }

                // Option 3: Exit Program
                case "3" -> {
                    if (customerBalance > 0.0) {
                        calcChange((int)(customerBalance * 100.0));
                    }

                    System.out.println("Goodbye.");
                    clearLog();
                    System.exit(0);
                }

                // Any other input
                default -> {
                    System.out.println("");
                    System.out.println("ERROR:");
                    System.out.println("Invalid response. Please try again");
                }
            }
        }
    }

    public static void purchaseMenu() {

        while (true) {

            // Display purchase menu and customer's balance
            System.out.println("");
            System.out.println(" --- Item Purchase ---");
            System.out.printf("Current Money Provided: $%.2f", customerBalance);
            System.out.println("\n(1) Feed Money");
            System.out.println("(2) Select Product");
            System.out.println("(3) Finish Transaction");
            System.out.print("Please select an option: ");
            String purchaseChoice = userInput.nextLine();


            switch (purchaseChoice) {
                // Option 1: Feed Money
                case "1" -> {
                    System.out.println("");
                    System.out.println(" --- Feed Money --- ");
                    System.out.printf("Current Money Provided: $%.2f", customerBalance);
                    System.out.print("\nEnter the amount of money you would like to insert (whole dollars only): ");
                    String dollarAmount = userInput.nextLine();

                    // Adds funds to customerBalance only if amount is a whole dollar value
                    double feedMoney = Double.parseDouble(dollarAmount);
                    if (((feedMoney * 100.0) % 100.0 == 0.0) && feedMoney > 0.0) {
                        customerBalance += feedMoney;
                        logFeedMoney(feedMoney, customerBalance);
                    } else {
                        System.out.println("");
                        System.out.println("ERROR:");
                        System.out.println("Must be a whole dollar amount greater than zero. Please try again.");
                    }
                }

                // Option 2: Show List of Available Products
                case "2" -> dispenseItem();

                // Option 3: Return Change
                case "3" -> {
                    if (customerBalance > 0.0) {
                        int[] coins = calcChange((int) (customerBalance * 100.0));
                        logChange(customerBalance);
                        customerBalance = 0.0;
                    }
                    mainMenu();
                }

                // Any other input
                default -> {
                    System.out.println("");
                    System.out.println("ERROR:");
                    System.out.println("Invalid response. Please try again.");
                }
            }
        }
    }

    public static void dispenseItem() {
        while (true) {

            // Print list of available products
            System.out.println("");
            for (Map.Entry<String, Item> item : itemList.entrySet()) {
                String key = (String)item.getKey();
                Item value = (Item)item.getValue();

                if (item.getValue().getQuantity() >= 1) {
                    System.out.printf("Enter " + key + " for " + value.getName() + " (Price: %.2f, Quantity: " + value.getQuantity() + ")\n", value.getPrice());
                } else {
                    // If quantity = 0, indicate that item is sold out
                    System.out.println("Enter " + key + " for " + value.getName() + " (SOLD OUT)");
                }
            }
            System.out.print("Enter the 2-digit code for the item you would like to purchase (C to cancel): ");

            // Compare user input to available products
            String itemChoice = userInput.nextLine().toUpperCase();

            // If code is NOT in Map
            if (!itemList.containsKey(itemChoice)) {
                // C to cancel
                if (itemChoice.equalsIgnoreCase("C")) {
                    mainMenu();
                } else {
                // If input is invalid
                    System.out.println("");
                    System.out.println("ERROR:");
                    System.out.println("Invalid code. Please try again.");
                }

            // If code IS in Map
            } else {
                Item choice = itemList.get(itemChoice);
                // If item is sold out
                if (choice.getQuantity() == 0) {
                    System.out.println("");
                    System.out.println("ERROR:");
                    System.out.println("Item is sold out. Please try again.");
                // If item is available, check for sufficient funds
                } else if (customerBalance > choice.getPrice()) {
                    System.out.println("");
                    System.out.println("Dispensing...");
                    System.out.printf("\nEnjoy your " + choice.getName() + " for $%.2f", choice.getPrice());
                    System.out.println("\n" + choice.yumYumStatement());
                    customerBalance -= choice.getPrice();
                    choice.removeOne();
                    logPurchase(choice, itemChoice);
                    purchaseMenu();
                // If insufficient funds
                } else {
                    System.out.println("");
                    System.out.println("ERROR:");
                    System.out.println("Insufficient funds.");
                    mainMenu();
                }
            }
        }
    }

    public static void displayItemList() {

        for (Map.Entry<String, Item> item : itemList.entrySet()) {
            Item value = item.getValue();
            System.out.printf("\n" + value.getName() + " (Price: $%.2f, Quantity: " + value.getQuantity() + ")", value.getPrice());
        }
    }

    public static void logFeedMoney(double deposited, double balance) {

        // Finds log file
        File logFile = new File("src/main/resources/Log.txt");

        // Writes to log file
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(logFile, true))) {

            // Gets and formats date/time
            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a");
            writer.printf(dateFormat.format(currentDate) + " FEED MONEY: $%.2f $%.2f\n", deposited, balance);
        } catch (FileNotFoundException e) {
            System.out.println("");
            System.out.println("ERROR:");
            System.out.println("Cannot log transaction. Log file not found.");
        }
    }

    public static void logPurchase(Item purchasedItem, String code) {

        //Finds log file
        File logFile = new File("src/main/resources/Log.txt");

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(logFile, true))){

            // Gets and formats date/time
            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a");
            writer.printf(dateFormat.format(currentDate) + " " + purchasedItem.getName() + " " + code + " $%.2f $%.2f\n", purchasedItem.getPrice(), customerBalance);
        } catch (FileNotFoundException e) {
            System.out.println("");
            System.out.println("ERROR:");
            System.out.println("Cannot log transaction. Log file not found.");
        }
    }

    public static void logChange(double change) {

        // Finds log file
        File logFile = new File("src/main/resources/Log.txt");

        // Writes to log file
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(logFile, true))) {

            // Gets and formats date/time
            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a");
            writer.printf(dateFormat.format(currentDate) + " GIVE CHANGE: $%.2f $0.00\n", change);
        } catch (FileNotFoundException var8) {
            System.out.println("");
            System.out.println("ERROR:");
            System.out.println("Cannot log transaction. Log file not found.");
        }
    }

    public static int[] calcChange(int changeInCents) {

        // Creates an array to hold number of quarters, dimes, and nickels respectively
        int[] coins = new int[3];

        // Calculates number of quarters to return
        int numberOfQuarters = changeInCents / 25;
        int remainder = changeInCents % 25;
        coins[0] = numberOfQuarters;

        // Calculates number of dimes to return
        int numberOfDimes = remainder / 10;
        remainder %= 10;
        coins[1] = numberOfDimes;

        // Calculates numbers of nickels to return
        int numberOfNickels = remainder / 5;
        coins[2] = numberOfNickels;

        // Dispense change
        System.out.println("");
        System.out.println("Your change is " + coins[0] + " quarters, " + coins[1] + " dimes, and " + coins[2] + " nickels");
        return coins;
    }

    public static void clearLog(){
        File logFile = new File("src/main/resources/Log.txt");

        try (PrintWriter writer = new PrintWriter(logFile)){
            writer.print("");
        } catch (FileNotFoundException e) {
            System.out.println("");
            System.out.println("ERROR:");
            System.out.println("Cannot access log. Log file not found");
        }
    }
}