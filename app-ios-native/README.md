# Native SwiftUI iOS app (`:app-ios-native`)

This module is the native iOS host for CommandsBox. Every screen is SwiftUI; the
business behavior comes from the shared `:common-presentation` ViewModels through
the app-owned bridge in
`app-ios-native/src/iosSimulatorArm64Main/kotlin/com/magictools/commandsbox/appiosnative/nativeapi/`.
There is no Compose view controller or composable in this module, no dependency
on `:common-ui`, and `:app-ios` is not changed by it.

## Build and run

The only configured target is `iosSimulatorArm64` (Apple Silicon simulator).
The Xcode app and UI-test targets use a deployment target of iOS 15.0, matching
the existing `:app-ios` project and the framework's `MinimumOSVersion`.

```sh
DEVELOPER_DIR=/Applications/Xcode.app/Contents/Developer \
  ./gradlew :app-ios-native:linkDebugFrameworkIosSimulatorArm64
```

Open `app-ios-native/iosApp/CommandsBoxNative.xcodeproj`, select the
`CommandsBoxNative` scheme and an Apple Silicon simulator, then Run. The Xcode
Run Script phase calls:

```sh
./gradlew :app-ios-native:embedAndSignAppleFrameworkForXcode
```

`DEVELOPER_DIR` may be needed for command-line builds when `xcode-select` points
at CommandLineTools.

The Xcode target has two Gradle build phases: `Run Script` embeds and signs the
static `NativeApp` framework, and `Copy Moko Resources` copies the framework's
Moko resource bundle into the app. Both phases are required for localized
strings, images and fonts to be available at runtime.

Moko 0.27.1 invokes its Apple asset compilation with an empty process
environment, which drops `DEVELOPER_DIR`. When `xcode-select` points at
CommandLineTools, that internal `actool` call cannot find the Xcode toolchain.
`:app-ios-native` therefore precompiles the generated asset catalog from its
Kotlin compile task with an explicit developer directory and removes the source
catalog before Moko's copy step. The workaround is confined to the native app
target and applies only when Moko generates the Apple asset catalog.

Run the UI tests with:

```sh
DEVELOPER_DIR=/Applications/Xcode.app/Contents/Developer \
  xcodebuild test \
  -project app-ios-native/iosApp/CommandsBoxNative.xcodeproj \
  -scheme CommandsBoxNative -configuration Debug \
  -destination 'platform=iOS Simulator,name=iPhone 18 Pro' \
  -only-testing:CommandsBoxNativeUITests
```

Uninstall the app first if you need a clean database:

```sh
xcrun simctl uninstall <device-udid> com.magictools.commandsbox.iosNative
```

## ViewModel and lifecycle

`NativeAppHost` creates one app-lifetime `NativeViewModelHost` after
`startIosNativeKoin()`, observes Main/Home/Settings state and Settings actions,
and dispatches the existing events. `startIosNativeKoin()` starts Koin with the
presentation module, the iOS data module, and this app's `SavedStateHandle` and
clipboard bindings. The only Swift-side state is presentation
state: the current screen, local text-control editing buffers, focus and the
toast overlay animation. Dialog labels, hints, toast messages and theme
preferences all still come from the shared ViewModels.

## Localization

No Swift translation table was added. SwiftUI static labels are loaded from the
same `:common-resources` string catalog through Moko Resources, generated into
the app module's build inputs by `:app-ios-native:syncMokoResources`. The Xcode
`Copy Moko Resources` phase packages the generated bundle, so base plus `de`,
`es`, `fr`, `hi`, `id`, `ja`, `ko`, `pt` and `ru` are available in the native
app. Text produced by the shared ViewModels continues to use the existing
Compose Resources `StringProviderImpl` path.

## Assets and fonts

The native app reuses the existing `:common-resources` artwork. The
`syncMokoResources` task derives the Moko resource tree from the shared sources:
Compose vector XML is converted to SVG, the shared WebP logo is rendered to the
1x/2x/3x PNG slots, and the shared TTF is copied. Moko's generated Swift APIs
are used from `MokoResources.swift`, and `Assets.xcassets` now only contains the
app icon.

No new artwork was designed for this app. `:common-resources` is unchanged.

## Known differences and limitations

- Native text and control rendering cannot be pixel-identical to Compose. The
  colors, spacing, radii, typography tokens, icons and animation timings were
  copied from the shared theme and screens. Tab content uses a native
  slide/fade approximation of the Compose `AnimatedContent` transition, and the
  splash's vertical spacer weights are approximated with SwiftUI spacers.
- Dialogs use the app's `backgroundSecondary` card color instead of the Material
  default dialog surface; the layout and actions match the shared dialogs.
- The toast overlay auto-dismisses through `OnToastDismissed`, as in the current
  Compose app.
- The UI test runner cannot read the simulator pasteboard (`Operation not
  authorized`), so clipboard content is not asserted in the test. The copy
  action goes through the unchanged shared `CustomClipboardManager`, and the
  success toast from the shared ViewModel is verified.
- Error toasts (repository failure path) are not triggered by the UI tests;
  the failure path is the same shared `showFailureToast()` implementation.
- The linker emits a warning that `libicu.icudtl_dat.o` inside the Kotlin/Native
  framework was built for a newer iOS-simulator version than the app's iOS 15.0
  deployment target. The framework's `MinimumOSVersion` is 15.0, and the iOS
  Simulator build, install and UI tests succeed with the 15.0 target, so the
  warning is not treated as a minimum-version requirement. The UI-test target
  also gets non-fatal warnings that the installed XCTest/XCUIAutomation dylibs
  were built for iOS 17.0; those are test-runner libraries, not app code.
- Multiline inputs use the SwiftUI `TextField(axis:)` API on iOS 16+ and a
  `TextEditor` fallback on iOS 15. The configured simulator (iOS 27.2)
  exercises the modern path.
- Physical-device builds and release signing are out of scope; the configured
  target is the Apple Silicon simulator.
