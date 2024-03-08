package projectgui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;

//will be using border pane for the various scenes to organize them better

public class Scene_Repository extends Application{
    @Override
    public void start(Stage primaryStage) {
        GridPane loginPane = new GridPane();
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setHgap(10);
        loginPane.setVgap(10);
        loginPane.setPadding(new Insets(25, 25, 25, 25));
        Label userNameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        TextField userField = new TextField();
        TextField passField = new TextField();
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        /*
        loginPane.add(userNameLabel, 0, 0);
        loginPane.add(userField, 1, 0);
        loginPane.add(passwordLabel, 0, 1);
        loginPane.add(passField, 1, 1);
        loginPane.add(loginButton, 3, 2);

        //loginPane.add(registerButton, 3,6); */
        loginPane.addRow(0, userNameLabel, userField);
        loginPane.addRow(1, passwordLabel, passField);
        loginPane.addRow(4, loginButton, registerButton);

        Scene loginScene = new Scene(loginPane);


        GridPane registerPane = new GridPane();
        Scene registerScene = new Scene(registerPane);



        BorderPane afterLogin = new BorderPane();
        Scene afterLoginScene = new Scene(afterLogin);



        VBox updateInfo = new VBox();
        Scene updateInfoScene = new Scene(updateInfo);



        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Employee Database");
        primaryStage.show();
    }
}
