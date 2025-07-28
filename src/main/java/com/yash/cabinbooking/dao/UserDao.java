package com.yash.cabinbooking.dao;

import com.yash.cabinbooking.model.User;

public interface UserDao {
    boolean registerUser(User user);
    User loginUser(String email, String password);
    boolean changeUserPassword(int userId, String newPassword);
    boolean isEmailExists(String email);
    User getUserById(int userId);    // Add this method
    boolean updateUser(User user);
    boolean deleteUser(int userId);// Add this method
}