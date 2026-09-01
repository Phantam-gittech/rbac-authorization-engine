# Dynamic RBAC Authorization Engine

> Take-home assignment for TechPulse IT Services — Java Developer role.
> This README is being filled in progressively as the project is built;
> sections marked TBD will be completed in later phases.

## Overview
A Role-Based Access Control (RBAC) authorization engine where roles and
permissions are stored in the database and evaluated dynamically at
runtime — no hardcoded `hasRole()`/`hasAuthority()` checks anywhere.
Authorization is enforced through a custom `PermissionEvaluator` combined
with Spring Security method-level security (`@PreAuthorize`).

## Tech stack
- Java 17+, Spring Boot 3.x, Maven
- Spring Web, Spring Data JPA, Spring Security, H2 Database, Validation
- `application.yml` for configuration

## Project setup & run instructions
1. Clone the repo
2. `mvn clean install`
3. `mvn spring-boot:run`
4. App runs on `http://localhost:8080`
5. H2 console (dev inspection only): `http://localhost:8080/h2-console`,
   JDBC URL `jdbc:h2:mem:rbac`, username `sa`, no password

## Authorization flow — TBD (Phase 4)
_Will explain: how a request reaches `@PreAuthorize`, how the custom
`PermissionEvaluator` resolves a user's permissions from the DB, and how
the decision (allow/deny) is made._

## Permission evaluation logic — TBD (Phase 4)

## How `PermissionEvaluator` is used — TBD (Phase 4)

## Why hardcoded roles are avoided
Authorization rules are stored as data (`RolePermission`, `UserRole` rows)
rather than as string literals in Java code. Changing who can do what is a
database write, not a code change and redeploy. See `Architecture.md` for
the full reasoning.

## Example permission checks — TBD (Phase 5)

## API request flow — TBD (Phase 5)
See `API.md` for the full endpoint list; this section will describe a
worked example (e.g. ADMIN creates a role → assigns a permission → assigns
role to a user → USER hits `/secure-data`).

## Assumptions & design decisions
- Explicit join entities (`RolePermission`, `UserRole` with their own
  `id`) used instead of a bare `@ManyToMany`, to match the spec literally
  and make the design easy to explain.
- `Permission` kept flat (`id` + `name` only) — no resource/action split.
- HTTP Basic authentication used (spec doesn't require JWT).
- H2 in-memory database with `ddl-auto: update` — acceptable for this
  assignment's scope; a production system would use migrations
  (Flyway/Liquibase) instead.

## Test coverage
Target: 80%+, measured via JaCoCo. — TBD (Phase 7)

## SonarQube
— TBD (Phase 8)

## Deployment
Optional per the assignment. — TBD (Phase 9, if time permits)
