# Native ViewModel observation cleanup

## Feature overview

Reduce repeated observation and cleanup handling in the iOS-native ViewModel
bridge while retaining the existing typed bridge API.

## Problem and expected value

`MainViewModelBridge`, `HomeViewModelBridge`, and `SettingsViewModelBridge`
repeat observation ownership and cleanup patterns. Centralizing that shared
handling should make the bridge easier to maintain without weakening the
screen-specific types used by the SwiftUI host.

## User flow

There is no direct user-facing flow change. The native app continues to create
one `NativeViewModelHost`, observe state/actions, dispatch events, cancel
subscriptions as needed, and close the host at app teardown.

## Functional requirements

- Keep the three screen-specific typed bridge classes and their existing
  Swift-facing methods.
- Centralize their repeated observation and cleanup handling in shared
  internal code.
- Preserve individual subscription cancellation and host cleanup behavior.
- Preserve callback delivery behavior and the current ViewModel lifetimes.
- Do not require changes to Swift call sites.

## Business rules

- The bridges remain delegators; they must not add screen state, business rules,
  input validation, or data transformation.
- The host continues to own the app-lifetime ViewModels. Closing the host cancels
  bridge observations and does not clear those ViewModels.

## Edge cases

- Canceling an individual subscription must stop that collection without
  preventing other active observations from receiving updates.
- Closing the host must stop all observations created through its bridges.
- Repeated cancellation/close calls must remain safe.

## Data and AI behavior

Not applicable. This change does not alter application data or AI behavior.

## Acceptance criteria

- Shared internal code replaces the repeated observation/cleanup implementation
  across the three bridges.
- The existing typed bridge methods and Swift call sites remain unchanged.
- State and action callbacks continue to be delivered with the existing behavior.
- Individual subscription cancellation and host close preserve their documented
  lifecycle behavior.
- The `iosSimulatorArm64` framework links successfully, and relevant native iOS
  tests/checks are run where available; report the actual results.

## Out of scope

- Replacing the typed bridges with a universal untyped API.
- Exposing the shared ViewModels directly to Swift.
- Introducing a Flow-to-Swift interop dependency or plugin.
- Changing ViewModel, state, event, action, Koin, or app navigation behavior.

## Confirmed decisions

- Retain the three typed bridge facades.
- Centralize repeated observation and cleanup in shared internal code.
- Preserve existing cancellation, callback, and lifecycle behavior.
- Avoid Swift call-site changes and new interop dependencies.

## Remaining assumptions

- The refactor can be implemented using the existing Kotlin coroutine and Flow
  setup without changing the exported framework API. Verify this against the
  actual Kotlin/Native visibility and framework export behavior during
  implementation.
