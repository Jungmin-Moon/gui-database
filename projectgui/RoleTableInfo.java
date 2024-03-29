package projectgui;

import javafx.beans.property.SimpleStringProperty;

public class RoleTableInfo {
    SimpleStringProperty firstName;
    SimpleStringProperty lastName;
    SimpleStringProperty id;
    SimpleStringProperty role;

    RoleTableInfo(String id, String firstName, String lastName, String role) {
        this.id = new SimpleStringProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.role = new SimpleStringProperty(role);
    }

    public String getId() {
        return id.get();
    }
    public void setId(String id) {
        this.id.set(id);
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

    public String getRole() {
        return role.get();
    }
    public void setRole(String role) {
        this.role.set(role);
    }
}
