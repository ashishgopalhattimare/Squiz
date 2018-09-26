package squiz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static Stage mainStage;

    @Override
    public void start(Stage primaryStage) {

        mainStage = primaryStage;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
            primaryStage.setScene(new Scene(root,600,400));
            primaryStage.setResizable(false);
            primaryStage.show();
        }
        catch(Exception e) {
            System.out.println("Start error");
        }
    }


    public static void main(String[] args) {

        new LoginUser();

        launch(args);
    }
}
