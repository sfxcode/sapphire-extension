package com.sfxcode.sapphire.extension.controller

import javafx.scene.Node
import javafx.scene.control.Control

import com.sfxcode.sapphire.core.controller.ViewController
import org.controlsfx.validation.{ ValidationSupport, Validator }

class EditorController extends ViewController {
  val validationSupport = new ValidationSupport()

  def registerValidatorForId[T](validator: Validator[T], fxid: String, parent: Node = null, required: Boolean = false) {
    val node = locateInternal(fxid, parent)
    registerValidator(validator, node, required)
  }

  def registerValidator[T](validator: Validator[T], node: Option[Node], required: Boolean = false) {
    node.foreach(c => {
      validationSupport.registerValidator(c.asInstanceOf[Control], required, validator)
    })
  }

}
