import NativeApp
import SwiftUI

struct SplashView: View {
    let colors: AppColors
    let onFinished: () -> Void

    @State private var logoVisible = false
    @State private var visibleCharacters = 0

    private var title: String { AppStrings.appNameSplash }

    var body: some View {
        VStack(spacing: 0) {
            Spacer(minLength: 0)
            Color.clear.frame(height: 40)

            Image(resource: \.commandsbox_logo_transparent)
                .resizable()
                .scaledToFill()
                .frame(width: 164, height: 164)
                .clipShape(Circle())
                .padding(8)
                .background(Circle().fill(colors.backgroundSecondary))
                .frame(width: 180, height: 180)
                .opacity(logoVisible ? 1 : 0)
                .animation(.easeInOut(duration: 1.5), value: logoVisible)
                .accessibilityIdentifier("splash_logo")

            Color.clear.frame(height: 40)

            Text(String(title.prefix(visibleCharacters)))
                .frame(maxWidth: .infinity)
                .padding(.horizontal, 32)
                .multilineTextAlignment(.center)
                .font(AppFont.cascadia(24).weight(.heavy))
                .foregroundStyle(colors.secondaryText)
                .accessibilityIdentifier("splash_title")

            Spacer(minLength: 0)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(colors.backgroundMain)
        .task {
            logoVisible = true

            guard await wait(milliseconds: 1_000) else { return }

            let characters = Array(title)
            for index in 1...max(characters.count, 1) {
                guard index <= characters.count else { break }
                visibleCharacters = index
                guard await wait(milliseconds: 100) else { return }
            }

            guard await wait(milliseconds: 500) else { return }
            onFinished()
        }
    }

    private func wait(milliseconds: UInt64) async -> Bool {
        do {
            try await Task.sleep(nanoseconds: milliseconds * 1_000_000)
            return !Task.isCancelled
        } catch {
            return false
        }
    }
}
