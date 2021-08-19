import database.DBManager;
import model.Player;
import view.Console;

import java.util.ArrayList;

public class RouletteGame {
    public static void main(String[] args) {
        loadPlayers();
        Console console = new Console();
        console.showStart();
    }

    private static void loadPlayers() {
        ArrayList<Player> players = DBManager.getInstance().loadPlayer();
        for (Player player : players)
            System.out.println(player);
    }
}