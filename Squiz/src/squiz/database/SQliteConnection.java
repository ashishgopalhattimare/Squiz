package squiz.database;

import squiz.LoginUser;
import squiz.Question;
import squiz.TestBuilder;

import java.sql.*;
import java.util.ArrayList;

public class SQliteConnection {

    public static String firstname, lastname;
    public static String username;
    public static Boolean studentLogin;

    public static boolean querySuccessful;

    public static Connection connectionCheck() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:TEACHER_DATABASE.db");
            return connection;
        }
        catch (Exception e) {
            System.out.println("TEACHER_DATABASE ERROR");
            return null;
        }
    }

    public static boolean userExistSignUp(String TABLE, String USERNAME)
    {
        Connection connection = connectionCheck();

        String query = "SELECT * FROM "+ TABLE +" WHERE USERNAME = '"+USERNAME+"'";

        if (connection != null) {
            try {
                Statement st = connection.createStatement();
                ResultSet set = st.executeQuery(query);

                // atleast one query has come from the database
                if(set.next()) {
                    connection.close();
                    return false;
                }

                // username has no such entry in the database - create new entry with this username
                else {
                    connection.close();
                    return true;
                }
            }
            catch (Exception e) {
                System.out.println("exist error");
                return false;
            }
        }

        return false;
    }

    public static boolean userExistLogin(String TABLE, String USERNAME, String PASSWORD)
    {
        Connection connection = connectionCheck();

        String query = "SELECT * FROM "+ TABLE +" WHERE USERNAME = '"+USERNAME+"' AND PASWORD = '"+PASSWORD+"'";

        if (connection != null) {
            try {
                Statement st = connection.createStatement();
                ResultSet set = st.executeQuery(query);

                // atleast one query has come from the database
                if(set.next()) {
                    firstname = set.getString("FIRSTNAME");
                    lastname = set.getString("LASTNAME");
                    username = set.getString("USERNAME");

                    connection.close();
                    return true;
                }
                // username has no such entry in the database
                else {
                    System.out.println("no data found");

                    connection.close();
                    return false;
                }
            }
            catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    public static void insertQuery(String query)
    {
        Connection connection = connectionCheck();

        querySuccessful = false;

        if (connection != null) {
            try {
                Statement st = connection.createStatement();
                int result = st.executeUpdate(query);

                querySuccessful = true;
            }
            catch (Exception e) {
                System.out.println("insert error");
            }
            finally {
                try {
                    connection.close();
                } catch(SQLException ex) {
                    System.out.println("Connection Close Error");
                }
            }
        }
    }

    public static void updateQuery(int SIGNAL)
    {
        String TABLE = studentLogin ? "STUDENT" : "TEACHER";

        String query = "UPDATE "+TABLE+" SET SIGNEDIN = "+SIGNAL+" WHERE USERNAME = '"+username+"'";

//        System.out.println(query);
        Connection connection = connectionCheck();

        querySuccessful = false;

        if (connection != null) {
            try {
                Statement st = connection.createStatement();
                int result = st.executeUpdate(query);

                querySuccessful = true;
            }
            catch (Exception e) {
                System.out.println("update error");
            }
            finally {
                try {
                    connection.close();
                } catch(SQLException ex) {
                    System.out.println("Connection Close Error");
                }
            }
        }
    }

    public static void submitTest(ArrayList<Question>testArray)
    {
        querySuccessful = false;

        TestBuilder test = new TestBuilder();
        int totalQuestions = test.setTestTable(testArray);

        String query = "INSERT INTO TEST (COORDINATOR,QUIZPAPER,QUESTIONS,SUBJECT,OPEN) VALUES ('"+LoginUser.username+"','"+test.getTestTable()+"',"+totalQuestions+",'"+"General Quiz"+"',"+1+")";
        insertQuery(query);

        querySuccessful = true;
    }
}
