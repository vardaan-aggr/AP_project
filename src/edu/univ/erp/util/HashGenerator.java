package edu.univ.erp.util;

// You need to add the jbcrypt.jar file to your project
import org.mindrot.jbcrypt.BCrypt;

public class HashGenerator {
    public static void main(String[] args) {
        String password = "test123";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(hashedPassword);
        // Example output: $2a$10$N9qo8uLOickgx2ZMR9iM0O9u...
    }
}
