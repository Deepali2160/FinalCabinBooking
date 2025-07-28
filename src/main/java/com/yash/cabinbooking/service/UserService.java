package com.yash.cabinbooking.service;

import com.yash.cabinbooking.model.User;

public interface UserService {
    boolean registerUser(User user);
    User loginUser(String email, String password);
    boolean isEmailExists(String email);
    User getUserById(int userId);
    public boolean updateUser(User user);
    boolean changePassword(int userId, String oldPassword, String newPassword);
}