package shablon.lab;

import java.util.*;

// ============================================================
// STRATEGY PATTERN — ТӨЛЕМ ӘДІСТЕРІ
// ============================================================

interface IPaymentStrategy {
    void pay(double amount);
}

class CreditCardPayment implements IPaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("💳 Банктік картамен төлем жасалды: " + amount + " ₸");
    }
}

class PayPalPayment implements IPaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("💻 PayPal арқылы төлем жасалды: " + amount + " ₸");
    }
}

class CryptoPayment implements IPaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("🪙 Криптовалюта арқылы төлем жасалды: " + amount + " ₸");
    }
}

class PaymentContext {
    private IPaymentStrategy strategy;

    public void setStrategy(IPaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void executePayment(double amount) {
        if (strategy == null) {
            System.out.println("⚠ Төлем әдісі таңдалмаған!");
        } else {
            strategy.pay(amount);
        }
    }
}

class StrategyDemo {
    public static void run() {
        Scanner sc = new Scanner(System.in);
        PaymentContext context = new PaymentContext();

        System.out.println("\n=== STRATEGY PATTERN ===");
        System.out.println("1 - Банктік карта");
        System.out.println("2 - PayPal");
        System.out.println("3 - Криптовалюта");

        System.out.print("Төлем әдісін таңдаңыз (1-3): ");
        int choice = sc.nextInt();

        System.out.print("Соманы енгізіңіз: ");
        double amount = sc.nextDouble();

        switch (choice) {
            case 1 -> context.setStrategy(new CreditCardPayment());
            case 2 -> context.setStrategy(new PayPalPayment());
            case 3 -> context.setStrategy(new CryptoPayment());
            default -> {
                System.out.println("⚠ Мұндай нұсқа жоқ!");
                return;
            }
        }

        context.executePayment(amount);
        sc.close();
    }
}

// ============================================================
// OBSERVER PATTERN — ВАЛЮТА КУРСЫН БАҚЫЛАУ
// ============================================================

interface IObserver {
    void update(double usdRate);
}

interface ISubject {
    void attach(IObserver observer);
    void detach(IObserver observer);
    void notifyObservers();
}

class CurrencyExchange implements ISubject {
    private List<IObserver> observers = new ArrayList<>();
    private double usdRate;

    public void setUsdRate(double rate) {
        this.usdRate = rate;
        System.out.println("\nВалюта бағамы өзгерді: 1 USD = " + rate + " ₸");
        notifyObservers();
    }

    @Override
    public void attach(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detach(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IObserver obs : observers) {
            obs.update(usdRate);
        }
    }
}

class MobileAppDisplay implements IObserver {
    @Override
    public void update(double usdRate) {
        System.out.println("Мобильді қосымша: USD бағамы жаңарды — " + usdRate + " ₸");
    }
}

class WebsiteDisplay implements IObserver {
    @Override
    public void update(double usdRate) {
        System.out.println("Веб-сайтта жаңа курс: " + usdRate + " ₸");
    }
}

class EmailNotifier implements IObserver {
    @Override
    public void update(double usdRate) {
        System.out.println("Email хабарлама: USD бағамы " + usdRate + " ₸ болып өзгерді.");
    }
}

class ObserverDemo {
    public static void run() {
        CurrencyExchange exchange = new CurrencyExchange();

        IObserver app = new MobileAppDisplay();
        IObserver site = new WebsiteDisplay();
        IObserver email = new EmailNotifier();

        exchange.attach(app);
        exchange.attach(site);
        exchange.attach(email);

        exchange.setUsdRate(478.25);
        exchange.setUsdRate(481.50);

        exchange.detach(site);
        exchange.setUsdRate(485.75);
    }
}

// ============================================================
// MAIN — ПАЙДАЛАНУШЫ ҮШІН МЕНЮ
// ============================================================

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== ДИЗАЙН ПАТТЕРНДЕР ДЕМОНСТРАЦИЯСЫ ===");
        System.out.println("1 - Strategy (Төлем жүйесі)");
        System.out.println("2 - Observer (Валюталық курс)");
        System.out.print("Таңдаңыз (1 немесе 2): ");

        int choice = sc.nextInt();

        switch (choice) {
            case 1 -> StrategyDemo.run();
            case 2 -> ObserverDemo.run();
            default -> System.out.println("Қате таңдау!");
        }

        sc.close();
    }
}