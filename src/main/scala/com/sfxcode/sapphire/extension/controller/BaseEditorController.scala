package com.sfxcode.sapphire.extension.controller

import com.sfxcode.sapphire.core.controller.ViewController
import com.sfxcode.sapphire.core.value.{ FXBean, FXBeanAdapter, KeyBindings }
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.Includes._

import scala.reflect.ClassTag

abstract class BaseEditorController extends ViewController {

  type R <: AnyRef

  def ct: ClassTag[R]

  var editableBean: Option[FXBean[R]] = None

  @FXML
  var formPane: Pane = _

  lazy val formAdapter: FXBeanAdapter[R] = FXBeanAdapter[R](this, formPane.asInstanceOf[Node])

  override def didGainVisibilityFirstTime() {
    super.didGainVisibilityFirstTime()
    val bindings = KeyBindings()
    updateBindings(bindings)
    formAdapter.addBindings(bindings)
  }

  def updateBindings(bindings: KeyBindings)

  def updateBean(bean: FXBean[R]): Unit = {
    val value = bean.asInstanceOf[FXBean[R]]
    editableBean = Some(value)
    formAdapter.beanProperty.value = value
  }

  def actionSave(event: ActionEvent): Unit = {
    editableBean.foreach(b => save(b.bean))
  }

  def actionRevert(event: ActionEvent): Unit = {
    editableBean.foreach(fxBean => fxBean.revert())
  }

  def save(beanValue: R)

}
