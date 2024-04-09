package projectgui;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.util.Date;

public class WorkTableInfo {
    SimpleIntegerProperty empID;
    SimpleStringProperty lastName;
    SimpleStringProperty firstName;
    SimpleObjectProperty<Date> userLicense;
    SimpleObjectProperty<Date> userCPRAED;
    SimpleStringProperty userEmail;
    SimpleStringProperty userDepartment;

    WorkTableInfo(int empID, String lastName, String firstName, Date license, Date cprAed, String email, String department) {
        this.empID = new SimpleIntegerProperty(empID);
        this.lastName = new SimpleStringProperty(lastName);
        this.firstName = new SimpleStringProperty(firstName);
        userLicense = new SimpleObjectProperty<>(license);
        userCPRAED = new SimpleObjectProperty<>(cprAed);
        userEmail = new SimpleStringProperty(email);
        userDepartment = new SimpleStringProperty(department);
    }

    public Property<Number> empIDProperty() {
        return empID;
    }

    public void setEmpId(int id) {
        empID.set(id);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public Property<Date> userLicenseProperty() {
        return userLicense;
    }

    public void setLicense(Date license) {
        userLicense.set(license);
    }

    public Property<Date> userCPRAEDProperty() {
        return userCPRAED;
    }

    public void setCprAed(Date cprAed) {
        userCPRAED.set(cprAed);
    }

    public Property<String> userEmailProperty() {
        return userEmail;
    }

    public void setEmail(String email) {
        userEmail.set(email);
    }

    public Property<String> userDepartmentProperty() {
        return userDepartment;
    }

    public void setDepartment(String department) {
        userDepartment.set(department);
    }
}
