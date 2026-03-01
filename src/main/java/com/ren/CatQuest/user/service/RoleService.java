package com.ren.CatQuest.user.service;


import com.ren.CatQuest.user.entity.Role;
import com.ren.CatQuest.user.entity.User;
import com.ren.CatQuest.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {
    private static final String DEFAULT_ROLE = "ROLE_USER";

    private final RoleRepository roleRepository;

    public Role getOrCreate(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() ->
                        roleRepository.save(
                                Role.create(roleName)
                        )
                );
    }

    public void assignDefaultRole(User user) {
        assignRole(user, DEFAULT_ROLE);
    }

    public void assignRole(User user, String roleName) {
        Role role = getOrCreate(roleName);
        user.addRole(role);
    }

    public void removeRole(User user, String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Role not found"));
        user.removeRole(role);
    }
}
