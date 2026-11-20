import NativeApp
import SwiftUI
import UIKit

enum Keyboard {
    static func dismiss() {
        UIApplication.shared.sendAction(
            #selector(UIResponder.resignFirstResponder),
            to: nil,
            from: nil,
            for: nil
        )
    }
}

struct AppColors {
    let backgroundMain: Color
    let backgroundSecondary: Color
    let mainText: Color
    let secondaryText: Color
    let icon: Color
    let toastSuccess: Color
    let toastFailure: Color
    let accent: Color
    let accentText: Color
    let onAccent: Color
    let destructive: Color

    static let light = AppColors(
        backgroundMain: Color(hex: 0xEAEAEA),
        backgroundSecondary: Color(hex: 0xFFFFFF),
        mainText: Color(hex: 0x000000),
        secondaryText: Color(hex: 0x454545),
        icon: Color(hex: 0x000000),
        toastSuccess: Color(hex: 0x6EFD90),
        toastFailure: Color(hex: 0xFD7474),
        accent: Color(hex: 0x16A34A),
        accentText: Color(hex: 0x166534),
        onAccent: Color(hex: 0x000000),
        destructive: Color(hex: 0xB91C1C)
    )

    static let dark = AppColors(
        backgroundMain: Color(hex: 0x191A1C),
        backgroundSecondary: Color(hex: 0x1E1F22),
        mainText: Color(hex: 0xBABABA),
        secondaryText: Color(hex: 0xA8A8A8),
        icon: Color(hex: 0xBABABA),
        toastSuccess: Color(hex: 0x246319),
        toastFailure: Color(hex: 0x631919),
        accent: Color(hex: 0x4ADE80),
        accentText: Color(hex: 0x4ADE80),
        onAccent: Color(hex: 0x000000),
        destructive: Color(hex: 0xF87171)
    )
}

extension Color {
    init(hex: UInt32) {
        self.init(
            .sRGB,
            red: Double((hex >> 16) & 0xFF) / 255.0,
            green: Double((hex >> 8) & 0xFF) / 255.0,
            blue: Double(hex & 0xFF) / 255.0,
            opacity: 1.0
        )
    }
}

enum AppFont {
    static func cascadia(_ size: CGFloat) -> Font {
        Font(resource: \.cascadiacodeitalic, withSize: size)
    }

    static let headlineLarge = Font.system(size: 20, weight: .heavy)
    static let headlineMedium = Font.system(size: 16, weight: .bold)
    static let headlineSmall = Font.system(size: 14, weight: .bold)
    static let bodyLarge = Font.system(size: 16, weight: .regular)
    static let bodyMedium = Font.system(size: 14, weight: .regular)
    static let bodySmall = Font.system(size: 12, weight: .regular)
}
