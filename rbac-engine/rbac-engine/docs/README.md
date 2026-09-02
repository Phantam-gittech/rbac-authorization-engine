# Dynamic RBAC Authorization Engine

> Take-home assignment for TechPulse IT Services — Java Developer role.

## Overview
A Role-Based Access Control (RBAC) authorization engine where roles and
permissions are stored in the database and evaluated dynamically at
runtime — no hardcoded `hasRole()`/`hasAuthority()` checks anywhere.
Authorization is enforced through a custom `PermissionEvaluator` combined
with Spring Security method-level security (`@PreAuthorize`).

## Tech stack
- Java 17+, Spring Boot 3.x, Maven
- Spring Web, Spring Data JPA, Spring Security, H2 Database, Validation, Lombok
- `application.yml` for configuration

## Project setup & run instructions
1. Clone the repo
2. `mvn clean install`
3. `mvn spring-boot:run`
4. App runs on `http://localhost:8080`
5. H2 console (dev inspection only): `http://localhost:8080/h2-console`,
   JDBC URL `jdbc:h2:mem:rbac`, username `sa`, no password
6. On every startup, `data.sql` seeds one test user (`admin` / see note
   below) already assigned the `ADMIN` role with every permission, so
   all 5 endpoints are testable immediately without manual setup.
7. **Login credentials for testing**: username `admin`, password
   `admin123` (HTTP Basic Auth). Note: the login check currently uses a
   temporary in-memory user store (`InMemoryUserDetailsManager`) seeded
   with these exact credentials — this is separate from the `admin` row
   in the database, which exists purely so the permission-lookup query
   has a matching username to resolve permissions against. Replacing the
   in-memory login store with a real DB-backed `UserDetailsService` is
   noted as a next step below.

## Entity model
```
User ──< UserRole >── Role ──< RolePermission >── Permission
```
Explicit join entities (`UserRole`, `RolePermission`) with their own `id`
were used instead of a bare `@ManyToMany`, to match the assignment spec
literally and make the design easy to explain. `Permission` is kept flat
(`id` + `name` only) — no resource/action split.

## Authorization flow
1. A request hits a controller endpoint annotated with
   `@PreAuthorize("hasPermission(null, 'PERMISSION_NAME')")`.
2. Spring Security's method security intercepts the call before the
   controller method runs, and evaluates the `hasPermission(...)` SpEL
   expression.
3. Because a custom `PermissionEvaluator` bean is registered (via a
   `MethodSecurityExpressionHandler` bean in `AppConfig`), Spring routes
   this call to `CustomPermissionEvaluator.hasPermission(...)`.
4. The evaluator reads the currently authenticated username from the
   `Authentication` object, then queries the database via
   `UserRepository.findPermissionNamesByUsername(username)` — a JPQL
   query joining `UserRole → Role → RolePermission → Permission` for
   that user.
5. If the requested permission name is present in that result set, the
   request proceeds to the controller method (200/201). Otherwise, Spring
   Security returns `401 Unauthorized` (no/invalid credentials) or
   `403 Forbidden` (valid credentials, insufficient permission).

At no point does Java code reference a role name (`"ADMIN"`) or a literal
`hasRole()`/`hasAuthority()` check — every decision is resolved from
database rows at request time. Changing who can do what is a SQL
`INSERT`, not a code change and redeploy.

## Permission evaluation logic
`CustomPermissionEvaluator implements PermissionEvaluator`. Only the
single-object `hasPermission(Authentication, Object, Object)` overload is
used, since this assignment's `Permission` model is flat (no per-object
checks needed) — the `targetDomainObject` argument is always passed as
`null`. The second overload
(`hasPermission(Authentication, Serializable, String, Object)`) is
required by the interface but never called by this application; it
throws `UnsupportedOperationException` intentionally.

## How `PermissionEvaluator` is used
Registered via a `MethodSecurityExpressionHandler` bean in `AppConfig`,
which is auto-detected by Spring once `@EnableMethodSecurity` is present.
From there, any `@PreAuthorize("hasPermission(null, 'X'))")` on any
controller method is automatically routed through it — no per-endpoint
wiring beyond the annotation itself.

## Why hardcoded roles are avoided
Authorization rules are stored as data (`RolePermission`, `UserRole`
rows) rather than as string literals in Java code. Changing who can do
what is a database write, not a code change and redeploy.

## API endpoints & example permission checks

| Method | Path | Required permission |
|---|---|---|
| POST | `/api/roles` | `CREATE_ROLE` |
| POST | `/api/permissions` | `CREATE_PERMISSION` |
| POST | `/api/roles/{roleId}/permissions/{permissionId}` | `ASSIGN_PERMISSION_TO_ROLE` |
| POST | `/api/users/{userId}/roles/{roleId}` | `ASSIGN_ROLE_TO_USER` |
| GET | `/api/secure-data` | `VIEW_SECURE_DATA` |

**Verified example — allowed:**
```
POST /api/roles
Authorization: Basic admin:admin123
Body: { "name": "TEST_ROLE" }

→ 201 Created
{ "id": 2, "name": "TEST_ROLE" }
```

**Verified example — denied (no credentials):**
```
POST /api/roles
(no Authorization header)

→ 401 Unauthorized
```

Both paths are covered by an automated test
(`RoleControllerSecurityTest`, MockMvc + full Spring context), in
addition to `GET /api/secure-data` tested the same way.

## Error handling
Centralized via `@RestControllerAdvice` (`GlobalExceptionHandler`).
Response shape:
```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Resource Not Found",
  "message": ["Role not found with id = 5"],
  "path": "/api/roles/5/permissions/1"
}
```
Handled cases: `ResourceNotFoundException` → 404, `BadRequestException` →
400, `DataConflictException` → 409, `MethodArgumentNotValidException` →
400 (bean validation failures), generic `Exception` → 500 (returns a
generic message to the client rather than the raw exception, to avoid
leaking internal details — the real exception is logged server-side).

## Assumptions & design decisions
- Explicit join entities used instead of a bare `@ManyToMany`, to match
  the spec literally.
- `Permission` kept flat — no resource/action split.
- HTTP Basic authentication used (spec doesn't require JWT).
- H2 in-memory database with `ddl-auto: update`, seeded via `data.sql`
  on every startup — acceptable for this assignment's scope; a
  production system would use migrations (Flyway/Liquibase) instead.
- No service interfaces (`RoleService` etc. are concrete classes) —
  deliberate, no second implementation exists and Spring doesn't require
  one for dependency injection.
- `@Component` mapper classes used over static factory methods on DTOs,
  for testability/mockability.

## Test coverage
- Unit tests (JUnit 5 + Mockito) for all four services (`RoleService`,
  `PermissionService`, `RolePermissionService`, `UserRoleService`) —
  happy paths and not-found/duplicate exception paths.
- One controller-level integration test class
  (`RoleControllerSecurityTest`, `@SpringBootTest` + `MockMvc`) proving
  `@PreAuthorize` correctly blocks unauthenticated requests and allows
  correctly-credentialed ones, across both a write endpoint (`/roles`)
  and the read endpoint (`/secure-data`).
- **Not completed due to time constraints**: `MockMvc` tests for the
  remaining controllers (Permission, RolePermission assignment, UserRole
  assignment); formal JaCoCo verification of the 80% coverage target.

## SonarQube
Not completed due to time constraints.

## Deployment
Not completed — optional per the assignment.

## Known limitations / honest notes for the reviewer
- Login currently uses a temporary in-memory user store seeded with one
  test user. A production version would replace this with a real
  DB-backed `UserDetailsService` querying the `User` table directly —
  this was scoped out due to the assignment's time limit, with the
  `PermissionEvaluator` (the actual core requirement) prioritized instead.
- Testing (particularly Mockito-based unit tests) was new to me going
  into this assignment; tests were written collaboratively with AI
  assistance while learning the pattern. I'm prepared to walk through and
  explain any test in the interview.