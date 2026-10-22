
import java.util.ArrayList;
import java.util.Scanner;

// Abstraction
abstract class BankAccount {
    private final int accountNumber;
    private final String holderName;
    private double balance;

    public BankAccount(int accountNumber, String holderName,
                       double balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }

    // Encapsulation
    public int getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println("Please enter a valid amount!");
            return;
        }

        balance += amount;
        System.out.printf(
            "Deposit successful! New balance: Rs. %.2f%n",
            balance
        );
    }

    protected void deductBalance(double amount) {
        balance -= amount;
    }

    public abstract void withdraw(double amount);

    public void displayDetails() {
        System.out.println("\n----- ACCOUNT DETAILS -----");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + holderName);
        System.out.printf("Account Balance: Rs. %.2f%n", balance);
    }
}

// Inheritance
class SavingsAccount extends BankAccount {

    public SavingsAccount(int number, String name, double balance) {
        super(number, name, balance);
    }

    // Polymorphism: Method overriding
    @Override
    public void withdraw(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println("Please enter a valid amount!");
        } else if (amount > getBalance()) {
            System.out.println("Insufficient balance!");
        } else {
            deductBalance(amount);
            System.out.printf(
                "Withdrawal successful! New balance: Rs. %.2f%n",
                getBalance()
            );
        }
    }
}

// Main class
public class BankingSystem {

    private static final Scanner sc = new Scanner(System.in);
    private static final ArrayList<BankAccount> accounts =
        new ArrayList<>();

    private static int nextAccountNumber = 1001;

    private static BankAccount findAccount(int number) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber() == number) {
                return account;
            }
        }
        return null;
    }

    private static double readAmount(String message) {
        while (true) {
            System.out.print(message);

            try {
                double amount =
                    Double.parseDouble(sc.nextLine().trim());

                if (Double.isFinite(amount)) {
                    return amount;
                }
            } catch (NumberFormatException e) {
                // Ask for valid input again.
            }

            System.out.println("Please enter a valid number.");
        }
    }

    private static int readInteger(String message) {
        while (true) {
            System.out.print(message);

            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static void createAccount() {
        System.out.print("Enter account holder name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }

        double amount = readAmount("Enter initial deposit: Rs. ");

        if (amount < 0) {
            System.out.println("Deposit cannot be negative!");
            return;
        }

        BankAccount account = new SavingsAccount(
            nextAccountNumber++, name, amount
        );

        accounts.add(account);

        System.out.println("\nAccount created successfully!");
        account.displayDetails();
    }

    private static BankAccount selectAccount() {
        int number = readInteger("Enter account number: ");
        BankAccount account = findAccount(number);

        if (account == null) {
            System.out.println("Account not found!");
        }

        return account;
    }

    private static void depositMoney() {
        BankAccount account = selectAccount();

        if (account != null) {
            account.deposit(readAmount("Enter deposit amount: Rs. "));
        }
    }

    private static void withdrawMoney() {
        BankAccount account = selectAccount();

        if (account != null) {
            account.withdraw(
                readAmount("Enter withdrawal amount: Rs. ")
            );
        }
    }

    private static void checkBalance() {
        BankAccount account = selectAccount();

        if (account != null) {
            account.displayDetails();
        }
    }

    private static void viewAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts created yet!");
            return;
        }

        for (BankAccount account : accounts) {
            account.displayDetails();
        }
    }

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n================================");
            System.out.println("     JAVA BANKING SYSTEM");
            System.out.println("================================");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Check Balance");
            System.out.println("5. View All Accounts");
            System.out.println("0. Exit");
            System.out.println("================================");

            choice = readInteger("Enter your choice: ");

            switch (choice) {
                case 1:
                    createAccount();
                    break;

                case 2:
                    depositMoney();
                    break;

                case 3:
                    withdrawMoney();
                    break;

                case 4:
                    checkBalance();
                    break;

                case 5:
                    viewAllAccounts();
                    break;

                case 0:
                    System.out.println(
                        "Thank you for using Java Banking System!"
                    );
                    break;

                default:
                    System.out.println(
                        "Invalid choice! Please try again."
                    );
            }

        } while (choice != 0);

        sc.close();
    }
}
