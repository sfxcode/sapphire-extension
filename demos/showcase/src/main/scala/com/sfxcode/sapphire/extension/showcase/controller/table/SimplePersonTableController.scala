package com.sfxcode.sapphire.extension.showcase.controller.table

import javafx.fxml.FXML
import javafx.scene.control.TableView
import com.sfxcode.sapphire.core.value.{ BeanConversions, FXBean }
import com.sfxcode.sapphire.extension.showcase.controller.BaseController
import com.sfxcode.sapphire.extension.showcase.model.{ Person, PersonDatabase }
import javafx.collections.ObservableList

class SimplePersonTableController extends BaseController with BeanConversions {

  @FXML
  var table: TableView[FXBean[Person]] = _

  def testString = "Test"

  override def didGainVisibilityFirstTime(): Unit = {
    super.didGainVisibilityFirstTime()
    val items: ObservableList[FXBean[Person]] = PersonDatabase.smallPersonList
    table.setItems(items)
  }
}

