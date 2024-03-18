package projectgui;


import javax.xml.transform.Result;
//import java.sql.SQLException;
import java.sql.*;

public class LoginChecker {
    protected boolean checkLoginInformation(String userName, String userPassword, Connection conn) {
        boolean validInformation = false;

        try {
            String query = "Select * from login_information where " +
                    "UserName = '" + userName + "' and userpassword = '" + userPassword + "';";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            if (result.next()) {
                validInformation = true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return validInformation;
    }


    protected String[] getInformation (String userName, Connection conn) {
        String[] results = new String[3];

        try {
            String query = "Select employee_id, firstName, lastName from login_information where " +
                    "username = '" + userName + "';";
            Statement getUserInfo = conn.createStatement();

            ResultSet resultSet = getUserInfo.executeQuery(query);
            while (resultSet.next()) {
                results[0] = resultSet.getString(1);
                results[1] = resultSet.getString(2);
                results[2] = resultSet.getString(3);
            }
            /*
            for (String i : results) {
                System.out.println(i);
            } */
            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //System.out.println(results[0] + " " + results[1] + " " + results[2]);


    }
}
