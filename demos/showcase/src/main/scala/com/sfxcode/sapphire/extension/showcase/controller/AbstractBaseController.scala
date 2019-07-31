package com.sfxcode.sapphire.extension.showcase.controller

import com.sfxcode.sapphire.core.controller.ViewController

class AbstractBaseController extends ViewController {

  def mainController: MainController = {
    parent.asInstanceOf[MainController]
  }

  def workspaceManager = mainController.workspaceManager

}
