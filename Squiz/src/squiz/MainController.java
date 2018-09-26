package squiz;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private Button studentButton;
    @FXML private Button teacherButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        studentButton.setStyle("-fx-background-color: #7aa8f4;");
        teacherButton.setStyle("-fx-background-color: #7aa8f4;");

        LoginUser loginUser = new LoginUser();

        studentButton.setOnAction(event -> {
            loginUser.validUser(true);
        });

        teacherButton.setOnAction(event -> {
            loginUser.validUser(false);
        });
    }

    public void studentMouseEnter(MouseEvent e) {
        studentButton.setStyle("-fx-background-color: #5e7eb2;");
    }

    public void studentMouseExit(MouseEvent e) {
        studentButton.setStyle("-fx-background-color: #7aa8f4;");
    }

    public void teacherMouseEnter(MouseEvent e) {
        teacherButton.setStyle("-fx-background-color: #5e7eb2;");
    }

    public void teacherMouseExit(MouseEvent e) {
        teacherButton.setStyle("-fx-background-color: #7aa8f4;");
    }
}