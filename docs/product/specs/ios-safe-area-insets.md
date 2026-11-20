# iOS Safe-Area Insets

## Feature overview

Adjust the iOS host layout so the shared Compose UI can draw beneath the iOS system-bar safe areas and apply its existing system-bar padding itself. Keep the change confined to `:app-ios`.

## Problem and expected value

The shared Home and Settings screens already apply Compose `navigationBarsPadding()` and `statusBarsPadding()`. The SwiftUI host may also constrain the Compose view to the iOS container safe area, resulting in excessive top and bottom spacing. Giving Compose the full container area should avoid applying both host-level and Compose-level insets, while preserving the shared UI's existing behavior across other targets.

## User flow

1. The user launches the iOS app.
2. The SwiftUI host presents the shared Compose UI edge-to-edge relative to the iOS container safe areas.
3. The shared Compose screens apply their existing status-bar and navigation-bar padding.
4. Existing keyboard safe-area behavior remains unchanged.

## Functional requirements

- The iOS host must allow the Compose view to extend into the container safe areas for system bars.
- Existing Compose system-bar padding in `common-ui` must remain unchanged.
- Existing keyboard safe-area handling in the SwiftUI host must be preserved.
- All implementation changes must be limited to the `app-ios` module.

## Business rules

- iOS must rely on the shared Compose UI's existing system-bar inset padding rather than adding another top/bottom safe-area inset in the host.
- Android and Desktop behavior must not change.

## Edge cases

- Confirm the view remains correctly positioned around both the top status area and bottom system navigation/home-indicator area.
- Confirm keyboard presentation retains its current safe-area behavior.
- The splash screen is part of the shared navigation flow; the implementation must not introduce an iOS-specific inset or appearance regression there.

## Data and AI behavior

Not applicable.

## Acceptance criteria

- The iOS Compose view extends beneath the iOS container safe areas, allowing the existing Compose system-bar padding to control content spacing.
- Existing keyboard safe-area behavior is preserved.
- No files outside `app-ios` are modified.
- The iOS target builds successfully, or any inability to run the build is reported explicitly.
- In a simulator, the Home and Settings content do not exhibit duplicated excessive top/bottom safe-area spacing.

## Out of scope

- Changing or removing `navigationBarsPadding()` / `statusBarsPadding()` in `common-ui`.
- Changing Android or Desktop host behavior.
- Redesigning system bars, keyboard handling, or screen layouts.

## Confirmed decisions

- The implementation is restricted to `app-ios`.
- Shared Compose inset modifiers remain unchanged.
- The SwiftUI host should allow Compose to occupy the container safe areas.
- Existing keyboard safe-area handling must be preserved.

## Remaining assumptions

- The excessive spacing is caused by the SwiftUI host's container safe-area layout being combined with the shared Compose system-bar padding. Validate this on the iOS Simulator; if it is not resolved by the host-only change, do not modify `common-ui` without returning to the product owner.
