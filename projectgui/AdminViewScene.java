package projectgui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Callback;

import java.sql.*;

public class AdminViewScene {

    public Scene adminView (Connection conn, Scene loginScene, Stage pStage) {
        BorderPane adminPane = new BorderPane();

        Scene adminScene = new Scene(adminPane, 500, 500);

        Text welcomeAdmin = new Text();
        welcomeAdmin.setText("Hello, Admin");
        adminPane.setTop(welcomeAdmin);

        adminPane.setCenter(returnTable(conn));

        GridPane bottomPane = new GridPane();
        TextArea sqlInputs = new TextArea();
        Button executeSQL = new Button("Execute");
        Button logout = new Button("Logout");
        Button viewLoginTable = new Button("View Login Table");
        Button viewWorkTable = new Button("View Work Table");
        Button viewRoleTable = new Button("View Role Table");

        HBox textAreaContainer = new HBox();
        sqlInputs.prefWidthProperty().bind(bottomPane.prefWidthProperty());
        HBox.setHgrow(sqlInputs, Priority.ALWAYS);
        textAreaContainer.getChildren().add(sqlInputs);

        HBox hbButtons = new HBox();

        //setting it so each button will scale
        //also makes it so their width is the max value of double
        HBox.setHgrow(executeSQL, Priority.ALWAYS);
        HBox.setHgrow(viewLoginTable, Priority.ALWAYS);
        HBox.setHgrow(viewWorkTable, Priority.ALWAYS);
        HBox.setHgrow(viewRoleTable, Priority.ALWAYS);
        HBox.setHgrow(logout, Priority.ALWAYS);
        executeSQL.setMaxWidth(Double.MAX_VALUE);
        viewLoginTable.setMaxWidth(Double.MAX_VALUE);
        viewWorkTable.setMaxWidth(Double.MAX_VALUE);
        viewRoleTable.setMaxWidth(Double.MAX_VALUE);
        logout.setMaxWidth(Double.MAX_VALUE);

        hbButtons.getChildren().addAll(executeSQL, viewLoginTable, viewWorkTable, viewRoleTable, logout);
        hbButtons.prefWidthProperty().bind(bottomPane.prefWidthProperty());
        hbButtons.setSpacing(10);

        Text updateText = new Text();

        bottomPane.addRow(0, updateText);
        bottomPane.addRow(1, textAreaContainer);
        bottomPane.addRow(2, hbButtons);
        bottomPane.setHgap(10);
        bottomPane.setVgap(10);
        bottomPane.setPadding(new Insets(10, 10, 10, 10));

        bottomPane.prefWidthProperty().bind(adminPane.widthProperty());

        adminPane.setBottom(bottomPane);

        logout.setOnAction(e -> logout(loginScene, pStage));
        executeSQL.setOnAction(e ->  {
            if (executeTextAreaSQL(sqlInputs.getText(), conn) > 0) {
                updateText.setText("Update successful.");
            } else {
                updateText.setText("Something went wrong with the SQL query.");
            }
            sqlInputs.setText("");
            adminPane.setCenter(returnTable(conn));
        });

        viewLoginTable.setOnAction(e -> {
            adminPane.setCenter(returnLoginTable(conn));
            updateText.setText("Viewing Table: login_information");
        });
        viewRoleTable.setOnAction(e -> {
            adminPane.setCenter(returnTable(conn));
            updateText.setText("Viewing Table: employee_roles");
        });
        viewWorkTable.setOnAction(e -> {
            adminPane.setCenter(returnWorkTable(conn));
            updateText.setText("Viewing Table: work_information");
        });

        return adminScene;
    }

    public void logout(Scene loginScene, Stage pStage) {
        pStage.setScene(loginScene);
    }

    public int executeTextAreaSQL(String text, Connection conn) {
        int successOrFail = 0;

        try {
            Statement stmt = conn.createStatement();
            successOrFail = stmt.executeUpdate(text);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return successOrFail;
    }

    public TableView<RoleTableInfo> returnTable(Connection conn) {
        TableView<RoleTableInfo> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        ObservableList<RoleTableInfo> data = FXCollections.observableArrayList();
        try {
            String query = "Select * from employee_roles;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                if (rs.getString(4) == null) {
                    RoleTableInfo rInfo = new RoleTableInfo(
                            String.valueOf(rs.getInt(1)),
                            rs.getString(2),
                            rs.getString(3),
                            "BLANK"
                    );
                    data.add(rInfo);
                } else {
                    RoleTableInfo rInfo = new RoleTableInfo(
                            String.valueOf(rs.getInt(1)),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4)
                    );
                    data.add(rInfo);
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        table.setItems(data);

        TableColumn idCol = new TableColumn("Employee ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("empId"));

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn roleCol = new TableColumn("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getColumns().addAll(idCol, firstNameCol, lastNameCol, roleCol);

        return table;
    }

    public TableView<LoginTableInfo> returnLoginTable(Connection conn) {
        TableView<LoginTableInfo> loginTable = new TableView<>();
        loginTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        ObservableList<LoginTableInfo> loginTableData = FXCollections.observableArrayList();

        //select * order is Employee_ID, LastName FirstName, UserName, UserPassword
        try {
            String query = "Select * from login_information;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                LoginTableInfo ltInfo = new LoginTableInfo(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)
                );

                loginTableData.add(ltInfo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        loginTable.setItems(loginTableData);

        TableColumn idCol = new TableColumn("Employee ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("empID"));

        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn userNameCol = new TableColumn("Username");
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));

        TableColumn userPassCol = new TableColumn("User Password");
        userPassCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        loginTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loginTable.getColumns().addAll(idCol, lastNameCol, firstNameCol, userNameCol, userPassCol);

        return loginTable;
    }

    public TableView<WorkTableInfo> returnWorkTable(Connection conn) {
        TableView<WorkTableInfo> workTable = new TableView<>();
        workTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        ObservableList<WorkTableInfo> workData = FXCollections.observableArrayList();

        //Employee_ID, LastName, FirstName, License (date), CPR_AED (date), Employee_Email, department
        try {
            String query = "Select * from work_information;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                WorkTableInfo wTableInfo = new WorkTableInfo(
                  rs.getInt(1),
                  rs.getString(2),
                  rs.getString(3),
                  rs.getDate(4),
                  rs.getDate(5),
                  rs.getString(6),
                  rs.getString(7)
                );

                workData.add(wTableInfo);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        workTable.setItems(workData);

        TableColumn idCol = new TableColumn("Employee ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("empID"));

        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn licenseCol = new TableColumn("License");
        licenseCol.setCellValueFactory(new PropertyValueFactory<>("userLicense"));

        TableColumn cpraedCol = new TableColumn("CPR/AED");
        cpraedCol.setCellValueFactory(new PropertyValueFactory<>("userCPRAED"));

        TableColumn emailCol = new TableColumn("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("userEmail"));

        TableColumn departmentCol = new TableColumn("Department");
        departmentCol.setCellValueFactory(new PropertyValueFactory<>("userDepartment"));

        workTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        workTable.getColumns().addAll(idCol, lastNameCol, firstNameCol, licenseCol, cpraedCol, emailCol, departmentCol);

        return workTable;
    }
}
