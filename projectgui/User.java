package projectgui;

public class User {
    private String firstName;
    private String lastName;
    private int empID;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return lastName + ", " + firstName;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }
    public int getEmpID() {
        return empID;
    }

    public void clearInformation() {
        firstName = null;
        lastName = null;
        empID = 0;
    }

}
