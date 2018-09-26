package squiz.database;

import squiz.Question;
import squiz.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
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

        System.out.println(query);
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
        try {
            System.out.println(-2);
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            System.out.println(-1);
            Connection connection = DriverManager.getConnection("jdbc:odbc:TEACHER_DATABASE.db");

            System.out.println(0);

            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);

                System.out.println(1);
                Test test = new Test(testArray);

                System.out.println(2);
                oos.writeObject(test);

                System.out.println(3);

                oos.flush(); oos.close(); bos.close();

                System.out.println(4);

                byte[] databytes = bos.toByteArray();

                String query = "INSERT INTO TEST (QUIZPAPER) VALUES (?)";
                PreparedStatement ps = connection.prepareStatement(query);

                ByteArrayInputStream bais = new ByteArrayInputStream(databytes);
                ps.setBinaryStream(1, bais, databytes.length);
                ps.executeUpdate();
                ps.close();
            }
            catch(Exception e) {
                System.out.println("Submit Test Error");
            }
            finally {
                try {
                    connection.close();
                } catch(SQLException ex) {
                    System.out.println("Connection Close Error");
                    ex.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            System.out.println("connection error submit");
            e.printStackTrace();
        }
    }
}
