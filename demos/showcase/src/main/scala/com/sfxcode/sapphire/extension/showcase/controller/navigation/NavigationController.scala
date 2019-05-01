package com.sfxcode.sapphire.extension.showcase.controller.navigation

import com.sfxcode.sapphire.extension.showcase.controller.AbstractBaseController
import com.sfxcode.sapphire.extension.showcase.controller.form._
import com.sfxcode.sapphire.extension.showcase.controller.master.PersonMasterController
import com.sfxcode.sapphire.extension.showcase.controller.table.{ FriendTableController, PersonTableController, SimplePersonTableController }
import javafx.event.ActionEvent

class DefaultNavigationController extends AbstractBaseController {
  lazy val formController = getController[FormController]()
  lazy val propertiesFormController = getController[PropertiesFormController]()
  lazy val listFormController = getController[ListFormController]()
  lazy val dualListFormController = getController[DualListFormController]()
  lazy val extensionFormController = getController[ExtensionFormController]()
  lazy val iconFontController = getController[IconFontController]()

  def actionShowForm(event: ActionEvent) {
    workspaceManager.updatePaneContent(formController)
  }
  def actionShowListForm(event: ActionEvent) {
    workspaceManager.updatePaneContent(listFormController)
  }

  def actionShowDualListForm(event: ActionEvent) {
    workspaceManager.updatePaneContent(dualListFormController)
  }

  def actionShowPropertiesForm(event: ActionEvent) {
    workspaceManager.updatePaneContent(propertiesFormController)
  }

  def actionShowExtensionForm(event: ActionEvent) {
    workspaceManager.updatePaneContent(extensionFormController)
  }

  def actionShowIcons(event: ActionEvent) {
    workspaceManager.updatePaneContent(iconFontController)
  }

}

class TableNavigationController extends AbstractBaseController {
  lazy val simplePersonTableController = getController[SimplePersonTableController]()
  lazy val personTableController = getController[PersonTableController]()
  lazy val friendTableController = getController[FriendTableController]()

  def actionShowSimplePersonTable(event: ActionEvent) {
    workspaceManager.updatePaneContent(simplePersonTableController)
  }

  def actionShowPersonTable(event: ActionEvent) {
    workspaceManager.updatePaneContent(personTableController)
  }

  def actionShowFriendTable(event: ActionEvent) {
    workspaceManager.updatePaneContent(friendTableController)
  }
}

class MasterNavigationController extends AbstractBaseController {
  lazy val personMasterController = getController[PersonMasterController]()

  def actionShowPersonMasterController(event: ActionEvent) {
    workspaceManager.updatePaneContent(personMasterController)
  }

}

