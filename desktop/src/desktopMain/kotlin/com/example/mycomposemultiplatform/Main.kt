package com.example.mycomposemultiplatform

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.mycomposemultiplatform.shared.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose Multiplatform",
    ) {
        App()
    }
}
