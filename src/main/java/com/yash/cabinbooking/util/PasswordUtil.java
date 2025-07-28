package com.yash.cabinbooking.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    private static final int BCRYPT_ROUNDS = 12; // Higher is more secure but slower

    // Private constructor to prevent instantiation
    private PasswordUtil() {}

    /**
     * Hashes a plain text password
     * @param plainPassword the password to hash
     * @return the hashed password
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    /**
     * Verifies a plain text password against a stored hash
     * @param plainPassword the password to verify
     * @param storedHash the stored hash to compare against
     * @return true if passwords match, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            return false;
        }
        if (storedHash == null || !storedHash.startsWith("$2a$")) {
            return false;
        }

        try {
            return BCrypt.checkpw(plainPassword, storedHash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Checks if a password needs rehashing (if rounds have changed)
     * @param storedHash the stored hash to check
     * @return true if password needs rehashing
     */
    public static boolean needsRehash(String storedHash) {
        if (storedHash == null || !storedHash.startsWith("$2a$")) {
            return true;
        }

        // Extract rounds from hash (format is $2a$rounds$salt+hash)
        int rounds = Integer.parseInt(storedHash.substring(4, 6));
        return rounds != BCRYPT_ROUNDS;
    }
}