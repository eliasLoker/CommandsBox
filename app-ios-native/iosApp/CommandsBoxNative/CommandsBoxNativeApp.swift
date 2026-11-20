import SwiftUI

@main
struct CommandsBoxNativeApp: App {
    @StateObject private var host = NativeAppHost()

    var body: some Scene {
        WindowGroup {
            RootView(host: host)
        }
    }
}
