import NativeApp
import SwiftUI

enum AppStrings {
    static var appName: String {
        MR.strings.shared.app_name.desc().localized()
    }

    static var appNameSplash: String {
        MR.strings.shared.app_name_splash.desc().localized()
    }

    static var edit: String {
        MR.strings.shared.edit.desc().localized()
    }

    static var delete: String {
        MR.strings.shared.delete_.desc().localized()
    }

    static var addTab: String {
        MR.strings.shared.add_tab.desc().localized()
    }

    static var addTabOrCommandTitle: String {
        MR.strings.shared.add_tab_or_command_title.desc().localized()
    }

    static var addTabButton: String {
        MR.strings.shared.add_tab_button.desc().localized()
    }

    static var addCommandButton: String {
        MR.strings.shared.add_command_button.desc().localized()
    }

    static var commandsInTabEmpty: String {
        MR.strings.shared.commands_in_tab_empty.desc().localized()
    }

    static var settingsTitle: String {
        MR.strings.shared.settings_title.desc().localized()
    }

    static var settingsBack: String {
        MR.strings.shared.settings_back.desc().localized()
    }

    static var settingsDarkTheme: String {
        MR.strings.shared.settings_dark_theme.desc().localized()
    }

    static var settingsThemeLight: String {
        MR.strings.shared.settings_theme_light.desc().localized()
    }

    static var settingsThemeDark: String {
        MR.strings.shared.settings_theme_dark.desc().localized()
    }

    static func addCommandToTabTitle(tabName: String) -> String {
        MR.strings.shared.add_command_to_tab_title.format(args_: [tabName]).localized()
    }

    static func settingsThemeCurrent(theme: String) -> String {
        MR.strings.shared.settings_theme_current.format(args_: [theme]).localized()
    }
}

extension Image {
    init(resource: KeyPath<MR.images, NativeApp.ImageResource>) {
        let imageResource = MR.images.shared[keyPath: resource]
        self.init(imageResource.assetImageName, bundle: imageResource.bundle)
    }
}

extension Font {
    init(resource: KeyPath<MR.fonts, FontResource>, withSize: Double) {
        self.init(MR.fonts.shared[keyPath: resource].uiFont(withSize: withSize))
    }
}
