package projectgui;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Employee_Info {


    protected String displaySingleEmployee(int employeeID, Connection conn) {
        String result = "";

        int id = 0;
        String lastName = "", firstName = "", email = "", department = "";
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
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
                //formattedDateLicense = dateFormat.format(licenseDate);
                result = result + "License Expiration Date: " + licenseDate + "\n";
            }

            if (cprAedDate == null) {
                result = result + "CPR/AED Expiration Date: NULL" + "\n";
            } else {
                //formattedDateCPRAED = dateFormat.format(cprAedDate);
                result = result + "CPR/AED Expiration Date: " + cprAedDate + "\n";
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

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            String query = "select lastname, firstname, employee_email, license, cpr_aed, department from work_information where employee_id = '" +
                            empID + "';";

            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery(query);

            while (set.next()) {
                info[0] = set.getString(1);
                info[1] = set.getString(2);

                if (set.getString(3) == null)
                    info[2] = "BLANK";
                else
                    info[2] = set.getString(3);
                if (set.getDate(4) == null)
                    info[3] = "BLANK";
                else
                    info[3] = String.valueOf(set.getDate(4));

                if (set.getDate(5) == null)
                    info[4] = "BLANK";
                else
                    info[4] = String.valueOf(set.getDate(5));

                if (set.getString(6) == null)
                    info[5] = "BLANK";
                else
                    info[5] = set.getString(6);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return info;
    }

    protected void updateEmployee(int id, String[] information, Connection conn) {
        /*
        last name, firstname, email, license, cpraed, department
         */
        //multiple update utility methods, so at most 6 extra methods
        if (!information[0].isEmpty()) {
            updateLastName(information[0], conn, id);
        }

        if (!information[1].isEmpty()) {
            updateFirstName(information[1], conn, id);
        }

        if (!information[2].isEmpty()) {
            updateEmail(information[2], conn, id);
        }

        if(!information[3].equalsIgnoreCase("blank")) {
            updateLicense(information[3], conn, id);
        }

        if(!information[4].equalsIgnoreCase("blank")) {
            updateCPRAED(information[4], conn, id);
        }

        if (!information[5].isEmpty()) {
            updateDepartment(information[5], conn, id);
        }
    }

    protected void updateLastName(String lastName, Connection conn, int id) {
        try {
            String query = "update work_information set lastname = '" + lastName + "' where employee_id = '" + id + "';";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void updateFirstName(String firstName, Connection conn, int id) {
        try {
            String query = "update work_information set firstname = '" + firstName + "' where employee_id = '" + id + "';";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void updateEmail(String email, Connection conn, int id) {
        try {
            String query = "update work_information set employee_email = '" + email + "' where employee_id = '" + id + "';";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //each date from date picker is in MM/DD/YYYY format
    protected void updateLicense(String license, Connection conn, int id) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(license, format);
        try {
            String query = "update work_information set license = '" + date + "' where employee_id = '" + id + "';";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    protected void updateCPRAED(String cprAed, Connection conn, int id) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(cprAed, format);
        try {
            String query = "update work_information set cpr_aed = '" + date + "' where employee_id = '" + id + "';";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void updateDepartment(String department, Connection conn, int id) {
        try {
            String query = "update work_information set department = '" + department + "' where employee_id = '" + id + "';";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
