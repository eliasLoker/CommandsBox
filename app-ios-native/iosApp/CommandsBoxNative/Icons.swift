import NativeApp
import SwiftUI

struct AppIcon: View {
    let resource: KeyPath<MR.images, NativeApp.ImageResource>
    let size: CGFloat
    let color: Color

    var body: some View {
        Image(resource: resource)
            .renderingMode(.template)
            .resizable()
            .scaledToFit()
            .frame(width: size, height: size)
            .foregroundStyle(color)
    }
}
