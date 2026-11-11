# MVI Architecture in CommandsBox

## 1. ViewState — UI State

Use one primary UI state object for a standard screen.

Requirements:

- Define the state as a dedicated `data class` for the screen.
- **Declare the screen-specific ViewState data class in its own separate Kotlin source file.**
- **Do not declare the ViewState data class in the Screen file, ViewModel file, or any other Kotlin source file containing unrelated declarations.**
- Name the file after the ViewState class, following the project's existing naming conventions. For example, `ExampleViewState` must be declared in `ExampleViewState.kt`.
- Keep the ViewState class in the appropriate package for the screen, following existing project conventions.
- Every standard new screen ViewState must include `val isLoading: Boolean = false`, even if loading behavior is not currently implemented.
- Mark standard screen ViewState classes with `@Serializable`.
- Implement `com.magictools.multimvi.state.ViewState`.
- Initialize the state in the screen's ViewModel using the existing `SavedStateHandle.viewState(...)` delegate.
- Follow the actual delegate signature and existing usage. Do not invent serializer, generic, or parameter names.

Source files:

- `multi-mvi/src/commonMain/kotlin/com/magictools/multimvi/state/ViewState.kt`
- `multi-mvi/src/commonMain/kotlin/com/magictools/multimvi/state/ViewStateDelegate.kt`

`ViewState.kt` defines the `ViewState` and `ViewStateHolder` interfaces.

The `viewState(...)` delegate accepts an initial state and a serializer. Verify its actual signature and review existing usage before using it.

Do not create multiple independent primary UI state objects for one screen unless there is a justified need.

## 2. ViewModel

- Base `ViewModel`s on `androidx.lifecycle.ViewModel`.
- Use constructor-injected `SavedStateHandle` for standard screen state.
- Initialize state with `savedStateHandle.viewState(...)`.
- Use `viewModelScope`, Kotlin Coroutines, and the dispatchers adopted by the project for asynchronous operations.
- Reuse existing repositories and dependencies instead of duplicating their functionality.
- Keep business logic and state transitions out of UI rendering functions.
- **Always** use `AppMainDispatcher` when providing a coroutine context.

Use `HomeScreen` and other existing screen-level classes as references when creating a `ViewModel`.

For every standard new screen described in Section 8, use
constructor-injected `SavedStateHandle` and initialize `viewState`
through `SavedStateHandle.viewState(...)`.

Do not omit these requirements merely because the screen currently
has no asynchronous operations or complex UI state.

Deviate from this convention only when the task explicitly requires
a different design or an existing architectural constraint makes
the standard pattern inapplicable. Explain the deviation in the
final report.

## 3. ViewEvent — Events from UI to ViewModel

Use `ViewEvent` to pass events from the UI to a `ViewModel`.

Source:

`multi-mvi/src/commonMain/kotlin/com/magictools/multimvi/event/ViewEvent.kt`

Conventions:

- Screen events implement `com.magictools.multimvi.event.ViewEvent`.
- Every standard new screen ViewModel must declare an `onNewEvent`
  property using the existing `listen(...)` delegate.
- Use the screen-specific `ViewEvent` type for `onNewEvent`.
- Inspect the actual `listen(...)` declaration and
  `HomeViewModel` before implementing the property.
- Match the project's actual generic parameters, lambda signature,
  return type, and event handling conventions.
- Do not replace `listen(...)` with a custom event-handling mechanism.
- Define event types according to user actions and existing project conventions.

Examples from `HomeScreen`:

- `OnAddCommandClicked`

## 4. ViewAction — Actions from ViewModel to UI

Use `ViewAction` to pass actions from a `ViewModel` to the UI.

Source files:

- `multi-mvi/src/commonMain/kotlin/com/magictools/multimvi/action/ViewAction.kt`
- `multi-mvi/src/commonMain/kotlin/com/magictools/multimvi/action/ViewActionDelegate.kt`
- `multi-mvi/src/commonMain/kotlin/com/magictools/multimvi/action/ViewActionEmitter.kt`

Conventions:

- Screen actions implement `com.magictools.multimvi.action.ViewAction`.
- Use the `actions()` delegate to emit actions.
- Use the existing `emit(...)` and `emitNow(...)` methods according to their purpose and calling context.
- Collect and handle actions in Compose following existing screens.
- Handle navigation and other UI effects at the screen integration level, not inside rendering functions.

Use the existing `collectActions(...)` function to collect actions in Compose.

Source:

`multi-mvi/src/commonMain/kotlin/com/magictools/multimvi/ViewActionCollectingCompose.kt`

Package: `com.magictools.multimvi`.

`collectActions` is an extension on `Flow<ViewActionBundle>`. It handles actions of the specified `ViewAction` type and the standard `DefaultViewAction.Dismiss` action.

When creating a screen, follow existing `collectActions` usage in `:common-ui`. Preserve the project's established handling of navigation and other UI effects.

Examples from `HomeScreen`:

- `HomeScreenAction`

Do not introduce a separate navigation event mechanism when the existing `ViewAction` mechanism is sufficient.

## 5. Internal ViewModel State

If a `ViewModel` needs state that should not be part of the UI state, use the existing `state(...)` delegate.

Source:

`multi-mvi/src/commonMain/kotlin/com/magictools/multimvi/viewmodelstate/ViewModelStateDelegate.kt`

This mechanism stores internal state using `SavedStateHandle` and serialization.

Conventions:

- When needed, define internal state in a separate private class inside the `ViewModel`.
- Use `@Serializable` if the state is persisted through JSON serialization.
- Initialize the state with the existing `state(...)` delegate.
- Follow the delegate's actual signature and existing usage.
- Do not duplicate the same data in internal state and `ViewState` without a specific reason.

Do not create internal `State` for every `ViewModel` by default. Use it only for data that genuinely does not belong in the UI state.

## 6. Separation of Screen and View

Follow the existing `HomeScreen` implementation.

**Screen:**

- Obtains the `ViewModel` through Koin using the project's established approach, such as `koinViewModel()`.
- Collects `viewState`.
- Collects and handles `ViewAction`s.
- Passes the state and event handler to the rendering function.

**View:**

- Accepts state and a callback for sending events.
- Renders the UI from the supplied data.
- Does not obtain a `ViewModel` itself.
- Does not perform navigation directly.
- Should remain independently testable and suitable for `@Preview` where possible.

**ViewModel:**

- Handles `ViewEvent`s.
- Coordinates repositories and other dependencies.
- Updates state through existing mechanisms.
- Emits `ViewAction`s for actions that the UI must perform.

Keep state and callbacks explicitly passed between layers. Do not couple rendering functions directly to a `ViewModel` when the existing screen pattern separates `Screen` and `View`.

## 7. Koin Registration

Register new `ViewModel`s in the existing module:

`common-ui/src/commonMain/kotlin/com/magictools/commandsBox/commonui/CommonUiModule.kt`

Follow existing registration patterns, including `viewModelOf(...)`.

Register other dependencies using established conventions, such as `factoryOf(...)` and `singleOf(...)`, when their lifecycles are appropriate.

Before changing dependency injection configuration:

1. Inspect the new `ViewModel` constructor.
2. Ensure all its dependencies are registered.
3. Review existing registrations.
4. Make only the necessary changes.

## 8. New Screen Creation Checklist

When creating a new screen, create all required screen components and connect them using the existing project architecture. Use `HomeScreen`, `HomeViewModel`, and the relevant `:multi-mvi` implementations as the primary references.

Do not stop after creating only the UI or model classes. Complete the screen structure, event handling, ViewModel state, previews, and required dependency injection registration.

### 8.1. ViewState

- **The screen-specific ViewState data class must be declared in its own dedicated Kotlin file.**
- **Never declare the ViewState data class inside the Screen file or ViewModel file.**
- Name the file after the ViewState class, for example, `ExampleViewState.kt` for `ExampleViewState`.
- Keep the Screen composables, ViewModel, and ViewState in separate source files, following the existing project package and naming conventions.
- Create a dedicated `ViewState` data class for the screen.
- Implement `com.magictools.multimvi.state.ViewState`.
- Always include `val isLoading: Boolean = false` as a default property, even when loading behavior is not yet implemented. This ensures the initial state can be constructed without requiring a loading argument.
- Add `@Serializable` and the necessary imports, following the existing serialization conventions.
- Always initialize the state using the existing
  `SavedStateHandle.viewState(...)` delegate for every standard
  new screen. Follow the actual delegate signature and existing
  project usage.
- Do not replace the concrete state data class with the marker interface when passing state to the rendering function.

### 8.2. Argument

- Always create a dedicated argument type for every standard
  new screen.
- Annotate the argument type with `@Serializable`.
- Follow the naming, package structure, serialization, and
  navigation conventions of existing screen argument types.
- Include only arguments required by the screen. Do not invent
  arbitrary fields merely to populate the class.
- If the screen requires no argument values, still provide the
  dedicated argument type using a valid representation supported
  by the project's serialization and navigation setup.
- Inspect the existing navigation implementation to determine how
  argument types are declared, passed, and consumed.
- Do not create an unused argument type that is disconnected from
  the screen's actual navigation or initialization flow.

### 8.3. Screen and View

Create a dedicated Screen file containing the screen's event and action contracts and both composable functions.

**Event and action contracts:**

- Always declare a screen-specific `sealed interface` extending
  `com.magictools.multimvi.event.ViewEvent`.
- Always declare a screen-specific `sealed interface` extending
  `com.magictools.multimvi.action.ViewAction`.
- Keep both declarations in the Screen file, following the
  organization of `HomeScreen`.
- Create concrete event and action implementations according
  to the screen's actual behavior.
- Do not invent unused behavior merely to populate the interfaces.
- Use `ViewEvent` for UI-to-ViewModel communication and
  `ViewAction` for ViewModel-to-UI effects.

**Public Screen function:**

- Create a public `Screen` composable function that accepts the screen's `Argument` object as a parameter, following existing project conventions.
- Obtain the ViewModel through Koin using the established project approach.
- Collect the ViewModel's `viewState`.
- Collect and handle `ViewAction` instances using the existing `collectActions(...)` mechanism.
- Pass the concrete state data class and the event callback to the separate rendering function.
- Preserve existing navigation and UI effect handling conventions.

**Separate View function:**

- Create a separate composable function named `View`.
- Accept the concrete screen-specific `ViewState` data class as the `state` parameter, not the `com.magictools.multimvi.state.ViewState` marker interface.
- Accept an event callback using the screen-specific event type.
- Render UI from the supplied state and dispatch user interactions through the callback.
- Do not obtain a ViewModel or perform navigation directly inside `View`.

### 8.4. ViewModel

- Create a dedicated ViewModel for the screen, following existing
  naming and package conventions.
- Base it on `androidx.lifecycle.ViewModel` and use constructor
  injection as established by the project.
- Use constructor-injected `SavedStateHandle`.
- Always declare the `viewState` property using the existing
  `SavedStateHandle.viewState(...)` delegate for a standard new screen.
- Initialize the state with the concrete screen-specific
  `ViewState` data class and its serializer.
- Always declare an `onNewEvent` property using the existing
  `listen(...)` delegate from `:multi-mvi`.
- Use the screen-specific `ViewEvent` type for `onNewEvent`.
- Handle the event variants according to the task requirements
  and update state through the established state mechanisms.
- Emit UI effects through the existing `ViewAction` mechanism
  when the screen requires them.
- Inspect `HomeViewModel` and the actual delegate
  implementations before writing the declarations.
- Do not guess generic parameters, lambda signatures, return types,
  or serializer parameters.
- Do not omit `viewState` or `onNewEvent` merely because the
  initial screen has no complex logic or asynchronous operations.

### 8.5. Compose Previews

- Inspect every `@Preview` function in `HomeScreen`.
- Create the equivalent preview variants for the new screen,
  adapting their content, sample states, and arguments.
- Preserve the same preview coverage categories and number of
  previews unless a particular variant is genuinely incompatible
  with the new screen.
- If a preview variant cannot be reproduced, explain why and
  provide an appropriate equivalent when possible.
- Previews should invoke the `View` function directly and remain
  independent of a running ViewModel, Koin, repositories, or
  platform services wherever the existing setup allows it.
- Do not omit previews merely to reduce the amount of generated code.

### 8.6. Dependency Injection and Verification

- Register the new ViewModel in `common-ui/src/commonMain/kotlin/com/magictools/commandsBox/commonui/CommonUiModule.kt`, following existing conventions such as `viewModelOf(...)`.
- Ensure that all required constructor dependencies are already registered or add only the necessary registrations.
- Inspect the complete set of files created and modified for the screen.
- Run the appropriate build or checks when possible.
- Check `git status --short` and ensure that all required new source files are included in the final changes. Explicitly report any remaining untracked files.
- Before finishing, verify that the Screen, View, ViewModel, state, event handling, actions, previews, and dependency injection registration are connected consistently.
- Inspect the existing navigation configuration and identify all
  registration or routing changes required for the new screen.
- When the task requires a fully integrated screen, register it
  in the appropriate existing navigation flow.
- Ensure the screen's `Argument` is passed and consumed according
  to the existing navigation and ViewModel initialization patterns.
- Do not introduce new navigation abstractions or routes without
  inspecting the existing implementation.
- If the task requests only screen components or a scaffold,
  do not expand the scope by inventing a new navigation flow.
  Report any integration that remains intentionally incomplete.
- Verify that the screen-specific ViewState data class is declared in its own Kotlin source file and is not embedded in the Screen or ViewModel file.
- Verify that every required screen component is located in the appropriate source file rather than grouped into a single file for convenience.