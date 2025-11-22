package edu.univ.erp.domain;

public class Course {
    private String course_code;
    private String title;
    private String section;
    private int credits;

    public Course(String course_code, String title, String section, int credits) {
        this.course_code = course_code;
        this.title = title;
        this.section = section;
        this.credits = credits;
    }

    public String getCourseCode() { return course_code; }
    public String getTitle() { return title; }
    public String getSection() { return section; }
    public int getCredits() { return credits; }
}