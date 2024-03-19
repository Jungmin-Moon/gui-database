package projectgui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import java.sql.*;

//will be using border pane for the various scenes to organize them better

public class Scene_Repository extends Application{
    LoginChecker loginCheck = new LoginChecker();
    RegistrationChecker registerCheck = new RegistrationChecker();
    User user = new User();
    Database_Connector dbConnect = new Database_Connector();
    Connection connection = dbConnect.establishConnection();



    @Override
    public void start(Stage primaryStage) {
        GridPane loginPane = new GridPane();
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setHgap(10);
        loginPane.setVgap(10);
        loginPane.setPadding(new Insets(25, 25, 25, 25));

        //labels
        Label userNameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        //text fields
        TextField userField = new TextField();
        PasswordField passField = new PasswordField();
        //buttons
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        Text loginText = new Text("");



        loginPane.addRow(0, userNameLabel, userField);
        loginPane.addRow(1, passwordLabel, passField);
        loginPane.addRow(4, loginButton, registerButton);
        loginPane.addRow(5, loginText);
        Scene loginScene = new Scene(loginPane, 400, 400);


        GridPane registerPane = new GridPane();
        registerPane.setAlignment(Pos.CENTER);
        registerPane.setHgap(10);
        registerPane.setVgap(10);
        registerPane.setPadding(new Insets(25, 25, 25, 25));
        //labels
        Label newFirstName = new Label("First Name:");
        Label newLastName = new Label("Last Name:");
        Label newUserNameLabel = new Label("Username:");
        Label newPasswordLabel = new Label("Enter a new password:");
        Label enterPassAgainLabel = new Label("Enter the password again:");

        //text fields
        TextField newFirstField = new TextField();
        TextField newLastField = new TextField();
        PasswordField newPassField = new PasswordField();
        PasswordField passAgainField = new PasswordField();
        TextField newUserName = new TextField();

        Text status = new Text();

        Button registerUser = new Button("Register");
        Button goBackLogin = new Button("Return");

        registerPane.addRow(0, newFirstName, newFirstField);
        registerPane.addRow(1, newLastName, newLastField);
        registerPane.addRow(2, newUserNameLabel, newUserName);
        registerPane.addRow(3, newPasswordLabel, newPassField);
        registerPane.addRow(4, enterPassAgainLabel, passAgainField);
        registerPane.addRow(5, registerUser, goBackLogin);
        registerPane.addRow(6, status);
        Scene registerScene = new Scene(registerPane, 400, 400);


        VBox updateInfo = new VBox();
        Scene updateInfoScene = new Scene(updateInfo);

        //event handling
        registerButton.setOnAction(e -> primaryStage.setScene(registerScene));
        loginButton.setOnAction(e -> {
            String uName = userField.getText();
            String uPass = passField.getText();
            if (validateInformation(uName, uPass, connection)) {
                String[] persistCheck = loginCheck.getInformation(uName, connection);
                setTokens(persistCheck);
                primaryStage.setScene(afterLoginScene());

            } else {
                loginText.setText("Does not exist in databasse.");
            }
        });

        registerUser.setOnAction(e -> {
            String pass1 = newPassField.getText();
            String pass2 = passAgainField.getText();
            String newUName = newUserName.getText();

            if (!registerCheck.checkUserExists(newUName, connection)) {
                if (pass1.equals(pass2)) {
                    String newFName = newFirstField.getText();
                    String newLName = newLastField.getText();
                    boolean registered = registerCheck.registerUser(newFName, newLName, pass1, newUName, connection);
                    if (registered) {
                        primaryStage.setScene(loginScene);
                    } else {
                        status.setText("Something went wrong and you were not registered. Please try again.");
                    }
                } else {
                    status.setText("Passwords do not match. Please enter them again.");
                }
            } else {
                status.setText("Username already exists. Please use a different one.");
            }
        });

        goBackLogin.setOnAction(e -> primaryStage.setScene(loginScene));

        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Employee Database");
        primaryStage.show();

    }


    private boolean validateInformation(String userName, String userPassword, Connection connection) {
        boolean isValid = loginCheck.checkLoginInformation(userName, userPassword, connection);
        if (isValid) {
            return true;
        } else {
            return false;
        }
    }

    private Scene afterLoginScene() {
        BorderPane afterLogin = new BorderPane();
        Scene afterLoginScene = new Scene(afterLogin, 400, 400);
        Text loggedIn = new Text();
        loggedIn.setText("Employee ID: " + user.getEmpID() + "\n" +
                "Hello, " + user.getName());
        afterLogin.setTop(loggedIn);
        loggedIn.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Employee_Info empInfo = new Employee_Info();
        Text currentInfo = new Text(empInfo.singleEmployee(user.getEmpID()));
        VBox employeeInformation = new VBox();
        employeeInformation.getChildren().add(currentInfo);



        Button updateButton = new Button("Update Information");
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        vBox.getChildren().add(updateButton);
        vBox.setAlignment(Pos.CENTER);


        afterLogin.setBottom(vBox);

        return afterLoginScene;
    }

    private void setTokens(String[] values) {
        user.setEmpID(Integer.parseInt(values[0]));
        user.setName(values[1] + " " + values[2]);
    }

    
}
