package com.ren.catquest.user.service;


import com.ren.catquest.auth.dto.RegisterRequest;
import com.ren.catquest.recruiter.service.RecruiterService;
import com.ren.catquest.user.entity.User;
import com.ren.catquest.user.repository.UserRepository;
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
    private final RecruiterService recruiterService;

    public User createUser(RegisterRequest req) {

        String email = normalize(req.email());
        String phone = normalize(req.phone());

        if ((email == null || email.isBlank()) && (phone == null || phone.isBlank())) {
            throw new IllegalArgumentException("Either email or phone must be provided");
        }

        if (email != null && userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already registered");
        }

        if (phone != null && userRepository.existsByPhone(phone)) {
            throw new IllegalStateException("Phone already registered");
        }

        String encodedPassword = passwordEncoder.encode(req.password());

        User user = User.create(email, phone, encodedPassword, req.fullName());

        roleService.assignDefaultRole(user);

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (user.getRecruiterProfile() != null) {
            recruiterService.deleteRecruiterProfile(user.getId());
        }
        user.getRoles().clear();
        userRepository.delete(user);
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

    @Transactional(readOnly = true)
    public User getByRecruiterId(Long id){
        return userRepository.findByRecruiterProfileId(id)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}