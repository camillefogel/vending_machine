package main.java;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

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
    public static Scanner userInput;
    public static Map<String, Item> itemList;
    public static final int MAX_QTY = 5;
    public static double customerBalance;

    public VendingMachineCLI() {
    }

    public static void main(String[] args) {
        File inventoryFile = new File("vendingmachine.csv");

        try {
            Scanner readFile = new Scanner(inventoryFile);

            try {
                while(readFile.hasNextLine()) {
                    String currentLine = readFile.nextLine();
                    String[] currentArray = currentLine.split("\\|");
                    if (currentArray[3].equals("Chip")) {
                        Chips chip = new Chips(currentArray[1], Double.parseDouble(currentArray[2]), 5);
                        itemList.put(currentArray[0], chip);
                    } else if (currentArray[3].equals("Candy")) {
                        Candy candies = new Candy(currentArray[1], Double.parseDouble(currentArray[2]), 5);
                        itemList.put(currentArray[0], candies);
                    } else if (currentArray[3].equals("Drink")) {
                        Beverages drinks = new Beverages(currentArray[1], Double.parseDouble(currentArray[2]), 5);
                        itemList.put(currentArray[0], drinks);
                    } else if (currentArray[3].equals("Gum")) {
                        Gum chewies = new Gum(currentArray[1], Double.parseDouble(currentArray[2]), 5);
                        itemList.put(currentArray[0], chewies);
                    }
                }
            } catch (Throwable var7) {
                try {
                    readFile.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }

                throw var7;
            }

            readFile.close();
        } catch (FileNotFoundException var8) {
            System.out.println("File not found");
        }

        mainMenu();
    }

    public static void mainMenu() {
        while(true) {
            System.out.println("Please select an option:");
            System.out.println("(1) Display Vending Machine Items");
            System.out.println("(2) Purchase");
            System.out.println("(3) Exit");
            String mainMenuInput = userInput.nextLine();
            if (mainMenuInput.equals("1")) {
                displayItemList();
                System.out.println("\nPress R to return to the main menu");

                for(String returnToMain = userInput.nextLine(); !returnToMain.equalsIgnoreCase("R"); returnToMain = userInput.nextLine()) {
                    System.out.println("Invalid response. Try again.");
                }
            } else {
                if (mainMenuInput.equals("2")) {
                    purchaseMenu();
                    return;
                }

                if (mainMenuInput.equals("3")) {
                    if (customerBalance > 0.0) {
                        calcChange((int)(customerBalance * 100.0));
                    }

                    System.out.println("Goodbye.");
                    System.exit(0);
                } else {
                    System.out.println("Invalid response. Please try again");
                }
            }
        }
    }

    public static void purchaseMenu() {
        while(true) {
            System.out.printf("Current Money Provided: $%.2f", customerBalance);
            System.out.println("\n(1) Feed Money");
            System.out.println("(2) Select Product");
            System.out.println("(3) Finish Transaction");
            String purchaseChoice = userInput.nextLine();
            if (purchaseChoice.equals("1")) {
                System.out.printf("Current Money Provided: $%.2f", customerBalance);
                System.out.println("\nEnter the amount of money you would like to insert (whole dollars only):");
                String dollarAmount = userInput.nextLine();
                double feedMoney = Double.parseDouble(dollarAmount);
                if (feedMoney * 100.0 % 100.0 == 0.0 && feedMoney > 0.0) {
                    customerBalance += feedMoney;
                    logFeedMoney(feedMoney, customerBalance);
                } else {
                    System.out.println("Must be a whole dollar amount greater than zero. Please try again.");
                }
            } else if (purchaseChoice.equals("2")) {
                dispenseItem();
            } else if (purchaseChoice.equals("3")) {
                if (customerBalance > 0.0) {
                    int[] coins = calcChange((int)(customerBalance * 100.0));
                    logChange(customerBalance);
                    customerBalance = 0.0;
                }

                mainMenu();
            } else {
                System.out.println("Invalid response. Please try again.");
            }
        }
    }

    public static void dispenseItem() {
        while(true) {
            Iterator var0 = itemList.entrySet().iterator();

            while(var0.hasNext()) {
                Map.Entry<String, Item> item = (Map.Entry)var0.next();
                String key = (String)item.getKey();
                Item value = (Item)item.getValue();
                if (((Item)item.getValue()).getQuantity() >= 1) {
                    System.out.printf("Enter " + key + " for " + value.getName() + " (Price: %.2f, Quantity: " + value.getQuantity() + ")\n", value.getPrice());
                } else {
                    System.out.println("Enter " + key + " for " + value.getName() + " (SOLD OUT)");
                }
            }

            System.out.println("Press C to cancel");
            String itemChoice = userInput.nextLine().toUpperCase();
            if (!itemList.containsKey(itemChoice)) {
                if (itemChoice.equalsIgnoreCase("C")) {
                    mainMenu();
                } else {
                    System.out.println("Invalid code. Please try again.");
                }
            } else {
                Item choice = (Item)itemList.get(itemChoice);
                if (choice.getQuantity() == 0) {
                    System.out.println("Item is sold out. Please try again.");
                } else if (customerBalance > choice.getPrice()) {
                    System.out.println("Dispensing...");
                    System.out.printf("\nEnjoy your " + choice.getName() + " for $%.2f", choice.getPrice());
                    System.out.println("\n" + choice.yumYumStatement());
                    customerBalance -= choice.getPrice();
                    choice.removeOne();
                    System.out.printf("Current Balance: $%.2f\n", customerBalance);
                    logPurchase(choice, itemChoice);
                    purchaseMenu();
                } else {
                    System.out.println("Insufficient funds.");
                    mainMenu();
                }
            }
        }
    }

    public static void displayItemList() {
        Iterator var0 = itemList.entrySet().iterator();

        while(var0.hasNext()) {
            Map.Entry<String, Item> item = (Map.Entry)var0.next();
            Item value = (Item)item.getValue();
            System.out.printf("\n" + value.getName() + " (Price: $%.2f, Quantity: " + value.getQuantity() + ")", value.getPrice());
        }

    }

    public static void logFeedMoney(double deposited, double balance) {
        File logFile = new File("src/main/resources/Log.txt");

        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(logFile, true));

            try {
                LocalDateTime currentDate = LocalDateTime.now();
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a");
                writer.printf(dateFormat.format(currentDate) + " FEED MONEY: $%.2f $%.2f\n", deposited, balance);
            } catch (Throwable var9) {
                try {
                    writer.close();
                } catch (Throwable var8) {
                    var9.addSuppressed(var8);
                }

                throw var9;
            }

            writer.close();
        } catch (FileNotFoundException var10) {
            System.out.println("Cannot log transaction. Log file not found.");
        }

    }

    public static void logPurchase(Item purchasedItem, String code) {
        File logFile = new File("src/main/resources/Log.txt");

        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(logFile, true));

            try {
                LocalDateTime currentDate = LocalDateTime.now();
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a");
                writer.printf(dateFormat.format(currentDate) + " " + purchasedItem.getName() + " " + code + " $%.2f $%.2f\n", purchasedItem.getPrice(), customerBalance);
            } catch (Throwable var7) {
                try {
                    writer.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }

                throw var7;
            }

            writer.close();
        } catch (FileNotFoundException var8) {
            System.out.println("Cannot log transaction. Log file not found.");
        }

    }

    public static void logChange(double change) {
        File logFile = new File("src/main/resources/Log.txt");

        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(logFile, true));

            try {
                LocalDateTime currentDate = LocalDateTime.now();
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a");
                writer.printf(dateFormat.format(currentDate) + " GIVE CHANGE: $%.2f $0.00\n", change);
            } catch (Throwable var7) {
                try {
                    writer.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }

                throw var7;
            }

            writer.close();
        } catch (FileNotFoundException var8) {
            System.out.println("Cannot log transaction. Log file not found.");
        }

    }

    public static int[] calcChange(int changeInCents) {
        int[] coins = new int[3];
        int numberOfQuarters = changeInCents / 25;
        int remainder = changeInCents % 25;
        coins[0] = numberOfQuarters;
        int numberOfDimes = remainder / 10;
        remainder %= 10;
        coins[1] = numberOfDimes;
        int numberOfNickels = remainder / 5;
        coins[2] = numberOfNickels;
        System.out.println("Your change is " + coins[0] + " quarters, " + coins[1] + " dimes, and " + coins[2] + " nickels");
        return coins;
    }

    static {
        userInput = new Scanner(System.in);
        itemList = new HashMap();
        customerBalance = 0.0;
    }
}