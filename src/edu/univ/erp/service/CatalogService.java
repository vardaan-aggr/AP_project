package edu.univ.erp.service;

import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.Sections;

import java.sql.SQLException;
import java.util.ArrayList;

public class CatalogService {

    public String[][] getCatalogTableData() throws SQLException {
        ArrayList<Course> courseList = ErpCommandRunner.getAllCourses();

        if (courseList == null || courseList.isEmpty()) {
            return new String[0][0];
        }

        String[][] strArr = new String[courseList.size()][4];
        for (int i = 0; i < courseList.size(); i++) {
            Course c = courseList.get(i);
            strArr[i][0] = c.getCourseCode();
            strArr[i][1] = c.getTitle();
            strArr[i][2] = c.getSection();
            strArr[i][3] = String.valueOf(c.getCredits()); 
        }
        return strArr;
    }

    public String[][] getSectionsTableData() throws SQLException {
        ArrayList<Sections> sectionList = ErpCommandRunner.getAllSections();

        if (sectionList == null || sectionList.isEmpty()) {
            return new String[0][0];
        }

        String[][] strArr = new String[sectionList.size()][8];
        for (int i = 0; i < sectionList.size(); i++) {
            Sections s = sectionList.get(i);
            strArr[i][0] = s.getCourseCode();
            strArr[i][1] = s.getSection();
            strArr[i][2] = s.getRollNo();
            strArr[i][3] = s.getDayTime();
            strArr[i][4] = s.getRoom();
            strArr[i][5] = s.getCapacity(); 
            strArr[i][6] = s.getSemester();
            strArr[i][7] = s.getYear();     
        }
        return strArr;
    }
}