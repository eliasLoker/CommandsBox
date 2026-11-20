# Moko Resources for `app-ios-native`

## Feature overview

Use Moko Resources in `:app-ios-native` to provide SwiftUI with native-accessible localized strings, images, and fonts, sourced from the existing `:common-resources` catalog. Keep the current Compose Resources setup available to the existing Compose app and shared ViewModels.

## Problem and expected value

The native SwiftUI app currently has its own Xcode asset copies for images and a Kotlin bridge for static labels. Moko Resources provides Swift-facing APIs and native Apple resource bundles, allowing the native UI to consume resources through its resource library while retaining the existing Compose resource system.

Expected value:

- Native SwiftUI code can access localized strings and image/font resources through generated Moko APIs.
- The existing shared catalog remains the source of resource content, reducing manual divergence.
- The Compose app and ViewModels retain their current resource behavior.

## User flow

There is no new user flow. The native app continues to display the same screens, strings, images, and font; the change is to resource delivery and Swift access.

## Functional requirements

- Configure Moko Resources for `:app-ios-native` only.
- Generate or synchronize the Moko resource set from the current `:common-resources` source assets and translations. Do not maintain an independent hand-edited copy of translations or artwork.
- Use Moko's Swift-accessible resources for native SwiftUI static strings, icons, logo, and font where supported.
- Preserve the current language set: base, German, Spanish, French, Hindi, Indonesian, Japanese, Korean, Portuguese, and Russian.
- Keep text supplied by the existing ViewModels on their existing Compose Resources/StringProvider path unless Moko can be used without changing the shared ViewModel behavior.
- Replace existing Xcode asset copies only when the corresponding Moko resource is successfully packaged and used in SwiftUI.
- Keep the existing Compose Resources plugin and usages available for `:common-ui` and `:app-ios`.

## Business rules

- The current `:common-resources` content remains the source of truth for strings and artwork.
- `:app-ios-native` presentation must continue to use the existing ViewModels for business and screen state; Moko is a resource-delivery/access mechanism only.
- No user-visible text, localization fallback, image, font, or screen behavior may change as a consequence of resource integration.

## Edge cases and risks

- Moko Resources has a separate resource layout from Compose Resources. A generated/synchronized resource set must preserve the original values and locale coverage.
- The existing icons are Compose XML vector drawables; Moko's Apple image generation supports SVG. Any conversion must derive from the existing vector paths and preserve their appearance.
- The existing logo is WebP and the font is TTF; confirm Moko handles their native packaging or use generated copies from the shared source files.
- Static Kotlin frameworks require Moko's resource bundle-copy integration in the Xcode build. Confirm the new app target packages and loads the bundle at runtime.
- Existing ViewModel-generated strings still use Compose Resources; this task must not cause missing resources or localization changes in those ViewModels.
- Moko's current release and the repository's Kotlin/Compose versions must be verified by the actual Gradle and Xcode builds. If incompatible, stop and report the evidence rather than silently upgrading Kotlin/Compose or changing shared resource ownership.

## Data and resource behavior

- Current source: `:common-resources` uses JetBrains Compose Resources under `src/commonMain/composeResources/`.
- Native-only Moko resources are generated or synchronized into the `:app-ios-native` build/resource inputs.
- Generated output must not be committed as a separately maintained translation/artwork source unless required by the chosen Moko integration and approved by the owner.
- The existing Compose source resources remain unchanged and continue to serve Compose UI/ViewModels.

## Acceptance criteria

1. Moko Resources is configured for `:app-ios-native` and successfully builds with the repository's existing Kotlin and Compose versions.
2. SwiftUI loads the native app's localized static strings and image/font assets from Moko-generated resources; it does not depend on hand-maintained duplicate localization tables.
3. All supported locales listed above remain available; a non-English simulator test verifies native resource selection.
4. The shared Compose resource source and Compose app continue to build and behave as before.
5. Existing Xcode assets are removed only when their Moko replacement is verified. No missing, unused replacement assets are left in the app target.
6. UI appearance is visually reviewed against the pre-change `app-ios-native` screens in light and dark modes.
7. `:app-ios`, `:common-resources`, and shared ViewModel behavior remain unchanged.

## Out of scope

- Migrating all project modules from Compose Resources to Moko Resources.
- Changing resource behavior or source ownership for `:common-ui`, `:app-ios`, Android, or Desktop.
- Changing ViewModel logic, screen behavior, or product copy.
- Adding physical iOS device targets.
- Upgrading Kotlin or Compose versions solely to accommodate Moko.

## Confirmed decisions

- Use Moko Resources for the native iOS app.
- Scope Moko to `:app-ios-native`; leave the existing Compose resource setup and `:common-resources` module unchanged.
- Generate or synchronize native resources from the current shared resource catalog rather than manually maintaining a second content source.
- Preserve the current locales and native UI appearance.

## Remaining assumptions

- A Gradle generation/synchronization step can translate the current Compose resource layout and vector files into Moko's accepted resource layout without modifying `:common-resources`.
- Moko's current release can be used with the repository's existing Kotlin, Compose, and static framework configuration; build verification is required.
