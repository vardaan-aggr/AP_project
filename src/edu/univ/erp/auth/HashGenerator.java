package edu.univ.erp.auth; 

import org.mindrot.jbcrypt.BCrypt;

public class HashGenerator {
    
    // Registration
    public static String makeHash(String in_pass) {
        return BCrypt.hashpw(in_pass, BCrypt.gensalt());
    } 

    // New Login 
    public static boolean verifyHash(String inputPass, String storedHash) {
        if (storedHash == null) {
            return false;
        }
        return BCrypt.checkpw(inputPass, storedHash);
    }
}