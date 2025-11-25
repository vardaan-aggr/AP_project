package edu.univ.erp.domain;

public class Admin {
    private String roll_no;
    private String username;

    public Admin(String roll_no, String username) {
        this.roll_no = roll_no;
        this.username = username;
    }

    public String getRollNo() { return roll_no; }
    public String getUsername() { return username; }
    
    public void setRollNo(String roll_no) { this.roll_no = roll_no; }
    public void setUsername(String username) { this.username = username; }
}