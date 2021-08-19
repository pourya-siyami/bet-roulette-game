package database;

import model.Player;

import java.sql.*;
import java.util.ArrayList;

public class DBManager {
    private Connection connection;
    private static DBManager instance;

    private DBManager() {
        String url = "jdbc:sqlite:bet.db";
        try {
            this.connection = DriverManager.getConnection(url);
        } catch (SQLException sqlException) {
            System.err.println("There is an error connecting to the database");
        }
    }

    public static DBManager getInstance() {
        if (instance == null) instance = new DBManager();
        return instance;
    }

    public ArrayList<Player> loadPlayer() {
        ArrayList<Player> players = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM \"players\";";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                double balance = resultSet.getDouble("Balance");
                players.add(new Player(name, balance));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return players;
    }

    public boolean savePlayer(Player player) {
        try {
            Statement statement = connection.createStatement();
            String query = String.format("INSERT INTO \"players\" (Name,Balance) " +
                            "VALUES( '%s', '%f');"
                    , player.getName(), player.getBalance());
            statement.execute(query);
            return true;
        } catch (SQLException throwables) {
            System.err.println("failed to save");
        }
        return false;
    }

    public void savePlayers(ArrayList<Player> players) {
        try {
            Statement statement = connection.createStatement();
            String deleteQuery = "DELETE FROM players";
            statement.executeUpdate(deleteQuery);
            String query = "INSERT INTO players (Name, Balance) VALUES ('%s', %f);";
            for (Player player : players) {
                statement.executeUpdate(String.format(query, player.getName(), player.getBalance()));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}