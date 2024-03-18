package projectgui;

import java.sql.*;

public class RegistrationChecker {

    protected boolean checkUserExists(String userName, Connection conn) {
        boolean exists = false;

        try {
            String query = "Select * from login_information where username = '" + userName + "';";

            Statement stmt = conn.createStatement();
            ResultSet rSet = stmt.executeQuery(query);

            if (rSet.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return exists;
    }

    protected boolean registerUser(String firstName, String lastName, String password, String userName, Connection conn) {
        try {
            String query = "Insert into login_information(firstname, lastname, username, userpassword) values ('" +
                    firstName + "', '" + lastName + "', '" + userName + "', '" + password + "');";

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);

            boolean insertedUser = checkInserted(userName, conn);

            if (insertedUser) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean checkInserted(String userName, Connection conn) {
        try {
            String query = "Select * from login_information where username = '" + userName + "';";
            Statement stmt = conn.createStatement();
            ResultSet rSet = stmt.executeQuery(query);

            if (rSet.next()) {
                addToWorkInformation(userName, conn);
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    protected void addToWorkInformation(String userName, Connection conn) {
        try {
            String query = "Select employee_id, lastname, firstname from login_information where username = '" + userName + "';";
            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery(query);

            while (set.next()) {
                int id = set.getInt(1);
                String lastName = set.getString(2);
                String firstName = set.getString(3);

                String insertStmt = "insert into work_information (employee_id, lastname, firstname) values ('" +
                                    id + "', '" + lastName + "', '" + firstName + "');";
                Statement tableInsert = conn.createStatement();
                tableInsert.executeUpdate(insertStmt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
