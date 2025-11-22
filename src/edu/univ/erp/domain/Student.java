package edu.univ.erp.domain;

public class Student {
    private String roll_no;
    private String username;
    private String program;
    private String year;

    public Student(String roll_no, String username, String program, String year) {
        this.roll_no = roll_no;
        this.username = username;
        this.program = program;
        this.year = year;
    }

    public String getUsername() { return username; }
    public String getRollNo() { return roll_no; }
    public String getProgram() { return program; }
    public String getYear() { return year; }

    public void setUsername(String username) { this.username = username; }
    public void setRollNo(String roll_no) { this.roll_no = roll_no; }
    public void setProgram(String program) { this.program = program; }
    public void setYear(String year) { this.year = year; }
}