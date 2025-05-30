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
        try (Connection connection = TournamentData.createDatabase("LogicCollege")) {
           //connection.createStatement().execute("PRAGMA foreign_keys = ON");

            // Drop tables in proper order
            TournamentData.dropTable("StudentsToClasses",connection);
            TournamentData.dropTable("Students",connection);
            TournamentData.dropTable("Classes",connection);
            TournamentData.dropTable("Instructors",connection);
            TournamentData.dropTable("Departments",connection);

            // Create tables
            TournamentData.createDepartmentsTable(connection);
            TournamentData.createInstructorsTable(connection);
            TournamentData.createStudentsTable(connection);
            TournamentData.createClassesTable(connection);
            TournamentData.createStudentsToClassesTable(connection);

            // Insert data
            insertDepartments(connection);
            insertInstructor(connection);
            insertStudents(connection);
            insertClasses(connection);
            insertStudentsToClasses(connection);

            System.out.print("Enter S to search for a record, Q to quit: ");
            Scanner keyboard = new Scanner(System.in);
            String response = keyboard.nextLine();

            while(!response.equals("Q"))
            {
                if (response.equals("S"))
                {
                    System.out.print("Enter 1-Department, 2-Instructor, 3-Student, 4-Classes, 5-Student Classes: ");
                    String table = keyboard.nextLine();
                    if(table.equals("1")){
                        System.out.print("Department ID: ");
                        String ID = keyboard.nextLine();
                        ResultSet set = TournamentData.selectRecordsFromDepartmentsTable(connection);
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
                    }
                    if(table.equals("2")){
                        System.out.print("Instructor ID: ");
                        String ID = keyboard.nextLine();
                        ResultSet set = TournamentData.selectRecordsFromInstructorsTable(connection);
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
                    }
                    if(table.equals("3")){
                        System.out.print("Student ID: ");
                        String ID = keyboard.nextLine();
                        ResultSet set = TournamentData.selectRecordsFromStudentsTable(connection);
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
                    }
                    if(table.equals("4")){
                        System.out.print("Classes ID: ");
                        String ID = keyboard.nextLine();
                        ResultSet set = TournamentData.selectRecordsFromClassesTable(connection);
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
                    }
                    if(table.equals("5")){
                        System.out.print("Students ID: ");
                        String ID = keyboard.nextLine();
                        ResultSet set = TournamentData.selectRecordsFrom_Students_StudentsToClasses_Classes_Table_Limited(Integer.parseInt(ID), connection);
                        outputResultSet(set);
                        /*while(set.next()){
                            if(set.getInt(1) == Integer.parseInt(ID)){
                                ResultSetMetaData resultSetMetaData = set.getMetaData();
                                int columnCount = resultSetMetaData.getColumnCount();
                                for (int index = 1; index <= columnCount; index++)
                                {
                                    String columnValue = set.getString(index);
                                    System.out.print(columnValue +  " ");
                                }
                            }*/
                        }
                    }
                else System.out.println("Input error...");
                System.out.println();
                System.out.print("\nEnter S to search for a record, Q to quit: ");
                response = keyboard.nextLine();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public static void insertInstructor(Connection connection){
        String filePath = "InstructorRecords.txt"; // Path to your CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                TournamentData.insertRecordInInstructorsTable(Integer.parseInt(values[0]), Integer.parseInt(values[1]),values[2], values[3], connection);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertStudents(Connection connection) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String filePath = "StudentRecords.txt"; // Path to your CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                java.sql.Date sqlDate = new java.sql.Date(df.parse(values[3]).getTime());
                TournamentData.insertRecordInStudentsTable(Integer.parseInt(values[0]), values[1], values[2], sqlDate, connection);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertClasses(Connection connection) throws ParseException {
        DateFormat df = new SimpleDateFormat("hh:mm");
        String filePath = "ClassesRecords.txt"; // Path to your CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                java.sql.Time sqlDate = new java.sql.Time(df.parse(values[7]).getTime());
                TournamentData.insertRecordInClassesTable(Integer.parseInt(values[0]), Integer.parseInt(values[1]),
                        values[2], values[3], Integer.parseInt(values[4]), Integer.parseInt(values[5]), values[6], sqlDate, connection);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertStudentsToClasses(Connection connection){
        String filePath = "StudentsToClassesRecords.txt"; // Path to your CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                TournamentData.insertRecordInStudentsToClassesTable(Integer.parseInt(values[0]), Integer.parseInt(values[1]), connection);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertDepartments(Connection connection){
        String filePath = "DepartmentRecords.txt"; // Path to your CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                TournamentData.insertRecordInDepartmentsTable(Integer.parseInt(values[0]), values[1], connection);
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


}

