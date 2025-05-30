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
                PercentOfMetaGame INTEGER NOT NULL,
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
                Color CHAR(50) NOT NULL,
                ManaValue INTEGER NOT NULL
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
                PlayerId1 INTEGER NOT NULL,
                PlayerId2 INTEGER NOT NULL,
                Score CHAR(50) NOT NULL,
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
    public static void insertRecordInDepartmentsTable(int departmentsId, String name, Connection connection) throws SQLException {
        String sqlCommand = "INSERT INTO Departments (DepartmentsId, Name) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, departmentsId);
        preparedStatement.setString(2, name);

        preparedStatement.executeUpdate();
    }
    public static void insertRecordInInstructorsTable(int instructorId, int departmentId, String firstName, String lastName,  Connection connection) throws SQLException {
        String sqlCommand = "INSERT INTO Instructors (InstructorId, DepartmentsId, FirstName, LastName) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, instructorId);
        preparedStatement.setInt(2, departmentId);
        preparedStatement.setString(3, firstName);
        preparedStatement.setString(4, lastName);


        preparedStatement.executeUpdate();
    }

    public static void insertRecordInStudentsTable(int studentId, String firstName, String lastName, Date birthDate, Connection connection) throws SQLException {
        String sqlCommand = "INSERT INTO Students (StudentId, FirstName, LastName, Birthdate) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, studentId);
        preparedStatement.setString(2, firstName);
        preparedStatement.setString(3, lastName);
        preparedStatement.setDate(4, birthDate);

        preparedStatement.executeUpdate();
    }

    public static void insertRecordInClassesTable(int classId, int instructorId, String title, String subject, int number, int credits, String term, Time time, Connection connection) throws SQLException {
        String sqlCommand = "INSERT INTO Classes (ClassId, InstructorId, Title, Subject, Number, Credits, Term, Time) VALUES (?, ?, ?, ?, ? ,? ,?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, classId);
        preparedStatement.setInt(2, instructorId);
        preparedStatement.setString(3, title);
        preparedStatement.setString(4,subject);
        preparedStatement.setInt(5, number);
        preparedStatement.setInt(6, credits);
        preparedStatement.setString(7, term);
        preparedStatement.setTime(8, time);

        preparedStatement.executeUpdate();
    }
    public static void insertRecordInStudentsToClassesTable(int studentId, int classId, Connection connection) throws SQLException {
        String sqlCommand = "INSERT INTO StudentsToClasses (StudentId, ClassId) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);

        preparedStatement.setInt(1, studentId);
        preparedStatement.setInt(2, classId);

        preparedStatement.executeUpdate();
    }

    public static ResultSet selectRecordsFromDepartmentsTable(Connection connection) 	throws SQLException{
        String sqlCommand = "SELECT * FROM Departments";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFromInstructorsTable(Connection connection) 	throws SQLException{
        String sqlCommand = "SELECT * FROM Instructors";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }


    public static ResultSet selectRecordsFromStudentsTable(Connection connection) 	throws SQLException{
        String sqlCommand = "SELECT * FROM Students";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }


    public static ResultSet selectRecordsFromClassesTable(Connection connection) 	throws SQLException{
        String sqlCommand = "SELECT * FROM Classes";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRecordsFromStudentsToClassesTable(Connection 	connection) throws SQLException{
        String sqlCommand = "SELECT * FROM StudentsToClasses";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

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
    }

}
