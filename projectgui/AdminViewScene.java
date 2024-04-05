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
import java.util.ArrayList;

public class AdminViewScene {

    public Scene adminView (Connection conn, Scene loginScene, Stage pStage) {
        BorderPane adminPane = new BorderPane();
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

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        HBox hbButtons = new HBox();
        hbButtons.getChildren().addAll(executeSQL, viewLoginTable, viewWorkTable, viewRoleTable, logout);
        hbButtons.setSpacing(10);

        Text successText = new Text();

        bottomPane.addRow(0, successText);
        bottomPane.addRow(1, sqlInputs);
        bottomPane.addRow(2, hbButtons);
        bottomPane.setHgap(10);
        bottomPane.setVgap(10);
        bottomPane.setPadding(new Insets(10, 10, 10, 10));

        adminPane.setBottom(bottomPane);

        logout.setOnAction(e -> logout(loginScene, pStage));
        executeSQL.setOnAction(e ->  {
            if (executeTextAreaSQL(sqlInputs.getText(), conn) > 0) {
                successText.setText("Update successful.");
            } else {
                successText.setText("Something went wrong with the SQL query.");
            }
            sqlInputs.setText("");
            adminPane.setCenter(returnTable(conn));
        });

        return new Scene(adminPane, 500,500);
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
        ObservableList<RoleTableInfo> data = FXCollections.observableArrayList();




        return loginTable;
    }

    public TableView<WorkTableInfo> returnWorkTable(Connection conn) {
        TableView<WorkTableInfo> workTable = new TableView<>();
        workTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        ObservableList<WorkTableInfo> workData = FXCollections.observableArrayList();


        return workTable;
    }
}
