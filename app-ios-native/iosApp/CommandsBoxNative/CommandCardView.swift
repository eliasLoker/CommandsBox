import NativeApp
import SwiftUI

struct CommandCardView: View {
    let state: CommandState
    let colors: AppColors
    let indexInList: Int
    let size: Int
    let onCommandQueryChange: (Int64, String) -> Void
    let onDescriptionQueryChange: (Int64, String) -> Void
    let onCloseEditClicked: (Int64) -> Void
    let onEditApplyClicked: (Int64) -> Void
    let onCopyClicked: (Int64) -> Void
    let onEnableEditClicked: (Int64) -> Void
    let onDeleteClicked: (Int64) -> Void
    let onUpClicked: (Int64) -> Void
    let onDownClicked: (Int64) -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            if state.isEditMode {
                editContent
            } else {
                displayContent
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
    }

    private var editContent: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(spacing: 0) {
                Text(AppStrings.edit)
                    .font(AppFont.headlineSmall)
                    .foregroundStyle(colors.secondaryText)
                    .frame(maxWidth: .infinity, alignment: .leading)

                Button {
                    onCloseEditClicked(state.id)
                } label: {
                    AppIcon(resource: \.ic_clear, size: 28, color: colors.icon)
                }
                .buttonStyle(.plain)
                .accessibilityIdentifier("command_close_edit_\(state.id)")
            }

            InputFieldView(
                state: state.commandInputViewState,
                colors: colors,
                identifier: "command_edit_command_\(state.id)",
                onQueryChange: { onCommandQueryChange(state.id, $0) }
            )
            .padding(.top, 8)

            InputFieldView(
                state: state.descriptionInputViewState,
                colors: colors,
                identifier: "command_edit_description_\(state.id)",
                onQueryChange: { onDescriptionQueryChange(state.id, $0) }
            )
            .padding(.top, 8)

            CommandsBoxButton(
                title: AppStrings.edit,
                colors: colors,
                identifier: "command_apply_edit_\(state.id)",
                action: { onEditApplyClicked(state.id) }
            )
            .padding(.top, 8)
        }
    }

    private var displayContent: some View {
        HStack(alignment: .center, spacing: 0) {
            Button {
                onCopyClicked(state.id)
            } label: {
                AppIcon(resource: \.ic_copy, size: 36, color: colors.accent)
            }
            .buttonStyle(.plain)
            .accessibilityIdentifier("command_copy_\(state.id)")

            VStack(alignment: .leading, spacing: 0) {
                Text(state.command)
                    .font(AppFont.cascadia(16).weight(.bold))
                    .foregroundStyle(colors.mainText)
                    .lineLimit(10)
                    .frame(maxWidth: .infinity, alignment: .leading)

                Text(state.description_)
                    .font(AppFont.bodyMedium)
                    .foregroundStyle(colors.secondaryText)
                    .lineLimit(10)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.top, 8)

                actionBar
                    .padding(.top, 16)
            }
            .padding(.horizontal, 16)

            reorderColumn
                .frame(maxHeight: .infinity)
        }
    }

    private var actionBar: some View {
        HStack(spacing: 0) {
            Button {
                onEnableEditClicked(state.id)
            } label: {
                Text(AppStrings.edit.uppercased())
                    .font(.system(size: 14, weight: .heavy))
                    .tracking(3)
                    .foregroundStyle(colors.accentText)
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 8)
            }
            .buttonStyle(.plain)
            .accessibilityIdentifier("command_edit_\(state.id)")

            Rectangle()
                .fill(colors.icon.opacity(0.2))
                .frame(width: 1, height: 16)

            Button {
                onDeleteClicked(state.id)
            } label: {
                Text(AppStrings.delete.uppercased())
                    .font(.system(size: 14, weight: .heavy))
                    .tracking(3)
                    .foregroundStyle(colors.destructive)
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 8)
            }
            .buttonStyle(.plain)
            .accessibilityIdentifier("command_delete_\(state.id)")
        }
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(colors.backgroundMain)
        )
    }

    private var reorderColumn: some View {
        let isUpEnabled = indexInList != 0
        let isDownEnabled = indexInList != size - 1

        return VStack(spacing: 0) {
            Button {
                onUpClicked(state.id)
            } label: {
                AppIcon(resource: \.ic_arrow_up_with_tail, size: 20, color: colors.icon)
                    .frame(width: 36, height: 36)
                    .background(Circle().fill(colors.backgroundMain))
            }
            .buttonStyle(.plain)
            .opacity(isUpEnabled ? 1 : 0.3)
            .disabled(!isUpEnabled)
            .accessibilityIdentifier("command_up_\(state.id)")

            Spacer(minLength: 8)

            Button {
                onDownClicked(state.id)
            } label: {
                AppIcon(resource: \.ic_arrow_up_with_tail, size: 20, color: colors.icon)
                    .rotationEffect(.degrees(180))
                    .frame(width: 36, height: 36)
                    .background(Circle().fill(colors.backgroundMain))
            }
            .buttonStyle(.plain)
            .opacity(isDownEnabled ? 1 : 0.3)
            .disabled(!isDownEnabled)
            .accessibilityIdentifier("command_down_\(state.id)")
        }
    }
}
