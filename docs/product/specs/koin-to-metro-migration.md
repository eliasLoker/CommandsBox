# Koin-to-Metro Migration

**Status: Approved**

## Feature overview

Replace Koin with Metro as the project’s dependency-injection framework. Remove Koin registrations, resolution APIs, and direct dependencies from the project’s current app and library code, and update engineering guidance that currently requires Koin.

This is an internal architecture migration. It should not change application features or user-facing behavior.

## Problem and expected value

The project currently uses Koin’s runtime module DSL, Compose integration, global context, and service lookup across shared code and multiple app entry points. Metro offers compile-time dependency graphs and validation, and supports the project’s current Android, JVM/Desktop, and iOS Simulator ARM64 platforms.

Expected value:

- Find unresolved or conflicting dependencies at compile time.
- Replace runtime Koin registration and lookup with explicit Metro graph creation and access.
- Keep dependency injection usable across the project’s current targets.

This value depends on successfully integrating Metro with the project’s Kotlin compiler version and builds; it is not a promise of measurable performance improvement.

## User flow

There should be no visible change for users.

At startup, each app entry point creates its platform-appropriate Metro graph, including the services for that platform. Compose screens obtain ViewModels through Metro’s Compose integration. The native iOS bridge obtains the ViewModels it needs from its graph and continues exposing the existing bridge API.

## Functional requirements

1. Remove Koin usage from all current project source, build configuration, and engineering guidance where it is currently present.
2. Provide a Metro graph setup for each current app integration:
   - Android app startup.
   - Desktop/JVM startup.
   - Compose iOS Simulator app startup.
   - Native iOS bridge startup and ViewModel access.
3. Resolve dependencies currently supplied by Koin, including shared presentation and data dependencies and platform-specific services.
4. Replace Koin Compose lookups in shared UI and app entry points with Metro-compatible resolution.
5. Preserve the existing ViewModel and navigation integration, including the behavior of `SavedStateHandle` where it is used.
6. Update `docs/architecture/mvi.md` so it no longer instructs contributors to use Koin.
7. Keep modules that do not use Koin or define Metro graph/binding code free of unnecessary DI configuration.

## Business rules

- Preserve current app behavior and the effective lifecycle/scope of existing dependencies. In particular, graph creation must not accidentally produce duplicate application-wide instances where the current setup provides singletons.
- Platform-specific bindings must stay platform-specific: Android, Desktop/JVM, and iOS have different database builders and clipboard implementations.
- Each app integration must initialize and use its own appropriate graph. The Compose iOS app and native iOS bridge are distinct entry paths; the migration must not assume they share a live graph instance.
- Metro’s compiler plugin should be applied where needed for graph or binding compilation, not automatically to every module merely because the project is migrating frameworks.

## Edge cases and risks

- **Compiler compatibility:** The repository uses Kotlin 2.4.21. Metro’s compatibility documentation lists Kotlin 2.4.20 support, but not 2.4.21 as a separately tested version. Build verification is required before relying on this combination.
- **Platform graph visibility:** Shared code cannot assume that a common graph can see platform-only bindings. The final graph arrangement must include each platform’s bindings.
- **Saved state and ViewModel ownership:** Compose ViewModels must continue to use the appropriate lifecycle owner and saved-state behavior on Android, Desktop, and iOS.
- **Android context:** Any Android binding that needs a `Context` must use the correct application-level context rather than an Activity-scoped context.
- **Native iOS startup:** Graph initialization must remain safe for the bridge’s startup flow and must not create unintended duplicate graph instances.
- **Dependency leakage:** Removing source-level Koin usage is insufficient if Koin artifacts remain as direct or transitive runtime dependencies of an app target.

## Data and AI behavior

Not applicable. This migration changes dependency wiring, not application data, AI behavior, or user content.

## Non-functional requirements

- Compile-time graph validation must be active for the Metro-managed graph.
- The migration must preserve the project’s current target support; it must not implicitly add or remove platforms.
- The generated dependency-injection integration must not require runtime Koin startup or lookup.
- Verification results must identify which platform builds and tests were actually run and any unavailable platform checks.

## Acceptance criteria

1. No Koin imports or APIs remain in project source code.
2. No direct Koin dependencies remain in module build files or the version catalog, and Koin runtime artifacts are not present in the app runtime dependency graphs.
3. The MVI architecture guide describes the new DI conventions and no longer prescribes Koin.
4. Android, Desktop/JVM, Compose iOS Simulator, and native iOS bridge entry paths can create and use their required dependencies through Metro.
5. All existing ViewModels resolve with their required dependencies; Compose ViewModel creation, lifecycle ownership, and saved-state behavior remain intact.
6. Android, Desktop/JVM, and iOS platform-specific database and clipboard bindings resolve in their respective graph.
7. Relevant builds and tests for the current targets are run where the environment permits. Any target that cannot be verified is explicitly reported with the reason.
8. No user-facing behavior or unrelated application functionality changes as part of this migration.

## Out of scope

- Changing screens, navigation, persistence, repository behavior, or ViewModel business logic except where strictly required to replace DI resolution.
- Adding new app targets or expanding the existing iOS target set.
- Reworking the MVI architecture or introducing a different presentation framework.
- Optimizing runtime performance without separate measurements and approval.

## Confirmed decisions

- The requested direction is to replace Koin with Metro across the project’s current app integrations.
- Koin removal from current source/build configuration and updates to Koin-specific engineering guidance are in scope.
- The migration should preserve current functionality rather than introduce user-visible changes.

## Remaining assumptions

- “Current targets” means the targets currently configured in this repository: Android, Desktop/JVM, iOS Simulator ARM64 Compose app, and the native iOS bridge using iOS Simulator ARM64.
- Implementation may choose the specific Metro graph composition pattern, provided it supports shared and platform-specific bindings and satisfies the requirements above.
