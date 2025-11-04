package edu.univ.erp.data;

import java.sql.*;


public class databaseConnector {
    public static void main (String[] arg) {
        try {
            // DriverManager class locate and load MariaDB Connector/J and makes connection 
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/DB?user=root&password=myPassword");        }
    }
}