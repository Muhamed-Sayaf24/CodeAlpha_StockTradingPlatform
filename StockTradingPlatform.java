import java.util.ArrayList;
import java.util.Scanner;

// Stock Class
class Stock {

    private String symbol;
    private String companyName;
    private double price;

    public Stock(String symbol, String companyName, double price) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getPrice() {
        return price;
    }
}


// Portfolio Item Class
class PortfolioItem {

    private Stock stock;
    private int quantity;
    private double purchasePrice;

    public PortfolioItem(Stock stock, int quantity, double purchasePrice) {
        this.stock = stock;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void removeQuantity(int quantity) {
        this.quantity -= quantity;
    }

    public double getInvestmentValue() {
        return quantity * purchasePrice;
    }

    public double getCurrentValue() {
        return quantity * stock.getPrice();
    }

    public double getProfitLoss() {
        return getCurrentValue() - getInvestmentValue();
    }
}


// Transaction Class
class Transaction {

    private String type;
    private String stockSymbol;
    private int quantity;
    private double amount;

    public Transaction(String type, String stockSymbol,
                       int quantity, double amount) {

        this.type = type;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.amount = amount;
    }

    public void displayTransaction() {

        System.out.printf(
                "%-8s %-10s %-10d $%.2f%n",
                type,
                stockSymbol,
                quantity,
                amount
        );
    }
}


// Main Class
public class StockTradingPlatform {

    static Scanner scanner = new Scanner(System.in);

    static ArrayList<Stock> marketStocks = new ArrayList<>();

    static ArrayList<PortfolioItem> portfolio = new ArrayList<>();

    static ArrayList<Transaction> transactions = new ArrayList<>();

    static double balance = 10000.00;


    public static void main(String[] args) {

        // Add Market Stocks
        initializeMarket();

        int choice;

        do {

            displayMenu();

            choice = getValidChoice();

            switch (choice) {

                case 1:
                    viewMarketStocks();
                    break;

                case 2:
                    buyStock();
                    break;

                case 3:
                    sellStock();
                    break;

                case 4:
                    viewPortfolio();
                    break;

                case 5:
                    viewTransactionHistory();
                    break;

                case 6:
                    viewAccountSummary();
                    break;

                case 7:
                    System.out.println(
                            "\nThank you for using the Stock Trading Platform!"
                    );
                    System.out.println("Have a great day!");
                    break;

                default:
                    System.out.println(
                            "\nInvalid choice. Please select a valid option."
                    );
            }

        } while (choice != 7);

        scanner.close();
    }


    // Initialize Stock Market
    public static void initializeMarket() {

        marketStocks.add(
                new Stock("AAPL", "Apple Inc.", 175.50)
        );

        marketStocks.add(
                new Stock("MSFT", "Microsoft Corporation", 420.00)
        );

        marketStocks.add(
                new Stock("GOOGL", "Alphabet Inc.", 165.75)
        );

        marketStocks.add(
                new Stock("TSLA", "Tesla Inc.", 250.00)
        );

        marketStocks.add(
                new Stock("AMZN", "Amazon.com Inc.", 180.25)
        );
    }


    // Display Main Menu
    public static void displayMenu() {

        System.out.println("\n=================================================");
        System.out.println("          STOCK TRADING PLATFORM");
        System.out.println("=================================================");

        System.out.println("1. View Market Stocks");
        System.out.println("2. Buy Stock");
        System.out.println("3. Sell Stock");
        System.out.println("4. View Portfolio");
        System.out.println("5. Transaction History");
        System.out.println("6. Account Summary");
        System.out.println("7. Exit");

        System.out.println("=================================================");

        System.out.print("Enter your choice: ");
    }


    // Get Valid Menu Choice
    public static int getValidChoice() {

        while (!scanner.hasNextInt()) {

            System.out.println("Invalid input! Please enter a number.");
            scanner.next();

            System.out.print("Enter your choice: ");
        }

        int choice = scanner.nextInt();
        scanner.nextLine();

        return choice;
    }


    // View Market Stocks
    public static void viewMarketStocks() {

        System.out.println("\n=================================================");
        System.out.println("               MARKET STOCKS");
        System.out.println("=================================================");

        System.out.printf(
                "%-10s %-25s %-15s%n",
                "SYMBOL",
                "COMPANY",
                "PRICE"
        );

        System.out.println("-------------------------------------------------");

        for (Stock stock : marketStocks) {

            System.out.printf(
                    "%-10s %-25s $%-15.2f%n",
                    stock.getSymbol(),
                    stock.getCompanyName(),
                    stock.getPrice()
            );
        }

        System.out.println("=================================================");
    }


    // Buy Stock
    public static void buyStock() {

        viewMarketStocks();

        System.out.print("\nEnter Stock Symbol to Buy: ");

        String symbol = scanner.nextLine().toUpperCase();

        Stock selectedStock = findMarketStock(symbol);

        if (selectedStock == null) {

            System.out.println("Stock not found!");
            return;
        }

        System.out.print("Enter Quantity: ");

        int quantity = getValidQuantity();

        double totalCost =
                selectedStock.getPrice() * quantity;

        System.out.printf(
                "Total Cost: $%.2f%n",
                totalCost
        );

        if (balance < totalCost) {

            System.out.println(
                    "Transaction Failed! Insufficient balance."
            );

            return;
        }

        balance -= totalCost;

        PortfolioItem existingItem =
                findPortfolioStock(symbol);

        if (existingItem != null) {

            existingItem.addQuantity(quantity);

        } else {

            portfolio.add(
                    new PortfolioItem(
                            selectedStock,
                            quantity,
                            selectedStock.getPrice()
                    )
            );
        }

        transactions.add(
                new Transaction(
                        "BUY",
                        symbol,
                        quantity,
                        totalCost
                )
        );

        System.out.println(
                "\nStock purchased successfully!"
        );

        System.out.printf(
                "Remaining Balance: $%.2f%n",
                balance
        );
    }


    // Sell Stock
    public static void sellStock() {

        if (portfolio.isEmpty()) {

            System.out.println(
                    "\nYour portfolio is empty. No stocks available to sell."
            );

            return;
        }

        viewPortfolio();

        System.out.print("\nEnter Stock Symbol to Sell: ");

        String symbol = scanner.nextLine().toUpperCase();

        PortfolioItem portfolioItem =
                findPortfolioStock(symbol);

        if (portfolioItem == null) {

            System.out.println(
                    "You do not own this stock!"
            );

            return;
        }

        System.out.print("Enter Quantity to Sell: ");

        int quantity = getValidQuantity();

        if (quantity > portfolioItem.getQuantity()) {

            System.out.println(
                    "Transaction Failed! You do not own enough shares."
            );

            return;
        }

        double sellingAmount =
                portfolioItem.getStock().getPrice() * quantity;

        portfolioItem.removeQuantity(quantity);

        balance += sellingAmount;

        if (portfolioItem.getQuantity() == 0) {

            portfolio.remove(portfolioItem);
        }

        transactions.add(
                new Transaction(
                        "SELL",
                        symbol,
                        quantity,
                        sellingAmount
                )
        );

        System.out.println(
                "\nStock sold successfully!"
        );

        System.out.printf(
                "Amount Received: $%.2f%n",
                sellingAmount
        );

        System.out.printf(
                "Updated Balance: $%.2f%n",
                balance
        );
    }


    // View Portfolio
    public static void viewPortfolio() {

        System.out.println("\n==============================================================");
        System.out.println("                     MY PORTFOLIO");
        System.out.println("==============================================================");

        if (portfolio.isEmpty()) {

            System.out.println("No stocks purchased yet.");

            return;
        }

        System.out.printf(
                "%-10s %-10s %-15s %-15s %-15s%n",
                "SYMBOL",
                "QUANTITY",
                "INVESTMENT",
                "CURRENT VALUE",
                "PROFIT/LOSS"
        );

        System.out.println("--------------------------------------------------------------");

        for (PortfolioItem item : portfolio) {

            System.out.printf(
                    "%-10s %-10d $%-14.2f $%-14.2f $%-14.2f%n",

                    item.getStock().getSymbol(),
                    item.getQuantity(),
                    item.getInvestmentValue(),
                    item.getCurrentValue(),
                    item.getProfitLoss()
            );
        }

        System.out.println("==============================================================");
    }


    // Transaction History
    public static void viewTransactionHistory() {

        System.out.println("\n==============================================");
        System.out.println("           TRANSACTION HISTORY");
        System.out.println("==============================================");

        if (transactions.isEmpty()) {

            System.out.println(
                    "No transactions available."
            );

            return;
        }

        System.out.printf(
                "%-8s %-10s %-10s %-15s%n",
                "TYPE",
                "SYMBOL",
                "QUANTITY",
                "AMOUNT"
        );

        System.out.println("----------------------------------------------");

        for (Transaction transaction : transactions) {

            transaction.displayTransaction();
        }

        System.out.println("==============================================");
    }


    // Account Summary
    public static void viewAccountSummary() {

        double totalInvestment = 0;

        double currentPortfolioValue = 0;

        for (PortfolioItem item : portfolio) {

            totalInvestment += item.getInvestmentValue();

            currentPortfolioValue += item.getCurrentValue();
        }

        double totalProfitLoss =
                currentPortfolioValue - totalInvestment;

        double totalAccountValue =
                balance + currentPortfolioValue;


        System.out.println("\n==============================================");
        System.out.println("              ACCOUNT SUMMARY");
        System.out.println("==============================================");

        System.out.printf(
                "Available Cash Balance : $%.2f%n",
                balance
        );

        System.out.printf(
                "Total Investment       : $%.2f%n",
                totalInvestment
        );

        System.out.printf(
                "Portfolio Value        : $%.2f%n",
                currentPortfolioValue
        );

        System.out.printf(
                "Profit / Loss          : $%.2f%n",
                totalProfitLoss
        );

        System.out.printf(
                "Total Account Value    : $%.2f%n",
                totalAccountValue
        );

        System.out.println("==============================================");
    }


    // Find Stock in Market
    public static Stock findMarketStock(String symbol) {

        for (Stock stock : marketStocks) {

            if (stock.getSymbol().equalsIgnoreCase(symbol)) {

                return stock;
            }
        }

        return null;
    }


    // Find Stock in Portfolio
    public static PortfolioItem findPortfolioStock(
            String symbol
    ) {

        for (PortfolioItem item : portfolio) {

            if (
                    item.getStock()
                            .getSymbol()
                            .equalsIgnoreCase(symbol)
            ) {

                return item;
            }
        }

        return null;
    }


    // Validate Quantity
    public static int getValidQuantity() {

        while (!scanner.hasNextInt()) {

            System.out.println(
                    "Invalid input! Please enter a valid number."
            );

            scanner.next();

            System.out.print("Enter Quantity: ");
        }

        int quantity = scanner.nextInt();

        scanner.nextLine();

        while (quantity <= 0) {

            System.out.println(
                    "Quantity must be greater than zero."
            );

            System.out.print("Enter Quantity: ");

            quantity = scanner.nextInt();

            scanner.nextLine();
        }

        return quantity;
    }
}