package test.java.edu.univ.erp.auth; 

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import edu.univ.erp.util.HashGenerator;
import org.mindrot.jbcrypt.BCrypt; 

public class HashGeneratorTest {

    // NO main() method here! The JUnit runner will find this method automatically.

    @Test
    void testHashIsGeneratedAndSecure() {
        String password = "testPassword123";
        // Ensure HashGenerator.makeHash() uses BCrypt.gensalt() internally
        String hash = HashGenerator.makeHash(password); 
        
        assertNotNull(hash, "The generated hash should not be null.");
        assertNotEquals(password, hash, "The hash should not match the original plaintext.");
        
        // BCrypt hashes start with $2a$, $2b$, or $2y$
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$"),
                   "Hash should be a valid BCrypt string.");
    }

    @Test
    void testPasswordVerificationSuccess() {
        String password = "securePassword456";
        String hash = HashGenerator.makeHash(password);

        // This verifies that the password works against the stored hash
        assertTrue(BCrypt.checkpw(password, hash), 
                   "Verification should succeed for the correct password.");
    }

    @Test
    void testPasswordVerificationFailure() {
        String correctPassword = "correctPassword789";
        String wrongPassword = "incorrectPassword";
        String hash = HashGenerator.makeHash(correctPassword);

        assertFalse(BCrypt.checkpw(wrongPassword, hash), 
                    "Verification should fail for an incorrect password.");
    }
}