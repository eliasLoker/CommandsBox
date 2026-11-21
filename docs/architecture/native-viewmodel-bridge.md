# Native ViewModel bridge (iOS)

This document describes the Swift-facing layer owned by `:app-ios-native` for the
native SwiftUI app. It does not define any UI: screens are SwiftUI, while screen
state and behavior stay in the Kotlin ViewModels in `:common-presentation`.

The bridge used to live in `:common-ui`. It now belongs to `:app-ios-native`, so
the native app no longer depends on the Compose UI module.

## API surface

Source files:

- `app-ios-native/src/iosSimulatorArm64Main/kotlin/com/magictools/commandsbox/appiosnative/nativeapi/NativeViewModelApi.kt`
- `app-ios-native/src/iosSimulatorArm64Main/kotlin/com/magictools/commandsbox/appiosnative/nativeapi/IosNativeEntryPoint.kt`

The ViewModel, ViewState, event, and action types it exposes come from
`:common-presentation` and are imported from
`com.magictools.commandsBox.commonpresentation`.

Entry points:

- `startIosNativeKoin()` initializes `DispatcherProvider` from
  `:common-presentation` with `Dispatchers.Main` and, if Koin has not been
  started yet, starts it with `iosNativePlatformModule`,
  `commonPresentationModule`, and `iosDataModule`.
  - `iosNativePlatformModule` provides a fresh `SavedStateHandle` factory and
    binds the app-owned `IosNativeClipboardManager` to the presentation
    `CustomClipboardManager` contract.
  - `commonPresentationModule` registers the presentation ViewModels and
    includes the shared `dataModule`.
  - `iosDataModule` supplies the iOS `RoomDatabase.Builder<AppDatabase>` that
    `dataModule` requires.
- `createIosViewModelHost()` resolves one Main, Home, and Settings ViewModel
  from the shared Koin graph and returns a `NativeViewModelHost`.

Each bridge exposes only delegation:

- `currentState` reads the ViewModel's `viewState.value`.
- `observeState(onState:)` collects the same `StateFlow` and invokes the
  callback on the main dispatcher.
- `dispatch(event:)` calls the ViewModel's existing `onNewEvent` handler.
- `observeActions(onAction:)` is available on Home and Settings. Settings uses
  it for `SettingsAction.NavigateBack`; Home currently has no actions.

`NativeUiStringsKt.isDarkTheme(settingsState:, systemInDarkTheme:)` keeps the
existing theme rule (`selectedTheme` wins, otherwise follow the system
appearance) in the native app module instead of depending on the
`:common-ui` theme helper.

No bridge stores screen state, validates input, copies data, or implements
business rules.

## Threading and lifecycle

- State and action callbacks are delivered on `Dispatchers.Main`.
- `observeState` and `observeActions` return a `ViewModelSubscription`.
  SwiftUI should cancel it when the owning screen disappears.
- `NativeViewModelHost` owns the three ViewModel instances for the app
  lifetime. `close()` cancels every observation created through the host but
  does not clear the ViewModels, because the ViewModels are app-scoped like
  they are in the Compose app. Create one host and keep it alive for the
  lifetime of the native app.
- `dispatch(event:)` is expected to be called on the main thread, as the
  existing Compose screens do.

## Clipboard binding

`IosNativeClipboardManager` in
`app-ios-native/src/iosSimulatorArm64Main/kotlin/com/magictools/commandsbox/appiosnative/IosNativeClipboardManager.kt`
implements the presentation `CustomClipboardManager` contract using
`UIPasteboard`. It is registered by `iosNativePlatformModule` and remains
outside `:common-presentation`, which does not contain platform adapters.

`:common-ui` keeps its own iOS clipboard adapter and `iosCommonUiModule` for the
Compose-based `:app-ios` app.

## Framework export

The `app-ios-native` framework exports `:common-presentation`, `:multi-mvi`,
and `:data` in `app-ios-native/build.gradle.kts`. `:common-ui` is not part of
the native app's dependency or export configuration. `:common-presentation`
contains the ViewModels and their ViewState/ViewEvent/ViewAction contracts,
`:multi-mvi` supplies `ViewState`, `ViewEvent`, and `ViewAction`, and `:data`
supplies `ThemePreference` used by `SettingsViewState`.

## Verification

The `iosSimulatorArm64` framework is linked with
`./gradlew :app-ios-native:linkDebugFrameworkIosSimulatorArm64`, and the Swift
host consumes it through the `NativeApp` module in
`app-ios-native/iosApp/CommandsBoxNative.xcodeproj`. The native UI test target
exercises the Main, Home, and Settings flows against that framework.
