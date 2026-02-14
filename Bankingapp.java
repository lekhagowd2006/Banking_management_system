import java.util.*;

class InvalidEmailException extends Exception {
    public InvalidEmailException(String message) {
        super(message);
    }
}

class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) {
        super(message);
    }
}

class InvalidAccountNumberException extends Exception {
    public InvalidAccountNumberException(String message) {
        super(message);
    }
}

class InvalidNameException extends Exception {
    public InvalidNameException(String message) {
        super(message);
    }
}

class InvalidBalanceException extends Exception {
    public InvalidBalanceException(String message) {
        super(message);
    }
}

interface LoanService {
    void applyLoan(double amount);
}

abstract class BankAccount {
    private int accountNumber;
    private String holdername;
    protected double balance;
    private String email;

    public BankAccount(int accountNumber, String holdername, double balance, String email) throws InvalidAccountNumberException, InvalidNameException, InvalidBalanceException, InvalidEmailException {
    if(accountNumber<=0) {
            throw new InvalidAccountNumberException("Account Number must be positive");
    }
    if(holdername.isEmpty()) {
            throw new InvalidNameException("Account Holder Name cannot be empty");
    }
    if(balance < 0) {
            throw new InvalidBalanceException("Balance cannot be negative");
    }
        if(!email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")){
            throw new InvalidEmailException("Invalid Email Format");
    }
    
        this.accountNumber = accountNumber;
        this.holdername = holdername;
        this.balance = balance;
        this.email = email;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void deposit(double amount) throws InvalidAmountException 
    {
        if(amount<=0){
            throw new InvalidAmountException("Amount must be positive");
        }

        if (amount>0) {
            balance += amount;
            System.out.println("Amount Deposited: " + amount);
        }
    }

    public void withdraw(double amount) throws InvalidAmountException{
        if(amount<=0){
            throw new InvalidAmountException("Amount must be positive");
        }
        if (amount <= balance) {
            balance -= amount;
            System.out.println("Amount Withdrawn:" + amount);
        } else { 
            System.out.println("Insufficient balance");
        }
    }

    public void showdetails() {
        System.out.println("Account Holder Name: " + holdername);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Email: " + email);
        System.out.println("Balance: " + balance);
    }

    abstract void calculateInterest();
}

class SavingsAccount extends BankAccount {
    private double interestRate = 5.0;

    public SavingsAccount(int accNo, String name, double balance, String email) throws InvalidAccountNumberException, InvalidNameException, InvalidBalanceException, InvalidEmailException {
        super(accNo, name, balance, email);
    }

    @Override
    void calculateInterest() {
        double interest = balance * interestRate / 100;
        System.out.println("Interest: " + interest);
    }
}

class CurrentAccount extends BankAccount {
    private double overdraftLimit = 10000;

    public CurrentAccount(int accNo, String name, double balance, String email) throws InvalidAccountNumberException, InvalidNameException, InvalidBalanceException, InvalidEmailException {
        super(accNo, name, balance, email);
    }
    @Override
    public void withdraw(double amount) throws InvalidAmountException {
        if(amount<=0){
            throw new InvalidAmountException("Amount must be positive");
        }
        if (amount <= balance + overdraftLimit) {
            balance -= amount;
            System.out.println("Amount Withdrawn: " + amount);
        } else {
            System.out.println("Overdraft limit exceeded");
        }
    }

    @Override
    void calculateInterest() {
        System.out.println("No interest for Current Account");
    }
}

class BankingApp1 {
    static ArrayList<BankAccount> accounts = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            try {
                System.out.println("==== BANK MENU ====");
                System.out.println("1. Create Account");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Show Details");
                System.out.println("5. Apply Interest");
                System.out.println("6. Exit");

                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        deposit();
                        break;
                    case 3:
                        withdraw();
                        break;
                    case 4:
                        showAccount();
                        break;
                    case 5:
                        applyInterest();
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    static void createAccount() {
        System.out.println("1. Savings Account");
        System.out.println("2. Current Account");
        int type = sc.nextInt();

        System.out.println("Enter Account Number:");
        int accNo = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter Name:");
        String name = sc.nextLine();

        System.out.println("Enter Email:");
        String email = sc.nextLine();

        if (!email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
            System.out.println("Invalid Email Format");
            return;
        }

        System.out.println("Enter Balance:");
        double balance = sc.nextDouble();

        try {
            if (type == 1) {
                accounts.add(new SavingsAccount(accNo, name, balance, email));
            } else if (type == 2) {
                accounts.add(new CurrentAccount(accNo, name, balance, email));
            } else {
                System.out.println("Invalid account type");
                return;
            }
        } catch (InvalidAccountNumberException | InvalidNameException | InvalidBalanceException | InvalidEmailException e) {
            System.out.println("Error creating account: " + e.getMessage());
            return;
        }

        System.out.println("Account Created Successfully");
    }

    static BankAccount findaccount(int accNo) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber() == accNo) {
                return account;
            }
        }
        return null;
    }

    static void deposit() throws InvalidAmountException {
        System.out.println("Enter Account Number:");
        int accNo = sc.nextInt();
        BankAccount account = findaccount(accNo);

        if (account == null) {
            System.out.println("Account not found");
            return;
        }

        System.out.println("Enter Amount:");
        double amount = sc.nextDouble();
        try {
            account.deposit(amount);
        } catch (InvalidAmountException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void withdraw() {
        System.out.println("Enter Account Number:");
        int accNo = sc.nextInt();
        BankAccount account = findaccount(accNo);

        if (account == null) {
            System.out.println("Account not found");
            return;
        }

        System.out.println("Enter Amount:");
        double amount = sc.nextDouble();
        try {
            account.withdraw(amount);
        } catch (InvalidAmountException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void showAccount() {
        System.out.println("Enter Account Number:");
        int accNo = sc.nextInt();
        BankAccount account = findaccount(accNo);

        if (account != null) {
            account.showdetails();
        } else {
            System.out.println("Account not found");
        }
    }

    static void applyInterest() {
        System.out.println("Enter Account Number:");
        int accNo = sc.nextInt();
        BankAccount account = findaccount(accNo);

        if (account != null) {
            account.calculateInterest();
        } else {
            System.out.println("Account not found");
        }
    }
}