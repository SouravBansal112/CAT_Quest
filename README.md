# CAT_Quest

CAT (Career Assist Tool) Quest is a backend system that integrates job aggregation and a job application portal into a single platform, designed to streamline and simplify the job search process.

---

## Key Features (Implemented)

* JWT-based authentication (access + refresh tokens)
* Role-based authorization (User / Recruiter)
* Core job and application business logic (service layer)
* Global exception handling with consistent error responses
* Layered architecture (Controller → Service → Repository)

---

## Current Status

* Core backend logic (service + repository layers) is implemented
* API layer is **partially complete** and currently being developed
* End-to-end API testing is **not yet done**
* Some modules are still being refined for consistency

---

## Planned Features (Not Yet Implemented)

* Complete REST API coverage for all modules
* Job scraping from external platforms
* Automation workflows for job applications
* Frontend (UI for users and recruiters)

---

## Tech Stack

Java 21 • Spring Boot • Spring Security • JWT • PostgreSQL • JPA/Hibernate

---

## Running Locally

```bash
git clone https://github.com/SouravBansal112/CAT_Quest.git
cd CAT_Quest
mvn spring-boot:run
```

Configure database in `application.yaml`.

---

## Notes

This project is currently focused on building a strong backend foundation.
API completion, testing, and external integrations are in progress.

---

## Contact

Sourav Bansal
[souravbansal112@gmail.com](mailto:souravbansal112@gmail.com)

