import javax.print.DocFlavor;
import java.sql.*;


public class TournamentData {
public static Connection createDatabase(String databaseName) throws SQLException {
    //Connection strings.
    String url = "jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false";
    String userId = "root";
    String password = "??????????";

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
        String password = "??????????";

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
                PercentOfMetagame INTEGER NOT NULL,
                Archetype CHAR(50) NOT NULL,
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
                    Location CHAR(50) NOT NULL,
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
    }

    public static void createPlayersToMatchesTable (Connection connection) throws SQLException {
        String sqlCommand = """
                CREATE TABLE IF NOT EXISTS PlayersToMatches(
                    PlayerId INTEGER NOT NULL,
                    MatchId INTEGER NOT NULL,
                    FOREIGN KEY (PlayerId) REFERENCES Players(PlayerId),
                    FOREIGN KEY (MatchId) REFERENCES Matches(MatchId)
                );""";
    }

    public static void createDecksToCardsTable(Connection connection) throws SQLException {
        String sqlCommand = """
            CREATE TABLE IF NOT EXISTS DecksToCards (
                DeckId INTEGER NOT NULL,
                CardId INTEGER NOT NULL,
                FOREIGN KEY (DeckId) REFERENCES Decks(DeckId),
                FOREIGN KEY (CardId) REFERENCES Classes(CardId)
            );
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlCommand);
        }
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
    public static void insertRecordInDecksTable(int deckId, int percentOfMetagame, String archetype,  Connection connection) throws SQLException {
        String sqlCommand = "INSERT INTO Decks (DeckId, PercentOfMetagame, Archetype) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, deckId);
        preparedStatement.setInt(2, percentOfMetagame);
        preparedStatement.setString(3, archetype);


        preparedStatement.executeUpdate();
    }

    public static void insertRecordInCardsTable(int cardId, String name, int manaValue, String color, zConnection connection) throws SQLException {
        String sqlCommand = "INSERT INTO Students (CardId, Name, Color, ManaValue) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, cardId);
        preparedStatement.setString(2, name);
        preparedStatement.setInt(3, manaValue);
        preparedStatement.setString(4, color);

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
        String sqlCommand = "INSERT INTO StudentsToClasses (DeckId, CardId) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, deckId);
        preparedStatement.setInt(2, cardId);

        preparedStatement.executeUpdate();
    }

    public static ResultSet selectRecordsFromPlayersTable(Connection connection) 	throws SQLException{
        String sqlCommand = "SELECT * FROM Players";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFromDecksTable(Connection connection) 	throws SQLException{
        String sqlCommand = "SELECT * FROM Decks";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }


    public static ResultSet selectRecordsFromCardsTable(Connection connection) 	throws SQLException{
        String sqlCommand = "SELECT * FROM Cards";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }


    public static ResultSet selectRecordsFromTournamentsTable(Connection 	connection) throws SQLException{
        String sqlCommand = "SELECT * FROM Tournaments";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFromMatchesTable(Connection connection) 	throws SQLException{
        String sqlCommand = "SELECT * FROM Matches";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFromPlayersToTournamentsTable(Connection connection) 	throws SQLException{
        String sqlCommand = "SELECT * FROM PlayersToTournaments";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFromPlayersToMatchesTable(Connection connection) 	throws SQLException{
        String sqlCommand = "SELECT * FROM PlayersToMatches";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFromDecksToCardsTable(Connection connection) 	throws SQLException{
        String sqlCommand = "SELECT * FROM DecksToCards";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    //TODO: Replace functions below with new ones related to the tournament data schema
    /*
    public static ResultSet selectRecordsFrom_Students_StudentsToClasses_Table(int 	studentId, Connection connection) throws SQLException{
        String sqlCommand = """
                    SELECT * FROM Students
                    JOIN StudentsToClasses ON Students.StudentId=StudentsToClasses.StudentId
                    WHERE Students.StudentId = (?)
                    """;
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        preparedStatement.setInt(1, studentId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    public static ResultSet selectRecordsFrom_Students_StudentsToClasses_Classes_Table(int studentId, 	Connection connection) throws SQLException{
        String sqlCommand = """
            SELECT * FROM Students
            JOIN StudentsToClasses ON Students.StudentId = StudentsToClasses.StudentId
            JOIN Classes on StudentsToClasses.ClassId = Classes.ClassId
            WHERE StudentsToClasses.StudentId = (?)
            """;
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        preparedStatement.setInt(1, studentId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFrom_Students_StudentsToClasses_Classes_Instructors_Table(int 	studentId, Connection connection) throws SQLException{
        String sqlCommand = """
                SELECT * FROM Students
                JOIN StudentsToClasses ON Students.StudentId = StudentsToClasses.StudentId
                JOIN Classes on StudentsToClasses.ClassId = Classes.ClassId
                JOIN Instructors on Classes.InstructorId = Instructors.InstructorId
                WHERE Students.StudentId = (?)
               """;
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        preparedStatement.setInt(1, studentId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFrom_Students_StudentsToClasses_Classes_Table_Limited(int 	studentId, Connection connection) throws SQLException{
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
