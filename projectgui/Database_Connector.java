package projectgui;

import java.sql.*;


public class Database_Connector {
    Connection connection = null;

    protected Connection establishConnection () {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/employee_information",
                    "luckyseven", "L}1h0#40iF40xEjYFB");

            Statement statement = connection.createStatement();

            //connection.close();
        }
        catch (Exception exception) {
            System.out.println(exception);
        } /*
        finally {
            if(connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
        } */

        return connection;
    }

    protected void closeConnection() throws SQLException {
        connection.close();
    }



}
