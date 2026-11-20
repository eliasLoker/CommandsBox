import NativeApp
import SwiftUI

struct AddBannerView: View {
    let state: AddBannerViewState
    let colors: AppColors
    let onTabQueryChange: (String) -> Void
    let onCommandQueryChange: (String) -> Void
    let onDescriptionQueryChange: (String) -> Void
    let onExpandIconClicked: () -> Void
    let onAddTabClicked: () -> Void
    let onAddCommandClicked: () -> Void

    private var title: String {
        state.hasTabs ? AppStrings.addTabOrCommandTitle : AppStrings.addTab
    }

    var body: some View {
        VStack(spacing: 0) {
            header

            if state.isExpanded {
                VStack(spacing: 0) {
                    InputFieldView(
                        state: state.tabInputViewState,
                        colors: colors,
                        identifier: "add_tab_input",
                        onQueryChange: onTabQueryChange
                    )
                    .padding(.top, 16)

                    CommandsBoxButton(
                        title: AppStrings.addTabButton,
                        colors: colors,
                        identifier: "add_tab_button",
                        action: onAddTabClicked
                    )
                    .padding(.top, 8)

                    if state.hasTabs {
                        Rectangle()
                            .fill(colors.icon.opacity(0.12))
                            .frame(height: 1)
                            .padding(.vertical, 16)

                        Text(AppStrings.addCommandToTabTitle(tabName: state.selectedTabName))
                            .font(AppFont.bodyMedium)
                            .foregroundStyle(colors.secondaryText)
                            .frame(maxWidth: .infinity, alignment: .leading)

                        InputFieldView(
                            state: state.commandInputViewState,
                            colors: colors,
                            identifier: "add_command_input",
                            onQueryChange: onCommandQueryChange
                        )
                        .padding(.top, 16)

                        InputFieldView(
                            state: state.descriptionInputViewState,
                            colors: colors,
                            identifier: "add_description_input",
                            onQueryChange: onDescriptionQueryChange
                        )
                        .padding(.top, 8)

                        CommandsBoxButton(
                            title: AppStrings.addCommandButton,
                            colors: colors,
                            identifier: "add_command_button",
                            action: onAddCommandClicked
                        )
                        .padding(.top, 8)
                    }
                }
                .transition(.opacity)
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 16)
        .background(
            RoundedRectangle(cornerRadius: 16)
                .fill(colors.backgroundSecondary)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 16)
                .stroke(colors.icon.opacity(0.12), lineWidth: 1)
        )
        .animation(.spring(response: 0.45, dampingFraction: 0.85), value: state.isExpanded)
    }

    private var header: some View {
        HStack(spacing: 0) {
            Button(action: onExpandIconClicked) {
                AppIcon(resource: \.ic_add_square, size: 32, color: colors.accent)
                    .frame(width: 40, height: 40)
                    .background(
                        Circle()
                            .fill(colors.accent.opacity(0.12))
                    )
            }
            .buttonStyle(.plain)
            .accessibilityIdentifier("add_banner_expand_button")

            Text(title)
                .font(AppFont.headlineMedium)
                .foregroundStyle(colors.mainText)
                .padding(.leading, 16)
                .contentShape(Rectangle())
                .onTapGesture(perform: onExpandIconClicked)

            Spacer(minLength: 8)

            Button(action: onExpandIconClicked) {
                AppIcon(resource: \.ic_arrow_up, size: 28, color: colors.icon)
                    .rotationEffect(.degrees(state.isExpanded ? 180 : 0))
            }
            .buttonStyle(.plain)
            .animation(.spring(response: 0.4, dampingFraction: 0.85), value: state.isExpanded)
            .accessibilityIdentifier("add_banner_arrow_button")
        }
    }
}
