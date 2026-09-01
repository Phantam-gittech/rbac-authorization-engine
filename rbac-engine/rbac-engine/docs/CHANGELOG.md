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
- Correction since last update: UserRole's @Table(name = ...) was originally "user_role" (singular) — mismatched Architecture.md/prior changelog which said user_roles. Fixed to @Table(name = "user_roles") to match docs. Verified via H2 console after restart (H2 is in-memory, table regenerates on each app start, no migration needed).
- **Correction since last update**: `UserRole`'s `@Table(name = ...)` was
  originally `"user_role"` (singular) — mismatched `Architecture.md`/prior
  changelog which said `user_roles`. Fixed to `@Table(name = "user_roles")`
  to match docs. Verified via H2 console after restart (H2 is in-memory,
  table regenerates on each app start, no migration needed).
## Phase 2 — In progress, approach changed to VERTICAL SLICE
Original plan was horizontal (all repos, then all services, then all
controllers). Deliberately switched to vertical: build one full feature
(entity → repo → mapper → DTOs → service → controller) end-to-end before
moving to the next. Docs are being kept honest about this deviation.

This also pulled in work originally scoped for later phases — validation
(Phase 6) and Global Exception Handling (Phase 6) — because they're needed
to make each vertical slice complete and testable in isolation.

### Global infrastructure (pulled forward from Phase 6) — ✅ complete
- `ApiErrorResponse` DTO: timestamp, status, error, message (list), path
- `GlobalExceptionHandler` (`@RestControllerAdvice`) with handlers for:
  - `ResourceNotFoundException` → 404
  - `BadRequestException` → 400
  - `DataConflictException` → 409
  - `MethodArgumentNotValidException` → 400 (bug fixed: was incorrectly
    returning 404, corrected to `HttpStatus.BAD_REQUEST`)
  - Generic `Exception` fallback → 500, deliberately returns a generic
    `"Unexpected Error Occurred"` message rather than `ex.getMessage()` to
    avoid leaking internal details to API callers (logged server-side
    instead — noted as a security-conscious choice for the README)
- Custom exceptions: `ResourceNotFoundException`, `BadRequestException`,
  `DataConflictException` (more added as needed)
### Role slice — ✅ complete
- `RoleRepository` (`existsByNameIgnoreCase`, `findByName`)
- `RoleRequest` DTO (`@NotBlank` name, trims whitespace in setter)
- `RoleResponse` DTO (id, name)
- `RoleMapper` (`@Component`, not a static method — chosen for
  testability/mockability and to keep DTOs free of logic)
- `RoleService.createRole()` — duplicate-name check → `DataConflictException`,
  `@Transactional`, structured logging (debug/warn/info)
- `RoleController` — `POST /api/roles`, `@Valid @RequestBody`, returns
  `201 Created` with `Location` header
### Permission slice — ✅ complete
- Same shape as Role slice: `PermissionRepository`, `PermissionRequest`,
  `PermissionResponse`, `PermissionMapper`, `PermissionService`,
  `PermissionController` (`POST /api/permissions`)
### RolePermission (assign permission to role) slice — 🚧 in progress
- `RolePermissionRepository` (empty for now, no custom queries yet)
- `RolePermissionResponse` DTO — nests `RoleResponse` + `PermissionResponse`
- `RolePermissionMapper` (`@Component`, injects `RoleMapper` +
  `PermissionMapper` to build the nested response)
- `RolePermissionService.assignPermissionToRole(roleId, permissionId)` —
  looks up both `Role` and `Permission` by ID (404 via
  `ResourceNotFoundException` if either missing), saves the join row
- **Endpoint placement decision**: NOT a standalone
  `RolePermissionController`. Endpoint lives inside `RoleController` at
  `POST /roles/{roleId}/permissions/{permissionId}` because controllers
  are organized by URL/resource shape (per spec), while services are
  organized by domain logic — `RolePermissionService` still gets its own
  class even though the endpoint is nested under `/roles`.
- **Known bugs just fixed**: log statement was logging
  `permission.getName()` twice instead of `role.getName()` once; URI
  mismatch between `@RequestMapping` (plural) and hand-built `Location`
  URI (was singular) in both `RoleController` and `PermissionController`.
- **Known bugs pending fix (not yet applied as of this update)**:
  1. `@PathVariable` name mismatch — mapping used `{roleID}` (capital ID),
     parameter was `roleId` — must match exactly or use
     `@PathVariable("roleID")` explicitly. Fix: rename template to
     `{roleId}`.
  2. URL used singular `/permission/{permissionId}` instead of spec's
     plural `/permissions/{permissionId}`.
- `@Positive` bean validation added on `roleId`/`permissionId` path
  variables (requires `@Validated` on the controller class, already
  present).
### UserRole (assign role to user) slice — 🔲 not started
Same pattern as RolePermission slice, next up.

### Tests — 🔲 not started for any slice yet
**Flagged risk**: no unit tests written yet despite the "write tests
immediately after each service method" guidance being agreed on. This is
a real gap, not just a formality — budget time for this before Phase 2
closes, don't let it slide into a Phase 7 crunch.

## Up next
- Fix the two pending RolePermission controller bugs (path variable case
  mismatch, singular/plural URL)
- Write unit tests for `RoleService`, `PermissionService`,
  `RolePermissionService` (Mockito, mock repositories + mappers)
- Build `UserRole` slice (repository, DTOs, mapper, service, controller
  endpoint under `UserController`: `POST /users/{userId}/roles/{roleId}`)
- Then: `/secure-data` endpoint (currently unsecured — no auth exists yet)
- Then: Phase 3 (Auth foundation) becomes urgent, since `/secure-data`
  needs a logged-in user to be meaningful




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
