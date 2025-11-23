package edu.univ.erp.domain;

public class Sections {
    private String course_code;
    private String section;
    private String roll_no;
    private String day_time;
    private String room;
    private String capacity;
    private String semester;
    private String year;

    // public Sections(String course_code, String section, String roll_no, String day_time, String room, String capacity, String semester, String year) {
    //     this.course_code = course_code;
    //     this.section = section;
    //     this.roll_no = roll_no;
    //     this.day_time = day_time;
    //     this.room = room;
    //     this.capacity = capacity;
    //     this.semester = semester;
    //     this.year = year;
    // }

    public String getCourseCode() { return course_code; }
    public String getSection() { return section; }
    public String getRollNo() { return roll_no; }
    public String getDayTime() { return day_time; }
    public String getRoom() { return room; }
    public String getCapacity() { return capacity; }
    public String getSemester() { return semester; }
    public String getYear() { return year; }
    
    public void setCourseCode(String courseCode) { this.course_code = courseCode; }
    public void setSection(String section) { this.section = section; }
    public void setRollNo(String rollNo) { this.roll_no = rollNo; }
    public void setDayTime(String dayTime) { this.day_time = dayTime; }
    public void setRoom(String room) { this.room = room; }
    public void setCapacity(String capacity) { this.capacity = capacity; }
    public void setSemester(String semester) { this.semester = semester; }
    public void setYear(String year) { this.year = year; }
}