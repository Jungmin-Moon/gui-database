package projectgui;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Employee_Info {


    protected String singleEmployee(int employeeID, Connection conn) {
        String result = "";

        int id = 0;
        String lastName = "", firstName = "", email = "", department = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date licenseDate = null, cprAedDate = null;
        String formattedDateLicense = null;
        String formattedDateCPRAED = null;

        try {
            String getInfo = "Select * from work_information where employee_id = '" + employeeID + "';";
            Statement stmt = conn.createStatement();

            ResultSet set = stmt.executeQuery(getInfo);

            while (set.next()) {
                id = set.getInt(1);
                lastName = set.getString(2);
                firstName = set.getString(3);
                licenseDate = set.getDate(4);
                cprAedDate = set.getDate(5);
                email = set.getString(6);
                department = set.getString(7);
            }

            //System.out.println(id + " " + lastName + " " + firstName + " " + licenseDate + " " + cprAedDate + " " + email + " " + department);

            result = "Employee ID: " + id + "\n" +
                    "First Name: " + firstName + "\n" +
                    "Last Name: " + lastName + "\n";

            if (licenseDate == null) {
                result = result + "License Expiration Date: NULL" + "\n";
            } else {
                formattedDateLicense = dateFormat.format(licenseDate);
                result = result + "License Expiration Date: " + formattedDateLicense + "\n";
            }

            if (cprAedDate == null) {
                result = result + "CPR/AED Expiration Date: NULL" + "\n";
            } else {
                formattedDateCPRAED = dateFormat.format(cprAedDate);
                result = result + "CPR/AED Expiration Date: " + formattedDateCPRAED + "\n";
            }

            if (email == null) {
                result = result + "Email: NULL" + "\n";
            } else {
                result = result + "Email: " + email + "\n";
            }

            if (department == null) {
                result = result + "Department: NULL" + "\n";
            } else {
                result = result + "Department: " + department + "\n";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return result;
    }
}
