package squiz;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import squiz.database.SQliteConnection;
import squiz.test.CreateTestController;

public class LoginUser {

    public static String firstname, lastname, username;
    public static final String prefix = "ID_";

    public static boolean accepted;
    public static boolean studentLogin;

    private double xOffset, yOffset;

    public static Stage s_window;

    public void validUser(boolean studentAccess)
    {
        username = ""; firstname = ""; lastname = "";
        studentLogin = studentAccess;

//        System.out.println(studentLogin);
        accepted = false;

        xOffset = 0;
        yOffset = 0;

        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage primaryStage = new Stage();
            s_window = primaryStage;

            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            });

            primaryStage.setScene(new Scene(root));

            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setResizable(false);
            primaryStage.showAndWait();

            if(accepted) {
                if(!studentAccess) {

                    xOffset = 0;
                    yOffset = 0;

                    try {
                        Parent teacherView = FXMLLoader.load(getClass().getResource("teacherLog.fxml"));
                        Main.mainStage.setScene(new Scene(teacherView));

                        teacherView.setOnMousePressed(event -> {
                            xOffset = event.getSceneX();
                            yOffset = event.getSceneY();
                        });

                        teacherView.setOnMouseDragged(event -> {
                            Main.mainStage.setX(event.getScreenX() - xOffset);
                            Main.mainStage.setY(event.getScreenY() - yOffset);
                        });

                        Main.mainStage.centerOnScreen();
                        Main.mainStage.showAndWait();
                    }
                    catch(Exception e) {}
                }
                else {

                    System.out.println("STUDENT PAGE");
                }
            }
        }
        catch (Exception e) {
            System.out.println("fail login");
        }
    }
}
