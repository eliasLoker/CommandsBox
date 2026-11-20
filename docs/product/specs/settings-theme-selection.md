# Settings: Theme Selection

## Feature overview

Add a Settings screen with a Light/Dark theme switch. Users open Settings from a settings icon in the Home screen header. Their selected theme is saved and applies across the app.

## Problem and expected value

The app currently follows the device theme, but users have no in-app way to choose Light or Dark. This feature gives them control while retaining the current system-following behavior until they make a choice.

## User flow

1. On first launch, the app follows the device theme.
2. The user taps the settings icon in the Home screen header.
3. The Settings screen shows the current theme and a switch.
4. The user changes the switch; the app theme updates immediately.
5. The choice is saved locally.
6. On later launches, the app uses the saved choice rather than following device theme changes.
7. Back navigation returns to Home.

## Functional requirements

- Provide a Settings screen reachable from Home via a settings icon in the header.
- In the Home header banner, inset the app logo/name cluster from the left edge and the settings button from the right edge by 16 dp. The banner already has 16 dp outer screen spacing; this requirement adds inset inside the banner.
- Provide only **Light** and **Dark** choices—no System option in Settings.
- The switch updates the app-wide theme immediately, including the Settings screen.
- Save the selected theme locally in the `:data` module.
- Use a KMP-compatible preferences/persistence dependency that supports Android, iOS, and Desktop/JVM; do not use a platform-specific-only dependency for this feature.
- If no valid saved choice exists, follow the device theme.
- Display the current effective theme state in the switch.

## Business rules

- Before the user makes a selection, the app follows the device theme.
- Once the user selects Light or Dark, that explicit choice persists across launches and takes precedence over device theme changes.
- The agreed switch semantics are **off = Light, on = Dark**.

## Edge cases

- Missing or invalid stored values fall back to following the device theme.
- If the user has selected a theme, later device theme changes do not override that choice.
- Because Settings has no System option, the user cannot switch back to following the device theme through the UI after making a selection. Clearing app data would remove the saved choice.

## Acceptance criteria

- With no saved theme choice, the app uses the device theme.
- The Home header provides a way to open Settings, and back navigation returns to Home.
- The switch represents Light/Dark and changing it updates the theme across the app without restarting.
- The selected theme is stored through the `:data` module and restored after restarting the app.
- After a saved selection, changing the device theme does not change the app theme.
- Missing or invalid stored data falls back to the device theme.
- The preferences dependency used supports Android, iOS, and Desktop/JVM.

## Out of scope

- A System option or a UI action to reset the choice to System.
- Additional settings.
- Cloud sync or account-level preferences.

## Confirmed decisions

- The Settings screen is opened from a settings icon in the Home screen header.
- The Home header logo/name cluster and settings button have 16 dp horizontal insets inside the banner.
- The available choices are Light and Dark only.
- The choice is saved.
- Theme persistence belongs in `:data`, using a KMP-compatible preferences-style dependency.
- The app follows the device theme until the user makes a selection.
- The switch is off for Light and on for Dark.

## Remaining assumptions

- None.
