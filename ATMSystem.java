
import java.io.*;
import java.util.*;

public class ATMSystem {
    static final String FILE = "atm_data.txt";
    static final String HISTORY = "atm_history.txt";
    static final Scanner sc = new Scanner(System.in);

    static String pin = "1234";
    static double balance = 5000.0;

    static void loadData() {
        File file = new File(FILE);

        if (!file.exists()) {
            saveData();
            return;
        }

        try (Scanner reader = new Scanner(file)) {
            if (reader.hasNextLine()) {
                pin = reader.nextLine().trim();
            }
            if (reader.hasNextLine()) {
                balance = Double.parseDouble(
                    reader.nextLine().trim()
                );
            }
        } catch (Exception e) {
            System.out.println("Could not load ATM data.");
        }
    }

    static void saveData() {
        try (PrintWriter out = new PrintWriter(
                new FileWriter(FILE))) {
            out.println(pin);
            out.println(balance);
        } catch (IOException e) {
            System.out.println("Error saving ATM data.");
        }
    }

    static void record(String message) {
        try (PrintWriter out = new PrintWriter(
                new FileWriter(HISTORY, true))) {
            out.println(new Date() + " - " + message);
        } catch (IOException e) {
            System.out.println("Error saving transaction.");
        }
    }

    static double readAmount() {
        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(sc.nextLine());

        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException(
                "Amount must be positive."
            );
        }
        return amount;
    }

    static void showHistory() {
        File file = new File(HISTORY);

        if (!file.exists()) {
            System.out.println("No transactions yet.");
            return;
        }

        System.out.println("\n--- Transaction History ---");

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                System.out.println(reader.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Cannot read history.");
        }
    }

    public static void main(String[] args) {
        loadData();

        System.out.println("===== Welcome to ATM =====");

        boolean loggedIn = false;

        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.print("Enter 4-digit PIN: ");
            String enteredPin = sc.nextLine();

            if (pin.equals(enteredPin)) {
                loggedIn = true;
                break;
            }

            System.out.println("Incorrect PIN. Attempt "
                    + attempt + " of 3.");
        }

        if (!loggedIn) {
            System.out.println("Too many attempts. Exiting.");
            return;
        }

        while (true) {
            System.out.println("\n1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transaction History");
            System.out.println("5. Change PIN");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");

            String choice = sc.nextLine();

            try {
                switch (choice) {
                    case "1":
                        System.out.printf(
                            "Balance: Rs. %.2f%n", balance
                        );
                        break;

                    case "2": {
                        double amount = readAmount();
                        balance += amount;
                        saveData();
                        record("Deposited Rs. " + amount);
                        System.out.println("Deposit successful.");
                        break;
                    }

                    case "3": {
                        double amount = readAmount();

                        if (amount > balance) {
                            System.out.println(
                                "Insufficient balance."
                            );
                        } else {
                            balance -= amount;
                            saveData();
                            record("Withdrawn Rs. " + amount);
                            System.out.println(
                                "Withdrawal successful."
                            );
                        }
                        break;
                    }

                    case "4":
                        showHistory();
                        break;

                    case "5":
                        System.out.print("Enter new 4-digit PIN: ");
                        String newPin = sc.nextLine();

                        if (newPin.matches("\\d{4}")) {
                            pin = newPin;
                            saveData();
                            record("PIN changed");
                            System.out.println("PIN changed.");
                        } else {
                            System.out.println(
                                "PIN must contain 4 digits."
                            );
                        }
                        break;

                    case "6":
                        saveData();
                        System.out.println("Thank you for using ATM.");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
