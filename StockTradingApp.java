import java.io.*;
import java.util.*;

class Stock {
    String symbol;
    String name;
    double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public void updatePrice(double newPrice) {
        this.price = newPrice;
    }
}

class Market {
    private Map<String, Stock> stocks = new HashMap<>();

    public void addStock(Stock stock) {
        stocks.put(stock.symbol, stock);
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    public void displayMarket() {
        System.out.println("Available Stocks:");
        for (Stock s : stocks.values()) {
            System.out.printf("%s (%s): $%.2f\n", s.name, s.symbol, s.price);
        }
    }
}

class User {
    String name;
    double balance;
    Map<String, Integer> portfolio = new HashMap<>();

    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public void buyStock(Stock stock, int quantity) {
        double totalCost = stock.price * quantity;
        if (totalCost > balance) {
            System.out.println("Insufficient balance.");
            return;
        }

        balance -= totalCost;
        portfolio.put(stock.symbol, portfolio.getOrDefault(stock.symbol, 0) + quantity);
        System.out.println("Bought " + quantity + " shares of " + stock.symbol);
    }

    public void sellStock(Stock stock, int quantity) {
        int owned = portfolio.getOrDefault(stock.symbol, 0);
        if (quantity > owned) {
            System.out.println("Not enough shares to sell.");
            return;
        }

        balance += stock.price * quantity;
        if (quantity == owned) {
            portfolio.remove(stock.symbol);
        } else {
            portfolio.put(stock.symbol, owned - quantity);
        }
        System.out.println("Sold " + quantity + " shares of " + stock.symbol);
    }

    public void displayPortfolio(Market market) {
        System.out.println("\n--- Portfolio for " + name + " ---");
        System.out.printf("Balance: $%.2f\n", balance);
        double total = balance;
        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            Stock s = market.getStock(entry.getKey());
            double value = s.price * entry.getValue();
            total += value;
            System.out.printf("%s: %d shares @ $%.2f = $%.2f\n", s.symbol, entry.getValue(), s.price, value);
        }
        System.out.printf("Total Portfolio Value: $%.2f\n", total);
    }

    public void savePortfolio(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(name);
            writer.println(balance);
            for (Map.Entry<String, Integer> e : portfolio.entrySet()) {
                writer.printf("%s,%d\n", e.getKey(), e.getValue());
            }
            System.out.println("Portfolio saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving portfolio.");
        }
    }

    public static User loadPortfolio(String filename) {
        try (Scanner sc = new Scanner(new File(filename))) {
            String name = sc.nextLine();
            double balance = Double.parseDouble(sc.nextLine());
            User user = new User(name, balance);

            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                user.portfolio.put(parts[0], Integer.parseInt(parts[1]));
            }

            System.out.println("Portfolio loaded from " + filename);
            return user;
        } catch (IOException e) {
            System.out.println("Error loading portfolio.");
            return null;
        }
    }
}

public class StockTradingApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Market market = new Market();
        market.addStock(new Stock("AAPL", "Apple Inc.", 180.25));
        market.addStock(new Stock("GOOGL", "Alphabet Inc.", 2500.75));
        market.addStock(new Stock("TSLA", "Tesla Inc.", 700.30));

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        User user;
        File saveFile = new File(name + "_portfolio.txt");

        if (saveFile.exists()) {
            user = User.loadPortfolio(saveFile.getName());
            if (user == null) user = new User(name, 10000);
        } else {
            user = new User(name, 10000);
        }

        while (true) {
            System.out.println("\n--- Stock Trading Menu ---");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Save Portfolio");
            System.out.println("6. Exit");
            System.out.print("Select option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    market.displayMarket();
                    break;
                case 2:
                    System.out.print("Enter stock symbol: ");
                    String buySymbol = scanner.next();
                    System.out.print("Enter quantity: ");
                    int buyQty = scanner.nextInt();
                    Stock buyStock = market.getStock(buySymbol.toUpperCase());
                    if (buyStock != null)
                        user.buyStock(buyStock, buyQty);
                    else
                        System.out.println("Stock not found.");
                    break;
                case 3:
                    System.out.print("Enter stock symbol: ");
                    String sellSymbol = scanner.next();
                    System.out.print("Enter quantity: ");
                    int sellQty = scanner.nextInt();
                    Stock sellStock = market.getStock(sellSymbol.toUpperCase());
                    if (sellStock != null)
                        user.sellStock(sellStock, sellQty);
                    else
                        System.out.println("Stock not found.");
                    break;
                case 4:
                    user.displayPortfolio(market);
                    break;
                case 5:
                    user.savePortfolio(saveFile.getName());
                    break;
                case 6:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
