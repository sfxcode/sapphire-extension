package com.sfxcode.sapphire.extension.showcase.controller.table

import javafx.beans.binding.Bindings
import javafx.fxml.FXML
import javafx.scene.control.TableView
import com.sfxcode.sapphire.core.controller.ViewController
import com.sfxcode.sapphire.core.value.{BeanConversions, FXBean}
import com.sfxcode.sapphire.extension.showcase.model.{Person, PersonDatabase}
import com.sfxcode.sapphire.extension.showcase.controller.MainController
import javafx.collections.ObservableList

class SimplePersonTableController extends ViewController with BeanConversions {

  @FXML
  var table: TableView[FXBean[Person]] = _

  def testString = "Test"

  override def didGainVisibilityFirstTime(): Unit = {
    super.didGainVisibilityFirstTime()
    val items: ObservableList[FXBean[Person]] = PersonDatabase.smallPersonList
    table.setItems(items)
    mainWindowController.statusBar.textProperty().bind(Bindings.format("%d records found", Bindings.size(items)))

  }

  def mainWindowController: MainController = {
    parent.asInstanceOf[MainController]
  }
}

