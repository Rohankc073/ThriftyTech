
package com.thriftytech.ecommerce.Service;

import com.thriftytech.ecommerce.Entity.User;
import com.thriftytech.ecommerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        // Check if the username is already taken
        if (userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            throw new IllegalArgumentException("Username or email is already taken");
        }
        return userRepository.save(user);
    }

    public User loginUser(String usernameOrEmail, String password) {
        System.out.println("Attempting login with usernameOrEmail: " + usernameOrEmail);

        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Simple password check (without encryption)
            if (password.equals(user.getPassword())) {
                System.out.println("Login successful for user: " + user.getUsername());
                return user;
            }
        }

        // Either user not found or password doesn't match
        System.out.println("Invalid username or password");
        throw new IllegalArgumentException("Invalid username or password");
    }

    public Optional<User> getUsersById(long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User patchUser(Long userId, Map<String, Object> updates) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();

            // Exclude username and email from being updated
            if ("username".equals(field) || "email".equals(field)) {
                throw new IllegalArgumentException(field + " cannot be updated");
            }

            // Set the field value using reflection
            try {
                Field userField = User.class.getDeclaredField(field);
                userField.setAccessible(true);
                userField.set(user, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Invalid field for update: " + field);
            }
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userRepository.delete(existingUser);
    }
}
