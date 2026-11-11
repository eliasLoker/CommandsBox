# Make the iOS App Launch from Xcode

## Feature overview

Make the existing `:app-ios` app build and launch from the Xcode project on the owner's M1 Mac, using an Apple Silicon iOS Simulator. Correct the Xcode navigator references so the app's native files open from the project.

## Problem and expected value

The iOS module and Xcode host exist, but pressing **Run** fails while Gradle links the Kotlin framework. Some files in Xcode's project navigator also fail to open. Resolving both issues should let the owner run and inspect the shared CommandsBox app through Xcode.

## User flow

1. Open `app-ios/iosApp/iosApp.xcodeproj` in Xcode.
2. Select the `iosApp` scheme and an available Apple Silicon iOS Simulator.
3. Press **Run**.
4. Xcode builds and links the Kotlin framework, launches the app in the Simulator, and displays the existing shared Compose UI.
5. The Swift and plist files shown in the Xcode project navigator open correctly.

## Functional requirements

1. Diagnose why the Gradle daemon disappears during `:app-ios:linkDebugFrameworkIosSimulatorArm64`, then apply the smallest evidence-based fix.
2. Verify that the Xcode Run Script can build and embed the `ComposeApp` framework for the selected Simulator.
3. Correct Xcode project file references so the native app source and configuration files represented in the navigator resolve to their actual locations and open.
4. Confirm that pressing **Run** launches the app in an Apple Silicon Simulator and displays the shared app UI.
5. Update the iOS handoff documentation with the verified Xcode and Simulator workflow, requirements, and any remaining limitations.
6. Report exactly which builds and launch checks were run and their results.

## Business rules and constraints

- Preserve the existing shared Compose UI and app behavior; this is platform setup and launch troubleshooting, not a UI change.
- Keep the configured `iosSimulatorArm64` target unless investigation shows a concrete compatibility issue.
- Do not assume the daemon failure is caused by memory or make broad dependency upgrades without evidence.
- Treat the `expect`/`actual` messages as warnings unless they prove to be a blocker. Do not suppress them just to make the log quieter.
- The “Run Script will be run during every build” note is informational; don't change the build phase solely to silence it.

## Edge cases and risks

- The Gradle daemon may be crashing or being terminated for a reason not visible in the supplied log. The cause must be identified or clearly reported if it cannot be determined.
- A successful framework build alone is not enough: the Xcode host must also build and start in the Simulator.
- If a file-opening failure remains after correcting project references, report which files fail and how to reproduce it.

## Acceptance criteria

1. The Xcode `iosApp` target builds successfully for an Apple Silicon Simulator.
2. The Gradle framework-linking step completes without the daemon disappearing.
3. Pressing **Run** launches CommandsBox in the Simulator and displays the existing shared UI.
4. The native files represented in the Xcode navigator open successfully.
5. No unrelated Android/Desktop behavior or shared user-visible functionality changes.
6. Documentation and the final report distinguish verified results from anything not tested.

## Out of scope

- Physical iPhone support, App Store release, provisioning, or signing for distribution.
- New features, UI redesign, and full iOS feature-parity testing.
- Unrelated dependency upgrades or suppressing non-blocking warnings without a demonstrated need.

## Confirmed decisions

- The goal is to launch the existing iOS module through Xcode on the owner's M1 Mac.
- The current launch attempt targets an iOS Simulator.
- The observed build failure is the Gradle daemon disappearing during framework linking; the Kotlin messages provided are warnings.

## Remaining assumptions

- “Launch” means run in the iOS Simulator, not on a physical iPhone.
- The intended UI is the existing shared Compose app, not a separate SwiftUI implementation.
