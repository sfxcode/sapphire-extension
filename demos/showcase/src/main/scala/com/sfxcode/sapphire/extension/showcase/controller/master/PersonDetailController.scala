package com.sfxcode.sapphire.extension.showcase.controller.master

import com.sfxcode.sapphire.core.value.KeyBindings
import com.sfxcode.sapphire.extension.controller.{ BaseDetailController, BaseMasterController }
import com.sfxcode.sapphire.extension.showcase.controller.MainController
import com.sfxcode.sapphire.extension.showcase.model.Person

import scala.reflect._

class PersonDetailController extends BaseDetailController {

  type R = Person

  def ct = classTag[R]

  def mainWindowController: MainController = {
    parent.asInstanceOf[MainController]
  }

  override def navigateToMasterController(masterController: BaseMasterController): Unit = {
    mainWindowController.masterNavigationController.actionShowPersonMasterController(null)
  }

  def updateBindings(bindings: KeyBindings): Unit = {
    formAdapter.addBindings(KeyBindings.forClass[Person]())
  }

  override def save(beanValue: Person): Unit = {

  }
}

