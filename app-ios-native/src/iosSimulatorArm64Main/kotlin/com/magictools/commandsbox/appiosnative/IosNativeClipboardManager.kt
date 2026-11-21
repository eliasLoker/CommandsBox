package com.magictools.commandsbox.appiosnative

import com.magictools.commandsBox.commonpresentation.utils.CustomClipboardManager
import platform.UIKit.UIPasteboard

class IosNativeClipboardManager : CustomClipboardManager {

    override fun copy(text: String) {
        UIPasteboard.generalPasteboard.string = text
    }
}
