package com.magictools.commandsBox.commonui

import com.magictools.commandsBox.commonui.utils.CustomClipboardManager
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class CustomClipboardManagerImpl : CustomClipboardManager {

    override fun copy(text: String) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val stringSelection = StringSelection(text)
        clipboard.setContents(stringSelection, null)
    }
}