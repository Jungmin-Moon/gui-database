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
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Scene_Repository extends Application{
    LoginChecker loginCheck = new LoginChecker();
    RegistrationChecker registerCheck = new RegistrationChecker();
    User user = new User();
    Database_Connector dbConnect = new Database_Connector();
    Connection connection = dbConnect.establishConnection();
    Employee_Info empInfo = new Employee_Info();

    AdminViewScene adminView = new AdminViewScene();

    private final Button logoutButton = new Button("Log Out");


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

        loginScene.getStylesheets().add("/projectgui/styles.css");
        loginPane.getStyleClass().add("loginPane");

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
        registerScene.getStylesheets().add("/projectgui/styles.css");
        registerPane.getStyleClass().add("registerPane");

        //event handling
        registerButton.setOnAction(e -> primaryStage.setScene(registerScene));
        loginButton.setOnAction(e -> {
            String uName = userField.getText();
            String uPass = passField.getText();
            userField.setText("");
            passField.setText("");
            if (validateInformation(uName, uPass, connection)) {
                String[] persistCheck = loginCheck.getInformation(uName, connection);
                if (persistCheck[1].equalsIgnoreCase("admin")) {
                    primaryStage.setScene(adminView.adminView(connection, loginScene, primaryStage));
                } else {
                    setTokens(persistCheck);
                    try {
                        primaryStage.setScene(afterLoginScene(primaryStage));
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            } else {
                loginText.setText("Does not exist in database.");
            }
        });

        registerUser.setOnAction(e -> {
            String pass1 = newPassField.getText();
            String pass2 = passAgainField.getText();
            String newUName = newUserName.getText();
            newPassField.setText("");
            passAgainField.setText("");
            newUserName.setText("");

            if (newUName.matches("\\badmin\\b|\\badmln\\b|\\badmLn\\b|\\b@dmin\\b|\\badmIn\\b|\\b@dmIn\\b|\\b@dmln\\b|\\b@dmLn\\b")) {
                status.setText("You can not use admin or any variant of admin in yor username.");
            } else {
                if (!registerCheck.checkUserExists(newUName, connection)) {
                    if (pass1.equals(pass2)) {
                        String newFName = newFirstField.getText();
                        String newLName = newLastField.getText();
                        newFirstField.setText("");
                        newLastField.setText("");
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
            }
        });

        logoutButton.setOnAction(e -> logout(primaryStage, loginScene));

        goBackLogin.setOnAction(e -> {
            newPassField.setText("");
            passAgainField.setText("");
            newUserName.setText("");
            newFirstField.setText("");
            newLastField.setText("");
            primaryStage.setScene(loginScene);

        });

        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Employee Database");
        primaryStage.show();

    }


    private boolean validateInformation(String userName, String userPassword, Connection connection) {
        return loginCheck.checkLoginInformation(userName, userPassword, connection);
    }

    private Scene afterLoginScene(Stage pStage) throws SQLException {
        BorderPane afterLogin = new BorderPane();
        Scene afterLoginScene = new Scene(afterLogin, 600, 600);

        GridPane topPane = new GridPane();
        Text loggedIn = new Text();
        loggedIn.setText("Employee ID: " + user.getEmpID() + "\n" +
                "Hello, " + user.getName());
        afterLogin.setTop(topPane);
        loggedIn.getStyleClass().add("loggedInText");

        LocalDate todayDate = LocalDate.now();
        Text licenseStatus = new Text();
        Text cpraedStatus = new Text();

        ResultSet licenseResult = empInfo.getLicenseDate(user.getEmpID(), connection);
        licenseResult.next();
        if (licenseResult.getDate(1) != null) {
            LocalDate currentLicense = LocalDate.parse(String.valueOf(licenseResult.getDate(1)));

            long daysLicenseExpire = ChronoUnit.DAYS.between(todayDate, currentLicense);

            if (daysLicenseExpire >= 61) {
                licenseStatus.setStyle("-fx-font-family: Arial; -fx-font-size: 20; -fx-font-weight: Bold");
                licenseStatus.setFill(Color.BLACK);
                licenseStatus.setText("Days till License expires: " + daysLicenseExpire); //no color
            } else if (daysLicenseExpire <= 60 && daysLicenseExpire >= 31) {
                licenseStatus.setStyle("-fx-font-family: Arial; -fx-font-size: 20; -fx-font-weight: Bold");
                licenseStatus.setFill(Color.YELLOW);
                licenseStatus.setText("Days till License expires: " + daysLicenseExpire); //Yellow text
            } else if (daysLicenseExpire <= 30 && daysLicenseExpire >= 1) {
                licenseStatus.setStyle("-fx-font-family: Arial; -fx-font-size: 20; -fx-font-weight: Bold");
                licenseStatus.setFill(Color.ORANGE);
                licenseStatus.setText("Days till License expires: " + daysLicenseExpire); //orange text
            } else {
                licenseStatus.setStyle("-fx-font-family: Arial; -fx-font-size: 20; -fx-font-weight: Bold");
                licenseStatus.setFill(Color.RED);
                licenseStatus.setText("Days till License expires: EXPIRED"); // red text
            }
        } else {
            licenseStatus.setText("No information about license set.");
        }

        ResultSet certResult = empInfo.getCertificateDate(user.getEmpID(), connection);
        certResult.next();
        if (certResult.getDate(1) != null) {
            LocalDate currentCprAed = LocalDate.parse(String.valueOf(certResult.getDate(1)));

            long daysCertExpire = ChronoUnit.DAYS.between(todayDate, currentCprAed);

            if (daysCertExpire >= 61) {
                cpraedStatus.setStyle("-fx-font-family: Arial; -fx-font-size: 20; -fx-font-weight: Bold");
                cpraedStatus.setFill(Color.BLACK);
                cpraedStatus.setText("Days till Certificate expires: " + daysCertExpire); //no color
            } else if (daysCertExpire <= 60 && daysCertExpire >= 31) {
                cpraedStatus.setStyle("-fx-font-family: Arial; -fx-font-size: 20; -fx-font-weight: Bold");
                cpraedStatus.setFill(Color.YELLOW);
                cpraedStatus.setText("Days till Certificate expires: " + daysCertExpire); //Yellow text
            } else if (daysCertExpire <= 30 && daysCertExpire >= 1) {
                cpraedStatus.setStyle("-fx-font-family: Arial; -fx-font-size: 20; -fx-font-weight: Bold");
                cpraedStatus.setFill(Color.ORANGE);
                cpraedStatus.setText("Days till Certificate expires: " + daysCertExpire); //orange text
            } else {
                cpraedStatus.setStyle("-fx-font-family: Arial; -fx-font-size: 20; -fx-font-weight: Bold");
                cpraedStatus.setFill(Color.RED);
                cpraedStatus.setText("Days till Certificate expires: EXPIRED"); // red text
            }
        } else {
            cpraedStatus.setText("No information about certificates set.");
        }

        topPane.addRow(0, loggedIn);
        topPane.addRow(2, licenseStatus);
        topPane.addRow(3, cpraedStatus);

        TextArea currentInfo = new TextArea();
        String result = empInfo.displaySingleEmployee(user.getEmpID(), connection);

        currentInfo.setText(result);
        currentInfo.setEditable(false);
        currentInfo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        VBox employeeInformation = new VBox();
        employeeInformation.getChildren().add(currentInfo);
        employeeInformation.setAlignment(Pos.CENTER);

        Button updateButton = new Button("Update Information");

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        vBox.getChildren().add(updateButton);
        vBox.getChildren().add(logoutButton);
        vBox.setAlignment(Pos.CENTER);

        afterLogin.setCenter(employeeInformation);
        afterLogin.setBottom(vBox);

        updateButton.setOnAction(e -> pStage.setScene(updateScene(pStage)));

        afterLoginScene.getStylesheets().add("/projectgui/styles.css");
        afterLogin.getStyleClass().add("afterLoginPane");
        currentInfo.getStyleClass().add("afterLoginTextArea");
        updateButton.getStyleClass().add("afterLoginUpdateButton");

        return afterLoginScene;
    }

    private void setTokens(String[] values) {
        user.setEmpID(Integer.parseInt(values[0]));
        user.setFirstName(values[1]);
        user.setLastName(values[2]);
    }

    private void logout(Stage pStage, Scene startScene) {
        pStage.setScene(startScene);
        user.clearInformation();
    }

    private Scene updateScene(Stage pStage) {
        BorderPane updatePane = new BorderPane();

        Scene updateSceneView = new Scene(updatePane, 600,600);

        Text token = new Text();
        token.setText("Employee ID: " + user.getEmpID() + "\n" +
                "Hello, " + user.getName());
        token.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        updatePane.setTop(token);

        VBox updateFields = new VBox();
        updateFields.setAlignment(Pos.CENTER);

        //labels and more
        Label lastName = new Label("Last Name: ");
        Label firstName = new Label("First Name: ");
        Label empEmail = new Label("Email: ");
        Label license = new Label("License Expiration: ");
        Label cprAED = new Label("CPR/AED Expiration: ");
        Label empDepartment = new Label("Department: ");

        Text databaseLastName = new Text();
        Text databaseFirstName = new Text();
        Text databaseEmail = new Text();
        Text databaseLicense = new Text();
        Text databaseCPRAED = new Text();
        Text databaseDepartment = new Text();

        String[] employeeInfo = empInfo.returnEmployeeInformation(user.getEmpID(), connection);
        databaseLastName.setText(employeeInfo[0]);
        databaseFirstName.setText(employeeInfo[1]);
        databaseEmail.setText(employeeInfo[2]);
        databaseLicense.setText(employeeInfo[3]);
        databaseCPRAED.setText(employeeInfo[4]);
        databaseDepartment.setText(employeeInfo[5]);

        TextField changeLastName = new TextField();
        TextField changeFirstName = new TextField();
        TextField changeEmail = new TextField();
        DatePicker changeLicense = new DatePicker();
        DatePicker changeCPRAED = new DatePicker();
        TextField changeDepartment = new TextField();

        //Gridpane to put on top of the VBox in the center of the borderpane
        GridPane table = new GridPane();
        table.setVgap(10);
        table.setHgap(10);
        table.addRow(1, lastName, databaseLastName, changeLastName);
        table.addRow(2, firstName, databaseFirstName, changeFirstName);
        table.addRow(3, empEmail, databaseEmail, changeEmail);
        table.addRow(4, license, databaseLicense, changeLicense);
        table.addRow(5, cprAED, databaseCPRAED, changeCPRAED);
        table.addRow(6, empDepartment, databaseDepartment, changeDepartment);
        updateFields.getChildren().add(table);

        Button updateInfo = new Button("Update");
        Button backOne = new Button("Return");

        VBox updateButtons = new VBox();
        updateButtons.setAlignment(Pos.CENTER);
        updateButtons.getChildren().addAll(updateInfo, backOne);
        updateButtons.setSpacing(10);
        updateButtons.setPadding(new Insets(10));

        updatePane.setCenter(updateFields);
        updatePane.setBottom(updateButtons);

        /*
        When the update info button is clicked. the program will attempt to update the information
        If it does so successfully, it will then check to see if the employee's name was changed and if it was update
        the "token" that is always in the top left of the window.
         */

        updateInfo.setOnAction(e ->  {

            String licenseCheck = "";
            String cpraedCheck = "";

            if (String.valueOf(changeLicense.getValue()) == null) {
                licenseCheck = "BLANK";
            } else {
                licenseCheck = String.valueOf(changeLicense.getValue());
            }

            if (String.valueOf(changeCPRAED.getValue()) == null) {
                cpraedCheck = "BLANK";
            } else {
                cpraedCheck = String.valueOf(changeCPRAED.getValue());
            }

            String[] updatedInformation = {changeLastName.getText(), changeFirstName.getText(), changeEmail.getText(),
                    licenseCheck, cpraedCheck, changeDepartment.getText()};
            empInfo.updateEmployee(user.getEmpID(), updatedInformation, connection);
            try {
                pStage.setScene((afterLoginScene(pStage)));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        backOne.setOnAction(e -> {
            try {
                pStage.setScene(afterLoginScene(pStage));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        updateSceneView.getStylesheets().add("/projectgui/styles.css");

        return updateSceneView;
    }

}
