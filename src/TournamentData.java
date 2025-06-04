import javax.print.DocFlavor;
import java.sql.*;


public class TournamentData {
public static Connection createDatabase(String databaseName) throws SQLException {
    //Connection strings.
    String url = "jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false";
    String userId = "root";
    String password = "";

    //Connect to the database server.
    Connection con = DriverManager.getConnection(url, userId, password);
    String sqlCommand = "CREATE DATABASE IF NOT EXISTS " + databaseName + ";";
    try (Statement stmt = con.createStatement()) {
        stmt.execute(sqlCommand);
    }

    url ="jdbc:mysql://localhost:3306/" + databaseName +"?autoReconnect=true&useSSL=false";
    con = DriverManager.getConnection(url, userId, password);
    return con;
}
    public static Connection createDatabase(String databaseName, boolean enforceForeignKeyConstraint) throws SQLException {
        //Connection strings.
        Connection con = null;
        String url = "jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false";
        String userId = "root";
        String password = "8679";

        if (enforceForeignKeyConstraint)
        {
            //SQLiteConfig sqLiteConfig = new SQLiteConfig();
            //sqLiteConfig.enforceForeignKeys(enforceForeignKeyConstraint);

            //connection = DriverManager.getConnection(url, sqLiteConfig.toProperties());
        }
        else {
            //Connect to the database server.
            con = DriverManager.getConnection(url, userId, password);
            String sqlCommand = "CREATE DATABASE IF NOT EXISTS " + databaseName + ";";
            try (Statement stmt = con.createStatement()) {
                stmt.execute(sqlCommand);
            }

            url = "jdbc:mysql://localhost:3306/" + databaseName + "?autoReconnect=true&useSSL=false";
            con = DriverManager.getConnection(url, userId, password);
        }
        return con;
    }


    public static void dropTable(String tableName, Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS " + tableName);
        }
    }

    public static void createPlayersTable(Connection connection) throws SQLException {
        String sqlCommand = """
            CREATE TABLE IF NOT EXISTS Players (
                PlayerId INTEGER PRIMARY KEY,
                FirstName CHAR(50) NOT NULL,
                LastName CHAR(50) NOT NULL,
                Result INTEGER NOT NULL,
                DeckId INTEGER NOT NULL,
                FOREIGN KEY (DeckId) REFERENCES Decks(DeckId)
            );
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlCommand);
        }
    }

    public static void createDecksTable(Connection connection) throws SQLException {
        String sqlCommand = """
            CREATE TABLE IF NOT EXISTS Decks (
                DeckId INTEGER PRIMARY KEY,
                Archetype CHAR(50) NOT NULL,
                PercentOfMetagame FLOAT NOT NULL
            );
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlCommand);
        }
    }

    public static void createCardsTable(Connection connection) throws SQLException {
        String sqlCommand = """
            CREATE TABLE IF NOT EXISTS Cards (
                CardId INTEGER PRIMARY KEY,
                Name CHAR(50) NOT NULL,
                ManaValue INTEGER NOT NULL,
                Color CHAR(50) NOT NULL
            );
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlCommand);
        }
    }

    public static void createMatchesTable(Connection connection) throws SQLException {
        String sqlCommand = """
            CREATE TABLE IF NOT EXISTS Matches (
                MatchId INTEGER PRIMARY KEY,
                Score CHAR(50) NOT NULL,
                PlayerId1 INTEGER NOT NULL,
                PlayerId2 INTEGER NOT NULL,
                TournamentId INTEGER NOT NULL,
                FOREIGN KEY (PlayerId1) REFERENCES Players(PlayerId),
                FOREIGN KEY (PlayerId2) REFERENCES Players(PlayerId),
                FOREIGN KEY (TournamentId) REFERENCES Tournaments(TournamentId)
            );
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlCommand);
        }
    }

    public static void createTournamentsTable(Connection connection) throws SQLException {
        String sqlCommand = """
                CREATE TABLE IF NOT EXISTS Tournaments (
                    TournamentId INTEGER PRIMARY KEY,
                    Name CHAR(50) NOT NULL,
                    Location CHAR(50) NOT NULL
                );
                """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlCommand);
        }
    }

    public static void createPlayersToTournamentsTable(Connection connection) throws SQLException {
        String sqlCommand = """
                CREATE TABLE IF NOT EXISTS PlayersToTournaments(
                    PlayerId INTEGER NOT NULL,
                    TournamentID INTEGER NOT NULL,
                    FOREIGN KEY (PlayerId) REFERENCES Players(PlayerId),
                    FOREIGN KEY (TournamentId) REFERENCES Tournaments(TournamentId)
                );
                """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlCommand);
        }
    }

    public static void createPlayersToMatchesTable (Connection connection) throws SQLException {
        String sqlCommand = """
                CREATE TABLE IF NOT EXISTS PlayersToMatches(
                    PlayerId INTEGER NOT NULL,
                    MatchId INTEGER NOT NULL,
                    FOREIGN KEY (PlayerId) REFERENCES Players(PlayerId),
                    FOREIGN KEY (MatchId) REFERENCES Matches(MatchId)
                );""";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlCommand);
        }
    }

    public static void createDecksToCardsTable(Connection connection) throws SQLException {
        String sqlCommand = """
            CREATE TABLE IF NOT EXISTS DecksToCards (
                DeckId INTEGER NOT NULL,
                CardId INTEGER NOT NULL,
                FOREIGN KEY (DeckId) REFERENCES Decks(DeckId),
                FOREIGN KEY (CardId) REFERENCES Cards(CardId)
            );
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlCommand);
        }
    }

    public static void deleteRecordInDecksTable(int deckId, Connection connection) throws SQLException {
        String sqlCommand = "DELETE FROM Decks WHERE DeckId = (DeckId) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, deckId);

        preparedStatement.executeUpdate();
    }

    public static void deleteRecordInCardsTable(int cardId, Connection connection) throws SQLException {
        String sqlCommand = "DELETE FROM Cards WHERE CardId = (CardId) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, cardId);


        preparedStatement.executeUpdate();
    }

    public static void deleteRecordInMatchesTable(int matchId, Connection connection) throws SQLException {
        String sqlCommand = "DELETE FROM Matches WHERE MatchId = (MatchId) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, matchId);

        preparedStatement.executeUpdate();
    }
    public static void deleteRecordInTournamentsTable(int tournamentId, Connection connection) throws SQLException {
        String sqlCommand = "DELETE FROM Tournaments WHERE TournamentId = (TournamentId) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, tournamentId);

        preparedStatement.executeUpdate();
    }
    public static void deleteRecordInPlayersToTournamentsTable(int playerId, Connection connection) throws SQLException {
        String sqlCommand = "DELETE FROM PlayersToTournaments WHERE PlayerId = (PlayerId) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, playerId);

        preparedStatement.executeUpdate();
    }

    public static void deleteRecordInPlayersToMatchesTable(int playerId, Connection connection) throws SQLException {
        String sqlCommand = "DELETE FROM PlayersToMatches WHERE PlayerId = (PlayerId) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, playerId);

        preparedStatement.executeUpdate();
    }

    public static void deleteRecordInDecksToCardsTable(int deckId, int cardId, Connection connection) throws SQLException {
        String sqlCommand = "DELETE FROM DecksToCards WHERE DeckId = (DeckId) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, deckId);

        preparedStatement.executeUpdate();
    }

    public static void insertRecordInPlayersTable(int playerId, String firstName, String lastName, int result, int deckId, Connection connection) throws SQLException {
        String sqlCommand = "INSERT INTO Players (PlayerId, FirstName, LastName, Result, DeckId) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, playerId);
        preparedStatement.setString(2, firstName);
        preparedStatement.setString(3, lastName);
        preparedStatement.setInt(4, result);
        preparedStatement.setInt(5, deckId);

        preparedStatement.executeUpdate();
    }

    public static void insertRecordInDecksTable(int deckId, float percentOfMetagame, String archetype,  Connection connection) throws SQLException {
        String sqlCommand = "INSERT INTO Decks (DeckId, Archetype, PercentOfMetagame) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, deckId);
        preparedStatement.setString(2, archetype);
        preparedStatement.setFloat(3, percentOfMetagame);

        preparedStatement.executeUpdate();
    }

    public static void insertRecordInCardsTable(int cardId, String name, int manaValue, String color, Connection connection) throws SQLException {
        String sqlCommand = "INSERT INTO Cards (CardId, Name, Color, ManaValue) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, cardId);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, color);
        preparedStatement.setInt(4, manaValue);

        preparedStatement.executeUpdate();
    }

    public static void insertRecordInMatchesTable(int matchId, String score, int playerId1, int playerId2, int tournamentId, Connection connection) throws SQLException {
        String sqlCommand = "INSERT INTO Matches (MatchId, Score, PlayerId1, PlayerId2, TournamentId) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, matchId);
        preparedStatement.setString(2, score);
        preparedStatement.setInt(3, playerId1);
        preparedStatement.setInt(4,playerId2);
        preparedStatement.setInt(5, tournamentId);

        preparedStatement.executeUpdate();
    }
    public static void insertRecordInTournamentsTable(int tournamentId, String name, String location, Connection connection) throws SQLException {
        String sqlCommand = "INSERT INTO Tournaments (TournamentId, Name, Location) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, tournamentId);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, location);
        preparedStatement.executeUpdate();
    }
    public static void insertRecordInPlayersToTournamentsTable(int playerId, int tournamentId, Connection connection) throws SQLException {
        String sqlCommand = "INSERT INTO PlayersToTournaments (PlayerId, TournamentId) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, playerId);
        preparedStatement.setInt(2, tournamentId);

        preparedStatement.executeUpdate();
    }

    public static void insertRecordInPlayersToMatchesTable(int playerId,int matchId, Connection connection) throws SQLException {
        String sqlCommand = "INSERT INTO PlayersToMatches (PlayerId, MatchId) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, playerId);
        preparedStatement.setInt(2, matchId);

        preparedStatement.executeUpdate();
    }

    public static void insertRecordInDecksToCardsTable(int deckId, int cardId, Connection connection) throws SQLException {
        String sqlCommand = "INSERT INTO DecksToCards (DeckId, CardId) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, deckId);
        preparedStatement.setInt(2, cardId);

        preparedStatement.executeUpdate();
    }
    public static ResultSet selectRecordsFromPlayersTable(Connection connection) throws SQLException{
        String sqlCommand = "SELECT * FROM Players";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFromDecksTable(Connection connection) throws SQLException{
        String sqlCommand = "SELECT * FROM Decks";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }


    public static ResultSet selectRecordsFromCardsTable(Connection connection) throws SQLException{
        String sqlCommand = "SELECT * FROM Cards";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }


    public static ResultSet selectRecordsFromTournamentsTable(Connection connection) throws SQLException{
        String sqlCommand = "SELECT * FROM Tournaments";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFromMatchesTable(Connection connection) throws SQLException{
        String sqlCommand = "SELECT * FROM Matches";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFromPlayersToTournamentsTable(Connection connection) throws SQLException{
        String sqlCommand = "SELECT * FROM PlayersToTournaments";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFromPlayersToMatchesTable(Connection connection) throws SQLException{
        String sqlCommand = "SELECT * FROM PlayersToMatches";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFromDecksToCardsTable(Connection connection) throws SQLException{
        String sqlCommand = "SELECT * FROM DecksToCards";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    //TODO: Replace functions below with new ones related to the tournament data schema

    public static ResultSet selectRecordsFrom_Players_Deck_Table(int playerId, Connection connection) throws SQLException{
        String sqlCommand = """
                    SELECT * FROM Players
                    JOIN Decks ON Players.DeckId=Decks.DeckId
                    WHERE Players.PlayerId = (?)
                    """;
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        preparedStatement.setInt(1, playerId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFrom_Decks_DecksToCards_Cards_Table(int deckID, Connection connection) throws SQLException{
        String sqlCommand = """
                    SELECT * FROM Decks
                    JOIN DecksToCards ON Decks.DeckId=DecksToCards.DeckId
                    JOIN Cards ON Cards.CardId = DecksToCards.CardId
                    WHERE Decks.DeckId = (?)
                    """;
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        preparedStatement.setInt(1, deckID);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    
    public static ResultSet selectRecordsFrom_Players_Decks_DecksToCards_Cards_Table(int playerId, Connection connection) throws SQLException{
        String sqlCommand = """
            SELECT * FROM Players
            JOIN Decks ON Players.DeckId = Decks.DeckId
            JOIN DecksToCards on Decks.DeckId = DecksToCards.DeckId
            JOIN Cards on DecksToCards.CardID = Cards.CardId
            WHERE Players.PlayerId = (?)
            """;
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        preparedStatement.setInt(1, playerId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFrom_Players_Decks_DecksToCards_Cards_Table_Limited(int playerId, Connection connection) throws SQLException{
        String sqlCommand = """
                SELECT Players.FirstName, Players.LastName,
                    Decks.Name AS DeckName,
                    Cards.Name AS CardName
                FROM Players
                JOIN Decks ON Players.DeckId = Decks.DeckId
                JOIN DecksToCards on Decks.DeckId = DecksToCards.DeckId
                JOIN Cards on DecksToCards.CardID = Cards.CardId
                WHERE Players.StudentId = (?)
               """;
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        preparedStatement.setInt(1, playerId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    
    public static ResultSet selectRecordsFrom_Players_PlayersToMatches_Matches_Table(int playerId, Connection connection) throws SQLException{
        String sqlCommand = """
            SELECT * FROM Players
            JOIN PlayersToMatches ON Players.PlayerId = PlayersToMatches.PlayerId
            JOIN Matches on Matches.MatchId = PlayersToMatches.MatchId
            WHERE Players.PlayerId = (?)
            """;
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        preparedStatement.setInt(1, playerId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

     public static ResultSet selectRecordsFrom_Players_PlayersToTournaments_Tournaments_Table(int playerId, Connection connection) throws SQLException{
        String sqlCommand = """
                    SELECT * FROM Players
                    JOIN PlayersToTournaments ON Players.PlayerId = PlayersToTournaments.PlayerId
                    JOIN Tournaments ON PlayersToTournaments.TournamentId = Tournaments.TournamentId
                    WHERE Players.PlayerId = (?)
                    """;
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        preparedStatement.setInt(1, playerId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    
    public static void createViews(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();

        stmt.execute("CREATE VIEW ViewPlayers AS SELECT * FROM Players");
        stmt.execute("CREATE VIEW ViewDecks AS SELECT * FROM Decks");
        stmt.execute("CREATE VIEW ViewCards AS SELECT * FROM Cards");
        stmt.execute("CREATE VIEW ViewMatches AS SELECT * FROM Matches");
        stmt.execute("CREATE VIEW ViewTournaments AS SELECT * FROM Tournaments");
        stmt.execute("CREATE VIEW ViewPlayersToTournaments AS SELECT * FROM PlayersToTournaments");
        stmt.execute("CREATE VIEW ViewPlayersToMatches AS SELECT * FROM PlayersToMatches");
        stmt.execute("CREATE VIEW ViewDecksToCards AS SELECT * FROM DecksToCards");

        stmt.close();
    }

    public static void dropViews(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();

        stmt.execute("DROP VIEW IF EXISTS ViewPlayers");
        stmt.execute("DROP VIEW IF EXISTS ViewDecks");
        stmt.execute("DROP VIEW IF EXISTS ViewCards");
        stmt.execute("DROP VIEW IF EXISTS ViewMatches");
        stmt.execute("DROP VIEW IF EXISTS ViewTournaments");
        stmt.execute("DROP VIEW IF EXISTS ViewPlayersToTournaments");
        stmt.execute("DROP VIEW IF EXISTS ViewPlayersToMatches");
        stmt.execute("DROP VIEW IF EXISTS ViewDecksToCards");

        stmt.close();
    }
    /*public static ResultSet selectRecordsFrom_Students_StudentsToClasses_Classes_Table_Limited(int 	studentId, Connection connection) throws SQLException{
        String sqlCommand = """
            SELECT Students.FirstName, Students.LastName,
                Classes.Title AS ClassesTitle,
                Classes.Subject AS ClassesSubject,
                Classes.Number AS ClassesNumber
            FROM Students
            JOIN StudentsToClasses ON Students.StudentId = StudentsToClasses.StudentId
            JOIN Classes ON StudentsToClasses.ClassId = Classes.ClassId
            WHERE StudentsToClasses.StudentId = (?)
            """;
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        preparedStatement.setInt(1, studentId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFrom_Students_StudentsToClasses_Classes_Instructors_Table_Limited(	int studentId, Connection connection) throws SQLException{
        String sqlCommand = """
            SELECT Students.FirstName, Students.LastName,
                Classes.Title AS ClassesTitle,
                Classes.Subject AS ClassesSubject,
                Classes.Number AS ClassesNumber,
                Instructors.FirstName AS InstructorsFirstName,
                Instructors.LastName AS InstructorsLastName
            FROM Students
            JOIN StudentsToClasses ON Students.StudentId = StudentsToClasses.StudentId
            JOIN Classes ON StudentsToClasses.ClassId = Classes.ClassId
            JOIN Instructors ON Classes.InstructorID = Instructors.InstructorID
            WHERE StudentsToClasses.StudentId = (?)
            """;
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        preparedStatement.setInt(1, studentId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }*/

}
