# CommandsBox

**CommandsBox** is an open-source CLI command manager. A lightweight and simple utility for developers who prefer a minimalist approach over bloated tools, providing a dedicated space to store and organize frequently used commands.

<p align="center">
  <img src="screenshots/1.png" height="600" alt="CommandsBox UI 1">
  <img src="screenshots/2.png" height="600" alt="CommandsBox UI 2">
</p>

## 🧪 About this project

This repository is a personal test project: a place for the author's own
thoughts and experiments in cross-platform development. It is a playground for
trying out architecture, shared UI, and platform integration approaches with
Kotlin Multiplatform, so its shape can change as those experiments evolve. It is
not a product roadmap and makes no stability promises.

Because of that, some modules and targets are intentionally more complete than
others. The iOS support described below is a working Simulator path, not a
commitment to ship on the App Store.

## ✨ Key Features

* **Kotlin Multiplatform:** Built from the ground up using **KMP**.
* **Shared UI:** Android, Desktop, and the Compose-based iOS app share almost 100% of their UI code through **Compose Multiplatform**. The separate SwiftUI iOS app (`:app-ios-native`) has native screens and reuses the shared ViewModels instead.
* **Multi-Platform Support:** Grouped by platform family — **Mobile** via `:app-android` on Android and `:app-ios` (Compose UI) and `:app-ios-native` (SwiftUI UI) on the iOS Simulator, and **Desktop** (Windows and Linux) via `:app-desktop`.
* **Localization:** Supports 10 languages (English, German, French, Spanish, Indonesian, Hindi, Japanese, Korean, Brazilian Portuguese, and Russian). The Compose UI and shared ViewModel text use **JetBrains Compose resources**; the native SwiftUI app (`:app-ios-native`) uses **Moko Resources** for its labels and assets. Both are sourced from `:common-resources` (Moko inputs are generated from the shared resources).
* **Cross-Platform Database:** **Room** implementation that works on Android, Desktop, and the iOS Simulator.

## 📦 Modules

| Module | Responsibility |
| --- | --- |
| `:app-android` | Android application entry point. |
| `:app-desktop` | Desktop (JVM) application entry point and native packaging. |
| `:app-ios` | Compose iOS entry point, Compose framework, and the Xcode host project. |
| `:app-ios-native` | Native SwiftUI iOS entry point, app-owned Kotlin ViewModel bridge and iOS startup/clipboard bindings, Moko resources generated from `:common-resources`, and the `CommandsBoxNative` Xcode host project. Does not host Compose UI or depend on `:common-ui`. |
| `:common-presentation` | Shared ViewModels, presentation contracts, and presentation dependency injection. |
| `:common-ui` | Shared Compose UI, navigation, and UI dependency injection. |
| `:data` | Repositories and Room persistence, with platform-specific database setup. |
| `:common-resources` | Shared Compose resources and localized strings. |
| `:multi-mvi` | Shared MVI state, events, actions, and collection helpers. |

## ✅ Requirements

* **JDK 21** — the Gradle modules use `jvmToolchain(21)`.
* **Android SDK** matching `compileSdk` / `targetSdk` from `gradle/libs.versions.toml`.
* **macOS with Xcode** for the iOS targets. The Compose host project lives in
  `app-ios/iosApp/`, and the native SwiftUI host project lives in
  `app-ios-native/iosApp/`.

## 🚀 Running the app

**Android**

```sh
./gradlew :app-android:assembleDebug
```

This produces `app-android/build/outputs/apk/debug/app-android-debug.apk`, which
can be installed with Android Studio or `adb install` on a device or emulator.

**Desktop**

```sh
./gradlew :app-desktop:run
```

**iOS (Apple Silicon Simulator)**

1. Open `app-ios/iosApp/iosApp.xcodeproj` in Xcode.
2. Select the `iosApp` scheme and an Apple Silicon iPhone Simulator.
3. Press **Run**.

The Xcode "Run Script" build phase builds the shared `ComposeApp` framework
through Gradle, so no separate Gradle step is needed. See
[`app-ios/README.md`](app-ios/README.md) for requirements, verification details,
and known limitations.

**iOS native SwiftUI app (Apple Silicon Simulator)**

1. Open `app-ios-native/iosApp/CommandsBoxNative.xcodeproj` in Xcode.
2. Select the `CommandsBoxNative` scheme and an Apple Silicon iPhone Simulator.
3. Press **Run**.

Its "Run Script" phase embeds and signs the static `NativeApp` framework through
`:app-ios-native:embedAndSignAppleFrameworkForXcode`; a `Copy Moko Resources`
phase packages the generated resource bundle. See
[`app-ios-native/README.md`](app-ios-native/README.md) for build, run, UI-test,
localization, and resource details.

## 🍎 iOS status

* Two separate iOS Simulator apps are configured. Both target
  `iosSimulatorArm64` only (Apple Silicon Simulator, deployment target iOS 15.0);
  `iosX64` is not configured.
* `:app-ios` — Compose Multiplatform UI with shared navigation and ViewModels.
  Xcode project: `app-ios/iosApp/iosApp.xcodeproj`, scheme `iosApp`, static
  framework `ComposeApp`.
* `:app-ios-native` — native SwiftUI screens that reuse the Main, Home, and
  Settings ViewModels from `:common-presentation` through the app-owned iOS
  native bridge. It does not host Compose UI or depend on `:common-ui`. Xcode
  project: `app-ios-native/iosApp/CommandsBoxNative.xcodeproj`, scheme
  `CommandsBoxNative`, static framework `NativeApp`. See
  [`app-ios-native/README.md`](app-ios-native/README.md).
* Both apps use the shared `:data` repositories, Room schema, MVI helpers, and
  the iOS platform implementations that are available (Room database in the app
  sandbox, clipboard access, and Metro dependency wiring).
* Localization and assets: the Compose app uses Compose Resources. Moko Resources
  is configured only for `:app-ios-native`, with generated inputs derived from
  the same `:common-resources` strings, images, and fonts; text produced by the
  shared ViewModels still uses the Compose Resources string provider.
* Not configured: physical iPhone, release builds, and distribution signing /
  provisioning.

## 🛠 Maintenance & Build

The project includes a `linux-release.sh` script in the root directory. This script:
1. Automates the `.deb` package creation.
2. Patches the desktop shortcut file.
3. Correctly initializes `StartupWMClass` for proper Linux dock integration.

## 📚 Documentation

* `docs/architecture/` — engineering guidance, including the MVI conventions.
* `docs/product/specs/` — approved product and platform specifications.

---
*Developed by [MagicTools](https://magictools-apps.com)*
