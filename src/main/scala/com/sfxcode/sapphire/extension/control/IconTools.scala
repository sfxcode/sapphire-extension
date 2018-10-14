package com.sfxcode.sapphire.extension.control

import org.kordamp.ikonli.javafx.FontIcon
import scalafx.scene.control.Button

object IconTools {

  def decoratedFontIconButton(code:String):Button = {
    val result = new Button()
    result.setGraphic(new FontIcon(code))
    result
  }
}
