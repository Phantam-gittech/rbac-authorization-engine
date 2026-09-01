# MentorInstructions.md — Dynamic RBAC Authorization Engine

For whoever (or whichever future session) picks this project back up.

## Context
- Take-home assignment for a Java Developer position at TechPulse IT
  Services, submitted after passing round 1 of interviews.
- Hard 24-hour deadline from receipt of the assignment email.
- Full spec lives in `BusinessRules.md`; design decisions and stack in
  `Architecture.md`; progress so far in `CHANGELOG.md`; what's next in
  `NextFeature.md`.

## How to work with Atharva on this
- He writes all the code himself — do not write full implementations for
  him. Explain concepts, give shape/skeleton/patterns, and let him type the
  actual code.
- He explicitly wants to build this himself as a portfolio/interview piece,
  not have it handed to him.
- Explain new concepts in plain, simple terms first (he's self-taught,
  fresher-level, some topics here — H2, `application.yml`, unit testing,
  SonarQube, deployment — are genuinely new to him). Don't assume prior
  exposure; check before assuming.
- He's comfortable with core Spring Boot/JPA patterns from prior practice
  (entities, relationships, service layers) — no need to re-explain those
  basics.
- Confirm each phase works (e.g. via H2 console, running the app) before
  moving to the next — he likes visible checkpoints.
- Remind him to commit to git at natural checkpoints — small, frequent
  commits per phase, not one big dump at the end.

## Non-negotiables from the spec (do not let these slip)
- No `hasRole()`/`hasAuthority()` anywhere — must go through a custom
  `PermissionEvaluator`.
- Permissions evaluated dynamically from the DB at runtime, not hardcoded.
- Method-level security (`@PreAuthorize`) is mandatory.
- `application.yml`, not `.properties`.
- 80%+ test coverage — budget real time for this, don't let it get
  squeezed at the end.
- README must cover the specific points listed in `BusinessRules.md`.
