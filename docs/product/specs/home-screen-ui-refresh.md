# Refresh Home Screen UI Design

## Feature overview

Visually refresh the existing Home screen so the elements already present on it (header banner, tab row, add banner, command cards, empty state, dialogs) look more attractive and consistent, using a terminal-green accent color. This is a design-only change: no new screens, no new user-visible features, no MVI/state/event/ViewModel changes.

## Problem and expected value

The current Home screen is flat greys with no accent color, no press feedback (all clickables use `indication = null`), inconsistent corner radii, a noisy tab row where every tab permanently shows up to four icons, a weak 4dp selection underline, a barely visible empty state, and stock Material dialogs. Refreshing the design gives the screen visual hierarchy and polish while keeping the existing structure and behavior.

## User flow

1. The user opens the app and sees the Home screen with a softly gradient header, a logo with an accent ring, and the app name.
2. The selected tab is clearly marked by an animated green pill; only the selected tab shows its edit/delete/move actions, keeping the tab row uncluttered.
3. The add banner and command cards are visually separated from the background with subtle borders; primary buttons are terminal-green.
4. Pressing any actionable element produces ripple feedback.
5. With no commands in the tab, a centered empty state with an icon and message is shown.
6. Dialogs use the app typography and colored confirm buttons (green for confirm, red for destructive confirm).

## Functional requirements

1. Extend `AppColors` with accent tokens used across the screen: `accent`, `accentText` (accessible accent text on background), `onAccent` (text on accent fill), and `destructive`. Light theme uses a darker terminal green for text/fills that must pass contrast; dark theme uses a bright terminal green (`#4ADE80`).
2. Add an `AppShapes` corner-radius token object (small 8dp, medium 12dp, large 16dp, banner 24dp) and apply it to the refreshed elements instead of ad hoc radii.
3. Restore bounded ripple indication on actionable elements in `HomeScreen`, `AddBannerView`, `CommandView`, and `CommandsBoxButton`, removing the `indication = null` pattern; filled backgrounds complement but do not replace ripple feedback.
4. Replace the 4dp tab underline with an animated pill: the selected tab gets an accent-tinted rounded background (`animateColorAsState`), its name renders in `accentText` with the existing bold style, and unselected tab names render in `secondaryText`.
5. Show tab action icons (shift-left, edit, delete, shift-right) only on the selected tab, revealed with `AnimatedVisibility` using a fade plus horizontal expand/shrink (not the default scale-in) so the slot grows and collapses smoothly; keep the tab content row at a constant 36dp height so the pill never jumps vertically, and drop the redundant `animateContentSize`/intrinsic-width pair that made the pill width chase the animation.
6. Add a subtle vertical gradient (accent-tinted → surface) and 24dp bottom corners to the header banner; give the logo circle a soft shadow and a thin accent ring; add slight letter spacing to the app name.
7. Add a 1dp translucent border to the add banner and command cards so surfaces separate from the background in both themes.
8. In the add banner, place the add icon in an accent-tinted circular container and promote the banner title to the bold headline style.
9. Make `CommandsBoxButton` a primary accent button: accent fill with `onAccent` text, using the small radius token.
10. In command cards, style the command text as a code block (8dp radius token, horizontal padding), tint the copy icon with the accent color, and move the gap above the action row from 32dp to 16dp.
11. Restyle the command EDIT/DELETE row as a segmented control: each half is a full-height tappable area with ripple, EDIT uses `accentText`, DELETE uses `destructive`, the divider uses a translucent icon color, and vertical padding is 8dp.
12. In command edit mode, move the close icon into a header row next to the existing "Edit" label so there is no empty gap above the fields.
13. Render the up/down reorder arrows always as 36dp circular chips with a subtle background; disable and dim (alpha 0.3) the arrow that is not applicable instead of removing it, so the layout does not jump.
14. Center the empty state vertically in the remaining space with an accent-tinted icon above the existing empty message; the message uses `secondaryText`.
15. Move the add banner out of the `LazyColumn` so it stays fixed under the tab row, and switch between the empty state and the command list with `AnimatedContent`.
16. Animate list item placement with `animateItem()` when commands are added, removed, or reordered.
17. Style the three dialogs with app typography (bold headline title, body message) and colored confirm buttons: `destructive` for the two delete dialogs, `accentText` for the edit-tab dialog; neutral dismiss buttons; 16dp shape.
18. Run the available Gradle checks for `:common-ui` and report their exact results.
19. When the selected tab changes, animate the content area with a direction-aware horizontal slide plus cross-fade (~300 ms, Material-style easing): the next tab enters from the right and leaves to the left, while moving to an earlier tab plays the mirrored direction. The first render must not animate, command edits inside a tab must not retrigger the transition (only the selected tab id is observed), and the outgoing tab must keep showing its own commands during the cross-fade. No swipe/drag pager is introduced; tapping a tab stays the only way to switch.

## Business rules and constraints

- No changes to `HomeViewState`, `HomeEvent`, `HomeAction`, `HomeViewModel`, navigation, repositories, or data model; events and callbacks stay exactly as they are.
- Follow `docs/architecture/mvi.md`: screen/view separation and existing MVI wiring remain untouched; this task only changes rendering.
- Keep code in `commonMain`; changes are limited to `:common-ui` theme and custom views plus this spec.
- Keep all existing user-visible strings; do not add new string resources.
- Keep the existing previews functional (same preview states, widths, and light/dark variants).
- The accent color is terminal-green and must remain readable in both light and dark themes; accent text on backgrounds must meet WCAG AA contrast for small text.
- No dependency upgrades, no new libraries, no unrelated refactoring.

## Edge cases and risks

- Light-theme bright green text on light grey fails contrast; use the darker `accentText` variant for text and reserve the bright/fill green for surfaces and icons on surfaces.
- Removing `indication = null` everywhere changes feedback behavior; verify ripples do not appear twice on nested clickables (tab pill is the container and stays without its own ripple).
- Moving the add banner out of the `LazyColumn` changes scroll behavior (banner becomes fixed); this is an intentional, documented change.
- Always-visible reorder arrows at reduced alpha must be disabled at list boundaries to avoid offering dead actions.
- Dark-theme cards on the dark background are low contrast; the new translucent border is the mitigation and must be verified in the dark preview.

## Acceptance criteria

1. `AppColors` contains `accent`, `accentText`, `onAccent`, and `destructive` values for light and dark themes; `AppShapes` exists and is used by the refreshed elements.
2. The selected tab is shown as an animated accent pill and only the selected tab exposes its action icons.
3. Header, add banner, and command cards use the gradient/border/radius treatment described above; primary buttons are accent-colored.
4. Empty state is vertically centered with an icon; dialogs use app typography and colored confirm buttons.
5. Actionable elements provide ripple feedback.
6. No MVI contracts, state, events, ViewModel, navigation, or data behavior change; existing strings are reused unchanged.
7. `:common-ui` compilation/checks available on this machine run successfully and the exact commands and results are reported.
8. Previews compile and cover the same variants as before.
9. Switching tabs plays the direction-aware content slide/cross-fade; the initial render is instant, and editing a command does not replay the transition.

## Out of scope

- New screens, new features, new user-visible copy, or data-model changes.
- Changes to `HomeViewModel`, navigation, repositories, or the MVI pattern.
- Redesign of other screens beyond the shared components used by Home (`AppColors`, `AppShapes`, `CommandsBoxButton`, `AddBannerView`, `CommandView`).
- Dependency upgrades or architecture refactoring.

## Confirmed decisions

- The accent color is terminal-green: light theme `#16A34A` (fill) with `#166534` for accent text; dark theme `#4ADE80` for both.
- The tab row uses the pill style with selection-gated action icons (icons hidden on unselected tabs), not the conservative always-visible variant.
- Shared components (`CommandView`, `AddBannerView`, `CommandsBoxButton`, `AppColors`) are in scope, not only `HomeScreen.kt`.
- Destructive actions (delete confirmations, DELETE label) use a red token derived for accessibility, distinct from the existing toast colors.
- Tab switching is smoothed with a direction-aware content transition (horizontal slide plus cross-fade toward the tab's position in the row), not with a sliding pill indicator or a swipeable pager; the pill keeps its animated color treatment and tapping remains the only way to switch.

## Remaining assumptions

- `animateItem()` and `AnimatedContent` are available in the Compose Multiplatform version pinned in `gradle/libs.versions.toml`; if not, the equivalent supported mechanism will be used and reported.
- The add banner remaining fixed (not scrolling with the list) is acceptable; if the owner prefers it to scroll, it can be moved back into the `LazyColumn` as a follow-up.
