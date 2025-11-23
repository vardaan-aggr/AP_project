package edu.univ.erp.service;

import java.sql.SQLException;
import java.util.ArrayList;

import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.domain.Grades;
import edu.univ.erp.util.modeOps;

public class StudentService {
    public int dropCourse (String rollNo, String courseCode) {
        int rowsDeleted = ErpCommandRunner.studentDropCourseHelper(rollNo, courseCode);
        return rowsDeleted;
    }

    public String[][] gradeData (String rollNo) throws SQLException {
        try {
            String data[][] = ErpCommandRunner.studentGradeHelper(rollNo);
            return data; 
        }  catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public int registerCourse (String rollNo, String courseCode, String section) {
        return ErpCommandRunner.studentRegisterHelper(rollNo, courseCode, section, "enrolled");
    }

    public boolean isSystemActive() {
        if ("true".equals(modeOps.getMaintainMode())) {
            return false; 
        }
        return true; 
    }

    public String[][] timetable (String rollNo) throws SQLException {
        try {
            String data[][] = ErpCommandRunner.studentTimeTableHelper(rollNo);
            return data; 
        }  catch (SQLException e) {
            throw new SQLException(e);
        }
    }
    public ArrayList<Grades> getTranscript(String rollNo) throws SQLException {
        return ErpCommandRunner.studentTranscriptHelper(rollNo);
    }
}
