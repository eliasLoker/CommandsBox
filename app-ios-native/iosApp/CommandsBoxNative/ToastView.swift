import NativeApp
import SwiftUI

struct ToastView: View {
    let state: ToastState
    let isError: Bool
    let colors: AppColors
    let onDismiss: () -> Void

    var body: some View {
        let icon: KeyPath<MR.images, NativeApp.ImageResource> = isError
            ? \.ic_failure
            : \.ic_success

        HStack(spacing: 8) {
            AppIcon(
                resource: icon,
                size: 24,
                color: colors.mainText
            )

            Text(state.data.message)
                .font(AppFont.bodySmall)
                .foregroundStyle(colors.mainText)
                .lineLimit(6)
                .frame(maxWidth: .infinity, alignment: .leading)
        }
        .padding(.horizontal, 8)
        .padding(.vertical, 16)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(isError ? colors.toastFailure : colors.toastSuccess)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 8)
                .stroke(colors.mainText.opacity(0.2), lineWidth: 1)
        )
        .accessibilityIdentifier("toast_view")
        .task(id: state.data.message) {
            guard state.isVisible else { return }
            let durationMs = max(Int64(0), state.durationMs)
            do {
                try await Task.sleep(nanoseconds: UInt64(durationMs) * 1_000_000)
            } catch {
                return
            }
            onDismiss()
        }
    }
}
