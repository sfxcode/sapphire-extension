package com.sfxcode.sapphire.extension.demo.controller

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.MenuBar
import javafx.scene.layout.{AnchorPane, HBox}
import com.sfxcode.sapphire.core.controller.ViewController
import com.sfxcode.sapphire.core.scene.ContentManager
import com.sfxcode.sapphire.extension.demo.ApplicationController
import com.sfxcode.sapphire.extension.demo.controller.navigation.{DefaultNavigationController, MasterNavigationController, TableNavigationController}
import org.controlsfx.control.StatusBar
import scalafx.Includes._

class MainWindowController extends ViewController {
  @FXML
  var menuBar: MenuBar = _

  @FXML
  var workspacePane: AnchorPane = _

  @FXML
  var navigationBox: HBox = _

  @FXML
  var statusBar: StatusBar = _

  var workspaceManager: ContentManager = _
  var navigationManager: ContentManager = _

  lazy val defaultNavigationController = getController[DefaultNavigationController]()
  lazy val tableNavigationController = getController[TableNavigationController]()
  lazy val masterNavigationController = getController[MasterNavigationController]()
  lazy val welcomeController = getController[WelcomeController]()

  override def didGainVisibilityFirstTime() {
    val startLoading = System.currentTimeMillis()

    menuBar.setUseSystemMenuBar(true)

    navigationManager = ContentManager(navigationBox, this, defaultNavigationController)
    workspaceManager = ContentManager(workspacePane, this, welcomeController)

    statusBar.setText("Welcome to Sapphire Demo Application")
  }

  def actionShowForm(event: ActionEvent) {
    workspaceManager.updatePaneContent(defaultNavigationController.formController)
    navigationManager.updatePaneContent(defaultNavigationController)
  }

  def actionShowTables(event: ActionEvent) {
    navigationManager.updatePaneContent(tableNavigationController)
    workspaceManager.updatePaneContent(tableNavigationController.simplePersonTableController)

  }

  def actionShowMaster(event: ActionEvent) {
    navigationManager.updatePaneContent(masterNavigationController)
    workspaceManager.updatePaneContent(masterNavigationController.personMasterController)

  }

  def actionReload(event: ActionEvent) {

    getBean[ApplicationController]().replacePrimarySceneContent()

  }

}
