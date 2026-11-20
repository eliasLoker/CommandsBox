import SwiftUI

struct DialogScaffold<Content: View>: View {
    let colors: AppColors
    let onDismiss: () -> Void
    let content: Content

    init(
        colors: AppColors,
        onDismiss: @escaping () -> Void,
        @ViewBuilder content: () -> Content
    ) {
        self.colors = colors
        self.onDismiss = onDismiss
        self.content = content()
    }

    var body: some View {
        ZStack {
            Color.black.opacity(0.32)
                .ignoresSafeArea()
                .onTapGesture(perform: onDismiss)

            VStack(alignment: .leading, spacing: 16) {
                content
            }
            .padding(24)
            .frame(maxWidth: 420, alignment: .leading)
            .background(
                RoundedRectangle(cornerRadius: 16)
                    .fill(colors.backgroundSecondary)
            )
            .contentShape(RoundedRectangle(cornerRadius: 16))
            .onTapGesture { Keyboard.dismiss() }
            .padding(.horizontal, 40)
        }
        .transition(.opacity)
        .zIndex(2)
    }
}

struct DialogTitle: View {
    let text: String
    let colors: AppColors

    init(_ text: String, colors: AppColors) {
        self.text = text
        self.colors = colors
    }

    var body: some View {
        Text(text)
            .font(AppFont.headlineSmall)
            .foregroundStyle(colors.mainText)
    }
}

struct DialogMessage: View {
    let text: String
    let colors: AppColors

    init(_ text: String, colors: AppColors) {
        self.text = text
        self.colors = colors
    }

    var body: some View {
        Text(text)
            .font(AppFont.bodyMedium)
            .foregroundStyle(colors.secondaryText)
            .fixedSize(horizontal: false, vertical: true)
    }
}

struct DialogButtons: View {
    let dismissTitle: String
    let confirmTitle: String
    let colors: AppColors
    var confirmColor: Color? = nil
    let onDismiss: () -> Void
    let onConfirm: () -> Void

    var body: some View {
        HStack(spacing: 8) {
            Spacer(minLength: 0)

            Button(action: onDismiss) {
                Text(dismissTitle)
                    .font(AppFont.headlineSmall)
                    .foregroundStyle(colors.secondaryText)
                    .padding(.horizontal, 8)
                    .padding(.vertical, 8)
            }
            .buttonStyle(.plain)
            .accessibilityIdentifier("dialog_dismiss_button")

            Button(action: onConfirm) {
                Text(confirmTitle)
                    .font(AppFont.headlineSmall)
                    .foregroundStyle(confirmColor ?? colors.destructive)
                    .padding(.horizontal, 8)
                    .padding(.vertical, 8)
            }
            .buttonStyle(.plain)
            .accessibilityIdentifier("dialog_confirm_button")
        }
    }
}
