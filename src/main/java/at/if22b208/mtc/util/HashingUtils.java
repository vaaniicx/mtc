package at.if22b208.mtc.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import at.if22b208.mtc.exception.HashingException;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@code HashingUtils} class provides utility methods for hashing and generating salts.
 * It uses the PBKDF2 algorithm with HMAC SHA-256 for password hashing.
 * <p>
 * Note: This class is a simplified example for educational purposes. In a production environment,
 * it's recommended to use a secure hashing algorithm like BCrypt and store the salt securely (and randomize it).
 * </p>
 * <p>
 * Usage:
 * </p>
 * <pre>
 * {@code
 * try {
 *     String hashedPassword = HashingUtils.hash("password", "user_salt");
 *     String salt = HashingUtils.generateSalt("username");
 * } catch (HashingException e) {
 *     // Handle the exception
 * }
 * }
 * </pre>
 */
@Slf4j
public class HashingUtils {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    /**
     * Hashes the provided plain text using PBKDF2 with HMAC SHA-256.
     *
     * @param plain The plain text to be hashed.
     * @param salt  The salt used for hashing.
     * @return The hashed value as a Base64-encoded string.
     * @throws HashingException If an error occurs during the hashing process.
     */
    public static String hash(String plain, String salt)
            throws HashingException {
        try {
            KeySpec spec = new PBEKeySpec(plain.toCharArray(), salt.getBytes(), 65536, 128);
            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = f.generateSecret(spec).getEncoded();

            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // Log or handle the exception as needed
            throw new HashingException("Error during hashing process", e);
        }
    }

    /**
     * Generates a salt for password hashing based on the provided username.
     *
     * <p>This method is intended to generate a unique salt for each user without storing it in the database.
     * It concatenates the username with a constant string to create the salt.</p>
     *
     * @param username The username for which the salt is generated.
     * @return A salt string for password hashing.
     */
    public static String generateSalt(String username) {
        // Normally, I would go with BCrypt, ...
        // To avoid storing the salt in the database, extraction of salt from hash etc.
        return username + "_SALT_CONSTANT";
    }
}
