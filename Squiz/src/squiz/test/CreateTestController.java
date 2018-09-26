package squiz.test;

import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import squiz.Main;
import squiz.Question;
import squiz.database.SQliteConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateTestController implements Initializable {

    @FXML private JFXListView<FlowPane> questionList;

    @FXML private Button newButton;
    @FXML private Button submitButton;
    @FXML private Button closeButton;

    @FXML private JFXButton accessButton;

    @FXML private ToggleGroup choiceGroup;

    @FXML private Label numberLabel;

    @FXML private JFXTextArea questionArea;

    @FXML private JFXRadioButton smcqButton;
    @FXML private JFXRadioButton tfButton;
    @FXML private JFXRadioButton mcqButton;

    @FXML private JFXRadioButton option1Button;
    @FXML private JFXRadioButton option2Button;
    @FXML private JFXRadioButton option3Button;
    @FXML private JFXRadioButton option4Button;

    @FXML private JFXTextField option1Text;
    @FXML private JFXTextField option2Text;
    @FXML private JFXTextField option3Text;
    @FXML private JFXTextField option4Text;

    ObservableList<Label>listView;

    private int totalQuestion;
    private int currentQuestion;

    public ArrayList<Question>questionArrayList;

    public String[] optionArray;
    public int[] answerArray;

    private JFXRadioButton[] JFXOptionArray = new JFXRadioButton[4];
    private JFXTextField[] JFXTextArray = new JFXTextField[4];

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        JFXOptionArray[0] = option1Button;
        JFXOptionArray[1] = option2Button;
        JFXOptionArray[2] = option3Button;
        JFXOptionArray[3] = option4Button;

        JFXTextArray[0] = option1Text;
        JFXTextArray[1] = option2Text;
        JFXTextArray[2] = option3Text;
        JFXTextArray[3] = option4Text;

        questionArrayList = new ArrayList<>();

        currentQuestion = 1;
        totalQuestion = -1;

        numberLabel.setText(Integer.toString(currentQuestion));

        listView = FXCollections.observableArrayList();

        submitButton.setOnAction(event -> {
            System.out.println("Current Question : " + currentQuestion);
        });

        closeButton.setOnAction(event -> {
            try {
                Parent testView = FXMLLoader.load(getClass().getResource("/squiz/teacherLog.fxml"));
                Scene testScene = new Scene(testView);

                Main.mainStage.setScene(testScene);
                Main.mainStage.show();
            }
            catch(Exception e) {
                System.out.println("text not opening");
            }
        });

        newButton.setOnAction(event -> {
            if(validateData()) {

                int quesType = 0;

                if(smcqButton.isSelected()) {
                    quesType = 1;
                    optionArray = new String[4];

                    for(int i = 0; i < 4; i++) {
                        optionArray[i] = JFXTextArray[i].getText();
                    }

                    smcqButton.setSelected(false);
                }
                else if(tfButton.isSelected()) {
                    quesType = 2;
                    optionArray = new String[2];

                    for(int i = 0; i < 2; i++) {
                        optionArray[i] = JFXTextArray[i].getText();
                    }

                    tfButton.setSelected(false);
                }
                else if(mcqButton.isSelected()) {
                    quesType = 3;
                    optionArray = new String[4];

                    for(int i = 0; i < 4; i++) {
                        optionArray[i] = JFXTextArray[i].getText();
                    }

                    mcqButton.setSelected(false);
                }

                if(currentQuestion == questionArrayList.size()+1) {
                    System.out.println("new question");

                    questionArrayList.add(new Question(questionArea.getText(), quesType, optionArray, answerArray));

                    numberLabel.setText(Integer.toString(currentQuestion));
                    buttonVisibility(false, false, false, false);
                    questionArea.setText("");

                    FlowPane panel = new FlowPane();
                    panel.setAlignment(Pos.CENTER);
                    panel.setMaxWidth(170);

                    Label questionLabel = new Label("Question " + currentQuestion);
                    panel.getChildren().add(questionLabel);

                    questionList.getItems().add(panel);

                    currentQuestion = questionArrayList.size()+1;
                }
                else {
                    System.out.println("update question");

                    questionArrayList.get(currentQuestion-1).constructQuestion(questionArea.getText(), quesType, optionArray, answerArray);

                    currentQuestion = questionArrayList.size()+1;
                }

                buttonVisibility(false, false, false, false);

                numberLabel.setText(Integer.toString(currentQuestion));

            }
            else {
                System.out.println("please CHECK your data");
            }
        });

        accessButton.setOnAction(event -> {

            try {
                int index = questionList.getSelectionModel().getSelectedIndex();
                System.out.println(index);

                if(index != -1) {
                    buttonVisibility(false, false, false, false);

                    Question quesTemp = questionArrayList.get(index);

                    currentQuestion = index+1;
                    numberLabel.setText(Integer.toString(currentQuestion));

                    questionArea.setText(quesTemp.getQuestion());

                    for(int i = 0; i < 2; i++) {
                        JFXTextArray[i].setText(quesTemp.getOptionContent(i));
                        JFXTextArray[i].setVisible(true);
                        JFXOptionArray[i].setVisible(true);
                    }

                    if(quesTemp.getQuesType() == 1 || quesTemp.getQuesType() == 3) {
                        for(int i = 2; i < 4; i++) {
                            JFXTextArray[i].setText(quesTemp.getOptionContent(i));
                            JFXTextArray[i].setVisible(true);
                            JFXOptionArray[i].setVisible(true);
                        }
                    }

                    for(int i : quesTemp.getAnswers()) {
                        JFXOptionArray[i-1].setSelected(true);
                    }

                    switch (quesTemp.getQuesType()) {
                        case 1: smcqButton.setSelected(true);
                            break;
                        case 2: tfButton.setSelected(true);
                            break;
                        case 3: mcqButton.setSelected(true);
                    }
                }
            }
            catch(Exception e) {}
        });

        smcqButton.setOnAction(this::radioSelected);
        mcqButton.setOnAction(this::radioSelected);
        tfButton.setOnAction(this::radioSelected);

        buttonVisibility(false, false, false, false);

    }

    private boolean validateData()
    {
        int selectedOptions;
        if(questionArea.getText().length() > 0) {

            selectedOptions = 0;
            for(int i = 0; i < 4; i++) {
                if(JFXOptionArray[i].isSelected()) {
                    selectedOptions++;
                }
            }

            if(selectedOptions == 0) {
                System.out.println(0);
                return false;
            }

            answerArray = new int[selectedOptions];

            int k = 0;
            for(int i = 0; i < 4; i++) {
                if(JFXOptionArray[i].isSelected()) {
                    answerArray[k++] = i+1;
                }
            }

            if(smcqButton.isSelected()) {
                for(int i = 0; i < 4; i++) {
                    if(JFXTextArray[i].getText().length() == 0) {
                        return false;
                    }
                }
                return (selectedOptions == 1);
            }
            else if(tfButton.isSelected()) {
                for(int i = 0; i < 2; i++) {
                    if(JFXTextArray[i].getText().length() == 0) {
                        return false;
                    }
                }
                return (selectedOptions == 1);
            }
            else if(mcqButton.isSelected()) {
                for(int i = 0; i < 4; i++) {
                    if(JFXTextArray[i].getText().length() == 0) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void resetChoiceButton()
    {
        for(int i = 0; i < 4; i++) {
            JFXOptionArray[i].setSelected(false);
        }
    }

    private void buttonVisibility(boolean option1, boolean option2, boolean option3, boolean option4)
    {
        boolean[] visibleArray = {option1, option2, option3, option4};

        for(int i = 0; i < 4; i++) {
            JFXOptionArray[i].setVisible(visibleArray[i]);
            JFXTextArray[i].setVisible(visibleArray[i]);
            JFXTextArray[i].setText("");
        }

        resetChoiceButton();
    }

    private void radioSelected(ActionEvent event) {

        if(event.getSource() == smcqButton) {
            buttonVisibility(true, true, true, true);
        }
        else if(event.getSource() == mcqButton) {
            buttonVisibility(true, true, true, true);
        }
        else if(event.getSource() == tfButton) {
            buttonVisibility(true, true, false, false);
        }
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }
}
