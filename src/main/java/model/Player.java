package model;

public class Player {
    private final String name;
    private double balance;

    public Player(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", cash=" + balance +
                '}';
    }
}