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
    //a class meant to be for admin view of the database
    //will only return a scene if the user who logged in is admin.

    //table view of entire table for roles



    public Scene adminView (Connection conn) {
        BorderPane adminPane = new BorderPane();
        Text welcomeAdmin = new Text();
        welcomeAdmin.setText("Hello, Admin");
        adminPane.setTop(welcomeAdmin);

        TableView<RoleTableInfo> table = new TableView<>();

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

        TableColumn idCol = new TableColumn("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn roleCol = new TableColumn("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("Role"));

        //table.setItems(data);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getColumns().addAll(idCol, firstNameCol, lastNameCol, roleCol);

        adminPane.setCenter(table);

        return new Scene(adminPane, 500,500);
    }



}
