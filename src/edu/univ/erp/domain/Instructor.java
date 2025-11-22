package edu.univ.erp.domain;

public class Instructor {
    private String roll_no;
    private String username;
    private String department;

    public Instructor(String roll_no, String username, String department) {
        this.roll_no = roll_no;
        this.username = username;
        this.department = department;
    }

    public String getUsername() { return username; }
    public String getRollNo() { return roll_no; }
    public String getDepartment() { return department; }

    public void setUsername(String username) { this.username = username; }
    public void setRollNo(String roll_no) { this.roll_no = roll_no; }
    public void setDepartment(String department) { this.department = department; }
}