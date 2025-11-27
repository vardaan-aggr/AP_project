package edu.univ.erp.domain;

public class Grades {
    private String roll_no;
    private String course_code;
    private String section;
    private String grade;
    private String quizMarks;
    private String midsemMarks;
    private String endsemMarks;

    // public Grades(String roll_no, String course_code, String section, String grade) {
    //     this.roll_no = roll_no;
    //     this.course_code = course_code;
    //     this.section = section;
    //     this.grade = grade;
    // }

    public String getRollNo() { return roll_no; }
    public String getCourseCode() { return course_code; }
    public String getSection() { return section; }
    public String getGrade() { return grade; }
    public String getQuizMarks() { return quizMarks; }
    public String getMidsemMarks() { return midsemMarks; }
    public String getEndsemMarks() { return endsemMarks; }

    public void setRollNo(String rollNo) { this.roll_no = rollNo; }
    public void setCourseCode(String courseCode) { this.course_code = courseCode; }
    public void setSection(String section) { this.section = section; }
    public void setGrade(String grade) { this.grade = grade; }
    public void setQuizMarks(String quizMarks) { this.quizMarks = quizMarks; }
    public void setMisemMarks(String midsemMarks) { this.midsemMarks = midsemMarks; }
    public void setEndsemMarks(String endsemMarks) { this.endsemMarks = endsemMarks; }
}