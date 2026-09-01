# NextFeature.md — Dynamic RBAC Authorization Engine

## Immediate next: Phase 2 — Repositories + basic CRUD services
- `RoleRepository`, `PermissionRepository`, `UserRepository` (and
  `RolePermissionRepository` / `UserRoleRepository` if needed directly,
  vs. navigating via the parent entities)
- Service methods: create role, create permission, assign permission to
  role, assign role to user — plain JPA, no security enforcement yet
  (security gets layered on in Phase 4)

## After that, in order
- **Phase 3 — Auth foundation**: Spring Security config, `UserDetailsService`
  mapping `UserRole → Role` into `GrantedAuthority`, `BCryptPasswordEncoder`,
  seed one ADMIN user via `CommandLineRunner`/`data.sql` so endpoints are
  testable
- **Phase 4 — Custom `PermissionEvaluator`** (core of the assignment):
  implement `PermissionEvaluator`, register via
  `MethodSecurityExpressionHandler`, `@EnableMethodSecurity`, wire
  `@PreAuthorize("hasPermission(...))")` on endpoints — permission names
  only, never role literals
- **Phase 5 — Controllers**: thin, delegate to services, `@PreAuthorize` on
  each of the 5 required endpoints
- **Phase 6 — Validation + Global Exception Handling**: DTOs with
  `@NotBlank`/`@NotNull`, `@RestControllerAdvice` covering validation
  errors, not-found, `AccessDeniedException` → 403, generic fallback
- **Phase 7 — Tests**: unit tests for services, dedicated tests for the
  `PermissionEvaluator` logic in isolation, `MockMvc` controller tests
  including a 403 case, JaCoCo to confirm 80%+ coverage
- **Phase 8 — SonarQube**: SonarCloud (free, public repo)
- **Phase 9 — README + optional deployment**
- **Phase 10 — Demo video** (5+ min)

## Open decisions still to make
- None blocking right now — Phase 0 and Phase 1 decisions are locked in.
  Next real decision point is in Phase 3: confirm HTTP Basic auth is
  sufficient (currently assumed yes, spec doesn't ask for JWT).
