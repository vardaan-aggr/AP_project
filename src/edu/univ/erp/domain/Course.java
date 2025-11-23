package edu.univ.erp.domain;

public class Course {
    private String course_code;
    private String title;
    private String section;
    private String credits;

    // public Course(String course_code, String title, String section, String credits) {
    //     this.course_code = course_code;
    //     this.title = title;
    //     this.section = section;
    //     this.credits = credits;
    // }

    public String getCourseCode() { return course_code; }
    public String getTitle() { return title; }
    public String getSection() { return section; }
    public String getCredits() { return credits; }

    public void setCourseCode(String courseCode) { this.course_code = courseCode; }
    public void setTitle(String title) { this.title = title; }
    public void setSection(String section) { this.section = section; }
    public void setCredits(String credits) { this.credits = credits; }
}