# iOS Simulator handoff

This note records the iOS Simulator preparation for CommandsBox. It is not a
claim that the iOS app was built or run on Ubuntu.

## Open on the MacBook

1. Open `app-ios/iosApp/iosApp.xcodeproj` in Xcode.
2. Select an iOS Simulator destination (an Apple Silicon simulator is required
   because the configured target is `iosSimulatorArm64`).
3. Run the `iosApp` scheme. The Xcode "Run Script" build phase invokes
   `./gradlew :app-ios:embedAndSignAppleFrameworkForXcode` from the repository
   root, then links the generated `ComposeApp.framework`.

The SwiftUI host only starts the shared Compose UI. `MainViewControllerKt.MainViewController()`
initializes Koin, the shared theme, `MainNavHost`, and the toast overlay, and then
renders the shared Compose content.

## Configured targets

- Simulator target: `iosSimulatorArm64` (Apple Silicon simulator).
- Deployment target: iOS 15.0 (Kotlin/Native default minimum for Kotlin 2.4.21).
- Framework: `ComposeApp`, static, produced by `:app-ios`.
- `iosX64` was not configured: Compose Multiplatform 1.12.2 removed Apple x86_64
  targets, and Kotlin 2.4.21 deprecates them.

## Verified on Ubuntu

- Gradle configuration and project resolution for the Android/Desktop/JVM
  targets.
- Kotlin/Native compilation of `:data`, `:common-ui`, and `:app-ios` for
  `iosSimulatorArm64` using `-Pkotlin.native.enableKlibsCrossCompilation=true`.
  This is a Klib cross-compilation check, not a linked framework or an app.
- Android and Desktop builds/tests that can run without Xcode.

## Not verified on Ubuntu

- Framework linking (`linkDebugFrameworkIosSimulatorArm64` is skipped on Linux)
  and `embedAndSignAppleFrameworkForXcode`.
- Xcode project opening, SwiftUI host compilation, simulator launch, and runtime
  behavior.
- Actual iOS UI, database, clipboard, and Koin behavior in the Simulator.

## Requirements discovered during preparation

- `@Database` must be annotated with `@ConstructedBy` and an `expect`/`actual`
  `RoomDatabaseConstructor` for non-Android targets. The KSP metadata task
  (`kspCommonMainKotlinMetadata`) fails without it.
- Room 2.8.5 KSP needs `kspIosSimulatorArm64` in the `:data` module in addition to
  the existing `kspCommonMainMetadata`, `kspAndroid`, and `kspJvm` targets.
- On Linux, KSP's `KspAATask` for iOS only runs when
  `-Pkotlin.native.enableKlibsCrossCompilation=true` is set. Without it the
  KSP task is skipped and `AppDatabaseConstructor` has no actual.
- The Xcode project must be opened on a Mac with Xcode installed; the iOS
  framework cannot be linked on Linux.
- `DEVELOPMENT_TEAM` is intentionally empty. Signing/provisioning is out of scope
  for this preparation task; a local simulator run may require a team or a
  simulator-only signing configuration.
