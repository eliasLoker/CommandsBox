# Native SwiftUI iOS App

## Feature overview

Create a separate `app-ios-native` iOS app whose screens are implemented in SwiftUI and whose state and business behavior come from the existing `:common-ui` ViewModels. The existing Compose screens and Compose-based `:app-ios` remain unchanged.

## Problem and expected value

The current iOS app displays the shared Compose UI. A separate native app will provide SwiftUI screens while reusing the existing Kotlin business logic, avoiding a second implementation of screen behavior.

## User flow

1. The app opens to the existing splash experience, then navigates to Home.
2. Home displays the current tabs and commands, or the existing empty states.
3. The user can perform the interactions already supported by the Home ViewModel, including adding, editing, deleting, reordering, selecting, and copying items.
4. The user can open Settings, change the theme, and return to Home.
5. Existing success and error toast behavior is shown and dismissed as it is today.

## Functional requirements

### Common ViewModels

- Reuse the existing Main, Home, and Settings ViewModels; do not duplicate their business logic in Swift.
- Prepare the ViewModels and required state/event APIs for access by the native app.
- SwiftUI must observe state changes and dispatch the corresponding existing ViewModel events.
- Do not change existing ViewModel behavior as part of the preparation.

### Native UI

- Implement the screens in SwiftUI. The native app must not render the Compose screens or host a Compose view controller.
- A Kotlin/native interop layer is allowed where needed to expose ViewModel state and events to Swift; it must not contain screen UI.
- Reproduce the current splash, Home, Settings, dialogs, empty states, and toast presentation and interactions.

### Navigation and theme

- Preserve the current Splash → Home → Settings/back flow.
- Preserve theme selection behavior, including the existing system-theme behavior and saved preference.
- Preserve current displayed text and supported localizations.

### Existing app and logic

- Create a separate `app-ios-native` module/app. Do not modify the existing `app-ios` module or its Xcode project.
- Keep changes to `:common-ui` limited to what is necessary to make its existing ViewModels usable from the native app. Compose dependencies are acceptable.

## Business rules

- The existing Kotlin ViewModels remain the source of truth for screen state and business actions.
- SwiftUI may manage presentation-only concerns, such as navigation and native control focus, but must not duplicate business rules or create a separate source of truth for ViewModel-managed state.
- Existing validation, error handling, theme persistence, and toast behavior remain governed by the current shared logic.

## Edge cases and risks

- Empty tabs or commands, long text, keyboard presentation, and smaller or larger simulator layouts must remain usable.
- State changes from asynchronous repository operations must appear in SwiftUI.
- Toast dismissal must work both after its current duration and when explicitly dismissed.
- The repository’s iOS documentation says full iOS feature parity, including data and clipboard behavior, has not been verified. These paths must be checked for the new app; missing platform support must be reported rather than replaced with duplicate Swift business logic.
- The current configured iOS target is simulator-only. This specification does not include physical-device support.

## Non-functional requirements

- Match the current app’s layout, colors, typography, icons, spacing, and state-dependent appearance as closely as the native rendering allows.
- Verify visual parity with screenshots of corresponding Compose and SwiftUI screens in matching states, including light and dark themes.
- **Assumption:** “Exactly the same appearance” means the same design and presentation, not guaranteed pixel-for-pixel raster identity. Differences inherent to native text and control rendering must be reviewed, not silently accepted.
- The app must build and run on the configured Apple Silicon iOS Simulator target.

## Acceptance criteria

1. `app-ios-native` builds and launches on the configured iOS Simulator.
2. The native app renders its screens with SwiftUI and does not display Compose UI.
3. The app uses the existing Main, Home, and Settings ViewModels; SwiftUI observes their state and dispatches their events.
4. The splash transition, navigation, Home interactions, dialogs, theme behavior, and toast behavior match the existing app.
5. Empty and populated content, success and error states, and light and dark themes are verified.
6. Screenshots are reviewed against the existing UI, with any visual differences documented and resolved or explicitly approved.
7. `:common-ui` changes are limited to ViewModel-sharing/API needs; the Compose screens and existing `:app-ios` remain unchanged.

## Out of scope

- Replacing or modifying the existing Compose-based `:app-ios`.
- Removing Compose dependencies from `:common-ui`.
- Physical iPhone/device support.
- Reimplementing business logic in Swift.
- Redesigning the existing UI or changing the behavior of its ViewModels.

## Confirmed decisions

- The new app is separate from `:app-ios`; `:app-ios` must not be changed.
- All current user-facing screens and flows are in scope.
- The new app’s UI is SwiftUI; existing `:common-ui` ViewModels are reused.
- Compose dependencies may remain in the Kotlin build.
- The first version targets the iOS Simulator only.
- Preparing `:common-ui` for ViewModel sharing is a separate prerequisite task.

## Remaining assumptions

- Visual parity means matching the design and behavior, rather than requiring identical rendered pixels.
