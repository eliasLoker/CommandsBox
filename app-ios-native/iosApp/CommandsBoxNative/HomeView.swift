import NativeApp
import SwiftUI

struct HomeView: View {
    @ObservedObject var host: NativeAppHost
    let colors: AppColors
    let onSettings: () -> Void

    private var state: HomeViewState { host.homeState }

    @State private var slideDirection = 1

    var body: some View {
        ZStack {
            VStack(spacing: 0) {
                header

                if state.tabs.isEmpty {
                    Color.clear.frame(height: 16)
                } else {
                    tabBar
                }

                AddBannerView(
                    state: state.addBannerViewState,
                    colors: colors,
                    onTabQueryChange: { host.dispatch(OnTabQueryChange(text: $0)) },
                    onCommandQueryChange: { host.dispatch(OnCommandQueryChange(text: $0)) },
                    onDescriptionQueryChange: { host.dispatch(OnDescriptionQueryChange(text: $0)) },
                    onExpandIconClicked: { host.dispatch(OnAddBannerExpandIconClicked.shared) },
                    onAddTabClicked: {
                        Keyboard.dismiss()
                        host.dispatch(OnAddTabClicked.shared)
                    },
                    onAddCommandClicked: {
                        Keyboard.dismiss()
                        host.dispatch(OnAddCommandClicked.shared)
                    }
                )

                Color.clear.frame(height: 8)

                ZStack {
                    content
                        .id(state.selectedTabId)
                        .transition(
                            .asymmetric(
                                insertion: .move(
                                    edge: slideDirection > 0 ? .trailing : .leading
                                ).combined(with: .opacity),
                                removal: .move(
                                    edge: slideDirection > 0 ? .leading : .trailing
                                ).combined(with: .opacity)
                            )
                        )
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .clipped()
                .animation(.easeInOut(duration: 0.25), value: state.selectedTabId)
            }
            .padding(.horizontal, 16)
            .padding(.bottom, 16)

            dialogs
        }
        .background(colors.backgroundMain.ignoresSafeArea())
        .animation(.easeInOut(duration: 0.2), value: state.selectedTabId)
    }

    private var header: some View {
        HStack(spacing: 0) {
            HStack(spacing: 0) {
                Image(resource: \.commandsbox_logo_transparent)
                    .resizable()
                    .scaledToFill()
                    .frame(width: 44, height: 44)
                    .clipShape(Circle())
                    .padding(4)
                    .background(
                        Circle()
                            .fill(colors.backgroundMain)
                    )
                    .overlay(
                        Circle()
                            .stroke(colors.accent, lineWidth: 1.5)
                    )
                    .frame(width: 52, height: 52)
                    .shadow(
                        color: Color.black.opacity(0.18),
                        radius: 4,
                        x: 0,
                        y: 2
                    )

                Text(AppStrings.appName)
                    .font(AppFont.cascadia(20).weight(.heavy))
                    .tracking(0.5)
                    .foregroundStyle(colors.mainText)
                    .padding(.leading, 16)
            }

            Spacer(minLength: 8)

            Button {
                Keyboard.dismiss()
                onSettings()
            } label: {
                AppIcon(resource: \.ic_settings, size: 20, color: colors.accentText)
                    .frame(width: 36, height: 36)
                    .background(Circle().fill(colors.backgroundMain))
            }
            .buttonStyle(.plain)
            .accessibilityLabel(AppStrings.settingsTitle)
            .accessibilityIdentifier("home_settings_button")
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 16)
        .background(
            LinearGradient(
                colors: [
                    colors.accent.opacity(0.10),
                    colors.backgroundSecondary
                ],
                startPoint: .top,
                endPoint: .bottom
            )
        )
        .contentShape(Rectangle())
        .onTapGesture { Keyboard.dismiss() }
        .clipShape(BannerShape())
    }

    private var tabBar: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 4) {
                ForEach(Array(state.tabs.enumerated()), id: \.element.id) { index, tab in
                    tabPill(tab, index: index)
                }
            }
            .padding(.vertical, 8)
        }
        .fixedSize(horizontal: false, vertical: true)
    }

    private func tabPill(_ tab: HomeViewState.HomeTab, index: Int) -> some View {
        let isSelected = state.selectedTabId == tab.id
        let isLast = index == state.tabs.count - 1

        return HStack(spacing: 0) {
            if isSelected && index != 0 {
                tabActionIcon(
                    resource: \.ic_arrow_right,
                    rotation: 180,
                    identifier: "tab_left_\(tab.id)"
                ) {
                    host.dispatch(OnTabLeftClicked(id: tab.id))
                }
            }

            Text(tab.name)
                .font(AppFont.headlineMedium)
                .foregroundStyle(isSelected ? colors.accentText : colors.secondaryText)
                .padding(.horizontal, 8)

            if isSelected {
                tabActionIcon(resource: \.ic_edit, identifier: "tab_edit_\(tab.id)") {
                    host.dispatch(OnTabEditClicked(id: tab.id))
                }
                Color.clear.frame(width: 4)
                tabActionIcon(resource: \.ic_delete, identifier: "tab_delete_\(tab.id)") {
                    host.dispatch(OnDeleteTabClicked(id: tab.id))
                }

                if !isLast {
                    Color.clear.frame(width: 4)
                    tabActionIcon(resource: \.ic_arrow_right, identifier: "tab_right_\(tab.id)") {
                        host.dispatch(OnTabRightClicked(id: tab.id))
                    }
                }
            }
        }
        .frame(minHeight: 36, alignment: .center)
        .padding(.horizontal, 8)
        .padding(.vertical, 8)
        .background(
            RoundedRectangle(cornerRadius: 12)
                .fill(isSelected ? colors.accent.opacity(0.15) : Color.clear)
        )
        .contentShape(RoundedRectangle(cornerRadius: 12))
        .onTapGesture {
            Keyboard.dismiss()
            let currentIndex = state.tabs.firstIndex { $0.id == state.selectedTabId } ?? 0
            slideDirection = index >= currentIndex ? 1 : -1
            withAnimation(.easeInOut(duration: 0.25)) {
                host.dispatch(OnTabSelected(tabId: tab.id))
            }
        }
    }

    private func tabActionIcon(
        resource: KeyPath<MR.images, NativeApp.ImageResource>,
        rotation: Double = 0,
        identifier: String,
        action: @escaping () -> Void
    ) -> some View {
        Button {
            Keyboard.dismiss()
            action()
        } label: {
            AppIcon(resource: resource, size: 20, color: colors.accentText)
                .rotationEffect(.degrees(rotation))
                .frame(width: 36, height: 36)
                .background(Circle().fill(colors.backgroundMain))
        }
        .buttonStyle(.plain)
        .accessibilityIdentifier(identifier)
    }

    @ViewBuilder
    private var content: some View {
        if state.commands.isEmpty {
            VStack(spacing: 0) {
                AppIcon(resource: \.ic_add_square, size: 56, color: colors.accent)
                Color.clear.frame(height: 12)
                Text(AppStrings.commandsInTabEmpty)
                    .font(AppFont.bodyLarge)
                    .foregroundStyle(colors.secondaryText)
                    .multilineTextAlignment(.center)
            }
            .padding(.horizontal, 32)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        } else {
            ScrollView {
                LazyVStack(spacing: 8) {
                    ForEach(Array(state.commands.enumerated()), id: \.element.id) { index, command in
                        CommandCardView(
                            state: command,
                            colors: colors,
                            indexInList: index,
                            size: state.commands.count,
                            onCommandQueryChange: { id, text in
                                host.dispatch(OnEditCommandQueryChange(id: id, text: text))
                            },
                            onDescriptionQueryChange: { id, text in
                                host.dispatch(OnEditDescriptionQueryChange(id: id, text: text))
                            },
                            onCloseEditClicked: {
                                Keyboard.dismiss()
                                host.dispatch(OnEditCloseClicked(id: $0))
                            },
                            onEditApplyClicked: {
                                Keyboard.dismiss()
                                host.dispatch(OnEditApplyClicked(id: $0))
                            },
                            onCopyClicked: {
                                Keyboard.dismiss()
                                host.dispatch(OnCopyCommandClicked(id: $0))
                            },
                            onEnableEditClicked: {
                                Keyboard.dismiss()
                                host.dispatch(OnEnableEditClicked(id: $0))
                            },
                            onDeleteClicked: {
                                Keyboard.dismiss()
                                host.dispatch(OnDeleteCommandClicked(id: $0))
                            },
                            onUpClicked: {
                                Keyboard.dismiss()
                                host.dispatch(OnCommandUpClicked(id: $0))
                            },
                            onDownClicked: {
                                Keyboard.dismiss()
                                host.dispatch(OnCommandDownClicked(id: $0))
                            }
                        )
                    }
                }
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .modifier(ScrollDismissKeyboardIfAvailable())
        }
    }

    @ViewBuilder
    private var dialogs: some View {
        if state.deleteCommandDialogState.isVisible {
            DialogScaffold(colors: colors, onDismiss: {
                host.dispatch(OnDeleteCommandDialogDismiss.shared)
            }) {
                DialogTitle(state.deleteCommandDialogState.title, colors: colors)
                DialogMessage(state.deleteCommandDialogState.subtitle, colors: colors)
                DialogButtons(
                    dismissTitle: state.deleteCommandDialogState.dismissButton,
                    confirmTitle: state.deleteCommandDialogState.confirmButton,
                    colors: colors,
                    onDismiss: { host.dispatch(OnDeleteCommandDialogDismiss.shared) },
                    onConfirm: { host.dispatch(OnDeleteCommandDialogConfirm.shared) }
                )
            }
        }

        if state.editTabDialogState.isVisible {
            DialogScaffold(colors: colors, onDismiss: {
                host.dispatch(OnTabEditDialogDismiss.shared)
            }) {
                DialogTitle(state.editTabDialogState.title, colors: colors)

                TabNameField(
                    value: state.editTabDialogState.initialName,
                    colors: colors,
                    onChange: { host.dispatch(OnTabEditDialogTextChanged(text: $0)) }
                )

                DialogButtons(
                    dismissTitle: state.editTabDialogState.dismissButton,
                    confirmTitle: state.editTabDialogState.confirmButton,
                    colors: colors,
                    confirmColor: colors.accentText,
                    onDismiss: {
                        Keyboard.dismiss()
                        host.dispatch(OnTabEditDialogDismiss.shared)
                    },
                    onConfirm: {
                        Keyboard.dismiss()
                        host.dispatch(OnTabEditDialogConfirm.shared)
                    }
                )
            }
        }

        if state.deleteTabDialogState.isVisible {
            DialogScaffold(colors: colors, onDismiss: {
                host.dispatch(OnDeleteTabDialogDismiss.shared)
            }) {
                DialogTitle(state.deleteTabDialogState.title, colors: colors)
                DialogMessage(state.deleteTabDialogState.subtitle, colors: colors)
                DialogButtons(
                    dismissTitle: state.deleteTabDialogState.dismissButton,
                    confirmTitle: state.deleteTabDialogState.confirmButton,
                    colors: colors,
                    onDismiss: { host.dispatch(OnDeleteTabDialogDismiss.shared) },
                    onConfirm: { host.dispatch(OnDeleteTabDialogConfirm.shared) }
                )
            }
        }
    }
}

private struct TabNameField: View {
    let value: String
    let colors: AppColors
    let onChange: (String) -> Void

    @State private var text: String
    @FocusState private var isFocused: Bool

    init(value: String, colors: AppColors, onChange: @escaping (String) -> Void) {
        self.value = value
        self.colors = colors
        self.onChange = onChange
        _text = State(initialValue: value)
    }

    var body: some View {
        TextField(
            "",
            text: Binding(
                get: { text },
                set: { newValue in
                    text = newValue
                    onChange(newValue)
                }
            )
        )
        .textFieldStyle(.plain)
        .font(AppFont.bodyMedium)
        .foregroundStyle(colors.mainText)
        .tint(colors.mainText)
        .lineLimit(1)
        .padding(12)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(colors.backgroundMain)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 8)
                .stroke(colors.secondaryText.opacity(0.5), lineWidth: 1)
        )
        .focused($isFocused)
        .submitLabel(.done)
        .onSubmit { Keyboard.dismiss() }
        .accessibilityIdentifier("dialog_tab_name_input")
        .onChange(of: value) { newValue in
            if !isFocused && newValue != text {
                text = newValue
            }
        }
        .onChange(of: isFocused) { focused in
            if !focused && value != text {
                text = value
            }
        }
    }
}

struct BannerShape: Shape {
    func path(in rect: CGRect) -> Path {
        var path = Path()
        let radius: CGFloat = 24
        path.move(to: CGPoint(x: rect.minX, y: rect.minY))
        path.addLine(to: CGPoint(x: rect.maxX, y: rect.minY))
        path.addLine(to: CGPoint(x: rect.maxX, y: rect.maxY - radius))
        path.addQuadCurve(
            to: CGPoint(x: rect.maxX - radius, y: rect.maxY),
            control: CGPoint(x: rect.maxX, y: rect.maxY)
        )
        path.addLine(to: CGPoint(x: rect.minX + radius, y: rect.maxY))
        path.addQuadCurve(
            to: CGPoint(x: rect.minX, y: rect.maxY - radius),
            control: CGPoint(x: rect.minX, y: rect.maxY)
        )
        path.closeSubpath()
        return path
    }
}
