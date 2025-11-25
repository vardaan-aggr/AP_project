package edu.univ.erp.data;  

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.univ.erp.domain.Instructor;
import edu.univ.erp.domain.Student;
import edu.univ.erp.domain.Admin;

public class AuthCommandRunner {
    public static class loginResult {
        public String hashPass;
        public String role;
        public String rollNo;
        public String result;
    }
    public static loginResult fetchUser (String username_in) throws SQLException {
        loginResult lr = new loginResult();
        try (Connection connection = DatabaseConnector.getAuthConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select hash_password, role, roll_no FROM auth_table WHERE username = ?; 
                    """)) {
                statement.setString(1, username_in);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if(resultSet.next()) {
                        lr.hashPass = resultSet.getString("hash_password");
                        lr.role = resultSet.getString("role");
                        lr.rollNo = resultSet.getString("roll_no");
                        lr.result = "true";
                        return lr;    
                    }
                }
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
        return null;
    }

    public static int registerUserAuth(String username, String role, String hashPass) throws SQLException {
        String query = "INSERT INTO auth_table (username, role, hash_password) VALUES (?, ?, ?)";
        
        try (Connection connection = DatabaseConnector.getAuthConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, username);
            statement.setString(2, role);
            statement.setString(3, hashPass);
            
            int rows = statement.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        }
        return -1; 
    }

    public static int UserAuth(String username, String role, String hashPass) throws SQLException {
        String query = "INSERT INTO auth_table (username, role, hash_password) VALUES (?, ?, ?)";
        
        try (Connection connection = DatabaseConnector.getAuthConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, username);
            statement.setString(2, role); 
            statement.setString(3, hashPass);
            
            int rows = statement.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        }
        return -1; 
    }

    public static int registerAuth(String username, String hashPass) throws SQLException {
        int rowsAffected = -1;
        try (Connection connection = DatabaseConnector.getAuthConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        INSERT INTO auth_table (username, role, hash_password) VALUES
                            (?, ?, ?)
                    """)) {
                statement.setString(1, username);
                statement.setString(2, "admin");
                statement.setString(3, hashPass);
                rowsAffected = statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
        return rowsAffected;
    }

    public static ArrayList<Student> searchStudentAuth() throws SQLException {
        ArrayList<Student> stdList = new ArrayList<>();
        
        try (Connection connection = DatabaseConnector.getAuthConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        SELECT roll_no, username FROM auth_table WHERE role = ?;
                    """)) {
                statement.setString(1, "student");
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Student s = new Student();
                        s.setRollNo(resultSet.getString("roll_no"));
                        s.setUsername(resultSet.getString("username")); 
                        s.setProgram("N/A");
                        s.setYear("N/A");
                        
                        stdList.add(s);
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return stdList;
    }

    public static ArrayList<Instructor> searchInstructorAuth() throws SQLException {
        ArrayList<Instructor> insList = new ArrayList<>();
        
        try (Connection connection = DatabaseConnector.getAuthConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        SELECT roll_no, username FROM auth_table WHERE role = ?;
                    """)) {
                statement.setString(1, "instructor");
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Instructor i = new Instructor();
                        i.setRollNo(resultSet.getString("roll_no"));
                        i.setUsername(resultSet.getString("username"));
                        i.setDepartment("N/A"); 
                        
                        insList.add(i); 
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return insList;
    }

    public static ArrayList<Admin> searchAuthAuth() throws SQLException {
        ArrayList<Admin> adminList = new ArrayList<>();
        
        try (Connection connection = DatabaseConnector.getAuthConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        SELECT roll_no, username FROM auth_table WHERE role = ?;
                    """)) {
                statement.setString(1, "admin");
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Admin a = new Admin(
                            resultSet.getString("roll_no"),
                            resultSet.getString("username")
                        );
                        adminList.add(a);
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return adminList;
    }

    public static void updateLastLogin(String rollNo) throws SQLException {
        String query = "UPDATE auth_table SET last_login = NOW() WHERE roll_no = ?";
        
        try (Connection connection = DatabaseConnector.getAuthConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, rollNo);
            statement.executeUpdate();
        }
    }
}