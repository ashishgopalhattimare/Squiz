package squiz;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginUser {

    public static String username, firstname, lastname;
    public static boolean accepted;
    public static boolean studentLogin;

    public static Stage s_window;

    public void validUser(boolean studentAccess)
    {
        username = ""; firstname = ""; lastname = "";
        studentLogin = studentAccess;

        System.out.println(studentLogin);
        accepted = false;

        try {
            Parent view = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage window = new Stage();
            s_window = window;

            window.setScene(new Scene(view));

            window.initModality(Modality.APPLICATION_MODAL);
            window.initStyle(StageStyle.UNDECORATED);
            window.setResizable(false);
            window.showAndWait();

            if(accepted) {
                if(!studentAccess) {
                    try {
                        Parent teacherView = FXMLLoader.load(getClass().getResource("teacherLog.fxml"));
                        Scene teacherScene = new Scene(teacherView);

                        Main.mainStage.setScene(teacherScene);

                        Main.mainStage.show();
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
