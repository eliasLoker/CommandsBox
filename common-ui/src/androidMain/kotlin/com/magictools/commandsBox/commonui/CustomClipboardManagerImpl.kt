package com.magictools.commandsBox.commonui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.magictools.commandsBox.commonui.utils.CustomClipboardManager

class CustomClipboardManagerImpl(
    private val context: Context
) : CustomClipboardManager {

    override fun copy(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
    }
}