# Repository Guide

CommandsBox is a Kotlin Multiplatform app for storing and organizing CLI commands. Android and Desktop are the current app targets; inspect module source sets before assuming target support. Treat source code as the current truth; do not assume planned features or platform support are implemented.

## Modules

- `:app-android`, `:app-desktop` — platform entry points and app setup.
- `:common-ui` — shared Compose UI, navigation, ViewModels, and UI DI.
- `:data` — repositories and Room persistence; platform-specific database setup lives in source sets.
- `:common-resources` — shared Compose resources and localized strings.
- `:multi-mvi` — shared MVI state, events, actions, and collection helpers.

## Stack and Conventions

- Kotlin Multiplatform, Compose Multiplatform, Coroutines/Flow, custom MVI, Koin, kotlinx.serialization, Room/SQLite, and Compose Navigation.
- Keep code in `commonMain` when its dependencies permit; follow existing platform source sets and module boundaries.
- Use the Gradle wrapper and `gradle/libs.versions.toml` for builds and dependency versions. Run relevant checks and report exactly what ran and its result.
- Before changing screens, ViewModels, or MVI, read `docs/architecture/mvi.md` and inspect the current implementation, especially `HomeScreen` and `HomeViewModel`.

## Documentation

- `docs/architecture/` contains engineering guidance; follow it for the relevant area.
- `docs/product/specs/` is for approved product specifications; `docs/product/decisions/` is for product decisions. Keep product documentation in English and distinguish decisions from assumptions.
