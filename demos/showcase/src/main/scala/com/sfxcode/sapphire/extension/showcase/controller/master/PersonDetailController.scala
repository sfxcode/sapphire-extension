package com.sfxcode.sapphire.extension.showcase.controller.master

import com.sfxcode.sapphire.core.value.KeyBindings
import com.sfxcode.sapphire.extension.controller.{ BaseDetailController, BaseMasterController }
import com.sfxcode.sapphire.extension.showcase.controller.BaseController
import com.sfxcode.sapphire.extension.showcase.model.Person

import scala.reflect._

class PersonDetailController extends BaseDetailController with BaseController {

  type R = Person

  def ct: ClassTag[Person] = classTag[R]

  override def navigateToMasterController(masterController: BaseMasterController): Unit = {
    updateShowcaseContent(masterController)
  }

  def updateBindings(bindings: KeyBindings): Unit = {
    formAdapter.addBindings(KeyBindings.forClass[Person]())
  }

  override def save(beanValue: Person): Unit = {

  }
}

