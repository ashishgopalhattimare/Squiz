package squiz;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private Button studentButton;
    @FXML private Button teacherButton;
    @FXML private Button closeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        studentButton.setStyle("-fx-background-color:#7aa8f4; -fx-background-radius:45");
        teacherButton.setStyle("-fx-background-color:#7aa8f4; -fx-background-radius:45");
        closeButton.setStyle("-fx-background-radius:45; -fx-background-color:#ffffff; -fx-border-color:#000000; -fx-border-radius:45");

        LoginUser loginUser = new LoginUser();

        studentButton.setOnAction(event -> {
            loginUser.validUser(true);
        });

        teacherButton.setOnAction(event -> {
            loginUser.validUser(false);
        });

        closeButton.setOnAction(event -> {
            System.out.println("Close the Application");
            Platform.exit();
        });
    }

    public void closeMouseEnter(MouseEvent e) {
        closeButton.setStyle("-fx-background-radius:45; -fx-background-color:#000000; -fx-border-color:#000000; -fx-border-radius:45");
    }

    public void closeMouseExit(MouseEvent e) {
        closeButton.setStyle("-fx-background-radius:45; -fx-background-color:#ffffff; -fx-border-color:#000000; -fx-border-radius:45");
    }

    public void studentMouseEnter(MouseEvent e) {
        studentButton.setStyle("-fx-background-color:#5e7eb2; -fx-background-radius:45");
    }

    public void studentMouseExit(MouseEvent e) {
        studentButton.setStyle("-fx-background-color:#7aa8f4; -fx-background-radius:45");
    }

    public void teacherMouseEnter(MouseEvent e) {
        teacherButton.setStyle("-fx-background-color:#5e7eb2; -fx-background-radius:45");
    }

    public void teacherMouseExit(MouseEvent e) {
        teacherButton.setStyle("-fx-background-color:#7aa8f4; -fx-background-radius:45");
    }
}