package com.ren.CatQuest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatQuestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatQuestApplication.class, args);
	}

}

/*com.ren.catquest
│
├── CatQuestApplication.java
│
├── config/                     # App-wide configuration
│   ├── SecurityConfig.java
│   ├── JwtConfig.java
│   ├── MailConfig.java
│   ├── WebConfig.java
│
├── common/                     # Shared utilities (VERY small)
│   ├── exception/
│   │   ├── ApiException.java
│   │   ├── GlobalExceptionHandler.java
│   │
│   ├── response/
│   │   └── ApiResponse.java
│   │
│   └── util/
│       ├── TimeUtil.java
│       └── RandomUtil.java
│
├── security/                   # Cross-feature security infra
│   ├── jwt/
│   │   ├── JwtService.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── JwtTokenType.java
│   │
│   ├── auth/
│   │   └── CustomUserDetailsService.java
│   │
│   └── model/
│       └── SecurityUser.java
│
├── auth/                       # 🔐 AUTH FEATURE
│   ├── controller/
│   │   └── AuthController.java
│   │
│   ├── service/
│   │   ├── AuthService.java
│   │   └── TokenService.java
│   │
│   └── dto/
│       ├── RegisterRequest.java
│       ├── LoginRequest.java
│       ├── AuthResponse.java
│       ├── RefreshTokenRequest.java
│       └── EmailVerificationRequest.java
│
├── user/                       # 👤 USER FEATURE
│   ├── entity/
│   │   ├── User.java
│   │   └── Role.java
│   │
│   ├── repository/
│   │   ├── UserRepository.java
│   │   └── RoleRepository.java
│   │
│   └── service/
│       ├── UserService.java
│       └── RoleService.java
│
├── job/                        # 💼 JOB FEATURE
│   ├── entity/
│   │   ├── Job.java
│   │   ├── Company.java
│   │   └── Location.java
│   │
│   ├── repository/
│   │   ├── JobRepository.java
│   │   ├── CompanyRepository.java
│   │   └── LocationRepository.java
│   │
│   ├── service/
│   │   └── JobService.java
│   │
│   └── controller/
│       └── JobController.java
│
├── mail/                       # 📧 EMAIL FEATURE
│   ├── service/
│   │   └── MailService.java
│   │
│   └── template/
│       └── verification.html
│
└── infrastructure/             # External systems
    ├── persistence/
    ├── scheduler/
    └── messaging/*/

