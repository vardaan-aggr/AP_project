package edu.univ.erp.domain;

public class Enrollments {
    private String roll_no;
    private String course_code;
    private String section;
    private String status;

    // public Enrollments(String roll_no, String course_code, String section, String status) {
    //     this.roll_no = roll_no;
    //     this.course_code = course_code;
    //     this.section = section;
    //     this.status = status;
    // }

    public String getRollNo() { return roll_no; }
    public String getCourseCode() { return course_code; }
    public String getSection() { return section; }
    public String getStatus() { return status; }

    public void setRollNo(String rollNo) { this.roll_no = rollNo; }
    public void setCourseCode(String courseCode) { this.course_code = courseCode; }
    public void setSection(String section) { this.section = section; }
    public void setStatus(String status) { this.status = status; }
}