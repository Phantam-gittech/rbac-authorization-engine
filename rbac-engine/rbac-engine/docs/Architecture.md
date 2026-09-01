# Architecture.md — Dynamic RBAC Authorization Engine

## Stack
- Spring Boot 3.x, Java 17+, Maven
- Spring Web, Spring Data JPA, Spring Security, H2 Database, Validation, Lombok
- Config via `application.yml` (not `.properties`)

## Package structure (as decided)
- `security` — Spring Security config (renamed from default `config` by choice)
- `model` / `entity` — JPA entities
- (to be extended as we build: `repository`, `service`, `controller`, `dto`, `exception`)

## Entity model

Five entities, chosen to match the assignment PDF literally (explicit join
entities with their own `id`, not bare `@ManyToMany`) so the design is easy
to explain in the README/demo video.

```
User ──< UserRole >── Role ──< RolePermission >── Permission
```

- **User**: `id`, `username` (unique), `password`
  - Not explicitly named in the assignment's entity list, but required since
    `UserRole.user_id` must point to something and someone needs to log in.
  - Table named `users` (not `user`) to avoid SQL reserved-word conflicts.
- **Role**: `id`, `name` (unique) — e.g. ADMIN, USER
- **Permission**: `id`, `name` (unique) — flat model, no resource/action split
  (deliberate decision — matches the spec's Permission entity exactly:
  just `id` + `name`)
- **RolePermission**: `id`, `role` (`@ManyToOne` → Role), `permission`
  (`@ManyToOne` → Permission) — join table `role_permissions`
- **UserRole**: `id`, `user` (`@ManyToOne` → User), `role` (`@ManyToOne` → Role)
  — join table `user_roles`

All `@ManyToOne` relations use `fetch = FetchType.LAZY` explicitly (default
for `@ManyToOne` is `EAGER` — overridden as a best-practice habit).

## Database
- H2, in-memory: `jdbc:h2:mem:rbac`
- `ddl-auto: update` — Hibernate creates/updates tables from entity
  annotations. (Tradeoff noted for README: fine for this assignment's scope;
  production systems would use Flyway/Liquibase migrations instead.)
- H2 console enabled at `/h2-console` for manual inspection during dev only
  — not part of the assignment's API surface.
- Verified working: all 5 tables (`users`, `roles`, `permissions`,
  `role_permissions`, `user_roles`) appear correctly in H2 console after
  entity creation.

## Security (planned)
- HTTP Basic auth (spec doesn't require JWT — kept in scope)
- `UserDetailsService` loads `User` → maps `UserRole → Role` into
  `GrantedAuthority`
- `BCryptPasswordEncoder` for passwords
- Custom `PermissionEvaluator` implementing Spring Security's interface,
  registered via `MethodSecurityExpressionHandler`
- `@EnableMethodSecurity` + `@PreAuthorize("hasPermission(...)")` on
  controller methods — **no `hasRole()`/`hasAuthority()` anywhere**
- `/h2-console/**` permitted + CSRF ignored + frame options disabled,
  scoped to dev inspection only

## Core design principle (why this shape)
Authorization must be resolvable entirely from DB rows at request time.
Java code only ever asks "does this user have permission X?" — it never
contains a literal role name. Changing who can do what should be a SQL
`INSERT`, not a code change + redeploy.
