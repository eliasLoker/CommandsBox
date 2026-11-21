# Native ViewModel bridge (iOS)

This document describes the small Swift-facing layer added to `:common-ui`
for a future native iOS consumer. It does not define any UI: screens are
expected to be SwiftUI, while screen state and behavior stay in the Kotlin
ViewModels in `:common-presentation`.

## API surface

Source files:

- `common-ui/src/iosSimulatorArm64Main/kotlin/com/magictools/commandsBox/commonui/nativeapi/NativeViewModelApi.kt`
- `common-ui/src/iosSimulatorArm64Main/kotlin/com/magictools/commandsBox/commonui/nativeapi/IosNativeEntryPoint.kt`

The bridge itself stays in `:common-ui`. The ViewModel, ViewState, event, and
action types it exposes come from `:common-presentation` and are imported from
`com.magictools.commandsBox.commonpresentation`.

Entry points:

- `startIosNativeKoin()` initializes `DispatcherProvider` from
  `:common-presentation` with `Dispatchers.Main` and, if Koin has not been
  started yet, starts it with `iosPlatformModule`, `commonUiModule`, and
  `iosCommonUiModule`. `commonUiModule` includes `commonPresentationModule`,
  so the presentation ViewModels are registered through the existing app
  startup module. The `iosPlatformModule` only provides a fresh
  `SavedStateHandle` factory, matching the existing `:app-ios` setup.
- `createIosViewModelHost()` resolves one Main, Home, and Settings ViewModel
  from the shared Koin graph and returns a `NativeViewModelHost`.

Each bridge exposes only delegation:

- `currentState` reads the ViewModel's `viewState.value`.
- `observeState(onState:)` collects the same `StateFlow` and invokes the
  callback on the main dispatcher.
- `dispatch(event:)` calls the ViewModel's existing `onNewEvent` handler.
- `observeActions(onAction:)` is available on Home and Settings. Settings uses
  it for `SettingsAction.NavigateBack`; Home currently has no actions.

`resolveDarkTheme(selectedTheme:, systemInDarkTheme:)` is public so the native
app can keep the existing rule (`selectedTheme` wins, otherwise follow the
system appearance) instead of duplicating it in Swift.

No bridge stores screen state, validates input, copies data, or implements
business rules.

## Threading and lifecycle

- State and action callbacks are delivered on `Dispatchers.Main`.
- `observeState` and `observeActions` return a `ViewModelSubscription`.
  SwiftUI should cancel it when the owning screen disappears.
- `NativeViewModelHost` owns the three ViewModel instances for the app
  lifetime. `close()` cancels every observation created through the host but
  does not clear the ViewModels, because the ViewModels are app-scoped like
  they are in the existing Compose app. Create one host and keep it alive for
  the lifetime of the native app.
- `dispatch(event:)` is expected to be called on the main thread, as the
  existing Compose screens do.

## Framework export

The `app-ios-native` framework exports `:common-presentation`, `:common-ui`,
`:multi-mvi`, and `:data` in `app-ios-native/build.gradle.kts`.
`:common-ui` contains the Swift-facing bridge, `:common-presentation` contains
the ViewModels and their ViewState/ViewEvent/ViewAction contracts, `:multi-mvi`
supplies `ViewState`, `ViewEvent`, and `ViewAction`, and `:data` supplies
`ThemePreference` used by `SettingsViewState`.

## Verification limitation

The public ABI of the `iosSimulatorArm64` compilation was inspected with the
Kotlin/Native `klib dump-abi` tool. Actual Swift compilation and consumption
cannot be verified until the separate `app-ios-native` module (Phase 2) exists.
