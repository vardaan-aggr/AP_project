package edu.univ.erp.data;  

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import edu.univ.erp.domain.Instructor;
import edu.univ.erp.domain.Student;

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
                        Select roll_no, username FROM auth_table WHERE role = ?;
                    """)) {
                statement.setString(1, "student");
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        Student s = new Student();
                        s.setRollNo(resultSet.getString("roll_no"));
                        s.setRollNo(resultSet.getString("username"));
                        stdList.add(s);
                    }
                    if (empty) {
                        JOptionPane.showMessageDialog(null, "Error: No student with given inputs exists.", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("\t (no student in auth database with given input)");
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: Couldn't fetch student details: " + e, "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            throw new SQLException(e);
        }
        return stdList;
    }

    public static ArrayList<Instructor> searchInstructorAuth() throws SQLException {
        ArrayList<Instructor> insList = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getAuthConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select roll_no, username FROM auth_table WHERE role = ?;
                    """)) {
                statement.setString(1, "instructor");
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        Instructor i = new Instructor();
                        i.setRollNo(resultSet.getString("roll_no"));
                        i.setUsername(resultSet.getString("username"));
                    }
                    if (empty) {
                        JOptionPane.showMessageDialog(null, "Error: No instructor with given inputs exists.", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("\t (no instructor in auth database with given input)");
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: Couldn't fetch instructor details: " + e, "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            throw new SQLException(e);
        }
        return insList;
    }

    public static ArrayList<String[]> searchAuthAuth() throws SQLException {
        ArrayList<String[]> data = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getAuthConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select roll_no, username FROM auth_table WHERE role = ?;
                    """)) {
                statement.setString(1, "admin");
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String rollNo = resultSet.getString("roll_no");
                        String username = resultSet.getString("username");
                        data.add(new String[]{username, rollNo});
                    }
                    if (empty) {
                        JOptionPane.showMessageDialog(null, "Error: No admin in database", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("\t (No admin in database)");
                        return null;
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: Couldn't fetch instructor details: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            throw new SQLException(ex);
        }
        return data;
    }
}