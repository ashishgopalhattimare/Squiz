package squiz;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import squiz.database.SQliteConnection;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private Label portalLabel;

    @FXML private ToggleButton login_button;

    @FXML private Button backButton;

    @FXML private ToggleButton signup_botton;

    @FXML private AnchorPane loginPage;

    @FXML private TextField login_username;

    @FXML private PasswordField login_password;

    @FXML private Button login_submit;

    @FXML private AnchorPane signupPage;

    @FXML private TextField signup_firstname;

    @FXML private TextField signup_lastname;

    @FXML private TextField signup_email;

    @FXML private TextField signup_username;

    @FXML private PasswordField signup_password;

    @FXML private Button signup_submit;

    private String password, username, firstname, lastname, email;
    private boolean dataValidation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(LoginUser.studentLogin) {
            portalLabel.setText("STUDENT PORTAL");
        }
        else {
            portalLabel.setText("TEACHER PORTAL");
        }

        pageVisible(true, false);
        pageOpacity(1, 0);

        signup_submit.setStyle("-fx-background-color: #7aa8f4;");

        backButton.setOnAction(event -> {
            LoginUser.s_window.close();
        });

        signup_botton.setOnAction(event -> {
            pageVisible(true, false);
            pageOpacity(1, 0);

            signup_firstname.setStyle("-fx-border-color : transparent");
            signup_lastname.setStyle("-fx-border-color : transparent");
            signup_username.setStyle("-fx-border-color : transparent");
            signup_password.setStyle("-fx-border-color : transparent");
            signup_email.setStyle("-fx-border-color : transparent");

            signup_submit.setStyle("-fx-background-color: #7aa8f4;");
        });

        login_button.setOnAction(event -> {
            pageVisible(false, true);
            pageOpacity(0, 1);

            login_username.setStyle("-fx-border-color : transparent");
            login_password.setStyle("-fx-border-color : transparent");

            login_submit.setStyle("-fx-background-color: #7aa8f4;");
        });

        /**
         * Action Event when new student or new teacher wants to sign up
         */
        signup_submit.setOnAction(event -> {

            firstname = signup_firstname.getText().trim().toUpperCase();
            lastname = signup_lastname.getText().trim().toUpperCase();
            email = signup_email.getText().trim().toLowerCase();
            password = signup_password.getText().trim();

            username = LoginUser.prefix+signup_username.getText().trim().toUpperCase();

            /**
             * Check if the signup details are correct before adding details to the
             * REGISTRATION.db database
             */
            if(dataValidSignUp(firstname, lastname, email, username, password)) {

                /**
                 * If the students wants to sign up into the institute
                 */
                if(LoginUser.studentLogin) {

                    SQliteConnection.DATABASE_PEOPLE = "STUDENT_DATABASE";
                    SQliteConnection.DATABASE_FOLLOW = "STUDENT_FOLLOW";

                    if(SQliteConnection.userAlreadyExists("STUDENT", username)) {
                        String query = "INSERT INTO STUDENT (FIRSTNAME,LASTNAME,EMAIL,USERNAME,PASWORD,SIGNEDIN) VALUES ('"+firstname+"','"+lastname+"','"+email+"','"+username+"','"+password+"',"+0+")";

                        // If the database of STUDENT does not contains username
                        // add student to REGISTRATION.db > STUDENT TABLE
                        SQliteConnection.insertQuery(query, "REGISTRATION");

                        if(SQliteConnection.querySuccessful) {
                            SQliteConnection.studentLogin = true;
                            signUpDetails();
                        }
                        else {
                            signup_username.setStyle("-fx-border-color : #ff5542");
                            dataValidation = false;
                        }
                    }
                    else {
                        signup_username.setStyle("-fx-border-color : #ff5542");
                        dataValidation = false;
                    }
                }

                /**
                 * If the teacher wants to sign up into the institute
                 */
                else {

                    SQliteConnection.DATABASE_PEOPLE = "TEACHER_DATABASE";
                    SQliteConnection.DATABASE_FOLLOW = "TEACHER_FOLLOW";

                    if(SQliteConnection.userAlreadyExists("TEACHER", username)) {
                        String query = "INSERT INTO TEACHER (FIRSTNAME,LASTNAME,EMAIL,USERNAME,PASWORD,SIGNEDIN) VALUES ('"+firstname+"','"+lastname+"','"+email+"','"+username+"','"+password+"',"+0+")";

                        SQliteConnection.insertQuery(query, "REGISTRATION");

                        if(SQliteConnection.querySuccessful) {
                            SQliteConnection.studentLogin = false;

                            SQliteConnection.createTable(username, "TEACHER_TESTS");
                            signUpDetails();
                        }
                        else {
                            signup_username.setStyle("-fx-border-color : #7aa8f4");
                            dataValidation = false;
                        }
                    }
                    else {
                        signup_username.setStyle("-fx-border-color : #ff5542");
                        dataValidation = false;
                    }
                }
            }
            else {
                System.out.println("sign up details error");
            }
        });

        login_submit.setOnAction(event -> {

            username = LoginUser.prefix + login_username.getText().trim().toUpperCase();
            password = login_password.getText().trim();

            if(dataValidLogin(username, password)) {
                if(LoginUser.studentLogin) { // Student is Login

                    SQliteConnection.DATABASE_PEOPLE = "STUDENT_DATABASE";
                    SQliteConnection.DATABASE_FOLLOW = "STUDENT_FOLLOW";

                    if(SQliteConnection.loginUser("STUDENT", username, password)) {

                        System.out.printf("-> %s %s : %s\n", LoginUser.firstname, LoginUser.lastname, LoginUser.username);

                        login_password.setStyle("-fx-border-color : transparent");
                        login_username.setStyle("-fx-border-color : transparent");

                        SQliteConnection.studentLogin = true;

                        LoginUser.accepted = true;
                        SQliteConnection.updateStatusQuery(1);
                        LoginUser.s_window.close();
                    }
                    else {
                        login_password.setStyle("-fx-border-color : #ff5542");
                        login_username.setStyle("-fx-border-color : #ff5542");
                    }
                }
                else { // Teacher is Signing up

                    SQliteConnection.DATABASE_PEOPLE = "TEACHER_DATABASE";
                    SQliteConnection.DATABASE_FOLLOW = "TEACHER_FOLLOW";

                    if(SQliteConnection.loginUser("TEACHER", username, password)) {

                        System.out.printf("-> %s %s : %s\n", LoginUser.firstname, LoginUser.lastname, LoginUser.username);

                        login_password.setStyle("-fx-border-color : transparent");
                        login_username.setStyle("-fx-border-color : transparent");

                        SQliteConnection.studentLogin = false;

                        LoginUser.accepted = true;
                        SQliteConnection.updateStatusQuery(1);
                        LoginUser.s_window.close();
                    }
                    else {
                        login_password.setStyle("-fx-border-color : #ff5542");
                        login_username.setStyle("-fx-border-color : #ff5542");
                    }
                }
            }
            else {
                System.out.println("loginuser error");
            }
        });
    }

    private void signUpDetails()
    {
        LoginUser.username = username;
        LoginUser.firstname = firstname;
        LoginUser.lastname = lastname;

        System.out.printf("transferDetails : %s %s %s\n", LoginUser.username, LoginUser.firstname, LoginUser.lastname);

        LoginUser.accepted = true;

        SQliteConnection.updateStatusQuery(1);
        LoginUser.s_window.close();
    }

    private boolean dataValidLogin(String username, String password)
    {
        dataValidation = true;

        if(username.length() == 0) {
            login_username.setStyle("-fx-border-color : #7aa8f4");
            dataValidation = false;
        }
        else {
            login_username.setStyle("-fx-border-color : transparent");
        }

        if(password.length() == 0) {
            login_password.setStyle("-fx-border-color : #7aa8f4");
            dataValidation = false;
        }
        else {
            login_password.setStyle("-fx-border-color : transparent");
        }

        return dataValidation;
    }

    private boolean dataValidSignUp(String firstname, String lastname, String email, String username, String password)
    {
        dataValidation = true;

        if(firstname.length() == 0) {
            signup_firstname.setStyle("-fx-border-color : #ff5542");
            dataValidation = false;
        }
        else {
            signup_firstname.setStyle("-fx-border-color : transparent");
        }

        if(lastname.length() == 0) {
            signup_lastname.setStyle("-fx-border-color : #ff5542");
            dataValidation = false;
        }
        else {
            signup_lastname.setStyle("-fx-border-color : transparent");
        }

        if(email.length() == 0 || !emailValidFormat(email)) {
            signup_email.setStyle("-fx-border-color : #ff5542");
            dataValidation = false;
        }
        else {
            signup_email.setStyle("-fx-border-color : transparent");
        }

        if(username.length() == 0) {
            signup_username.setStyle("-fx-border-color : #ff5542");
            dataValidation = false;
        }
        else {
            signup_username.setStyle("-fx-border-color : transparent");
        }

        if(password.length() == 0) {
            signup_password.setStyle("-fx-border-color : #ff5542");
            dataValidation = false;
        }
        else {
            signup_password.setStyle("-fx-border-color : transparent");
        }

        return dataValidation;
    }

    private boolean emailValidFormat(String email) {
        return true;
    }

    public void pageVisible(boolean signup, boolean login)
    {
        if(signup) {
            signup_botton.setStyle("-fx-background-color : #5e7eb2");
            login_button.setStyle("-fx-background-color : #f4f4f4");

            signup_submit.setStyle("-fx-background-color : #5e7eb2");
        }
        else {
            signup_botton.setStyle("-fx-background-color : #f4f4f4");
            login_button.setStyle("-fx-background-color : #5e7eb2");

            login_submit.setStyle("-fx-background-color : #5e7eb2");
        }

        signupPage.setVisible(signup);
        loginPage.setVisible(login);
    }

    public void pageOpacity(double signup, double login)
    {
        signupPage.setOpacity(signup);
        loginPage.setOpacity(login);
    }

    public void loginMouseEnter(MouseEvent e) {
        login_submit.setStyle("-fx-background-color: #5e7eb2;");
    }

    public void loginMouseExit(MouseEvent e) {
        login_submit.setStyle("-fx-background-color: #7aa8f4;");
    }

    public void signupMouseEnter(MouseEvent e) {
        signup_submit.setStyle("-fx-background-color: #5e7eb2;");
    }

    public void signupMouseExit(MouseEvent e) {
        signup_submit.setStyle("-fx-background-color: #7aa8f4;");
    }
}
