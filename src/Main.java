import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        DateFormat df2 = new SimpleDateFormat("hh:mm a");
        try (Connection connection = TournamentData.createDatabase("TournamentData")) {
            //connection.createStatement().execute("PRAGMA foreign_keys = ON");

            // Drop tables in proper order
            TournamentData.dropTable("PlayersToMatches", connection);
            TournamentData.dropTable("Matches", connection);
            TournamentData.dropTable("PlayersToTournaments", connection);
            TournamentData.dropTable("Tournaments", connection);
            TournamentData.dropTable("Players", connection);
            TournamentData.dropTable("DecksToCards", connection);
            TournamentData.dropTable("Cards", connection);
            TournamentData.dropTable("Decks", connection);
            TournamentData.dropViews(connection);
            TournamentData.dropTriggers(connection);
            TournamentData.dropProcedures(connection);

            // Create tables
            TournamentData.createDecksTable(connection);
            TournamentData.createCardsTable(connection);
            TournamentData.createDecksToCardsTable(connection);
            TournamentData.createPlayersTable(connection);
            TournamentData.createTournamentsTable(connection);
            TournamentData.createPlayersToTournamentsTable(connection);
            TournamentData.createMatchesTable(connection);
            TournamentData.createPlayersToMatchesTable(connection);
            TournamentData.createProcedures(connection);

            //Create Triggers
            TournamentData.createTriggers(connection);

            // Insert data
            insertDecks(connection);
            insertCards(connection);
            insertDecksToCards(connection);
            insertPlayers(connection);
            insertTournaments(connection);
            insertMatches(connection);
            insertPlayersToMatches(connection);
            insertPlayersToTournaments(connection);

            // create views
            TournamentData.createViews(connection);

            System.out.print("Enter S to search for a record, Q to quit: ");
            Scanner s = new Scanner(System.in);
            String response = s.nextLine();

            while(!response.equals("Q"))
            {
                if (response.equals("S"))
                {
                    System.out.print("Enter 1-Players, 2-Decks, 3-Cards, 4-Matches, 5-Tournaments, 6-Players Decks Cards, 7-Players Matches, 8-Players Tournaments: ");
                    String table = s.nextLine();
                    printIds(connection, table);
                    if(table.equals("1")){
                        System.out.print("Player ID: ");
                        String ID = s.nextLine();
                        ResultSet set = TournamentData.selectRecordsFromPlayersTable(connection);
                        while(set.next()){
                            if(set.getInt(1) == Integer.parseInt(ID)){
                                ResultSetMetaData resultSetMetaData = set.getMetaData();
                                int columnCount = resultSetMetaData.getColumnCount();
                                for (int index = 1; index <= columnCount; index++)
                                {
                                    String columnValue = set.getString(index);
                                    System.out.print(columnValue +  " ");
                                }
                            }
                        }
                        System.out.println();
                    } else if(table.equals("2")){
                        System.out.print("Deck ID: ");
                        String ID = s.nextLine();
                        ResultSet set = TournamentData.selectRecordsFromDecksTable(connection);
                        while(set.next()){
                            if(set.getInt(1) == Integer.parseInt(ID)){
                                ResultSetMetaData resultSetMetaData = set.getMetaData();
                                int columnCount = resultSetMetaData.getColumnCount();
                                for (int index = 1; index <= columnCount; index++)
                                {
                                    String columnValue = set.getString(index);
                                    System.out.print(columnValue +  " ");
                                }
                            }
                        }
                        System.out.println();
                    } else if(table.equals("3")){
                        System.out.print("Card ID: ");
                        String ID = s.nextLine();
                        ResultSet set = TournamentData.selectRecordsFromCardsTable(connection);
                        while(set.next()){
                            if(set.getInt(1) == Integer.parseInt(ID)){
                                ResultSetMetaData resultSetMetaData = set.getMetaData();
                                int columnCount = resultSetMetaData.getColumnCount();
                                for (int index = 1; index <= columnCount; index++)
                                {
                                    String columnValue = set.getString(index);
                                    System.out.print(columnValue +  " ");
                                }
                            }
                        }
                        System.out.println();
                    } else if(table.equals("4")){
                        System.out.print("Match ID: ");
                        String ID = s.nextLine();
                        ResultSet set = TournamentData.selectRecordsFromMatchesTable(connection);
                        while(set.next()){
                            if(set.getInt(1) == Integer.parseInt(ID)){
                                ResultSetMetaData resultSetMetaData = set.getMetaData();
                                int columnCount = resultSetMetaData.getColumnCount();
                                for (int index = 1; index <= columnCount; index++)
                                {
                                    String columnValue = set.getString(index);
                                    System.out.print(columnValue +  " ");
                                }
                            }
                        }
                        System.out.println();
                    } else if(table.equals("5")){
                        System.out.print("Tournament ID: ");
                        String ID = s.nextLine();
                        ResultSet set = TournamentData.selectRecordsFromTournamentsTable(connection);
                        while(set.next()){
                            if(set.getInt(1) == Integer.parseInt(ID)){
                                ResultSetMetaData resultSetMetaData = set.getMetaData();
                                int columnCount = resultSetMetaData.getColumnCount();
                                for (int index = 1; index <= columnCount; index++)
                                {
                                    String columnValue = set.getString(index);
                                    System.out.print(columnValue +  " ");
                                }
                            }
                        }
                        System.out.println();
                    } else if(table.equals("6")){
                        System.out.print("Player ID: ");
                        String ID = s.nextLine();
                        ResultSet set = TournamentData.selectRecordsFrom_Players_Decks_DecksToCards_Cards_Table_Limited(Integer.parseInt(ID), connection);
                        outputResultSet(set);
                    } else if(table.equals("7")){
                        System.out.print("Player ID: ");
                        String ID = s.nextLine();
                        ResultSet set = TournamentData.selectRecordsFrom_Players_PlayersToMatches_Matches_Table(Integer.parseInt(ID), connection);
                        outputResultSet(set);
                    } else if(table.equals("8")){
                        System.out.print("Player ID: ");
                        String ID = s.nextLine();
                        ResultSet set = TournamentData.selectRecordsFrom_Players_PlayersToTournaments_Tournaments_Table_Limited(Integer.parseInt(ID), connection);
                        outputResultSet(set);
                    }  else {
                        System.out.println("Input error...");
                    }
                    System.out.print("Enter S to search for a record, Q to quit: ");
                    response = s.nextLine();
                } else {
                    System.out.println("Input error...");
                    System.out.print("Enter S to search for a record, Q to quit: ");
                    response = s.nextLine();
                }
            }
        }	catch(Exception e){
            System.out.println(e);
        }
    }
    public static void insertPlayers(Connection connection){
        String filePath = "Players.txt"; // Path to your CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(", ");
                TournamentData.insertRecordInPlayersTable(Integer.parseInt(values[0]), values[1],values[2], Integer.parseInt(values[3]), Integer.parseInt(values[4]), connection);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertDecks(Connection connection) throws ParseException {
        String filePath = "Decks.txt"; // Path to your CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(", ");
                TournamentData.insertRecordInDecksTable(Integer.parseInt(values[0]), Float.parseFloat(values[2]), values[1], connection);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertCards(Connection connection) throws ParseException {
        String filePath = "Cards.txt"; // Path to your CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(", ");
                TournamentData.insertRecordInCardsTable(Integer.parseInt(values[0]), values[1], Integer.parseInt(values[2]), values[3], connection);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertMatches(Connection connection){
        String filePath = "Matches.txt"; // Path to your CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(", ");
                TournamentData.insertRecordInMatchesTable(Integer.parseInt(values[0]), values[1], Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]), connection);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertTournaments(Connection connection){
        String filePath = "Tournaments.txt"; // Path to your CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(", ");
                TournamentData.insertRecordInTournamentsTable(Integer.parseInt(values[0]), values[1], values[2], connection);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertPlayersToTournaments(Connection connection){
        String filePath = "PlayersToTournaments.txt"; // Path to your CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(", ");
                TournamentData.insertRecordInPlayersToTournamentsTable(Integer.parseInt(values[0]), Integer.parseInt(values[1]), connection);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertPlayersToMatches(Connection connection){
        String filePath = "PlayersToMatches.txt"; // Path to your CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(", ");
                TournamentData.insertRecordInPlayersToMatchesTable(Integer.parseInt(values[0]), Integer.parseInt(values[1]), connection);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

     public static void insertDecksToCards(Connection connection){
        String filePath = "DecksToCards.txt"; // Path to your CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(", ");
                TournamentData.insertRecordInDecksToCardsTable(Integer.parseInt(values[0]), Integer.parseInt(values[1]), connection);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void outputResultSet(ResultSet resultSet) throws SQLException
    {
        while (resultSet.next())
        {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            int columnCount = resultSetMetaData.getColumnCount();

            for (int index = 1; index <= columnCount; index++)
            {
                String columnValue = resultSet.getString(index);

                System.out.print(columnValue +  " ");
            }

            System.out.println();
        }
    }

    public static void printIds (Connection connection, String table) throws SQLException {
        String procedure = "{CALL fetchId";

        switch (table) {
            case "1", "6", "7", "8" -> procedure += "Players()}";
            case "2" -> procedure += "Decks()}";
            case "3" -> procedure += "Cards()}";
            case "4" -> procedure += "Matches()}";
            case "5" -> procedure += "Tournaments()}";
        }

        if(procedure.equals("{CALL fetchId")){
            return;
        }

        PreparedStatement preparedStatement = connection.prepareStatement(procedure);
        ResultSet resultSet = preparedStatement.executeQuery();
        outputResultSet(resultSet);
    }


}