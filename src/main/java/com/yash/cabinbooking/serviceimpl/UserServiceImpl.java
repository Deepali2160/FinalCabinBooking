package com.yash.cabinbooking.serviceimpl;

import com.yash.cabinbooking.dao.UserDao;
import com.yash.cabinbooking.daoimpl.UserDaoImpl;
import com.yash.cabinbooking.model.User;
import com.yash.cabinbooking.service.UserService;
import com.yash.cabinbooking.util.PasswordUtil;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    @Override
    public boolean registerUser(User user) {
        if (user == null || userDao.isEmailExists(user.getEmail())) {
            return false;
        }

        // Validate password strength before registration
        if (!isPasswordStrong(user.getPassword())) {
            return false;
        }


        // Set default role if not specified
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("customer");
        }

        return userDao.registerUser(user);
    }

    @Override
    public User loginUser(String email, String password) {
        if (email == null || password == null) {
            return null;
        }
        return userDao.loginUser(email, password);
    }

    @Override
    public boolean isEmailExists(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return userDao.isEmailExists(email);
    }

    @Override
    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public boolean updateUser(User user) {
        if (user == null || user.getId() <= 0) {
            return false;
        }
        return userDao.updateUser(user);
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);
        if (user == null || !PasswordUtil.verifyPassword(oldPassword, user.getPassword())) {
            return false;
        }

        if (!isPasswordStrong(newPassword)) {
            return false;
        }

        // Hash the new password before storing
        user.setPassword(PasswordUtil.hashPassword(newPassword));
        return userDao.updateUser(user);
    }

    private boolean isPasswordStrong(String password) {
        // Minimum 8 characters, at least one letter, one number and one special character
        return password != null &&
                password.length() >= 8 &&
                password.matches(".*[A-Za-z].*") && // At least one letter
                password.matches(".*\\d.*") &&      // At least one digit
                password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"); // At least one special char
    }
}