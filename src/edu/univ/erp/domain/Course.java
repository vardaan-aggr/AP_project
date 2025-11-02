package edu.univ.erp.domain;

// This class is a "POJO" (Plain Old Java Object).
// Its only job is to hold data.
public class Course {
    private String code;
    private String title;
    private int credits;
    
    // You can add more fields from your tables, like capacity or instructor name

    // Constructor
    public Course(String code, String title, int credits) {
        this.code = code;
        this.title = title;
        this.credits = credits;
    }

    // "Getter" methods so the UI can read the data
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
}