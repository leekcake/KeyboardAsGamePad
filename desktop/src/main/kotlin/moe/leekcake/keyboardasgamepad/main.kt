package moe.leekcake.keyboardasgamepad

import javafx.application.Application.launch
import javafx.scene.text.FontWeight
import javafx.stage.Stage
import tornadofx.*

class KAGPApp : App(MainView::class, Styles::class)

class Styles : Stylesheet() {
    init {
        label {
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#cecece")
            minWidth = 300.px
            minHeight = 300.px
        }
    }
}