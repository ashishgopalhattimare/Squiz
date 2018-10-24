package squiz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static Stage mainStage;

    private double xOffset, yOffset;

    @Override
    public void start(Stage primaryStage) {

        mainStage = primaryStage;
        try {

            xOffset = 0;
            yOffset = 0;

            Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.initStyle(StageStyle.UNDECORATED);

            root.setOnMousePressed(event12 -> {
                xOffset = event12.getSceneX();
                yOffset = event12.getSceneY();
            });

            root.setOnMouseDragged(event1 -> {
                Main.mainStage.setX(event1.getScreenX() - xOffset);
                Main.mainStage.setY(event1.getScreenY() - yOffset);
            });

            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.show();
        }
        catch(Exception e) {
            System.out.println("Start error");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
