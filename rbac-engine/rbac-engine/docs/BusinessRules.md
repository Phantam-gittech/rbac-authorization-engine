# BusinessRules.md — Dynamic RBAC Authorization Engine

Source: TechPulse IT Services take-home assignment PDF + assignment email.

## Core idea
Authorization decisions must be driven entirely by database-configured
roles and permissions — never hardcoded in Java logic. Changing an
authorization rule = a DB write, not a code change.

## Mandatory security rules
- Permissions evaluated dynamically at runtime (DB lookup on every check)
- No hardcoded role checks — `hasRole()`, `hasAuthority()` etc. are
  **forbidden**, anywhere
- Method-level security is mandatory (`@PreAuthorize` on service/controller
  methods)
- Authorization must go through a **custom `PermissionEvaluator`**

## Roles
- **ADMIN** — manages roles and permissions; assigns roles to users
- **USER** — accesses secured resources based on assigned permissions

## Required API surface
| Method | Path                                     | Role  | Purpose                          |
|--------|-------------------------------------------|-------|-----------------------------------|
| POST   | `/roles`                                   | ADMIN | Create a new role                 |
| POST   | `/permissions`                             | ADMIN | Create a new permission           |
| POST   | `/roles/{roleId}/permissions/{permissionId}` | ADMIN | Assign permission to role       |
| POST   | `/users/{userId}/roles/{roleId}`           | ADMIN | Assign role to user               |
| GET    | `/secure-data`                             | USER  | Accessible only with required permission |

## Database rules
- H2 database, all roles/permissions/mappings stored persistently
  (no in-memory or hardcoded authorization data — the DB engine itself may
  run in H2's `mem:` mode, but the *rules* must live in DB rows, not code)

## Implementation expectations
- `@PreAuthorize` with custom permission expressions
- `PermissionEvaluator` interface implemented
- Permissions loaded from DB at runtime
- Authorization logic centralized and reusable (write once, reuse across
  all `@PreAuthorize` checks)
- Optional caching for performance allowed (not required)

## Additional requirements (from assignment email, beyond the PDF)
- Request payload validation wherever applicable
- Global Exception Handling
- `application.yml` instead of `.properties`
- At least 80% test coverage
- SonarQube integration
- Deployment to a free cloud platform — **optional**
- README covering: setup/run instructions, API request flow, assumptions
  and design decisions

## Submission
- GitHub repo (public, or accessible to reviewers)
- README as above, plus: authorization flow, permission evaluation logic,
  how `PermissionEvaluator` is used, why hardcoded roles are avoided,
  example permission checks, run steps
- Recorded demo video, 5+ minutes: implementation approach, project
  structure, API demonstration, deployment (if applicable)
- **Deadline: 24 hours after receiving the assignment email**

## Explicit non-requirement
- No UI required — backend-focused assignment only
