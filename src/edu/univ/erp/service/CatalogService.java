package edu.univ.erp.service;

import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.Sections;

import java.sql.SQLException;
import java.util.ArrayList;

public class CatalogService {

    public ArrayList<Course> getAllCourses() throws SQLException {
        return ErpCommandRunner.getAllCoursesHelper();
    }

    public ArrayList<Sections> getAllSections() throws SQLException {
        return ErpCommandRunner.getAllSectionsHelper();
    }
    
}