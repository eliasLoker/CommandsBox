# Prepare CommandsBox for iOS Simulator

## Feature overview

Prepare the existing CommandsBox project so it has an iOS Simulator app path, reusing the current shared Kotlin and Compose code. Add a new `:app-ios` Gradle module for iOS app startup and framework configuration, with a minimal SwiftUI/Xcode host project inside it. The implementation agent is not required to build or run the iOS app in Simulator.

## Problem and expected value

The repository currently has Android and Desktop targets but no iOS target or app host. Preparing the targets, platform services, and host project on Ubuntu should leave a concrete starting point for opening and testing the app on macOS later.

## User flow

When the project is later opened on macOS, the owner should be able to open the iOS host project in Xcode, select an available Simulator, and attempt to launch the shared CommandsBox app.

The app should use the existing startup flow, navigation, Home UI, and repository behavior. This preparation task does not require a full feature-by-feature parity validation.

## Functional requirements

1. Configure appropriate iOS Simulator targets in the shared dependency chain: `:common-ui`, `:data`, `:common-resources`, and `:multi-mvi`.
2. Add required `iosMain` source sets and keep existing common code shared where its dependencies permit. Configure the Simulator architecture supported by the current project setup and document the selected target(s).
3. Create a new `:app-ios` Gradle module and include it in `settings.gradle.kts`. Configure the module for the selected iOS Simulator target(s), make it depend on the shared UI module, and configure it to produce the framework consumed by the native host.
4. Reuse the current Room database, entities, DAO, repository, and schema. Add an iOS database-builder implementation that uses a persistent app-sandbox path.
5. Configure Room/KSP for the chosen iOS target(s) as required by the versions pinned in the project. Verify the configuration against the current dependency documentation rather than assuming the Android/JVM KSP setup covers iOS.
6. Implement `CustomClipboardManager` for iOS so command-copy functionality has a platform implementation.
7. Add iOS-specific Koin wiring that registers the clipboard and database modules. Keep platform service implementations in their shared platform modules where appropriate; use `:app-ios` for app startup and composition of the app's modules. Ensure shared dependencies can be resolved, including `SavedStateHandle` if the selected iOS Koin/ViewModel integration requires it.
8. Provide an iOS actual for `Modifier.onDesktopScroll`. Keep it neutral on iOS unless an appropriate iOS behavior is verified.
9. Provide the `:app-ios` Kotlin entry point that initializes Koin and hosts the existing theme, `MainNavHost`, and toast UI before rendering the shared UI.
10. Create a minimal SwiftUI/Xcode project under `app-ios` and host code that invokes the framework. Keep native code limited to app startup and hosting the shared Compose content.
11. Run relevant checks available on Ubuntu, such as project configuration and existing Android/Desktop checks. Do not claim an iOS build or Simulator run passed unless it was actually run on macOS.
12. Add a short handoff note stating what to open or run on the MacBook, configured Simulator target(s), what could not be verified on Ubuntu, and discovered Xcode, Gradle, or dependency requirements.

## Business rules

- Reuse the existing shared Compose screens, navigation, ViewModels, MVI, repository, and data model.
- Use `:app-ios` as the iOS platform entry-point module, parallel to `:app-android` and `:app-desktop`; it owns iOS startup, the Compose framework entry point, and app-level Koin initialization.
- Keep the Xcode host project inside `app-ios`; Gradle configures/builds the Kotlin framework, while Xcode owns the native Simulator app host.
- Keep platform-specific implementations in the appropriate iOS source sets.
- Do not change shared user-visible behavior as part of this platform setup.
- Follow `docs/architecture/mvi.md`; this task should not create new screens or introduce a new MVI pattern.
- Avoid unrelated dependency upgrades or architecture changes.
- Record the chosen iOS deployment target and Simulator architecture(s). These are implementation constraints for this preparation task, not a commitment to release support.

## Edge cases and risks

- Room/KSP Native target configuration may differ from the existing Android/JVM configuration.
- The Xcode host project, framework linkage, or native dependency integration may need correction on macOS. Report this as unverified if it cannot be checked on Ubuntu.
- If an existing plugin or dependency version blocks an iOS target, report the exact blocker and proposed minimal resolution instead of making broad upgrades.
- Do not claim Simulator readiness based only on the presence of project files.

## Acceptance criteria

1. The shared modules have iOS Simulator target configuration appropriate to the existing dependency graph.
2. `:app-ios` is included in Gradle settings, configures the selected iOS Simulator target(s), and exposes the shared Compose framework to its native host.
3. iOS platform source includes the required database, clipboard, DI, and `onDesktopScroll` implementations.
4. The project contains a minimal Xcode/SwiftUI host under `app-ios`, wired to the shared Compose entry point.
5. No unrelated user-facing functionality or shared MVI behavior is changed.
6. Relevant Ubuntu-runnable checks are executed and their exact results reported.
7. The final report clearly says that Xcode build and Simulator launch remain unverified and are deferred to the MacBook.
8. The report lists changed files, configured targets, known blockers, and Mac follow-up steps.

## Out of scope

- Building or running the app in Xcode or an iOS Simulator during this Ubuntu task.
- Full iOS feature-parity testing.
- App Store distribution, release signing, provisioning, or physical-device support.
- New app behavior, UI redesign, data-model changes, or unrelated refactoring.

## Confirmed decisions

- The target is an app that runs in the iOS Simulator.
- Add `:app-ios` as a Gradle module to match the platform-entry-point organization of `:app-android` and `:app-desktop`.
- Keep the Xcode app host inside `app-ios`; it is distinct from the Kotlin framework Gradle module.
- Work will be done on Ubuntu; Simulator build and run are deferred to the MacBook.
- Create the minimal SwiftUI/Xcode host project now, even though it cannot be verified on Ubuntu.
- Reuse the existing shared UI and behavior; do not build a separate SwiftUI version of the app.
- App Store, release, signing, and physical-device work are out of scope.

## Remaining assumptions

- The configured Simulator architecture(s) will be compatible with the owner's MacBook and installed Xcode; this must be confirmed during later Mac testing.
- The iOS deployment target will be selected based on current dependency requirements and documented by the implementation agent.
