# CHANGELOG.md — Dynamic RBAC Authorization Engine

## Phase 0 — Project setup
- Generated Spring Boot project via Spring Initializr (Web, JPA, Security,
  H2, Validation, Lombok)
- `git init` + first commit: "Initial commit : Spring Boot project setup"
- Replaced `application.properties` with `application.yml`
- Configured H2 datasource: `jdbc:h2:mem:rbac`, `ddl-auto: update`,
  `show-sql: true`
- Enabled H2 console at `/h2-console`
- Added `SecurityConfig` class (in `security` package, not `config`) to
  permit `/h2-console/**`, ignore CSRF for it, and disable frame options —
  required once Spring Security is on the classpath, otherwise the console
  is blocked
- Verified: H2 console loads, connects, and accepts real SQL
  (created + dropped a test table successfully)
- Committed: "Configure H2 datasource, verify H2 console"

## Phase 1 — Entity model
- Created 5 entities: `User`, `Role`, `Permission`, `RolePermission`,
  `UserRole` (explicit join entities per spec, not bare `@ManyToMany`)
- Verified: all 5 tables (`users`, `roles`, `permissions`,
  `role_permissions`, `user_roles`) appear correctly in H2 console
- Commit pending: "Add Role, Permission, User, RolePermission, UserRole
  entities"

## Up next
- Phase 2: Repositories + basic CRUD services
- Phase 3: Auth foundation (Spring Security config, UserDetailsService,
  BCrypt, seed admin user)
- Phase 4: Custom `PermissionEvaluator` (core of the assignment)
- Phase 5: Controllers
- Phase 6: Validation + Global Exception Handling
- Phase 7: Tests (target 80% coverage, JaCoCo)
- Phase 8: SonarQube (SonarCloud, free hosted)
- Phase 9: README + optional deployment
- Phase 10: Demo video (5+ min)
