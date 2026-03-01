package com.ren.CatQuest.user.service;


import com.ren.CatQuest.user.entity.User;
import com.ren.CatQuest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public User createUser(String email, String phone, String rawPassword, String fullName) {

        email = normalize(email);
        phone = normalize(phone);

        if ((email == null || email.isBlank()) && (phone == null || phone.isBlank())) {
            throw new IllegalArgumentException("Either email or phone must be provided");
        }

        if (email != null && userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already registered");
        }

        if (phone != null && userRepository.existsByPhone(phone)) {
            throw new IllegalStateException("Phone already registered");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = User.create(email, phone, encodedPassword, fullName);

        roleService.assignDefaultRole(user);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    @Transactional(readOnly = true)
    public User getByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}