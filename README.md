# CommandsBox

**CommandsBox** is an open-source CLI command manager. A lightweight and simple utility for developers who prefer a minimalist approach over bloated tools, providing a dedicated space to store and organize frequently used commands.

<p align="center">
  <img src="screenshots/1.png" height="600" alt="CommandsBox UI 1">
  <img src="screenshots/2.png" height="600" alt="CommandsBox UI 2">
</p>

## ✨ Key Features

* **Kotlin Multiplatform:** Built from the ground up using **KMP**.
* **Shared UI:** Almost 100% shared UI code across all platforms with minimal platform-specific adjustments.
* **Multi-Platform Support:** Ready-to-run modules for **Android** and **Desktop** (Windows & Linux) via `:app-android` and `:app-desktop`.
* **Localization:** Supports 10 languages (English, German, French, Spanish, Indonesian, Hindi, Japanese, Korean, Brazilian Portuguese, and Russian) using **JetBrains Compose resources**.
* **Cross-Platform Database:** **Room** implementation that works seamlessly on both Android and Desktop.

## 🛠 Maintenance & Build

The project includes a `linux-release.sh` script in the root directory. This script:
1. Automates the `.deb` package creation.
2. Patches the desktop shortcut file.
3. Correctly initializes `StartupWMClass` for proper Linux dock integration.
---
*Developed by [MagicTools](https://magictools-apps.com)*