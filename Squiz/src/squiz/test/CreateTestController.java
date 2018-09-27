package squiz.test;

import com.jfoenix.controls.*;
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
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import squiz.Main;
import squiz.Question;
import squiz.database.SQliteConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateTestController implements Initializable {

    @FXML private JFXListView<FlowPane> questionList;

    @FXML private Button newButton;
    @FXML private Button submitButton;
    @FXML private Button closeButton;
    @FXML private Button deleteButton;

    @FXML private JFXButton accessButton;

    @FXML private Label numberLabel;

    @FXML private JFXTextArea questionArea;
    @FXML private TextField subjectField;

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

    private int currentQuestion;
    private int preType,curType;

    public ArrayList<Question>questionArrayList;
    public ArrayList<Label>questionLabelList;

    public String[] optionArray;
    public int[] answerArray;

    private JFXRadioButton[] JFXOptionArray = new JFXRadioButton[4];
    private JFXTextField[] JFXTextArray = new JFXTextField[4];

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        JFXOptionArray[0] = option1Button;
        JFXOptionArray[1] = option2Button;
        JFXOptionArray[2] = option3Button;
        JFXOptionArray[3] = option4Button;

        JFXTextArray[0] = option1Text;
        JFXTextArray[1] = option2Text;
        JFXTextArray[2] = option3Text;
        JFXTextArray[3] = option4Text;

        questionArrayList = new ArrayList<>();
        questionLabelList = new ArrayList<>();

        currentQuestion = 1;
        preType = -1;
        curType = -1;

        numberLabel.setText(Integer.toString(currentQuestion));

        submitButton.setOnAction(this::submitEventHandler);

        closeButton.setOnAction(event -> {
            teacherLog();
        });

        newButton.setOnAction(this::newModifyQuestion);

        accessButton.setOnAction(this::getQuestionAccess);

        deleteButton.setOnAction(this::deleteQuestion);

        smcqButton.setOnAction(this::radioSelected);
        mcqButton.setOnAction(this::radioSelected);
        tfButton.setOnAction(this::radioSelected);

        buttonVisibility(false, false, false, false);
        resetChoiceButton();
        resetTextField();

    }

    private void teacherLog()
    {
        try {
            Parent testView = FXMLLoader.load(getClass().getResource("/squiz/teacherLog.fxml"));
            Scene testScene = new Scene(testView);

            Main.mainStage.setScene(testScene);
            Main.mainStage.show();
        }
        catch(Exception e) {
            System.out.println("text not opening");
        }
    }

    private void submitEventHandler(ActionEvent event)
    {
        if(questionArrayList.size() > 0) {
            SQliteConnection.submitTest(questionArrayList);

            if(SQliteConnection.querySuccessful) {
                teacherLog();
            }
        }
    }

    private void deleteQuestion(ActionEvent actionEvent)
    {
        int index = questionList.getSelectionModel().getSelectedIndex();

        if(index != -1) {

            questionList.getItems().remove(index);
            questionLabelList.remove(index);
            questionArrayList.remove(index);

            currentQuestion--;

            for (int i = 0; i < questionLabelList.size(); i++) {
                questionLabelList.get(i).setText("Question " + (i+1));
            }
        }
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

    private void resetTextField()
    {
        for(int i = 0; i < 4; i++) {
            JFXTextArray[i].setText("");
        }
    }

    private void buttonVisibility(boolean option1, boolean option2, boolean option3, boolean option4)
    {
        boolean[] visibleArray = {option1, option2, option3, option4};

        for(int i = 0; i < 4; i++) {
            JFXOptionArray[i].setVisible(visibleArray[i]);
            JFXTextArray[i].setVisible(visibleArray[i]);
        }
    }

    private void radioSelected(ActionEvent event)
    {
        preType = curType;
        if(event.getSource() == smcqButton) {

            curType = 1;
            buttonVisibility(true, true, true, true);

            if(preType != 3) resetTextField();
            resetChoiceButton();
        }
        else if(event.getSource() == tfButton) {

            curType = 2;
            buttonVisibility(true, true, false, false);
            resetTextField();
            resetChoiceButton();
        }
        else if(event.getSource() == mcqButton) {

            curType = 3;
            buttonVisibility(true, true, true, true);

            if(preType != 1) resetTextField();
            resetChoiceButton();
        }

    }

    private void getQuestionAccess(ActionEvent event)
    {
        try {
            int index = questionList.getSelectionModel().getSelectedIndex();

            if (index != -1) {
                buttonVisibility(false, false, false, false);
                resetChoiceButton();
                resetTextField();

                Question quesTemp = questionArrayList.get(index);

                currentQuestion = index + 1;
                numberLabel.setText(Integer.toString(currentQuestion));

                questionArea.setText(quesTemp.getQuestion());

                for (int i = 0; i < 2; i++) {
                    JFXTextArray[i].setText(quesTemp.getOptionContent(i));
                    JFXTextArray[i].setVisible(true);
                    JFXOptionArray[i].setVisible(true);
                }

                if (quesTemp.getQuesType() == 1 || quesTemp.getQuesType() == 3) {
                    for (int i = 2; i < 4; i++) {
                        JFXTextArray[i].setText(quesTemp.getOptionContent(i));
                        JFXTextArray[i].setVisible(true);
                        JFXOptionArray[i].setVisible(true);
                    }
                }

                for (int i : quesTemp.getAnswers()) {
                    JFXOptionArray[i - 1].setSelected(true);
                }

                switch (quesTemp.getQuesType()) {
                    case 1:
                        smcqButton.setSelected(true);
                        break;
                    case 2:
                        tfButton.setSelected(true);
                        break;
                    case 3:
                        mcqButton.setSelected(true);
                }

                questionList.getSelectionModel().clearSelection();
            }
        }
        catch (Exception e) {
            System.out.println("Question Access Error");
        }
    }

    private void newModifyQuestion(ActionEvent event)
    {
        if (validateData()) {

            if (smcqButton.isSelected()) {
                curType = 1;
                optionArray = new String[4];

                for (int i = 0; i < 4; i++) {
                    optionArray[i] = JFXTextArray[i].getText();
                }

                smcqButton.setSelected(false);
            }
            else if (tfButton.isSelected()) {
                curType = 2;
                optionArray = new String[2];

                for (int i = 0; i < 2; i++) {
                    optionArray[i] = JFXTextArray[i].getText();
                }

                tfButton.setSelected(false);
            }
            else if (mcqButton.isSelected()) {
                curType = 3;
                optionArray = new String[4];

                for (int i = 0; i < 4; i++) {
                    optionArray[i] = JFXTextArray[i].getText();
                }

                mcqButton.setSelected(false);
            }

            if (currentQuestion == questionArrayList.size() + 1) {

                questionArrayList.add(new Question(questionArea.getText(), curType, optionArray, answerArray));

                numberLabel.setText(Integer.toString(currentQuestion));

                FlowPane panel = new FlowPane();
                panel.setAlignment(Pos.CENTER);
                panel.setMaxWidth(170);

                Label questionLabel = new Label("Question " + currentQuestion);
                panel.getChildren().add(questionLabel);

                questionLabelList.add(questionLabel);
                questionList.getItems().add(panel);

                currentQuestion = questionArrayList.size() + 1;
            }
            else {
                questionArrayList.get(currentQuestion - 1).constructQuestion(questionArea.getText(), curType, optionArray, answerArray);
                currentQuestion = questionArrayList.size() + 1;
            }

            questionArea.setText("");
            questionList.getSelectionModel().clearSelection();

            buttonVisibility(false, false, false, false);
            resetChoiceButton();
            resetTextField();

            numberLabel.setText(Integer.toString(currentQuestion));

        } else {
            System.out.println("please CHECK your data");
        }
    }
}
