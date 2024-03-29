package projectgui;

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
        ObservableList<RoleTableInfo> data = getList(conn);
        table.setItems(data);

        TableColumn empIDColumn = new TableColumn("Employee ID");
        TableColumn firstNameColumn = new TableColumn("First Name");
        TableColumn lastNameColumn = new TableColumn("Last Name");
        TableColumn roleColumn = new TableColumn("Role");
        
        empIDColumn.setCellValueFactory(new PropertyValueFactory<RoleTableInfo, String>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<RoleTableInfo, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<RoleTableInfo, String>("lastName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<RoleTableInfo, String>("role"));

        adminPane.setCenter(table);


        return new Scene(adminPane, 500,500);
    }

    public ObservableList<RoleTableInfo> getList(Connection conn) {
        ObservableList<RoleTableInfo> data = FXCollections.observableArrayList();
        try {
            String query = "Select * from employee_roles;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rd = rs.getMetaData();

            ArrayList<RoleTableInfo> roleTableList = new ArrayList<>();

            while (rs.next()) {
                roleTableList.add(new RoleTableInfo(String.valueOf(rs.getInt(1)), rs.getString(2), rs.getString(3),
                                    rs.getString(4)));
            }
            data.addAll(roleTableList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

}
