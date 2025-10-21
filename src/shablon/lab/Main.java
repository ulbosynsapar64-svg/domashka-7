package shablon.lab;

import java.util.*;

// =======================
// ===== 1. COMMAND =====
// =======================

// Интерфейс команд
interface ICommand {
    void execute();
    void undo();
}

// Устройства
class Light {
    public void turnOn() {
        System.out.println("💡 Жарық қосылды");
    }
    public void turnOff() {
        System.out.println("🌑 Жарық сөндірілді");
    }
}

class Door {
    public void open() {
        System.out.println("🚪 Есік ашылды");
    }
    public void close() {
        System.out.println("🔒 Есік жабылды");
    }
}

class Thermostat {
    private int temperature = 22;
    public void increase() {
        temperature++;
        System.out.println("🔥 Температура көтерілді: " + temperature + "°C");
    }
    public void decrease() {
        temperature--;
        System.out.println("❄️ Температура төмендеді: " + temperature + "°C");
    }
}

// Команды
class LightOnCommand implements ICommand {
    private Light light;
    public LightOnCommand(Light light) { this.light = light; }
    public void execute() { light.turnOn(); }
    public void undo() { light.turnOff(); }
}

class DoorOpenCommand implements ICommand {
    private Door door;
    public DoorOpenCommand(Door door) { this.door = door; }
    public void execute() { door.open(); }
    public void undo() { door.close(); }
}

class ThermostatIncreaseCommand implements ICommand {
    private Thermostat thermostat;
    public ThermostatIncreaseCommand(Thermostat thermostat) { this.thermostat = thermostat; }
    public void execute() { thermostat.increase(); }
    public void undo() { thermostat.decrease(); }
}

// Invoker
class Invoker {
    private Stack<ICommand> history = new Stack<>();
    public void executeCommand(ICommand command) {
        command.execute();
        history.push(command);
    }
    public void undoLastCommand() {
        if (!history.isEmpty()) {
            ICommand last = history.pop();
            System.out.println("⏪ Соңғы әрекет жойылды:");
            last.undo();
        } else {
            System.out.println("❗ Жоюға команда жоқ!");
        }
    }
}


// ==============================
// ===== 2. TEMPLATE METHOD =====
// ==============================

abstract class Beverage {
    final void prepareRecipe() {
        boilWater();
        brew();
        pourInCup();
        if (customerWantsCondiments()) addCondiments();
    }

    void boilWater() { System.out.println("💧 Су қайнатылды"); }
    void pourInCup() { System.out.println("☕ Кесе толтырылды"); }

    abstract void brew();
    abstract void addCondiments();

    boolean customerWantsCondiments() { return true; }
}

class Tea extends Beverage {
    void brew() { System.out.println("🍵 Шай демделді"); }
    void addCondiments() { System.out.println("🍋 Лимон қосылды"); }
}

class Coffee extends Beverage {
    void brew() { System.out.println("☕ Кофе қайнатылды"); }
    void addCondiments() { System.out.println("🥛 Қант пен сүт қосылды"); }
    boolean customerWantsCondiments() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Қант пен сүт қосайын ба (иә/жоқ)? ");
        String ans = sc.nextLine();
        return ans.equalsIgnoreCase("иә");
    }
}

class HotChocolate extends Beverage {
    void brew() { System.out.println("🍫 Какао ұнтағы араластырылды"); }
    void addCondiments() { System.out.println("🍬 Кілегей мен зефир қосылды"); }
}


// ============================
// ===== 3. MEDIATOR CHAT =====
// ============================

interface IMediator {
    void sendMessage(String message, User sender);
    void addUser(User user);
}

class ChatRoom implements IMediator {
    private List<User> users = new ArrayList<>();
    public void addUser(User user) {
        users.add(user);
        System.out.println("👤 " + user.getName() + " чатқа қосылды.");
    }
    public void sendMessage(String message, User sender) {
        for (User user : users) {
            if (user != sender) {
                user.receive(message, sender.getName());
            }
        }
    }
}

abstract class User {
    protected IMediator mediator;
    protected String name;

    public User(IMediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
    }

    public String getName() { return name; }
    public abstract void send(String message);
    public abstract void receive(String message, String sender);
}

class ChatUser extends User {
    public ChatUser(IMediator mediator, String name) {
        super(mediator, name);
    }

    public void send(String message) {
        System.out.println("💬 " + name + " жібереді: " + message);
        mediator.sendMessage(message, this);
    }

    public void receive(String message, String sender) {
        System.out.println("📩 " + name + " алды (" + sender + "): " + message);
    }
}


// ============================
// ======== MAIN ==============
// ============================

public class Main {
    public static void main(String[] args) {
        System.out.println("\n============================");
        System.out.println("🏠 ПАТТЕРН 1: COMMAND (Ақылды үй)");
        System.out.println("============================");

        Light light = new Light();
        Door door = new Door();
        Thermostat thermostat = new Thermostat();
        Invoker remote = new Invoker();

        remote.executeCommand(new LightOnCommand(light));
        remote.executeCommand(new DoorOpenCommand(door));
        remote.executeCommand(new ThermostatIncreaseCommand(thermostat));
        remote.undoLastCommand();

        System.out.println("\n============================");
        System.out.println("☕ ПАТТЕРН 2: TEMPLATE METHOD (Сусындар)");
        System.out.println("============================");

        Beverage tea = new Tea();
        tea.prepareRecipe();

        Beverage coffee = new Coffee();
        coffee.prepareRecipe();

        Beverage chocolate = new HotChocolate();
        chocolate.prepareRecipe();

        System.out.println("\n============================");
        System.out.println("💬 ПАТТЕРН 3: MEDIATOR (Чат)");
        System.out.println("============================");

        ChatRoom chatRoom = new ChatRoom();
        User ulbo = new ChatUser(chatRoom, "Улбо");
        User ali = new ChatUser(chatRoom, "Әли");
        User aisha = new ChatUser(chatRoom, "Айша");

        chatRoom.addUser(ulbo);
        chatRoom.addUser(ali);
        chatRoom.addUser(aisha);

        ulbo.send("Сәлем, достар!");
        ali.send("Сәлем, Улбо!");
        aisha.send("Қалайсыңдар?");
    }
}
