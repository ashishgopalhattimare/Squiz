package squiz;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherLogController implements Initializable {

    @FXML private Button addTestButton;
    @FXML private Button openTestButton;

    @FXML public JFXListView<Label> listView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println("Initialize");
        openTestButton.setOnAction(event -> {
            System.out.println("open this test");
        });

        addTestButton.setOnAction(event -> {
            try {
                Parent testView = FXMLLoader.load(getClass().getResource("testLog.fxml"));
                Main.mainStage.setScene(new Scene(testView));
                Main.mainStage.show();
            }
            catch(Exception e) {
                System.out.println("ADD NEW TEST TO TEACHER TABLE");
            }
        });
    }
}
