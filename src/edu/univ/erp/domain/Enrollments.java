package edu.univ.erp.domain;

public class Enrollments {
    private int roll_no;
    private String course_code;
    private String section;
    private String status;

    public Enrollments(int roll_no, String course_code, String section, String status) {
        this.roll_no = roll_no;
        this.course_code = course_code;
        this.section = section;
        this.status = status;
    }

    public int getRollNo() { return roll_no; }
    public String getCourseCode() { return course_code; }
    public String getSection() { return section; }
    public String getStatus() { return status; }
}