package edu.univ.erp.util;

import org.mindrot.jbcrypt.BCrypt;

public class HashGenerator {
    public static String makeHash(String in_pass) {
        return BCrypt.hashpw(in_pass, BCrypt.gensalt());
    } 
    //When a method is non-static (instance method), it can only be called after creating an object

    // public static void main(String[] args) {
    //     String password = "mdo1";
    //     String hashpass = BCrypt.hashpw(password, BCrypt.gensalt());
    //     System.out.println(hashpass);
    // }
}
