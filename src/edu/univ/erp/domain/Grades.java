package edu.univ.erp.domain;

public class Grades {
    private int roll_no;
    private String course_code;
    private String section;
    private String grade;

    public Grades(int roll_no, String course_code, String section, String grade) {
        this.roll_no = roll_no;
        this.course_code = course_code;
        this.section = section;
        this.grade = grade;
    }

    public int getRollNo() { return roll_no; }
    public String getCourseCode() { return course_code; }
    public String getSection() { return section; }
    public String getGrade() { return grade; }
}