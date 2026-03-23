package com.ren.catquest.recruiter.service;


import com.ren.catquest.auth.dto.AuthResponse;
import com.ren.catquest.job.entity.Company;
import com.ren.catquest.job.entity.Job;
import com.ren.catquest.job.repository.CompanyRepository;
import com.ren.catquest.job.repository.JobRepository;
import com.ren.catquest.recruiter.entity.RecruiterProfile;
import com.ren.catquest.recruiter.repository.RecruiterProfileRepository;
import com.ren.catquest.security.auth.CustomUserDetailsService;
import com.ren.catquest.security.jwt.JwtService;
import com.ren.catquest.security.jwt.JwtTokenType;
import com.ren.catquest.security.model.SecurityUser;
import com.ren.catquest.user.entity.Role;
import com.ren.catquest.user.entity.User;
import com.ren.catquest.user.repository.RoleRepository;
import com.ren.catquest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruiterService {
    private final JobRepository jobRepository;
    private final RecruiterProfileRepository recruiterRepo;
    private final UserRepository userRepo;
    private final CompanyRepository companyRepo;
    private final RoleRepository roleRepo;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    private static final String ROLE_RECRUITER = "ROLE_RECRUITER";

    // CREATE RECRUITER PROFILE
    @Transactional
    public  AuthResponse createRecruiterProfile(Long userId,
                                                   Long companyId,
                                                   String designation,
                                                   String industry,
                                                   String location,
                                                   String description) {

        // Fetch user
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUserId(userId);
        // Prevent duplicate recruiter profile
        if (recruiterRepo.existsByUserId(userId)) {
            throw new RuntimeException("Recruiter profile already exists");
        }

        // Fetch company
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // Assign recruiter role
        Role recruiterRole = roleRepo.findByName(ROLE_RECRUITER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.addRole(recruiterRole);

        // Create recruiter profile
        RecruiterProfile profile = RecruiterProfile.create(
                user,
                company,
                designation,
                industry,
                location,
                description
        );

        user.setRecruiterProfile(profile);

        recruiterRepo.save(profile);

        String accessToken = jwtService.generateToken(securityUser, JwtTokenType.ACCESS);
        String refreshToken = jwtService.generateToken(securityUser, JwtTokenType.REFRESH);

        return new AuthResponse(accessToken, refreshToken);

    }

    // GET RECRUITER (WITH COMPANY)
    @Transactional
    public RecruiterProfile getByUserId(Long userId) {
        return recruiterRepo.findWithCompanyByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
    }

    // SIMPLE FETCH (no join fetch)
    public RecruiterProfile getSimpleByUserId(Long userId) {
        return recruiterRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
    }

    // CHECK IF USER IS RECRUITER
    public boolean isRecruiter(Long userId) {
        return recruiterRepo.existsByUserId(userId);
    }

    // UPDATE RECRUITER PROFILE
    @Transactional
    public void updateProfile(Long userId,
                              String designation,
                              String industry,
                              String location,
                              String description) {

        RecruiterProfile profile = recruiterRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));

        profile.updateCompanyDetails(
                profile.getCompany(),
                industry,
                location,
                description
        );
    }

    @Transactional
    public AuthResponse deleteRecruiterProfile(Long recruiterId) {

        RecruiterProfile profile = recruiterRepo.findByUserId(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));

        jobRepository.softDeleteByRecruiterId(recruiterId);
        jobRepository.updatePostedByToNullByRecruiterId(recruiterId);

        recruiterRepo.delete(profile);

        User user = userRepo.findByRecruiterProfileId(recruiterId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role recruiterRole = roleRepo.findByName(ROLE_RECRUITER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.removeRole(recruiterRole);
        SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUserId(user.getId());

        String accessToken = jwtService.generateToken(securityUser, JwtTokenType.ACCESS);
        String refreshToken = jwtService.generateToken(securityUser, JwtTokenType.REFRESH);

        return new AuthResponse(accessToken, refreshToken);
    }

}
