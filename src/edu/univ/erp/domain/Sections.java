package edu.univ.erp.domain;

public class Sections {
    private String course_code;
    private String section;
    private String roll_no;
    private String day_time;
    private String room;
    private int capacity;
    private String semester;
    private int year;

    public Sections(String course_code, String section, String roll_no, String day_time, String room, int capacity, String semester, int year) {
        this.course_code = course_code;
        this.section = section;
        this.roll_no = roll_no;
        this.day_time = day_time;
        this.room = room;
        this.capacity = capacity;
        this.semester = semester;
        this.year = year;
    }

    public String getCourseCode() { return course_code; }
    public String getSection() { return section; }
    public String getRollNo() { return roll_no; }
    public String getDayTime() { return day_time; }
    public String getRoom() { return room; }
    public int getCapacity() { return capacity; }
    public String getSemester() { return semester; }
    public int getYear() { return year; }
}