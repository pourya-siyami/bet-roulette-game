package view;

import database.DBManager;
import model.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Console {
    private final Scanner scanner = new Scanner(System.in);
    ArrayList<Player> players = DBManager.getInstance().loadPlayer();
    double firstAmount;
    double amount;
    boolean loginStatus;

    public void showStart() {
        System.out.println("----Welcome to roulette game----");
        System.out.println("press 1 to login \npress 2 to sign up\npress 3 to exit");
        String firstChoice = scanner.next();
        switch (firstChoice) {
            case "1":
                login();
                break;
            case "2":
                signUp();
                break;
            case "3":
                break;
            default:
                System.err.println("please enter a valid number");
        }
    }

    public void login() {
        while (!loginStatus) {
            System.out.println("Enter your name: (or press e to exit)");
            String name = scanner.next();
            if (name.startsWith("e")) {
                break;
            }
            for (Player player : players) {
                if (player.getName().equals(name)) {
                    bet(player);
                    loginStatus = true;
                    break;
                }
            }
            if (!loginStatus) {
                System.err.println("invalid name, please enter a valid name or signup");
            }
        }
    }

    private void signUp() {
        System.out.println("Enter your name:");
        String name = scanner.next();
        System.out.println("Enter your cash:");
        double cash = scanner.nextDouble();
        Player p = new Player(name, cash);
        players.add(p);
        saveToDatabase(p);
        bet(p);
    }

    private static void saveToDatabase(Player p) {
        boolean sucess = DBManager.getInstance().savePlayer(p);
        if (sucess)
            System.out.println("saved to DB");
        else
            System.out.println("failed to save");
    }

    public void bet(Player player) {
        Scanner keyboard = new Scanner(System.in);
        Random generator = new Random();
        double total = 500;
        int choice, win = 0, lose = 0, spin = 0;
        int number;
        int rouletteNum;
        int result;
        char response = 'y';
        int[] resultArr = new int[37];

        while (response == 'y' || response == 'Y' && total <= 0) {
            if (player.getBalance() == 0) {
                System.err.println("You can not play. Please top up your account");
                break;
            } else if (player.getBalance() >= 1000) {
                System.out.println("You have reached 10,000 and won");
                break;
            } else {
                System.out.println("Enter your bet amount up to ==> " + player.getBalance());
                amount = keyboard.nextDouble();
                firstAmount = amount;
            }

            if (amount <= player.getBalance()) {
                System.out.print("0 - Even\n1 - Odd\n2 - Number\n");
                choice = -1;
                while (choice < 0 || choice > 2) {
                    System.out.print("Place your bet on: ");
                    choice = keyboard.nextInt();
                }
                number = 0;
                if (choice == 2) {
                    while (number < 1 || number > 36) {
                        System.out.print("Place your bet on number(1-36): ");
                        number = keyboard.nextInt();
                    }
                }
                rouletteNum = generator.nextInt(37);
                spin++;
                System.out.println("Roulette number: " + rouletteNum);

                if (choice == 2) {
                    if (rouletteNum == number)
                        result = 35;
                    else
                        result = 0;
                } else {
                    if (rouletteNum == 0 || rouletteNum % 2 != choice)
                        result = 0;
                    else
                        result = 1;
                }

                //Print out game result, win/lose amount
                if (result > 0) {
                    System.out.println("Congratulatons!!! You win!");
                    System.out.printf("You have won $%.2f \n", result * amount);
                    System.out.printf("Here's your money back: $%.2f \n",
                            (result + 1) * amount);
                    total = (result + 1) * amount + total;
                    win++;
                    resultArr[rouletteNum]++;
                    player.setBalance(player.getBalance() - firstAmount + firstAmount * 2);
                    DBManager.getInstance().savePlayers(players);

                } else {
                    System.out.println("You lose. Better luck next time!");
                    System.out.printf("You have lost $%.2f \n",
                            (result + 1) * amount);
                    total = total - (result + 1) * (amount);
                    lose++;
                    resultArr[rouletteNum]++;
                    player.setBalance(player.getBalance() - firstAmount);
                    DBManager.getInstance().savePlayers(players);

                    if (total <= 0) {
                        break;
                    }
                }
                //Ask for another game
                for (int totals = 1; totals < 36; totals++) {
                    if (resultArr[totals] > 0) {
                        System.out.println("The number " + totals + " won " + resultArr[totals] + " times.");
                    }
                }
                System.out.println("You have won " + win + " games.");
                System.out.println("You have lost " + lose + " games.");
                System.out.println("The wheel has been spun " + spin + " times.");
                System.out.print("\nWould you like to play another game? (y/n) ");
                response = keyboard.next().charAt(0);

            } else {
                System.err.println("enter your cash up to " + player.getBalance());
            }
        }
    }
}