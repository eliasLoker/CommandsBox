import NativeApp
import SwiftUI
import UIKit

struct InputFieldView: View {
    let state: InputViewState
    let colors: AppColors
    let identifier: String
    let onQueryChange: (String) -> Void

    @FocusState private var isFocused: Bool

    @State private var text: String

    init(
        state: InputViewState,
        colors: AppColors,
        identifier: String,
        onQueryChange: @escaping (String) -> Void
    ) {
        self.state = state
        self.colors = colors
        self.identifier = identifier
        self.onQueryChange = onQueryChange
        _text = State(initialValue: Self.displayedQuery(for: state))
    }

    private var displayedQuery: String {
        state.singleLine
            ? text.replacingOccurrences(of: "\n", with: " ")
            : text
    }

    private static func displayedQuery(for state: InputViewState) -> String {
        state.singleLine
            ? state.query.replacingOccurrences(of: "\n", with: " ")
            : state.query
    }

    private var iconResource: KeyPath<MR.images, NativeApp.ImageResource> {
        switch state.icon.name {
        case "TAB":
            return \.ic_tab
        case "DESCRIPTION":
            return \.ic_details
        default:
            return \.ic_command
        }
    }

    @ViewBuilder
    private var textEditor: some View {
        let binding = Binding(
            get: { displayedQuery },
            set: { newValue in
                text = newValue
                onQueryChange(newValue)
            }
        )

        if #available(iOS 16.0, *) {
            TextField("", text: binding, axis: state.singleLine ? .horizontal : .vertical)
                .lineLimit(state.singleLine ? 1 : max(1, Int(state.maxLines)))
                .fixedSize(horizontal: false, vertical: !state.singleLine)
                .submitLabel(state.singleLine ? .done : .return)
                .onSubmit { Keyboard.dismiss() }
        } else if state.singleLine {
            TextField("", text: binding)
                .submitLabel(.done)
                .onSubmit { Keyboard.dismiss() }
        } else {
            TextEditor(text: binding)
                .frame(minHeight: CGFloat(max(1, Int(state.maxLines))) * 20)
                .modifier(HideScrollBackgroundIfAvailable())
                .foregroundColor(colors.mainText)
                .onAppear {
                    UITextView.appearance().backgroundColor = .clear
                }
        }
    }

    var body: some View {
        HStack(spacing: 8) {
            AppIcon(resource: iconResource, size: 32, color: colors.mainText)

            ZStack(alignment: .leading) {
                if displayedQuery.isEmpty {
                    Text(state.hint)
                        .font(AppFont.bodyMedium)
                        .foregroundStyle(colors.icon.opacity(0.3))
                        .lineLimit(1)
                }

                textEditor
                    .textFieldStyle(.plain)
                    .font(AppFont.bodyMedium)
                    .foregroundStyle(colors.mainText)
                    .tint(colors.mainText)
                    .focused($isFocused)
                    .accessibilityIdentifier(identifier)
            }

            Button {
                text = ""
                onQueryChange("")
                isFocused = false
            } label: {
                AppIcon(resource: \.ic_clear, size: 24, color: colors.icon)
                    .frame(width: 36, height: 36)
            }
            .buttonStyle(.plain)
            .opacity(displayedQuery.isEmpty ? 0 : 1)
            .disabled(displayedQuery.isEmpty)
            .accessibilityIdentifier("\(identifier)_clear")
        }
        .padding(.horizontal, 8)
        .padding(.vertical, 4)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(colors.backgroundMain)
        )
        .onChange(of: state.query) { newValue in
            if !isFocused && newValue != text {
                text = state.singleLine
                    ? newValue.replacingOccurrences(of: "\n", with: " ")
                    : newValue
            }
        }
        .onChange(of: isFocused) { focused in
            guard !focused else { return }
            let current = state.singleLine
                ? state.query.replacingOccurrences(of: "\n", with: " ")
                : state.query
            if current != text {
                text = current
            }
        }
    }
}

struct CommandsBoxButton: View {
    let title: String
    let colors: AppColors
    let identifier: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(title.uppercased())
                .font(AppFont.bodyMedium)
                .foregroundStyle(colors.onAccent)
                .lineLimit(1)
                .frame(maxWidth: .infinity)
                .padding(.horizontal, 16)
                .padding(.vertical, 12)
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .fill(colors.accent)
                )
        }
        .buttonStyle(.plain)
        .accessibilityIdentifier(identifier)
    }
}

struct HideScrollBackgroundIfAvailable: ViewModifier {
    func body(content: Content) -> some View {
        if #available(iOS 16.0, *) {
            content.scrollContentBackground(.hidden)
        } else {
            content
        }
    }
}

struct ScrollDismissKeyboardIfAvailable: ViewModifier {
    func body(content: Content) -> some View {
        if #available(iOS 16.0, *) {
            content.scrollDismissesKeyboard(.immediately)
        } else {
            content
        }
    }
}
