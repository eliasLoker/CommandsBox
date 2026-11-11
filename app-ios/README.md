# iOS app: build and run from Xcode

This note covers building and running the existing `:app-ios` CommandsBox app
from the Xcode host project. It records the workflow and the checks that were
actually executed on macOS. Anything under "Not verified" was not run.

## Requirements

- Apple Silicon Mac. Verified on macOS 27.2.1 (arm64).
- Xcode with an Apple Silicon iOS Simulator runtime. Verified with Xcode 27.2
  and the iOS 27.2 Simulator runtime.
- Xcode selected as the developer directory for command-line builds:
  `sudo xcode-select -s /Applications/Xcode.app/Contents/Developer`, or prefix
  commands with `DEVELOPER_DIR=/Applications/Xcode.app/Contents/Developer`.
  On the verification machine `xcode-select -p` pointed at CommandLineTools, so
  the command-line checks used the `DEVELOPER_DIR` prefix. Pressing **Run** in
  the Xcode GUI does not depend on `xcode-select`.
- JDK 21 for Gradle. In the verified setup Gradle used an auto-provisioned
  Eclipse Temurin 21 JDK from `~/.gradle/jdks`; `gradle.properties` sets
  `org.gradle.jvmargs=-Xmx2048m`.
- The only configured iOS target is `iosSimulatorArm64` (Apple Silicon
  simulator).

## Run from Xcode

1. Open `app-ios/iosApp/iosApp.xcodeproj`.
2. Select the `iosApp` scheme and an Apple Silicon iPhone Simulator
   (for example "iPhone 18 Pro").
3. Press **Run**. The "Run Script" build phase changes to the repository root
   and runs `./gradlew :app-ios:embedAndSignAppleFrameworkForXcode`, then Xcode
   links the static `ComposeApp.framework` produced by `:app-ios`.

The app shows the shared Compose UI. `MainViewControllerKt.MainViewController()`
initializes Koin, the shared theme, `MainNavHost` and the toast overlay.
`ContentView.swift` only hosts that Compose view controller; there is no
separate SwiftUI implementation.

## Verified on macOS (2026-11-11)

- `./gradlew :app-ios:linkDebugFrameworkIosSimulatorArm64` with the Xcode 27.2
  toolchain: `BUILD SUCCESSFUL` in 25 s. The daemon stayed up and the framework
  was produced at
  `app-ios/build/bin/iosSimulatorArm64/debugFramework/ComposeApp.framework`.
- `xcodebuild` build for the iPhone 18 Pro Simulator used during verification:
  `** BUILD SUCCEEDED **`. The Gradle Run Script phase reported
  `BUILD SUCCESSFUL`; the Gradle daemon did not disappear. The exact command
  was:

  ```sh
  DEVELOPER_DIR=/Applications/Xcode.app/Contents/Developer \
    xcodebuild -project app-ios/iosApp/iosApp.xcodeproj -scheme iosApp \
    -configuration Debug \
    -destination 'platform=iOS Simulator,id=AA3B6BBD-B10D-450F-94ED-B72519CABFD9' \
    build
  ```

  The `id=` value is that Simulator's UDID on the verification machine; find
  your own with `xcrun simctl list devices available`. A portable equivalent is
  `-destination 'platform=iOS Simulator,name=iPhone 18 Pro'` (the `name=` form
  was not used in this verified run).
- Installed the built app into the booted iPhone 18 Pro (iOS 27.2) Simulator
  with `xcrun simctl install` and started it with `xcrun simctl launch`. After
  30 seconds the process was still alive, no new crash report was produced, and
  a Simulator screenshot showed the shared UI (CommandsBox header,
  "Add first tab", "Command list is empty"). The Simulator locale was Russian,
  so the screenshot displayed the Russian translations
  (`add_tab` / `commands_in_tab_empty`); the English strings above are the
  canonical `values/strings.xml` wording for the same keys.
- The built debug dylib exports `ComposeAppMainViewControllerKt`, confirming
  that the Kotlin framework is linked into the app.
- Every `PBXFileReference` for `ContentView.swift`, `iOSApp.swift`,
  `Info.plist` and `Assets.xcassets` resolves to an existing file under
  `app-ios/iosApp/iosApp/`, and `xcodebuild` compiled the Swift sources from
  those resolved paths.

### Not verified

- Clicking **Run** in the Xcode GUI itself. The equivalent build, install and
  launch path was exercised with `xcodebuild` and `simctl`.
- Opening the individual files from the Xcode project navigator by hand. The
  references were checked by resolving their paths and by a successful
  `xcodebuild` that consumed those files, not by GUI interaction.
- Physical iPhone, signing/provisioning for distribution, release builds.
- Full iOS feature parity (database, clipboard, navigation beyond the initial
  screen).

## The "Gradle build daemon disappeared unexpectedly" failure

The failure was not memory pressure and not the Kotlin/Native linker. Evidence
from the Gradle daemon log (`~/.gradle/daemon/9.9.1/daemon-5145.out.log`) and
the Xcode build activity log:

- `:app-ios:compileKotlinIosSimulatorArm64` completed and
  `:app-ios:linkDebugFrameworkIosSimulatorArm64` started.
- The daemon then logged
  `Daemon will be stopped at the end of the build stop command received` and
  shut down; the Gradle client reported
  `Gradle build daemon disappeared unexpectedly`.
- That message is Gradle's graceful shutdown path for a `Stop` command, not a
  crash or OOM kill. There is no JVM crash log and the daemon reported
  `heap usage: 0% of 2 GiB`.
- The daemon had been started earlier by Android Studio's Gradle sync (its
  startup environment and JDK 21 match the Android Studio Tooling API). Android
  Studio closed the project at 18:09:52 (`Project CommandsBox is disposed`,
  `Unable to send daemon message for Gradle 9.9.1`) and logged
  `IDE SHUTDOWN` at 18:10:42; the daemon stop and the Xcode failure followed at
  about 18:10:46. A Gradle daemon started by an IDE is stopped by that IDE's
  Tooling API connection when the IDE exits, even if Xcode is using the same
  daemon at that moment.

Consequences:

- Framework linking itself works; the link task completed in 25 s standalone
  and Xcode's Gradle phase completed successfully afterwards.
- If Xcode's Run Script fails with "daemon disappeared", check whether Android
  Studio (or another Gradle client) was restarted or closed at that moment.
  Wait for the IDE to finish restarting, then build again. The project's
  Gradle heap settings, dependencies and warning flags were deliberately left
  unchanged because they were not the cause.

## Xcode navigator file references (fixed)

The project referenced `ContentView.swift`, `iOSApp.swift`, `Info.plist` and
`Assets.xcassets` relative to `SRCROOT` (`app-ios/iosApp`), while the files live
in `app-ios/iosApp/iosApp/`. The references now include the `iosApp/` prefix and
resolve to existing files. `xcodebuild` also consumed them successfully: the
same mismatch had caused Xcode's `Build input files cannot be found` error in
the Swift compile phase, and that error is gone.

Opening the individual files from the Xcode project navigator by hand was not
performed (GUI interaction was unavailable during this task), so the navigator
itself is not claimed as manually verified. To check it, open the project in
Xcode and click `ContentView.swift`, `iOSApp.swift`, `Info.plist` and
`Assets.xcassets`; they should open normally now.

The `ComposeApp.framework` reference intentionally points at the generated
`../build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)` path and only resolves
after the Gradle Run Script has produced it.

## Compose `Info.plist` requirement (fixed)

Compose Multiplatform's iOS runtime stops the app when
`CADisableMinimumFrameDurationOnPhone` is missing or `false` on ProMotion
iPhones. A launch during verification failed with
`IllegalStateException` from `androidx.compose.ui.uikit.PlistSanityCheck`.
`app-ios/iosApp/iosApp/Info.plist` now sets:

```xml
<key>CADisableMinimumFrameDurationOnPhone</key>
<true/>
```

With this key the app stayed running and rendered the shared UI in the
Simulator.

## Configured targets

- Simulator target: `iosSimulatorArm64` (Apple Silicon simulator only).
- Deployment target: iOS 15.0 (Kotlin/Native default minimum for Kotlin 2.4.21).
- Framework: `ComposeApp`, static, produced by `:app-ios`.
- `iosX64` is not configured: Compose Multiplatform 1.12.2 removed Apple x86_64
  targets, and Kotlin 2.4.21 deprecates them.

## Linux preparation notes (not a run verification)

- Gradle configuration and project resolution for the Android/Desktop/JVM
  targets.
- Kotlin/Native compilation of `:data`, `:common-ui`, and `:app-ios` for
  `iosSimulatorArm64` using `-Pkotlin.native.enableKlibsCrossCompilation=true`.
  This is a Klib cross-compilation check, not a linked framework or an app.
- `@Database` must be annotated with `@ConstructedBy` and an `expect`/`actual`
  `RoomDatabaseConstructor` for non-Android targets. The KSP metadata task
  (`kspCommonMainKotlinMetadata`) fails without it.
- Room 2.8.5 KSP needs `kspIosSimulatorArm64` in the `:data` module in addition
  to the existing `kspCommonMainMetadata`, `kspAndroid`, and `kspJvm` targets.
- On Linux, KSP's `KspAATask` for iOS only runs when
  `-Pkotlin.native.enableKlibsCrossCompilation=true` is set. Without it the KSP
  task is skipped and `AppDatabaseConstructor` has no actual.
- Framework linking and `embedAndSignAppleFrameworkForXcode` cannot run on
  Linux; a Mac with Xcode is required.

## Limitations

- `DEVELOPMENT_TEAM` is intentionally empty. Signing/provisioning for a
  physical device or distribution is out of scope; simulator runs do not need a
  team.
- The Run Script phase is left as an always-run phase. Xcode's note that it runs
  on every build is informational.
- The `expect`/`actual` Kotlin warnings are expected and are not suppressed.
