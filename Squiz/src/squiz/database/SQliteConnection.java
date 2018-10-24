package squiz.database;

import squiz.LoginUser;
import squiz.Question;
import squiz.TestBuilder;
import squiz.test.CreateTestController;

import java.sql.*;
import java.util.ArrayList;

public class SQliteConnection {

    public static String DATABASE_PEOPLE = "#";
    public static String DATABASE_FOLLOW = "#";

    public static Boolean studentLogin;

    public static boolean querySuccessful;

    public static Connection connectionDatabase(String database) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:"+ database +".db");
            return connection;
        }
        catch (Exception e) {
            System.out.println(database +" ERROR");
            return null;
        }
    }

    public static boolean userAlreadyExists(String TABLE, String USERNAME)
    {
        Connection connection = connectionDatabase("REGISTRATION");

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

    public static boolean loginUser(String TABLE, String USERNAME, String PASSWORD)
    {
        Connection connection = connectionDatabase("REGISTRATION");

        String query = "SELECT * FROM "+ TABLE +" WHERE USERNAME = '"+USERNAME+"' AND PASWORD = '"+PASSWORD+"'";

        if (connection != null) {
            try {
                Statement st = connection.createStatement();
                ResultSet resultset = st.executeQuery(query);

                // atleast one query has come from the database
                if(resultset.next()) {
                    LoginUser.firstname = resultset.getString("FIRSTNAME");
                    LoginUser.lastname = resultset.getString("LASTNAME");
                    LoginUser.username = resultset.getString("USERNAME");

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

    public static void createTable(String TABLE, String database)
    {
        Connection connection = connectionDatabase(database);

        String query = "CREATE TABLE IF NOT EXISTS " + TABLE + " (ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, COORDINATOR TEXT NOT NULL, QUIZPAPER TEXT NOT NULL, QUESTIONS INTEGER, SUBJECT TEXT NOT NULL, OPEN INTEGER NOT NULL);";

        if(connection != null) {
            try {
                Statement st = connection.createStatement();
                st.execute(query);
            }
            catch(Exception e) {
                System.out.println("create new table error");
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

    public static void insertQuery(String query, String database)
    {
        Connection connection = connectionDatabase(database);

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

    /**
     * Method Name : updateStatusQuery
     * Purpose : Update the status online of the current teacher or student
     * @param SIGNAL
     */
    public static void updateStatusQuery(int SIGNAL)
    {
        String TABLE = studentLogin ? "STUDENT" : "TEACHER";

        String query = "UPDATE "+TABLE+" SET SIGNEDIN = "+SIGNAL+" WHERE USERNAME = '"+LoginUser.username+"'";
        Connection connection = connectionDatabase("REGISTRATION");

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

    public static void updateQuery(String query, String database)
    {
        Connection connection = connectionDatabase(database);

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

    public static void deleteQuery(String query, String database)
    {
        Connection connection = connectionDatabase(database);

        querySuccessful = false;

        if (connection != null) {
            try {
                Statement st = connection.createStatement();
                int result = st.executeUpdate(query);
                querySuccessful = true;
            }
            catch (Exception e) {
                System.out.println("delete error");
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

    public static String getQuery(String query, String database)
    {
        Connection connection = connectionDatabase(database);

        if(connection != null) {
            try {
                Statement st = connection.createStatement();
                ResultSet resultset = st.executeQuery(query);

                if(resultset.next()) {
                    return resultset.getString("QUIZPAPER");
                }
            }
            catch(Exception e) {
                System.out.println("getQuery error");
                return null;
            }
            finally {
                try {
                    connection.close();
                } catch(SQLException ex) {
                    System.out.println("Connection Close Error");
                }
            }
        }
        return null;
    }

    public static void submitTest(ArrayList<Question>testArray, String subject)
    {
        querySuccessful = false;

        TestBuilder test = new TestBuilder();
        int totalQuestions = test.setTestTable(testArray);

        // Test update query
        if(CreateTestController.updateTest) {
            String query = "UPDATE "+LoginUser.username+" SET COORDINATOR='"+LoginUser.username+"', QUIZPAPER='"+test.getTestTable()+"', QUESTIONS='"+totalQuestions+"', SUBJECT='"+subject+"', OPEN='"+1+"' WHERE ID='"+CreateTestController.updateTestID+"'";
//            System.out.println("submitTest : " + query);
            updateQuery(query, "TEACHER_TESTS");
        }
        else {
            String query = "INSERT INTO " + LoginUser.username +" (COORDINATOR,QUIZPAPER,QUESTIONS,SUBJECT,OPEN) VALUES ('"+LoginUser.username+"','"+test.getTestTable()+"',"+totalQuestions+",'"+subject+"',"+1+")";

//            System.out.println("submitTest : " + query);
            insertQuery(query, "TEACHER_TESTS");
        }
        querySuccessful = true;
    }
}
