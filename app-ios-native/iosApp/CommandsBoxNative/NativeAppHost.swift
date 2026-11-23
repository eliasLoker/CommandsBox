import Foundation
import NativeApp
import SwiftUI

final class NativeAppHost: ObservableObject {

    private let viewModels: NativeViewModelHost
    private var subscriptions: [ViewModelSubscription] = []

    @Published private(set) var mainState: MainViewState
    @Published private(set) var homeState: HomeViewState
    @Published private(set) var settingsState: SettingsViewState
    @Published private(set) var settingsBackSignal: Int = 0

    init() {
        IosNativeEntryPointKt.startIosNative()
        viewModels = IosNativeEntryPointKt.createIosViewModelHost()

        mainState = viewModels.main.currentState
        homeState = viewModels.home.currentState
        settingsState = viewModels.settings.currentState

        observe()
    }

    deinit {
        subscriptions.forEach { $0.cancel() }
        subscriptions.removeAll()
        viewModels.close()
    }

    private func observe() {
        subscriptions.append(
            viewModels.main.observeState { [weak self] state in
                guard let self else { return }
                self.onMain { self.mainState = state }
            }
        )

        subscriptions.append(
            viewModels.home.observeState { [weak self] state in
                guard let self else { return }
                self.onMain { self.homeState = state }
            }
        )

        subscriptions.append(
            viewModels.settings.observeState { [weak self] state in
                guard let self else { return }
                self.onMain { self.settingsState = state }
            }
        )

        subscriptions.append(
            viewModels.settings.observeActions { [weak self] action in
                guard let self else { return }
                guard NativeUiStringsKt.isNavigateBackAction(action: action) else { return }
                self.onMain { self.settingsBackSignal += 1 }
            }
        )
    }

    private func onMain(_ block: @escaping () -> Void) {
        if Thread.isMainThread {
            block()
        } else {
            DispatchQueue.main.async(execute: block)
        }
    }

    func dispatch(_ event: HomeEvent) {
        viewModels.home.dispatch(event: event)
    }

    func dispatchSettings(_ event: SettingsEvent) {
        viewModels.settings.dispatch(event: event)
    }

    func dismissToast() {
        viewModels.main.dispatch(event: OnToastDismissed.shared)
    }

    func darkTheme(systemInDarkTheme: Bool) -> Bool {
        NativeUiStringsKt.isDarkTheme(
            settingsState: settingsState,
            systemInDarkTheme: systemInDarkTheme
        )
    }

    func isErrorToast() -> Bool {
        NativeUiStringsKt.isErrorToast(state: mainState)
    }
}
