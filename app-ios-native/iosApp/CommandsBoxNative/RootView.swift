import NativeApp
import SwiftUI

enum AppScreen {
    case splash
    case home
    case settings
}

struct RootView: View {
    @ObservedObject var host: NativeAppHost
    @Environment(\.colorScheme) private var systemColorScheme
    @State private var screen: AppScreen = .splash

    private var isDark: Bool {
        host.darkTheme(systemInDarkTheme: systemColorScheme == .dark)
    }

    private var colors: AppColors {
        isDark ? .dark : .light
    }

    var body: some View {
        ZStack(alignment: .top) {
            colors.backgroundMain.ignoresSafeArea()

            Group {
                switch screen {
                case .splash:
                    SplashView(
                        colors: colors
                    ) {
                        show(.home)
                    }
                case .home:
                    HomeView(
                        host: host,
                        colors: colors,
                        onSettings: { show(.settings) }
                    )
                case .settings:
                    SettingsView(
                        host: host,
                        colors: colors,
                        isDark: isDark
                    )
                }
            }
            .transition(.opacity)

            if host.mainState.toastState.isVisible {
                ToastView(
                    state: host.mainState.toastState,
                    isError: host.isErrorToast(),
                    colors: colors
                ) {
                    host.dismissToast()
                }
                .padding(.horizontal, 16)
                .padding(.top, 20)
                .transition(.move(edge: .top).combined(with: .opacity))
                .zIndex(1)
            }
        }
        .animation(.easeInOut(duration: 0.2), value: host.mainState.toastState.isVisible)
        .preferredColorScheme(isDark ? .dark : .light)
        .onChange(of: host.settingsBackSignal) { _ in
            if screen == .settings {
                show(.home)
            }
        }
    }

    private func show(_ next: AppScreen) {
        withAnimation(.easeInOut(duration: 0.25)) {
            screen = next
        }
    }
}
