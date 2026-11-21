package com.magictools.commandsBox.commonui

import com.magictools.commandsBox.commonpresentation.utils.CustomClipboardManager
import platform.UIKit.UIPasteboard

class CustomClipboardManagerImpl : CustomClipboardManager {

    override fun copy(text: String) {
        UIPasteboard.generalPasteboard.string = text
    }
}
