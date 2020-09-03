package com.sfxcode.sapphire.extension.showcase.controller

import com.sfxcode.sapphire.core.fxml.FxmlLoader
import com.sfxcode.sapphire.core.showcase.{ ShowcaseController, ShowcaseItem }
import com.sfxcode.sapphire.extension.showcase.controller.font.IconFontController
import com.sfxcode.sapphire.extension.showcase.controller.form.{
  DualListFormController,
  ExtensionFormController,
  FormController,
  ListFormController,
  PropertiesFormController
}
import com.sfxcode.sapphire.extension.showcase.controller.master.PersonMasterController
import com.sfxcode.sapphire.extension.showcase.controller.table.{
  FriendTableController,
  PersonTableController,
  SimplePersonTableController
}
import com.typesafe.scalalogging.LazyLogging

@FxmlLoader(path = "/showcase/ShowcaseView.fxml")
class ShowcaseViewController extends ShowcaseController with LazyLogging {

  private val welcomeItem = ShowcaseItem("Welcome", "Welcome", () => getController[WelcomeController]())
  private val formItem = ShowcaseItem("Form", "Basic Form", () => getController[FormController]())
  private val listFormItem = ShowcaseItem("Form", "List Form", () => getController[ListFormController]())
  private val dualListFormItem = ShowcaseItem("Form", "Dual List Form", () => getController[DualListFormController]())
  private val propertiesFormItem =
    ShowcaseItem("Form", "Properties Form", () => getController[PropertiesFormController]())
  private val controlsfxFormItem = ShowcaseItem("Form", "ControlsFX", () => getController[ExtensionFormController]())
  private val controllerMasterItem =
    ShowcaseItem("Controller", "Master / Detail", () => getController[PersonMasterController]())
  private val fontIconItem = ShowcaseItem("Font", "Icons", () => getController[IconFontController]())
  private val tableBaseItem = ShowcaseItem("Table", "Base", () => getController[SimplePersonTableController]())
  private val dataTableBaseItem = ShowcaseItem("DataTable", "Base", () => getController[FriendTableController]())
  private val dataTableExtendedItem =
    ShowcaseItem("DataTable", "Extended", () => getController[PersonTableController]())

  private val items = List(
    formItem,
    listFormItem,
    dualListFormItem,
    propertiesFormItem,
    controlsfxFormItem,
    fontIconItem,
    controllerMasterItem,
    tableBaseItem,
    dataTableBaseItem,
    dataTableExtendedItem)

  override def didGainVisibilityFirstTime(): Unit = {
    super.didGainVisibilityFirstTime()
    updateShowcaseItems(items)
    changeShowcaseItem(welcomeItem)
  }
}
