# Remove `:common-ui` from `:app-ios-native`

## Feature overview

Decouple the native SwiftUI app from `:common-ui`. Keep its existing ViewModel behavior and native UI, but have its Kotlin/Native framework and startup wiring use the presentation and platform/data dependencies it actually needs.

## Problem and expected value

`:app-ios-native` currently depends on `:common-ui` for a Swift-facing ViewModel bridge, startup wiring, and a theme helper. That couples the native app to the shared Compose UI module. Removing this dependency will make the native app’s module boundary clearer without moving Compose or platform-adapter code into `:common-presentation`.

## User flow

No user-visible change. The native app continues to launch, show its existing screens, and use the shared Main, Home, and Settings ViewModels as before.

## Functional requirements

- Remove `:common-ui` from `:app-ios-native`’s dependency and framework-export configuration.
- Preserve the Swift-facing bridge and app startup behavior needed by `NativeAppHost`.
- Keep `:app-ios-native` using the existing ViewModels and their state, events, and actions from `:common-presentation`.
- Preserve theme resolution behavior for the native app.
- Preserve iOS data and clipboard behavior used by the existing ViewModels.
- Keep the existing Compose-based apps and their `:common-ui` behavior unchanged.

## Business rules

- The ViewModels in `:common-presentation` remain the source of truth for shared screen state and business behavior.
- `:common-presentation` must not become the home for platform adapters or Compose UI implementation.
- Android/JVM platform implementations currently in `:common-ui` remain there unless a separate need is identified.

## Edge cases

- Removing `:common-ui` also removes access to its iOS Koin module, which currently includes `iosDataModule` and registers the clipboard adapter.
- The native app must continue resolving every ViewModel dependency, including clipboard support, without introducing a dependency back to `:common-ui`.
- The theme helper currently used by the native Kotlin source is in `:common-ui`; its behavior must be preserved without retaining that dependency.
- The framework must continue exposing the Kotlin APIs used by Swift.

## Data and AI behavior

Not applicable. This change does not alter data behavior, AI behavior, or the existing ViewModels’ business logic.

## Non-functional requirements

- Keep the native app’s existing user-visible behavior unchanged.
- Do not introduce a dependency from `:common-presentation` to `:common-ui`.
- Preserve the existing configured iOS Simulator target and framework API required by the Swift host.

## Acceptance criteria

1. `:app-ios-native` has no direct or transitive dependency on `:common-ui`.
2. Its framework links for the configured iOS Simulator target and exposes the APIs needed by the Swift host.
3. The native app launches and continues to support its existing screens, ViewModel interactions, theme behavior, data operations, and clipboard action.
4. The Compose-based Android, Desktop, and iOS apps continue to build or pass their relevant checks without behavior changes.
5. No platform adapters are moved into `:common-presentation`.
6. The implementation report identifies the changed files and the exact builds/tests run and their results.

## Out of scope

- Moving Android or JVM platform implementations out of `:common-ui`.
- Removing Compose dependencies from `:common-ui`.
- Redesigning native UI or changing ViewModel behavior.
- Creating a general-purpose platform module unless implementation reveals a concrete need.

## Confirmed decisions

- The goal is to remove `:common-ui` from `:app-ios-native`’s dependency graph.
- Platform adapters should remain outside `:common-presentation`.
- This is intended as an architecture-only change with no user-visible behavior change.

## Remaining assumptions

- The native app can own its Swift-facing bridge and required iOS startup/bindings, including clipboard registration, without adding a new shared module. If implementation shows that this would create inappropriate duplication or a dependency issue, the developer must return for a product decision rather than silently expanding scope.
