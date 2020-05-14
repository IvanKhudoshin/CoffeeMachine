package machine;

import java.util.Scanner;

public class CoffeeMachine {

    private enum State {
        GLOBAL_MENU,
        INPUT_GLOBAL_MENU,
        CHOOSE_COFFEE,
        BUY_COFFEE,
        FILL_RESOURCES,
        ADD_WATER,
        INPUT_WATER,
        ADD_MILK,
        INPUT_MILK,
        ADD_COFFEE,
        INPUT_COFFEE,
        ADD_CUPS,
        INPUT_CUPS,
        TAKE_MONEY,
        REMAINING,
        EXIT,
        NONE_WORK
    }

    private State state = State.NONE_WORK;

    private TypeCoffee[] types = new TypeCoffee[]{
            new TypeCoffee(250, 0, 16, 4),
            new TypeCoffee(350, 75, 20, 7),
            new TypeCoffee(200, 100, 12, 6)
    };

    private int coffee;
    private int milk;
    private int water;
    private int cup;

    private int money;

    private boolean work = false;

    public CoffeeMachine(int water, int milk, int coffee, int cup, int money) {
        this.water = water;
        this.milk = milk;
        this.coffee = coffee;
        this.cup = cup;
        this.money = money;
    }

    public CoffeeMachine() {}

    public void start() {
        state = State.GLOBAL_MENU;
        handler();
    }

    public void stop() {
        state = State.NONE_WORK;
    }

    public boolean isWork() {
        return state != State.NONE_WORK;
    }

    public void printState() {
        System.out.print("The coffee machine has:\n");
        System.out.printf("%d of water\n", water);
        System.out.printf("%d of milk\n", milk);
        System.out.printf("%d of coffee beans\n", coffee);
        System.out.printf("%d of disposable cups\n", cup);
        System.out.printf("%d of money\n", money);
    }

    public void addWater(int add) {
        water += add;
    }

    public void addCoffee(int add) {
        coffee += add;
    }

    public void addMilk(int add) {
        milk += add;
    }

    public void addCup(int add) {
        cup += add;
    }

    public void addMoney(int add) {
        money += add;
    }

    public void addResources(int water, int milk, int coffee, int cup) {
        addWater(water);
        addMilk(milk);
        addCoffee(coffee);
        addCup(cup);
    }

    private void getResources(int water, int milk, int coffee, int cup) throws MakeCoffeeException {

        if (this.water < water) {
            throw new MakeCoffeeException("water");
        }

        if (this.milk < milk) {
            throw new MakeCoffeeException("milk");
        }

        if (this.coffee < coffee) {
            throw new MakeCoffeeException("coffee");
        }

        if (this.cup < cup) {
            throw new MakeCoffeeException("cup");
        }

        this.water -= water;
        this.milk -= milk;
        this.coffee -= coffee;
        this.cup -= cup;

    }

    private void getResources(int water, int milk, int coffee) throws MakeCoffeeException {
        getResources(water, milk, coffee, 1);
    }

    public int takeMoney() {
        int tmp = money;
        money = 0;
        return tmp;
    }

    private void buyCoffee(int type) throws MakeCoffeeException {
        TypeCoffee coffee = types[type];

        getResources(coffee.getWater(),
                    coffee.getMilk(),
                    coffee.getCoffee());

        money += coffee.getCost();
    }

    public void handler(String s) {
        switch (state) {
            case REMAINING:
                printState();
                state = State.GLOBAL_MENU;

                handler();

                break;

            case CHOOSE_COFFEE:
                System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");

                state = State.BUY_COFFEE;

                break;

            case BUY_COFFEE:

                if (s.equals("back")) {
                    state = State.GLOBAL_MENU;

                    handler();

                    break;
                }


                try {
                    int type = Integer.parseInt(s);

                    try {
                        buyCoffee(type - 1);

                        System.out.println("I have enough resources, making you a coffee!");
                    }
                    catch (MakeCoffeeException e) {
                        System.out.printf("Sorry, not enough %s!\n", e.getResources());
                    }

                    state = State.GLOBAL_MENU;

                }
                catch (NumberFormatException e) {
                    System.out.println("I don't understand!");

                    state = State.CHOOSE_COFFEE;
                }

                handler();

                break;

            case FILL_RESOURCES:
                state = State.ADD_WATER;
                handler();

                break;

            case ADD_WATER:
                System.out.println("Write how many ml of water do you want to add: ");
                state = State.INPUT_WATER;

                break;

            case INPUT_WATER:
                addWater(Integer.parseInt(s));

                state = State.ADD_MILK;
                handler();

                break;

            case ADD_MILK:
                System.out.println("Write how many ml of milk do you want to add: ");

                state = State.INPUT_MILK;

                break;

            case INPUT_MILK:
                addMilk(Integer.parseInt(s));

                state = State.ADD_COFFEE;
                handler();

                break;

            case ADD_COFFEE:
                System.out.println("Write how many grams of coffee beans do you want to add: ");

                state = State.INPUT_COFFEE;

                break;

            case INPUT_COFFEE:
                addCoffee(Integer.parseInt(s));

                state = State.ADD_CUPS;
                handler();

                break;

            case ADD_CUPS:
                System.out.println("Write how many disposable cups of coffee do you want to add: ");

                state = State.INPUT_CUPS;

                break;

            case INPUT_CUPS:
                addCup(Integer.parseInt(s));

                state = State.GLOBAL_MENU;
                handler();

                break;

            case TAKE_MONEY:

                System.out.printf("I gave you $%d\n", takeMoney());

                state = State.GLOBAL_MENU;

                handler();

                break;

            case EXIT:

                stop();

                break;

            case GLOBAL_MENU:

                System.out.println("Write action (buy, fill, take, remaining, exit):");

                state = State.INPUT_GLOBAL_MENU;

                break;

            case INPUT_GLOBAL_MENU:

                state = State.GLOBAL_MENU;

                switch (s) {
                    case "buy":
                        state = State.CHOOSE_COFFEE;

                        break;

                    case "fill":
                        state = State.FILL_RESOURCES;

                        break;

                    case "take":
                        state = State.TAKE_MONEY;

                        break;

                    case "remaining":
                        state = State.REMAINING;

                        break;

                    case "exit":
                        state = State.EXIT;

                        break;
                }

                handler();

                break;
        }

    }

    private void handler() {
        handler("");
    }

    private static class TypeCoffee {
        private final int water;
        private final int milk;
        private final int coffee;
        private final int cost;

        public TypeCoffee(int water, int milk, int coffee, int cost) {
            this.water = water;
            this.milk = milk;
            this.coffee = coffee;
            this.cost = cost;
        }

        public int getWater() {
            return water;
        }

        public int getMilk() {
            return milk;
        }

        public int getCoffee() {
            return coffee;
        }

        public int getCost() {
            return cost;
        }
    }

    private static class MakeCoffeeException extends Exception {
        private String resources;

        public MakeCoffeeException() {
        }

        public MakeCoffeeException(String resources) {
            this.resources = resources;
        }

        public String getResources() {
            return resources;
        }
    }
}
