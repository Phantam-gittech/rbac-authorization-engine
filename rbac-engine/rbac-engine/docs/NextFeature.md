# NextFeature.md — Dynamic RBAC Authorization Engine

## Status at submission
Core assignment complete: all 5 entities, all 5 endpoints, validation,
global exception handling, custom PermissionEvaluator fully wired and
verified end-to-end via real API calls, unit tests for all 4 services,
one MockMvc controller-level security test proving @PreAuthorize works
through the full HTTP + security layer, not just the service layer.

## Not completed due to time constraint
- Full MockMvc test coverage across all controllers (only Role +
  secure-data tested at HTTP layer; service layer has full unit test
  coverage)
- JaCoCo precision check for exact 80% — likely close, not formally
  verified due to time
- SonarQube integration — not completed

## Settled decisions (for interview reference)
- Vertical slicing over horizontal phase-by-phase build order
- No service interfaces — deliberate, no second implementation exists
- `@Component` mapper classes over static factory methods, for
  testability
- Generic 500 error message to API callers (not `ex.getMessage()`) —
  security-conscious choice, real exception logged server-side instead
- HTTP Basic auth over JWT — spec didn't require JWT, kept in scope