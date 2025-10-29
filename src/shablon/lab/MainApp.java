interface Beverage {
    String getDescription();
    double getCost();
}

class Espresso implements Beverage {
    public String getDescription() {
        return "Espresso";
    }
    public double getCost() {
        return 700.0;
    }
}

class Tea implements Beverage {
    public String getDescription() {
        return "Tea";
    }
    public double getCost() {
        return 500.0;
    }
}

class Latte implements Beverage {
    public String getDescription() {
        return "Latte";
    }
    public double getCost() {
        return 900.0;
    }
}

class Mocha implements Beverage {
    public String getDescription() {
        return "Mocha";
    }
    public double getCost() {
        return 950.0;
    }
}

abstract class BeverageDecorator implements Beverage {
    protected Beverage beverage;

    public BeverageDecorator(Beverage beverage) {
        this.beverage = beverage;
    }

    public String getDescription() {
        return beverage.getDescription();
    }

    public double getCost() {
        return beverage.getCost();
    }
}

class Milk extends BeverageDecorator {
    public Milk(Beverage beverage) {
        super(beverage);
    }
    public String getDescription() {
        return beverage.getDescription() + ", Milk";
    }
    public double getCost() {
        return beverage.getCost() + 150.0;
    }
}

class Sugar extends BeverageDecorator {
    public Sugar(Beverage beverage) {
        super(beverage);
    }
    public String getDescription() {
        return beverage.getDescription() + ", Sugar";
    }
    public double getCost() {
        return beverage.getCost() + 50.0;
    }
}

class WhippedCream extends BeverageDecorator {
    public WhippedCream(Beverage beverage) {
        super(beverage);
    }
    public String getDescription() {
        return beverage.getDescription() + ", Whipped Cream";
    }
    public double getCost() {
        return beverage.getCost() + 200.0;
    }
}

interface IPaymentProcessor {
    void processPayment(double amount);
}

class PayPalPaymentProcessor implements IPaymentProcessor {
    public void processPayment(double amount) {
        System.out.println("Processing PayPal payment of " + amount + " тг...");
    }
}

class StripePaymentService {
    public void makeTransaction(double totalAmount) {
        System.out.println("Processing Stripe transaction of " + totalAmount + " тг...");
    }
}

class StripePaymentAdapter implements IPaymentProcessor {
    private StripePaymentService stripeService;

    public StripePaymentAdapter(StripePaymentService stripeService) {
        this.stripeService = stripeService;
    }

    public void processPayment(double amount) {
        stripeService.makeTransaction(amount);
    }
}

class KaspiPayService {
    public void sendPayment(double sum) {
        System.out.println("Processing Kaspi payment of " + sum + " тг...");
    }
}

class KaspiPaymentAdapter implements IPaymentProcessor {
    private KaspiPayService kaspiService;

    public KaspiPaymentAdapter(KaspiPayService kaspiService) {
        this.kaspiService = kaspiService;
    }

    public void processPayment(double amount) {
        kaspiService.sendPayment(amount);
    }
}

public class MainApp {
    public static void main(String[] args) {
        System.out.println("=== ДЕКОРАТОР ПАТТЕРН ===\n");

        Beverage order1 = new Espresso();
        order1 = new Milk(order1);
        order1 = new Sugar(order1);
        System.out.println("Order 1: " + order1.getDescription());
        System.out.println("Total cost: " + order1.getCost() + " тг\n");

        Beverage order2 = new Latte();
        order2 = new WhippedCream(order2);
        order2 = new Sugar(order2);
        order2 = new Milk(order2);
        System.out.println("Order 2: " + order2.getDescription());
        System.out.println("Total cost: " + order2.getCost() + " тг\n");

        System.out.println("=== АДАПТЕР ПАТТЕРН ===\n");

        IPaymentProcessor paypal = new PayPalPaymentProcessor();
        paypal.processPayment(5000.0);

        StripePaymentService stripe = new StripePaymentService();
        IPaymentProcessor stripeAdapter = new StripePaymentAdapter(stripe);
        stripeAdapter.processPayment(7500.0);

        KaspiPayService kaspi = new KaspiPayService();
        IPaymentProcessor kaspiAdapter = new KaspiPaymentAdapter(kaspi);
        kaspiAdapter.processPayment(10000.0);
    }
}
