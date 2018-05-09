package com.sfxcode.sapphire.extension.controller

import com.sfxcode.sapphire.core.controller.ViewController
import javafx.fxml.FXML
import javafx.scene.control._
import scalafx.Includes._

abstract class BaseTabController extends ViewController {

  @FXML var tabPane: TabPane = _

  override def didGainVisibilityFirstTime() {
    super.didGainVisibilityFirstTime()
    tabPane.getSelectionModel.selectedItemProperty.onChange((_, oldValue, newValue) => {
      tabPaneHasChanged(oldValue, newValue)
    })
  }

  def selectedTab: Tab = tabPane.getSelectionModel.getSelectedItem

  def tabPaneHasChanged(oldValue: Tab, newValue: Tab)

}

