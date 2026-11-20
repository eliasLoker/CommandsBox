import NativeApp
import SwiftUI

struct SettingsView: View {
    @ObservedObject var host: NativeAppHost
    let colors: AppColors
    let isDark: Bool

    private var currentThemeName: String {
        isDark ? AppStrings.settingsThemeDark : AppStrings.settingsThemeLight
    }

    var body: some View {
        VStack(spacing: 0) {
            HStack(spacing: 12) {
                Button {
                    host.dispatchSettings(OnBackClicked.shared)
                } label: {
                    AppIcon(resource: \.ic_arrow_right, size: 20, color: colors.accentText)
                        .rotationEffect(.degrees(180))
                        .frame(width: 36, height: 36)
                        .background(Circle().fill(colors.backgroundSecondary))
                }
                .buttonStyle(.plain)
                .accessibilityLabel(AppStrings.settingsBack)
                .accessibilityIdentifier("settings_back_button")

                Text(AppStrings.settingsTitle)
                    .font(AppFont.headlineLarge)
                    .foregroundStyle(colors.mainText)

                Spacer(minLength: 0)
            }

            Color.clear.frame(height: 24)

            HStack(spacing: 12) {
                VStack(alignment: .leading, spacing: 4) {
                    Text(AppStrings.settingsDarkTheme)
                        .font(AppFont.headlineMedium)
                        .foregroundStyle(colors.mainText)

                    Text(AppStrings.settingsThemeCurrent(theme: currentThemeName))
                        .font(AppFont.bodyMedium)
                        .foregroundStyle(colors.secondaryText)
                }

                Spacer(minLength: 0)

                CommandsBoxSwitch(isOn: isDark, colors: colors) { newValue in
                    host.dispatchSettings(OnThemeChanged(isDark: newValue))
                }
            }
            .padding(16)
            .background(
                RoundedRectangle(cornerRadius: 16)
                    .fill(colors.backgroundSecondary)
            )

            Spacer(minLength: 0)
        }
        .padding(16)
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
        .background(colors.backgroundMain.ignoresSafeArea())
    }
}

struct CommandsBoxSwitch: View {
    let isOn: Bool
    let colors: AppColors
    let onChange: (Bool) -> Void

    var body: some View {
        Button {
            onChange(!isOn)
        } label: {
            ZStack(alignment: isOn ? .trailing : .leading) {
                Capsule()
                    .fill(isOn ? colors.accent : colors.backgroundMain)
                    .overlay(
                        Capsule()
                            .stroke(isOn ? Color.clear : colors.secondaryText, lineWidth: 1)
                    )
                    .frame(width: 51, height: 31)

                Circle()
                    .fill(isOn ? colors.onAccent : colors.secondaryText)
                    .frame(width: 27, height: 27)
                    .padding(2)
            }
        }
        .buttonStyle(.plain)
        .animation(.easeInOut(duration: 0.2), value: isOn)
        .accessibilityIdentifier("settings_theme_switch")
    }
}
