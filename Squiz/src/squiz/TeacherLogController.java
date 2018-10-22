package squiz;

import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import squiz.database.SQliteConnection;
import sun.rmi.runtime.Log;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class TeacherLogController implements Initializable {

    @FXML private Button editTestButton;
    @FXML private Button addTestButton;
    @FXML private Button refreshButton;
    @FXML private Button deleteButton;
    @FXML private Button closeButton;
    @FXML private Button studentButton;

    @FXML private ImageView refreshImage;

    @FXML private TextField questionText;
    @FXML private TextField dateText;
    @FXML private TextField openText;

    @FXML private Label nameLabel;

    private double xOffset, yOffset;

    @FXML private JFXListView<Label> listView;

    private int totalQuestions, listLength;
    public static int [] testIDArray = new int[1000];

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        editTestButton.setOnAction(event -> {
            System.out.println("open this test");
        });

        addTestButton.setOnAction(event -> {
            try {
                Parent testView = FXMLLoader.load(getClass().getResource("testLog.fxml"));
                Main.mainStage.setScene(new Scene(testView));

                testView.setOnMousePressed(event12 -> {
                    xOffset = event12.getSceneX();
                    yOffset = event12.getSceneY();
                });

                testView.setOnMouseDragged(event1 -> {
                    Main.mainStage.setX(event1.getScreenX() - xOffset);
                    Main.mainStage.setY(event1.getScreenY() - yOffset);
                });

                Main.mainStage.show();
            }
            catch(Exception e) {
                System.out.println("FAILED 2 ADD NEW TEST 2 TEACHER TABLE");
            }
        });

        closeButton.setOnAction(event -> {
            try {
                Parent testView = FXMLLoader.load(getClass().getResource("main.fxml"));
                Scene testScene = new Scene(testView);

                SQliteConnection.updateStatusQuery(0);

                Main.mainStage.setScene(testScene);
                Main.mainStage.show();
            }
            catch(Exception e) {
                System.out.println("main Frame not opening");
            }
        });

        refreshButton.setOnAction(event -> {
            updateTeacherTable();
        });

        deleteButton.setOnAction(event -> {
            int index = listView.getSelectionModel().getSelectedIndex();

            if(index != -1) {
                int id = testIDArray[index];

                String query = "DELETE FROM "+ LoginUser.username +" WHERE ID = " + id;
                SQliteConnection.deleteQuery(query, "TEACHER_TESTS");

                if(SQliteConnection.querySuccessful) {
                    listView.getItems().remove(index);
                }

                listView.getSelectionModel().clearSelection();
            }
        });

        listView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number testIndex) {

                if((int)testIndex != -1) {
                    getTestContent(testIDArray[(int)testIndex]);
                }
                else {
                    questionText.setText("");
                    dateText.setText("");
                    openText.setText("");
                }
            }
        });

        System.out.printf("TeacherLogController : %s %s\n", LoginUser.firstname, LoginUser.lastname);
        nameLabel.setText(LoginUser.firstname + " " + LoginUser.lastname);

        updateTeacherTable();
    }

    private void getTestContent(int testid)
    {
        Connection connection = SQliteConnection.connectionDatabase("TEACHER_TESTS");
        String query = "SELECT * FROM "+ LoginUser.username +" WHERE ID = '"+testid+"'";

        System.out.println("getTestContent : " + query);

        if (connection != null) {
            try {
                ResultSet set = connection.createStatement().executeQuery(query);

                if(set.next()) {
                    int questions = set.getInt("QUESTIONS");
                    int open = set.getInt("OPEN");

                    questionText.setText(Integer.toString(questions));
                    dateText.setText("XX-XX-XXXX");

                    if(open == 1) {
                        openText.setText("OPEN");
                    }
                    else {
                        openText.setText("CLOSED");
                    }
                }
            }
            catch (Exception e) {
                System.out.println("getTestContent error");
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

    public void updateTeacherTable()
    {
        Connection connection = SQliteConnection.connectionDatabase("TEACHER_TESTS");
        String query = "SELECT * FROM "+ LoginUser.username;

        System.out.println("updateTeacherTable : " + query);

        if (connection != null) {
            try {
                Statement st = connection.createStatement();
                ResultSet set = st.executeQuery(query);

                listLength = listView.getItems().size();
                for(int i = 0; i < listLength; i++) {
                    listView.getItems().remove(0);
                }

                totalQuestions = 0;
                while(set.next()) {
                    int id = set.getInt("ID");
                    String coordinator = set.getString("COORDINATOR");
                    String quizpaper = set.getString("QUIZPAPER");
                    int questions = set.getInt("QUESTIONS");
                    String subject = set.getString("SUBJECT");
                    int open = set.getInt("OPEN");

                    Label label = new Label(subject);
                    listView.getItems().add(label);

                    testIDArray[totalQuestions++] = id;
                }
            }
            catch (Exception e) {
                System.out.println("updateTeacherTable error");
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

    public void refreshMouseEnter(MouseEvent e)
    { }

    public void refreshMouseExit(MouseEvent e)
    { }
}
