# NextFeature.md — Dynamic RBAC Authorization Engine

## Where things actually stand
Phase 2 was done as a **vertical slice** (entity → repo → mapper → DTOs →
service → controller, one full feature at a time), not horizontally as
originally planned. This pulled Phase 6 (validation + Global Exception
Handling) forward, since each slice needed it to be complete and
demoable on its own. See `CHANGELOG.md` for full detail per slice.

## Immediate next steps (in order)

1. **Fix the known bug in `UserRoleService.assignRoleToUser`**: the role
   lookup uses `roleRepository.findById(userId)` — wrong variable, should
   be `roleId`. This is a silent-wrong-data bug, not a crash, so it's
   easy to miss in manual testing. Fix before doing anything else.
2. **Fix the log message bug** in the same method (logs `.getRole().getId()`
   where message text says "role name" — should be `.getName()`).
3. **Confirm the two `RolePermission` controller fixes from earlier were
   applied**: `{roleID}` → `{roleId}` path variable case match, and
   `/permission/` → `/permissions/` (plural) in the URL.
4. **Write unit tests — this is the biggest real gap right now.** No
   tests exist yet for `RoleService`, `PermissionService`,
   `RolePermissionService`, or `UserRoleService`, despite agreeing early
   on to write tests immediately after each service method rather than
   batching them into Phase 7. This is actively a risk to the 80%
   coverage requirement and to catching exactly the class of bug found
   twice already (copy-paste log/variable mistakes). Priority: write these
   before starting Phase 3, not after.
  - Mockito (`@ExtendWith(MockitoExtension.class)`), mock repositories
    and mappers, assert on returned DTOs and on exception types thrown
    for the not-found / duplicate paths.
5. **`/secure-data` endpoint** — the 5th required endpoint. Currently
   nothing exists for it. It's meaningless without auth, so it may make
   sense to build it together with Phase 3 rather than before.

## Phase 3 — Auth foundation (next real phase)
- Spring Security config, `UserDetailsService` mapping `UserRole → Role`
  into `GrantedAuthority`
- `BCryptPasswordEncoder` — already in use inside `UserMapper` for
  password hashing on create; same bean will be reused for login
  comparison
- Seed one ADMIN user via `CommandLineRunner` or `data.sql` so endpoints
  are testable end-to-end
- Confirm HTTP Basic is sufficient (still the assumption, unchanged)

## After Phase 3, unchanged from original roadmap
- **Phase 4 — Custom `PermissionEvaluator`** (core of the assignment)
- **Phase 5 — Controllers**: mostly done already via vertical slicing,
  this phase is really just "confirm all 5 endpoints exist and are wired
  to `@PreAuthorize`"
- **Phase 6 — Validation + Global Exception Handling**: already done,
  pulled forward — this phase is complete
- **Phase 7 — Tests**: partially pulled forward per item 4 above; this
  phase becomes "close any remaining coverage gaps + `PermissionEvaluator`-
  specific tests + `MockMvc` controller tests" rather than starting from
  zero
- **Phase 8 — SonarQube**
- **Phase 9 — README + optional deployment**
- **Phase 10 — Demo video**

## Open decisions still to make
- HTTP Basic auth sufficiency — same as before, assumed yes.
- Interface-vs-concrete-class for services: deliberately skipped
  interfaces (no second implementation exists, Spring doesn't require it
  for DI) — settled, mention as a deliberate call if asked in interview.
- Mapper pattern: `@Component` mapper classes chosen over static factory
  methods, for testability/mockability — settled.