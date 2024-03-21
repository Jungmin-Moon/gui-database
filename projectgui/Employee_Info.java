package projectgui;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Employee_Info {


    protected String displaySingleEmployee(int employeeID, Connection conn) {
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


    protected String[] returnEmployeeInformation(int empID, Connection conn) {
        String[] info = new String[6];

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

        try {
            String query = "select lastname, firstname, license, cpr_aed, employee_email, department from work_information where employee_id = '" +
                            empID + "';";

            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery(query);

            while (set.next()) {
                info[0] = set.getString(1);
                info[1] = set.getString(2);

                if (set.getDate(3) == null)
                    info[2] = "BLANK";
                else
                    info[2] = dateFormat.format(set.getDate(3));
                if (set.getDate(4) == null)
                    info[3] = "BLANK";
                else
                    info[3] = dateFormat.format(set.getDate(4));

                if (set.getString(5) == null)
                    info[4] = "BLANK";
                else
                    info[4] = set.getString(5);

                if (set.getString(5) == null)
                    info[5] = "BLANK";
                else
                    info[5] = set.getString(6);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return info;
    }
}
