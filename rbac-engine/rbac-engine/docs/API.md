# API.md — Dynamic RBAC Authorization Engine

Status legend: 🔲 not started · 🚧 in progress · ✅ done

| Status | Method | Path | Required Role/Permission | Purpose | Request Body |
|--------|--------|------|---------------------------|---------|---------------|
| 🔲 | POST | `/roles` | ADMIN (via permission, not hardcoded role) | Create a new role | `{ "name": "string" }` |
| 🔲 | POST | `/permissions` | ADMIN | Create a new permission | `{ "name": "string" }` |
| 🔲 | POST | `/roles/{roleId}/permissions/{permissionId}` | ADMIN | Assign a permission to a role | — (path vars only) |
| 🔲 | POST | `/users/{userId}/roles/{roleId}` | ADMIN | Assign a role to a user | — (path vars only) |
| 🔲 | GET | `/secure-data` | USER (with required permission) | Accessible only if user has the required permission | — |

## Notes
- "Role: ADMIN" / "Role: USER" in the spec means these endpoints require a
  user who *effectively* has admin/user-level permissions — enforcement
  itself must go through the custom `PermissionEvaluator`, not a literal
  `hasRole('ADMIN')` check.
- Example permission checks (to fill in once implemented) will show the
  exact `@PreAuthorize` expression used per endpoint.
- Auth: HTTP Basic (planned) — credentials against seeded `User` rows.
- Error responses: to be documented once Global Exception Handling (Phase
  6) is implemented — will include shape (timestamp, status, message,
  path) and codes used (400 validation, 403 access denied, 404 not found).

## To fill in as each endpoint is built
For each endpoint once implemented: example `curl` request, example
success response, example 403/400 response.
