# `:common-presentation` Module Separation

## Feature overview

Split presentation logic from the existing `:common-ui` module by introducing a multiplatform `:common-presentation` module.

`:common-ui` will retain screens, navigation, and design-system UI. `:common-presentation` will contain ViewModels and their presentation contracts, including ViewState, ViewEvent, and ViewAction types.

## Problem and expected value

The current `:common-ui` module combines Compose UI and navigation with ViewModels and their state and interaction contracts. Separating these responsibilities should clarify module ownership and keep presentation logic independent of the UI module.

## User flow

No user-visible behavior is intended to change:

1. The user opens a screen through the existing navigation.
2. The screen obtains its ViewModel through the existing dependency-injection setup.
3. The screen displays the ViewModel's state and sends it events.
4. The ViewModel updates state or emits an action.
5. The screen renders the updated state or handles the action.

## Functional requirements

- Create a multiplatform `:common-presentation` module.
- Move ViewModels and their presentation contracts there, including ViewState, ViewEvent, and ViewAction types.
- Keep screens, navigation, and design-system UI in `:common-ui`.
- Preserve existing user flows and behavior.
- Ensure `:common-ui` can consume the presentation types it needs without creating a dependency cycle.
- Keep ViewModel registration available through the existing app startup and Koin setup.

## Business rules

- The intended dependency direction is `:common-ui` → `:common-presentation`. `:common-presentation` must not depend on `:common-ui`.
- Keep the module compatible with the project's current shared targets; do not make extracted presentation code Android-only.
- Treat this as a module-boundary change, not a product behavior change.

## Edge cases

- Some ViewModels currently use types and utilities located in `:common-ui`. Those dependencies need an appropriate home that does not create a cycle.
- Current app entry points initialize Koin through `commonUiModule`; the extraction must preserve ViewModel registration for each app entry point.
- State and contract types used by both screens and ViewModels must remain accessible to both modules.

## Data and AI behavior

Not applicable. This change does not alter persisted data or AI behavior.

## Acceptance criteria

- `:common-presentation` contains the ViewModels and their agreed presentation contracts.
- `:common-ui` retains screens, navigation, and design-system UI, and can use the contracts it needs.
- There is no circular dependency between the modules.
- Existing platform app startup still registers and resolves the required ViewModels.
- Existing user flows remain unchanged.
- Relevant builds and tests are run, with actual results reported.

## Out of scope

- New screens, navigation flows, or user-facing behavior.
- Refactoring ViewModel logic beyond changes needed for the module split.
- Broader architectural changes unrelated to this separation.

## Confirmed decisions

- The new module is named `:common-presentation`.
- ViewState, ViewEvent, and ViewAction types move with the ViewModels.
- User-visible behavior remains unchanged.

## Remaining assumptions

- The module should support the same platforms currently used by shared code.
- Any other shared types or utilities needed by ViewModels will be relocated only as necessary to preserve the module boundary; their exact destination can be determined during technical preparation.
