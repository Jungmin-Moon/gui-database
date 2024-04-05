package projectgui;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LoginTableInfo {
    SimpleIntegerProperty empID;
    SimpleStringProperty firstName;
    SimpleStringProperty lastName;
    SimpleStringProperty userName;
    SimpleStringProperty password;

    LoginTableInfo(int employeeID, String lastName, String firstName, String userName, String pass) {
        empID = new SimpleIntegerProperty(employeeID);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.userName = new SimpleStringProperty(userName);
        password = new SimpleStringProperty(pass);
    }

    public Property<Number> empIDProperty() {
        return empID;
    }

    public void setEmpID (int id){
        empID.set(id);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getUserName() {
        return userName.get();
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

}
